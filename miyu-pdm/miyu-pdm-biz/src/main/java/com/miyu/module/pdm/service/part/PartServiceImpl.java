package com.miyu.module.pdm.service.part;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.miyu.module.pdm.controller.admin.part.vo.*;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessChangeRespVO;
import com.miyu.module.pdm.dal.dataobject.attachment.PartAttachmentDO;
import com.miyu.module.pdm.dal.dataobject.document.PartDocumentVersionDO;
import com.miyu.module.pdm.dal.dataobject.master.PartMasterDO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.NcDO;
import com.miyu.module.pdm.dal.dataobject.version.PartVersionDO;
import com.miyu.module.pdm.dal.mysql.feasibilityTask.FeasibilityTaskMapper;
import com.miyu.module.pdm.dal.mysql.part.*;
import com.miyu.module.pdm.dal.mysql.processTask.ProcessTaskMapper;
import com.miyu.module.pdm.netty.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.ORDER_CHANGE_IS_EXISTS;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.PART_TASK_EXITS;

@Service
@Validated
public class PartServiceImpl implements PartService {


    @Resource
    private PartMapper partMapper;
    @Resource
    private PartVersionMapper partVersionMapper;
    @Resource
    private PartBomMapper partBomMapper;
    @Resource
    private PartAttachmentMapper partAttachmentMapper;

    @Resource
    private PartDoucmentVersionMapper partDoucmentVersionMapper;
    @Autowired
    private PartMasterMapper partMasterMapper;
    @Resource
    private ProjPartBomMapper projPartBomMapper;
    @Resource
    private FeasibilityTaskMapper feasibilityTaskMapper;
    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Override
    public PartVersionDO selectPartInfoById(String partVersionId) {
        return partVersionMapper.selectPartInfoById(partVersionId);
    }

    @Override
    public List<PartInstanceDO> getPartInstanceList(PartInstanceListReqVO listReqVO) {
        String tableName = partMapper.selectTableNameByProductIdAndType(listReqVO.getRootproductId());
        if (StringUtils.isEmpty(tableName)) return new ArrayList<>();
        List<PartInstanceDO> partDOList = partMapper.selectAllFromDynamicTable(tableName);
        // 数据为空，直接返回
        if (CollUtil.isEmpty(partDOList)) return new ArrayList<>();
        //排序
        CollUtil.sort(partDOList, Comparator.comparing(PartInstanceDO::getParentId));
        return partDOList;
    }


//    @Override
//    public PartVersionDO selectPartByPartVersionId(String partVersionId) {
//        return partVersionMapper.selectPartByPartVersionId(partVersionId);
//    }

    @Override
    public List<Map<String, Object>> selectDetailsByPartVersionId(String partVersionId) {
        return partBomMapper.selectByBom(partVersionId);
    }

    @Override
    public List<Map<String, Object>> getCombinedDataByPartVersionId(String partVersionId) {
        // 步骤1：根据part_version_id查询pdm_datapackage_bom表中的id
        List<String> bomIds = partBomMapper.getIdByPartVersionId(partVersionId);

        // 存储最终结果的列表
        List<Map<String, Object>> combinedResults = new ArrayList<>();
        String bomId = "";
        if (!bomIds.isEmpty()) {
            bomId = bomIds.get(0);
        } else {// 手动录入零件信息时无bomId，取PVID
            bomId = partVersionId;
        }
        combinedResults = partAttachmentMapper.findAttachmentsByDatapackageBomId(bomId);
        return combinedResults;
    }

    @Override
    public PartDocumentVersionDO getDocumentInfoByPartVersionId(String partVersionId) {
            return partDoucmentVersionMapper.getDocumentInfoByPartVersionId(partVersionId);
        }


    @Override
    public List<Map<String, Object>> getPartAttrs(PartAttrReqVO reqVO) {
        // 零件动态设计属性
        List<Map<String, Object>> partAttrs = new ArrayList<>();

        String customizedIndex = reqVO.getCustomizedIndex();
        String rootProductId = reqVO.getRootProductId();
        String partVersionId = reqVO.getPartVersionId();
        String stdDataObject = "PartVersion";
        String pvTableName = "pdm_"+customizedIndex+"_part_version";

        // 获取产品数据对象pv定义的动态客户化属性
        String customizedAttrs = partMapper.selectCustomizedAttrs(rootProductId, stdDataObject, customizedIndex);
        // 获取动态pv表属性
        List<Map<String,Object>> pvList = partMapper.selectPvAttrValues(pvTableName, partVersionId);
        if(pvList != null && pvList.size() > 0) {
            Map<String,Object> pvMap = pvList.get(0);
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(customizedAttrs);
            JsonArray jsonArray = element.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                String attrAlias = obj.get("attr_alias").getAsString();
                String attrName = obj.get("attr_name").getAsString();
                for (Map.Entry<String, Object> entry : pvMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().toString();
                    if(attrName.equals(key)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("attrAlias", attrAlias);
                        map.put("attrValue", value);
                        partAttrs.add(map);
                    }
                }
            }
        }
        return partAttrs;
    }

    @Override
    public String getModelUrl(String fileName, String fileType) {
        return partDoucmentVersionMapper.getModelUrl(fileName, fileType);
    }

    @Override
    public PartAttachmentDO findAttachmentsByDatapackageBomId(String id) {
        return partAttachmentMapper.selectById(id);
    }

    @Override
    public void saveNewFile(PartAttachmentRespVO saveReqVO) {
        String id = IdUtil.fastSimpleUUID();
        PartAttachmentDO partAttachmentDO = BeanUtils.toBean(saveReqVO, PartAttachmentDO.class)
                .setId(id)
                .setDatapackageBomId(saveReqVO.getDatapackageBomId());

        partAttachmentMapper.insert(partAttachmentDO);
    }

    @Override
    public void deleteNewFile(PartAttachmentRespVO saveReqVO) {
//        PartAttachmentDO partAttachmentDO = BeanUtils.toBean(saveReqVO, PartAttachmentDO.class);
        partAttachmentMapper.deleteNewFile(saveReqVO);

    }

    @Override
    public void addPart(AddPartReqVO reqVO) {
         String partNumber = reqVO.getPartNumber();
         String processCondition = reqVO.getProcessCondition();
         String companyId = reqVO.getCompanyId();
         String partVersion = reqVO.getPartVersion();
         String partVersionId = null;
         String tableName = null;
         String PMID = null;
         String PVID = null;
         String PPBID = null;

         List<AddPartRespVO> PM =  partMasterMapper.selectPM(partNumber, processCondition);
         if(PM.isEmpty()) {
             PMID = UUIDUtil.randomUUID32();
             Map<String ,Object> pmMap = new HashMap<String, Object>();
              pmMap.put("id",PMID);
              pmMap.put("part_number",reqVO.getPartNumber());
              pmMap.put("part_name",reqVO.getPartName());
              pmMap.put("process_condition",processCondition);
              pmMap.put("root_product_id","97e734f8f5fe4e068f38c7be046b3537");
              pmMap.put("product_type","0");
             partMasterMapper.insertPM(pmMap);
         }
        List<AddPartRespVO> PM1 =  partMasterMapper.selectPM(partNumber, processCondition);
        List<String> PMList = PM1.stream()
                .map(AddPartRespVO::getId)
                .collect(Collectors.toList());
        PMID = String.join(",", PMList);

        List<AddPartRespVO> PV =  partVersionMapper.selectPV(PMID,partVersion);
        if(PV.isEmpty()) {
            PVID = UUIDUtil.randomUUID32();
            Map<String ,Object> pvMap = new HashMap<String, Object>();
             pvMap.put("id",PVID);
             pvMap.put("part_version",reqVO.getPartVersion());
             pvMap.put("table_name","pdm_std_part_version");
             pvMap.put("part_master_id",PMID);
             pvMap.put("source","1");
            partVersionMapper.insertPV(pvMap);
         }
        List<AddPartRespVO> PV1 =  partVersionMapper.selectPV(PMID,partVersion);
        List<String> PVList = PV1.stream()
                .map(AddPartRespVO::getId)
                .collect(Collectors.toList());
        List<String> TableNameList = PV1.stream()
                .map(AddPartRespVO::getTableName)
                .collect(Collectors.toList());
        PVID = String.join(",", PVList);
        tableName = String.join(",", TableNameList);

        List<AddPartRespVO> PPB = projPartBomMapper.selectPPB(companyId,PVID);
        if(PPB.isEmpty()) {
            PPBID = UUIDUtil.randomUUID32();
            Map<String, Object> ppbMap = new HashMap<String, Object>();
             ppbMap.put("id",PPBID);
             ppbMap.put("part_version_id",PVID);
             ppbMap.put("company_id",reqVO.getCompanyId());
             ppbMap.put("company_name",reqVO.getCompanyName());
             ppbMap.put("table_name",tableName);
             projPartBomMapper.insertPPB(ppbMap);
        }
        List<String> PPBList =  PPB.stream()
                .map(AddPartRespVO::getId)
                .collect(Collectors.toList());
        PPBID = String.join(",", PPBList);

    }

    @Override
    public void deletePart(AddPartReqVO reqVO) {
        String partVersionId = reqVO.getPartVersionId();
        String partNumber = reqVO.getPartNumber();
        String processCondition = reqVO.getProcessCondition();
        String partVersion = reqVO.getPartVersion();

        List<AddPartRespVO> CFT = feasibilityTaskMapper.selectCftByPvId(partVersionId);
        List<AddPartRespVO> CPT = processTaskMapper.selectCptByPvId(partVersionId);
        if(CFT.isEmpty() && CPT.isEmpty()) {

            List<AddPartRespVO> PM =  partMasterMapper.selectPM(partNumber, processCondition);
            List<String> PMList = PM.stream()
                    .map(AddPartRespVO::getId)
                    .collect(Collectors.toList());
            String PMID = String.join(",", PMList);
            partMasterMapper.deletePM(PMID);

            List<AddPartRespVO> PV =  partVersionMapper.selectPV(PMID,partVersion);
            List<String> PVList = PV.stream()
                    .map(AddPartRespVO::getId)
                    .collect(Collectors.toList());
            String PVID  = String.join(",", PVList);
            partVersionMapper.deletePV(PVID);
            projPartBomMapper.deletePPB(PVID);
        }else{
            throw exception(PART_TASK_EXITS);
        }
    }

    @Override
    public List<PartMasterDO> getPartInfoList(String partNumber) {
        List<PartMasterDO> partMasterDOList = partMasterMapper.selectList(new LambdaQueryWrapperX<PartMasterDO>()
                .eq(PartMasterDO::getPartNumber, partNumber)
                .orderByAsc(PartMasterDO::getProcessCondition));
        return partMasterDOList;
    }
}






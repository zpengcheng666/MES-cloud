package com.miyu.module.qms.service.inspectionscheme;

import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import com.miyu.module.qms.dal.mysql.samplingstandard.SamplingStandardMapper;
import com.miyu.module.qms.enums.InspectionSchemeEffectiveStatusEnum;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import com.miyu.module.qms.controller.admin.inspectionscheme.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.dal.mysql.inspectionscheme.InspectionSchemeMapper;
import com.miyu.module.qms.dal.mysql.inspectionschemeitem.InspectionSchemeItemMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 检验方案 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InspectionSchemeServiceImpl implements InspectionSchemeService {

    @Resource
    private InspectionSchemeMapper inspectionSchemeMapper;
    @Resource
    private InspectionSchemeItemMapper inspectionSchemeItemMapper;
    @Resource
    private SamplingStandardMapper samplingStandardMapper;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createInspectionScheme(InspectionSchemeSaveReqVO createReqVO) {
        // 插入
        InspectionSchemeDO inspectionScheme = BeanUtils.toBean(createReqVO, InspectionSchemeDO.class);
        inspectionSchemeMapper.insertOrUpdate(inspectionScheme);

        // 插入子表
        createInspectionSchemeItemList(inspectionScheme.getId(), createReqVO.getInspectionSchemeItems());
        // 返回
        return inspectionScheme.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = QMS_SCHEME_TYPE, subType = QMS_UPDATE_SCHEME_SUB_TYPE, bizNo = "{{#scheme.id}}",
            success = QMS_UPDATE_SCHEME_SUCCESS)
    public void updateInspectionScheme(InspectionSchemeSaveReqVO updateReqVO) {
        // 校验存在
        InspectionSchemeDO scheme = validateInspectionSchemeExists(updateReqVO.getId());
        // 更新
        InspectionSchemeDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSchemeDO.class);
        inspectionSchemeMapper.updateById(updateObj);

        // 子表不更新
        updateInspectionSchemeItemList(updateReqVO.getId(), updateReqVO.getInspectionSchemeItems());

        // 记录操作日志上下文
        LogRecordContext.putVariable("scheme", scheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionScheme(String id) {
        // 校验存在
        validateInspectionSchemeExists(id);
        // 删除
        inspectionSchemeMapper.deleteById(id);


        // 删除子表
        deleteInspectionSchemeItemByInspectionSchemeId(id);
    }

    private InspectionSchemeDO validateInspectionSchemeExists(String id) {
        InspectionSchemeDO inspectionSchemeDO = inspectionSchemeMapper.selectById(id);
        if (inspectionSchemeDO == null) {
            throw exception(INSPECTION_SCHEME_NOT_EXISTS);
        }
        return inspectionSchemeDO;
    }

    @Override
    public InspectionSchemeDO getInspectionScheme(String id) {

        InspectionSchemeDO inspectionSchemeDO = inspectionSchemeMapper.selectById(id);
        SamplingStandardDO samplingRuleDO= samplingStandardMapper.selectById(inspectionSchemeDO.getSamplingStandardId());
//        inspectionSchemeDO.setSamplingNo(samplingRuleDO!=null?samplingRuleDO.getSamplingNo():null);
//        inspectionSchemeDO.setSamplingNumber(samplingRuleDO!=null?samplingRuleDO.getSamplingNumber():null);
//        inspectionSchemeDO.setMaxUnqualifiedQuantity(samplingRuleDO!=null?samplingRuleDO.getMaxUnqualifiedQuantity():null);
//        return inspectionSchemeDO;

        return inspectionSchemeDO;
    }

    @Override
    public void submitEffective(String id, Integer isEffective) {
        InspectionSchemeDO inspectionSchemeDO = validateInspectionSchemeExists(id);

        //如果要生效  需要判断 是否已配置好检验方案
        if (InspectionSchemeEffectiveStatusEnum.EFFECTIVE.getStatus().equals(isEffective)) {

            //判断是否配置检测项目
            List<InspectionSchemeItemDO> schemeItemDOS = getInspectionSchemeItemListByInspectionSchemeId(id);
            if (CollectionUtils.isEmpty(schemeItemDOS)) {
                throw exception(INSPECTION_SCHEME_ITEM_NOT_EXISTS);
            }


        }
        inspectionSchemeDO.setIsEffective(isEffective);
        inspectionSchemeMapper.updateById(inspectionSchemeDO);

    }

    @Override
    public PageResult<InspectionSchemeDO> getInspectionSchemePage(InspectionSchemePageReqVO pageReqVO) {
        // 物料类别ID不为空
        if(StringUtils.isNotBlank(pageReqVO.getMaterialTypeId())){
            // 物料类别id查询物料类型集合
            List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeId(pageReqVO.getMaterialTypeId()).getCheckedData();
            // 物料类型id集合
            List<String> materialConfigIds = materialConfigList.stream().map(MaterialConfigRespDTO::getId).collect(Collectors.toList());
            pageReqVO.setMaterialConfigIds(materialConfigIds.stream().distinct().collect(Collectors.toList()));
        }

        return inspectionSchemeMapper.selectPage(pageReqVO);
    }

    /**
     * 获取检验方案集合
     * @param reqVO
     * @return
     */
    @Override
    public List<InspectionSchemeDO> getInspectionSchemeList4InspectionSheet(InspectionSchemeReqVO reqVO) {
        // 查询检验方案
        // 物料类型ID有可能不是原ID
        List<InspectionSchemeDO> list = inspectionSchemeMapper.selectScheme4InspectionSheet(reqVO);
        // 物料类型没查到检验方案
        // 通过原类型ID去查
        if (list.size() == 0) {
            List<MaterialConfigRespDTO> configList = materialMCCApi.getMaterialConfigList(Lists.newArrayList(reqVO.getMaterialConfigId())).getCheckedData();
            if(configList.size() > 0 && StringUtils.isNotBlank(configList.get(0).getMaterialSourceId())) {
                // 原物料类型ID
                reqVO.setMaterialConfigId(configList.get(0).getMaterialSourceId());
                list = inspectionSchemeMapper.selectScheme4InspectionSheet(reqVO);
            }
            else {
                return new ArrayList<>();
            }
        }
        return list;
    }

    /**
     * 发布检验方案
     * @param technologyId
     * @param isEffective
     */
    @Override
    public void submitEffectiveByTechnologyId(String technologyId, Integer isEffective) {
        // 通过工艺查询未发布的检验方案集合
        List<InspectionSchemeDO> schemeList = inspectionSchemeMapper.selectList(InspectionSchemeDO::getTechnologyId, technologyId, InspectionSchemeDO::getIsEffective, InspectionSchemeEffectiveStatusEnum.NOEFFECTIVE.getStatus());
        // 存在未发布的检验方案
        if (schemeList.size() > 0) {
            schemeList.forEach(item -> item.setIsEffective(InspectionSchemeEffectiveStatusEnum.EFFECTIVE.getStatus()));
            inspectionSchemeMapper.updateBatch(schemeList);
        }
    }

    /**
     * 工序ID查询检验方案
     * @param technologyId
     * @return
     */
    @Override
    public List<InspectionSchemeDO> getInspectionSchemeByProcessId(String technologyId, String processId) {
        return inspectionSchemeMapper.selectList(InspectionSchemeDO::getTechnologyId, technologyId, InspectionSchemeDO::getProcessId, processId);
    }

    /**
     * 工艺ID删除检验方案
     * @param technologyId
     */
    @Override
    @Transactional
    public void deleteInspectionSchemeByTechnologyId(String technologyId) {
        // 工艺ID查询检验方案集合
        List<InspectionSchemeDO> schemeList = inspectionSchemeMapper.selectList(InspectionSchemeDO::getTechnologyId, technologyId);
        // 检验方案ID集合
        List<String> ids = schemeList.stream().map(InspectionSchemeDO::getId).collect(Collectors.toList());
        // 删除方案
        inspectionSchemeMapper.deleteBatchIds(ids);
        // 查询检测项集合
        List<InspectionSchemeItemDO> schemeItemList = inspectionSchemeItemMapper.selectList(InspectionSchemeItemDO::getInspectionSchemeId, ids);
        List<String> itemIds = schemeItemList.stream().map(InspectionSchemeItemDO::getId).collect(Collectors.toList());
        // 删除检测项
        inspectionSchemeItemMapper.deleteBatchIds(itemIds);
    }

    // ==================== 子表（检验方案检测项目详情） ====================

    @Override
    public List<InspectionSchemeItemDO> getInspectionSchemeItemListByInspectionSchemeId(String inspectionSchemeId) {
        return inspectionSchemeItemMapper.selectListByInspectionSchemeId(inspectionSchemeId);
    }


    private void createInspectionSchemeItemList(String inspectionSchemeId, List<InspectionSchemeItemDO> list) {
        list.forEach(o -> o.setInspectionSchemeId(inspectionSchemeId));
        inspectionSchemeItemMapper.insertOrUpdateBatch(list);
    }

    private void updateInspectionSchemeItemList(String inspectionSchemeId, List<InspectionSchemeItemDO> list) {
        deleteInspectionSchemeItemByInspectionSchemeId(inspectionSchemeId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createInspectionSchemeItemList(inspectionSchemeId, list);
    }

    private void deleteInspectionSchemeItemByInspectionSchemeId(String inspectionSchemeId) {

        inspectionSchemeItemMapper.deleteByInspectionSchemeId(inspectionSchemeId);

    }

}

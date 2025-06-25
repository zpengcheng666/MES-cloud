package com.miyu.module.pdm.service.toolingApply;


import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.pdm.controller.admin.dataobject.vo.DataObjectSaveReqVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import com.miyu.module.pdm.dal.dataobject.toolingApply.ToolingApplyDO;
import com.miyu.module.pdm.dal.mysql.dataobject.DataObjectMapper;
import com.miyu.module.pdm.dal.mysql.product.ProductMapper;
import com.miyu.module.pdm.dal.mysql.toolingApply.ToolingApplyMapper;
import com.miyu.module.pdm.enums.PDMAuditStatusEnum;
import com.miyu.module.pdm.enums.ToolingApplyStatusEnum;
import com.miyu.module.pdm.netty.DataObjectUtil;
import com.miyu.module.pdm.netty.UUIDUtil;
import com.miyu.module.pdm.service.dataobject.DataObjectService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ApiConstants.TOOLING_APPLY_KEY;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.TOOLING_NUMBER_DUPLICATE;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;

@Service
@Validated
public class ToolingApplyServiceImpl implements ToolingApplyService{

    @Resource
    private ToolingApplyMapper toolingApplyMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private DataObjectMapper dataObjectMapper;

    @Resource
    private DataObjectService dataObjectService;

    @Override
    public PageResult<ToolingApplyDO> getToolingApplyPage(ToolingApplyReqVO reqVO) {
        return toolingApplyMapper.selectPage(reqVO);
    }

    @Override
    public List<ToolingApplyDO> getToolingApplyList(ToolingApplyReqVO reqVO) {
        return toolingApplyMapper.getToolingApplyList(reqVO);
    }

    @Override
    public ToolingApplyReqVO getToolingApply(String id) {
        return toolingApplyMapper.selectById1(id);
    }

    @Override
    public String createToolingApply(ToolingApplyReqVO createReqVO) {
        //工装图号校验唯一性
        validateToolingNumberUnique(null,createReqVO.getToolingNumber());
        // 插入
        ToolingApplyDO toolingApplyDO = BeanUtils.toBean(createReqVO, ToolingApplyDO.class)
                .setId(IdUtil.fastSimpleUUID());
        String shortUniqueId = UUIDUtil.randomStr8(8);
        toolingApplyDO.setCustomizedIndex(shortUniqueId);
        toolingApplyMapper.insert(toolingApplyDO);
        // 返回
        return toolingApplyDO.getId();
    }

    private void validateToolingApplyExists(String id) {
        if (toolingApplyMapper.selectById(id) == null) {}
    }

    @Override
    @LogRecord(type = PDM_TOOLING_APPLY_TYPE, subType = PDM_TOOLING_APPLY_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_TOOLING_APPLY_DELETE_SUCCESS)
    public void deleteToolingApply(String id) {
            // 校验存在
            validateToolingApplyExists(id);
            // 删除
            toolingApplyMapper.deleteById(id);
            // 记录日志
            ToolingApplyDO toolingApplyDO = toolingApplyMapper.selectById(id);
            LogRecordContext.putVariable("toolingApplyName", toolingApplyDO.getToolingName());
        }

    @Override
    @LogRecord(type = PDM_TOOLING_APPLY_TYPE, subType = PDM_TOOLING_APPLY_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_TOOLING_APPLY_UPDATE_SUCCESS)
    public void updateToolingApply(ToolingApplyReqVO updateReqVO) {
        // 校验存在
        validateToolingApplyExists(updateReqVO.getId());
        ToolingApplyReqVO before = toolingApplyMapper.selectById1(updateReqVO.getId());
        boolean isStepNumChanged = !Objects.equals(before.getToolingNumber(), updateReqVO.getToolingNumber());
        if (isStepNumChanged) {
            validateToolingNumberUnique(updateReqVO.getId(), updateReqVO.getToolingNumber());
        }
        // 更新
        ToolingApplyDO updateObj = BeanUtils.toBean(updateReqVO, ToolingApplyDO.class);
        toolingApplyMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("toolingApplyName", updateReqVO.getToolingName());
    }

    private void validateToolingNumberUnique(String id,String toolingNumber) {
        ToolingApplyDO toolingApply = toolingApplyMapper.selectByIdAndToolingNumber(toolingNumber);
        if(toolingApply == null){
            return;
        }
        if(id == null ){
            throw exception(TOOLING_NUMBER_DUPLICATE);
        }
        if(!Objects.equals(toolingApply.getId(),id)){
            throw exception(TOOLING_NUMBER_DUPLICATE);
        }
    }

    @Override
    public void startApplyInstance(ToolingApplyReqVO updateReqVO) {
        ToolingApplyDO applyDO = BeanUtils.toBean(updateReqVO, ToolingApplyDO.class);
        // 1. 创建技术评估审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String toolingApplyInstanceId = bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(TOOLING_APPLY_KEY).setBusinessKey(updateReqVO.getId()).setVariables(variables)).getCheckedData();
        // 2. 更新技术评估任务流程实例号及审批状态
        toolingApplyMapper.updateById(applyDO.setProcessInstanceId(toolingApplyInstanceId)
                .setApprovalStatus(PDMAuditStatusEnum.PROCESS.getStatus())
                .setStatus(applyDO.getStatus()));
    }

    @Override
    public void updateApplyInstanceStatus(String id, Integer status) {
        ToolingApplyDO applyDO = toolingApplyMapper.selectById(id);
        applyDO.setId(id);
        applyDO.setApprovalStatus(status);
        if (PDMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后 更新任务状态已完成
            applyDO.setStatus(ToolingApplyStatusEnum.FINISH.getStatus());
            //审批通过后 处理动态表相关信息(工装图号不存在时处理)
            String toolingNumber = applyDO.getToolingNumber();
            Long productCount = productMapper.selectCount(new LambdaQueryWrapperX<ProductDO>()
                    .eq(ProductDO::getProductNumber, toolingNumber)
                    .eq(ProductDO::getProductType, 1));
            if(productCount == 0) {
                //1、处理产品pdm_root_product
                ProductDO productDO = new ProductDO();
                productDO.setId(id);
                productDO.setProductName(applyDO.getToolingName());
                productDO.setProductNumber(toolingNumber);
                productDO.setProductType(1);
                productMapper.insert(productDO);
                //2、处理产品数据对象pdm_object
                String customizedIndex = applyDO.getCustomizedIndex();
                List<DataObjectDO> list = DataObjectUtil.getDO7Forinsert(productDO.getId(), customizedIndex);
                for (DataObjectDO pdm_data_object : list) {
                    dataObjectMapper.insertSelective(pdm_data_object);
                }
                //3、实例化动态表
                DataObjectSaveReqVO dataObjectSaveReqVO = new DataObjectSaveReqVO();
                dataObjectSaveReqVO.setRootproductId(productDO.getId());
                dataObjectSaveReqVO.setCustomizedIndex(applyDO.getCustomizedIndex());
                dataObjectService.createIndex(dataObjectSaveReqVO);
                //4、处理动态PM PV
                String pmTableName = "pdm_"+customizedIndex+"_part_master";
                String pmId = UUIDUtil.randomUUID32();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("pmTableName",pmTableName);
                map.put("id","'"+pmId+"'");
                map.put("rootProductId","'"+productDO.getId()+"'");
                map.put("partNumber","'"+toolingNumber+"'");
                map.put("partName","'"+applyDO.getToolingName()+"'");
                map.put("partType","'CATPart'");
                dataObjectService.savePM(map);
                String pvTableName = "pdm_"+customizedIndex+"_part_version";
                Map<String, Object> pvMap = new HashMap<String, Object>();
                String pvId = UUIDUtil.randomUUID32();
                pvMap.put("pvTableName",pvTableName);
                pvMap.put("id", "'"+pvId+"'");
                pvMap.put("partMasterId", "'"+pmId+"'");
                pvMap.put("partVersion", "'A'");
                dataObjectService.savePV(pvMap);
                //5、处理固定PM PV
                Map<String, Object> pmParams = new HashMap<String, Object>();
                pmParams.put("id",pmId);
                pmParams.put("part_number",toolingNumber);
                pmParams.put("part_name", applyDO.getToolingName());
                pmParams.put("root_product_id",productDO.getId());
                pmParams.put("part_type","CATPart");
                pmParams.put("product_type","1");
                dataObjectService.savePartMaster(pmParams);
                Map<String, Object> pvParams = new HashMap<String, Object>();
                pvParams.put("id",pvId);
                pvParams.put("part_master_id",pmId);
                pvParams.put("part_version","A");
                pvParams.put("table_name",pvTableName);
                dataObjectService.savePartVersion(pvParams);
            }
        } else if (PDMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过 更新任务状态审核失败
            applyDO.setStatus(ToolingApplyStatusEnum.REJECT.getStatus());
        }
        toolingApplyMapper.updateById(applyDO);
    }
}

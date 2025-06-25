package com.miyu.module.mcc.service.materialconfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import com.miyu.module.mcc.service.materialtype.MaterialTypeService;
import com.miyu.module.ppm.enums.common.ContractAuditStatusEnum;
import com.miyu.module.ppm.enums.common.ContractStatusEnum;
import com.miyu.module.ppm.enums.consignmentrefund.ConsignmentRefundEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.PpmAuditStatusEnum;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.dto.MCCMaterialConfigReqDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.sound.sampled.AudioSystem;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.applet.AudioClip;
import java.util.*;
import com.miyu.module.mcc.controller.admin.materialconfig.vo.*;
import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.materialconfig.MaterialConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.mcc.enums.ApiConstants.MATERIAL_CONFIG_AUDIT;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.ApiConstants.PM_REFUND_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.CONTRACT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS;

/**
 * 物料类型 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class MaterialConfigServiceImpl implements MaterialConfigService {

    @Resource
    private MaterialConfigMapper materialConfigMapper;
    @Resource
    private MaterialConfigApi materialConfigApi;
    @Resource
    private MaterialTypeService materialTypeService;


    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createMaterialConfig(MaterialConfigSaveReqVO createReqVO) {
        // 插入
        MaterialConfigDO materialConfig = BeanUtils.toBean(createReqVO, MaterialConfigDO.class);
        materialConfigMapper.insert(materialConfig);

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(MATERIAL_CONFIG_AUDIT).setBusinessKey(String.valueOf(materialConfig.getId())).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号

        materialConfig.setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus());

        materialConfigMapper.updateById(materialConfig);

        // 返回
        MCCMaterialConfigReqDTO reqDTO = BeanUtils.toBean(materialConfig,MCCMaterialConfigReqDTO.class);
        MaterialTypeDO materialTypeDO= materialTypeService.getMaterialType(reqDTO.getMaterialParentTypeId());
        reqDTO.setMaterialParentTypeCode(materialTypeDO.getCode());
        reqDTO.setMaterialParentTypeName(materialTypeDO.getName());
        MaterialTypeDO materialTypeDO1= materialTypeService.getMaterialType(reqDTO.getMaterialTypeId());
        reqDTO.setMaterialTypeCode(materialTypeDO1.getCode());
        materialConfigApi.createMaterialConfig(reqDTO);
        return materialConfig.getId();
    }

    @Override
    public String createMaterialConfigNoSubmit(@Valid MaterialConfigSaveReqVO createReqVO) {
        // 插入
        MaterialConfigDO materialConfig = BeanUtils.toBean(createReqVO, MaterialConfigDO.class);
        materialConfig.setStatus(PpmAuditStatusEnum.APPROVE.getStatus());
        materialConfigMapper.insert(materialConfig);

        // 返回
        MCCMaterialConfigReqDTO reqDTO = BeanUtils.toBean(materialConfig,MCCMaterialConfigReqDTO.class);
        MaterialTypeDO materialTypeDO= materialTypeService.getMaterialType(reqDTO.getMaterialParentTypeId());
        reqDTO.setMaterialParentTypeCode(materialTypeDO.getCode());
        reqDTO.setMaterialParentTypeName(materialTypeDO.getName());
        MaterialTypeDO materialTypeDO1= materialTypeService.getMaterialType(reqDTO.getMaterialTypeId());
        reqDTO.setMaterialTypeCode(materialTypeDO1.getCode());
        materialConfigApi.createMaterialConfig(reqDTO);
        return materialConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMaterialConfig(MaterialConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialConfigExists(updateReqVO.getId());
        // 更新
        MaterialConfigDO updateObj = BeanUtils.toBean(updateReqVO, MaterialConfigDO.class);


        materialConfigMapper.updateById(updateObj);

        MCCMaterialConfigReqDTO reqDTO = BeanUtils.toBean(updateReqVO,MCCMaterialConfigReqDTO.class);
        MaterialTypeDO materialTypeDO= materialTypeService.getMaterialType(reqDTO.getMaterialParentTypeId());
        reqDTO.setMaterialParentTypeCode(materialTypeDO.getCode());
        reqDTO.setMaterialParentTypeName(materialTypeDO.getName());
        materialConfigApi.updateMaterialConfig(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterialConfig(String id) {
        // 校验存在
        validateMaterialConfigExists(id);
        // 删除
        materialConfigMapper.deleteById(id);
        materialConfigApi.deleteMaterialConfig(id);
    }

    private MaterialConfigDO validateMaterialConfigExists(String id) {
      MaterialConfigDO materialConfigDO =   materialConfigMapper.selectById(id);
        if (materialConfigDO == null) {
            throw exception(MATERIAL_CONFIG_NOT_EXISTS);
        }
        return materialConfigDO;
    }

    @Override
    public MaterialConfigDO getMaterialConfig(String id) {
        return materialConfigMapper.selectById(id);
    }

    @Override
    public PageResult<MaterialConfigDO> getMaterialConfigPage(MaterialConfigPageReqVO pageReqVO) {
        return materialConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigListByTypeId(String materialTypeId) {
        return materialConfigMapper.getMaterialConfigListByTypeId(materialTypeId);
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigListByIds(Collection<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        materialConfigWrapperX
                .leftJoin(MaterialConfigDO.class, "mc1",MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialSourceId)
                .leftJoin(MaterialTypeDO.class,"mt1",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialTypeId)
                .leftJoin(MaterialTypeDO.class,"mt2",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialParentTypeId)
                .selectAs("mc1",MaterialConfigDO::getMaterialNumber,MaterialConfigDO::getMaterialNumberSource)
                .selectAs("mt1",MaterialTypeDO::getName,MaterialConfigDO::getMaterialTypeName)
                .selectAs("mt2",MaterialTypeDO::getName,MaterialConfigDO::getMaterialParentTypeName)
                .selectAs("mt2",MaterialTypeDO::getCode,MaterialConfigDO::getMaterialParentTypeCode)
                .selectAll(MaterialConfigDO.class);



        return materialConfigMapper.selectList(materialConfigWrapperX.inIfPresent(MaterialConfigDO::getId,ids));
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigListByCodes(Collection<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        materialConfigWrapperX
                .leftJoin(MaterialConfigDO.class, "mc1",MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialSourceId)
                .leftJoin(MaterialTypeDO.class,"mt1",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialTypeId)
                .leftJoin(MaterialTypeDO.class,"mt2",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialParentTypeId)
                .selectAs("mc1",MaterialConfigDO::getMaterialNumber,MaterialConfigDO::getMaterialNumberSource)
                .selectAs("mt1",MaterialTypeDO::getName,MaterialConfigDO::getMaterialTypeName)
                .selectAs("mt2",MaterialTypeDO::getName,MaterialConfigDO::getMaterialParentTypeName)
                .selectAs("mt2",MaterialTypeDO::getCode,MaterialConfigDO::getMaterialParentTypeCode)
                .selectAll(MaterialConfigDO.class);

        materialConfigWrapperX.inIfPresent(MaterialConfigDO::getMaterialNumber, codes);
        return materialConfigMapper.selectList(materialConfigWrapperX);
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigListByCode(String code) {
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        materialConfigWrapperX
                .leftJoin(MaterialConfigDO.class, "mc1",MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialSourceId)
                .leftJoin(MaterialTypeDO.class,"mt1",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialTypeId)
                .leftJoin(MaterialTypeDO.class,"mt2",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialParentTypeId)
                .selectAs("mc1",MaterialConfigDO::getMaterialNumber,MaterialConfigDO::getMaterialNumberSource)
                .selectAs("mt1",MaterialTypeDO::getName,MaterialConfigDO::getMaterialTypeName)
                .selectAs("mt2",MaterialTypeDO::getName,MaterialConfigDO::getMaterialParentTypeName)
                .selectAs("mt2",MaterialTypeDO::getCode,MaterialConfigDO::getMaterialParentTypeCode)
                .selectAll(MaterialConfigDO.class);
        materialConfigWrapperX.eqIfPresent(MaterialConfigDO::getMaterialNumber, code);
        return materialConfigMapper.selectList(MaterialConfigDO::getMaterialNumber,code);
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigListByTypeCode(MaterialConfigReqDTO reqDTO) {

        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        materialConfigWrapperX
                .leftJoin(MaterialConfigDO.class, "mc1",MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialSourceId)
                .leftJoin(MaterialTypeDO.class,"mt1",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialTypeId)
                .leftJoin(MaterialTypeDO.class,"mt2",MaterialTypeDO:: getId,MaterialConfigDO :: getMaterialParentTypeId)
                .selectAs("mc1",MaterialConfigDO::getMaterialNumber,MaterialConfigDO::getMaterialNumberSource)
                .selectAs("mt1",MaterialTypeDO::getName,MaterialConfigDO::getMaterialTypeName)
                .selectAs("mt2",MaterialTypeDO::getName,MaterialConfigDO::getMaterialParentTypeName)
                .selectAs("mt2",MaterialTypeDO::getCode,MaterialConfigDO::getMaterialParentTypeCode)
                .selectAll(MaterialConfigDO.class);


       return materialConfigMapper.selectList(new LambdaQueryWrapperX<MaterialConfigDO>()
                .eqIfPresent(MaterialConfigDO::getMaterialNumber, reqDTO.getMaterialNumber())
                .likeIfPresent(MaterialConfigDO::getMaterialName, reqDTO.getMaterialName())
                .eqIfPresent(MaterialConfigDO::getMaterialTypeCode, reqDTO.getMaterialTypeCode())
        );
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigList() {
        return materialConfigMapper.selectList();
    }

    @Override
    public void updateAudit(String bussinessKey, Integer status) {

        // 1.1 校验合同是否存在
        MaterialConfigDO materialConfigDO = validateMaterialConfigExists(bussinessKey);
        // 1.2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(materialConfigDO.getStatus(), ContractAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(CONTRACT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        materialConfigDO.setStatus(status);
        // 更新合同审批结果
        materialConfigMapper.updateById(materialConfigDO);
        MCCMaterialConfigReqDTO reqDTO = BeanUtils.toBean(materialConfigDO,MCCMaterialConfigReqDTO.class);
        MaterialTypeDO materialTypeDO= materialTypeService.getMaterialType(reqDTO.getMaterialParentTypeId());
        reqDTO.setMaterialParentTypeCode(materialTypeDO.getCode());
        reqDTO.setMaterialParentTypeName(materialTypeDO.getName());
        materialConfigApi.updateMaterialConfig(reqDTO);

    }
}

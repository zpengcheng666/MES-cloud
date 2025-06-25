package com.miyu.module.wms.service.materialconfig;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.wms.controller.admin.materialconfig.vo.*;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.materialconfig.MaterialConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料类型 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class MaterialConfigServiceImpl implements MaterialConfigService {

    @Resource
    private MaterialConfigMapper materialConfigMapper;

    @Override
    public String createMaterialConfig(MaterialConfigSaveReqVO createReqVO) {
/*
        if(createReqVO.getMaterialParentId() != null){
            if(!createReqVO.getMaterialParentId().isEmpty() ){
                // 校验父物料类型的有效性
                validateParentMaterialConfig(null, createReqVO.getMaterialParentId());
                // 校验父物料类型的唯一性
                validateMaterialParentIdMaterialNumberUnique(null, createReqVO.getMaterialParentId(), createReqVO.getMaterialNumber());
            }
        }
*/

        if(StringUtils.isBlank(createReqVO.getId())){
            throw exception(MATERIAL_TYPE_PARENT_NOT_EXITS);
        }
        MaterialConfigDO materialConfig = BeanUtils.toBean(createReqVO, MaterialConfigDO.class);

        // 来源类型不为空 是 说明是毛坯生产成半成品 物料类型更新 物料条码不变
        if(StringUtils.isNotBlank(createReqVO.getMaterialSourceId())){
            MaterialConfigDO mainConfig = this.getMaterialConfig(createReqVO.getMaterialSourceId());
            if(mainConfig == null){
                throw exception(MATERIAL_TYPE_PARENT_NOT_EXITS);
            }
            // 生产的半成品不能为容器类物料 因为物料实体不会新生成
            if(Objects.equals(mainConfig.getMaterialContainer(), DictConstants.INFRA_BOOLEAN_TINYINT_YES)){
                throw exception(MATERIAL_TYPE_HALF_PRODUCT_CONTAINER_ERROR);
            }
            materialConfig.setContainerType(mainConfig.getContainerType());
            materialConfig.setMaterialOutRule(mainConfig.getMaterialOutRule());
            materialConfig.setMaterialContainer(mainConfig.getMaterialContainer());
            materialConfig.setMaterialStorage(mainConfig.getMaterialStorage());
            materialConfig.setContainerConfigIds(mainConfig.getContainerConfigIds());
            materialConfig.setDefaultWarehouseId(mainConfig.getDefaultWarehouseId());
        }
        // 插入
        validateMaterialConfigRelation(materialConfig);
        materialConfigMapper.insert(materialConfig);
        // 返回
        return materialConfig.getId();
    }

    @Override
    public void updateMaterialConfig(MaterialConfigSaveReqVO updateReqVO) {
        // 校验存在
        /*validateMaterialConfigExists(updateReqVO.getId());
        if(updateReqVO.getMaterialParentId() !=null){
            if(!updateReqVO.getMaterialParentId().isEmpty()){
                // 校验父物料类型的有效性
                validateParentMaterialConfig(updateReqVO.getId(), updateReqVO.getMaterialParentId());
                // 校验父物料类型的唯一性
                validateMaterialParentIdMaterialNumberUnique(updateReqVO.getId(), updateReqVO.getMaterialParentId(), updateReqVO.getMaterialNumber());
            }
        }*/
        if(StringUtils.isBlank(updateReqVO.getId())){
            throw exception(MATERIAL_TYPE_PARENT_NOT_EXITS);
        }
        // 更新
        MaterialConfigDO updateObj = BeanUtils.toBean(updateReqVO, MaterialConfigDO.class);
        validateMaterialConfigRelation(updateObj);
        materialConfigMapper.updateById(updateObj);
    }


    // 检验物料属性管理关系
    private void validateMaterialConfigRelation(MaterialConfigDO entity) {
        if(entity.getMaterialManage() == null){
            entity.setMaterialManage(1);
        }
        if(entity.getMaterialContainer() == null){
            entity.setMaterialContainer(0);
        }
        if(entity.getMaterialStorage() == null){
            entity.setMaterialStorage(1);
        }
        if(Objects.equals(entity.getMaterialManage(), 2)){
            //如果是批量管理
            // 则默认不是容器
            entity.setMaterialContainer(0);
        }
        if(Objects.equals(entity.getMaterialContainer() ,0)){
            // 如果不是容器
            // 则默认单储位
            entity.setMaterialStorage(1);
        }
        if(Objects.equals(entity.getMaterialStorage(),1)){
            // 如果是单储位
            // 则默认 层排列为 null
            entity.setMaterialLayer(null);
            entity.setMaterialRow(null);
            entity.setMaterialCol(null);
        }
    }

    @Override
    public void deleteMaterialConfig(String id) {
        // 校验存在
        validateMaterialConfigExists(id);
        // 校验是否有子物料类型
       /* if (materialConfigMapper.selectCountByMaterialParentId(id) > 0) {
            throw exception(MATERIAL_TYPE_EXITS_CHILDREN);
        }*/
        // 删除
        materialConfigMapper.deleteById(id);
    }

    @Override
    public int insert(MaterialConfigDO entity){
        this.saveHandle(entity);
        return materialConfigMapper.insert(entity);
    }

    @Override
    public int updateById(MaterialConfigDO entity){
        this.saveHandle(entity);
        return materialConfigMapper.updateById(entity);
    }


    /**
     * 物料类型的规则： 只有单件管理的物料 才能赋予容器属性（否则无法自动生成）
     * 物料类型的规则： 单储位物料生成的储位编码为物料条码
     */
    private void saveHandle(MaterialConfigDO entity) {
        // 单件1 false \ 批量2 true
        boolean isMaterialManage = entity.getMaterialManage() == 2;
        // 批量  并且 为容器
        if(isMaterialManage && entity.getMaterialContainer() == 1){
            throw exception(MATERIAL_TYPE_CONTAINER_ERROR);
        }

    }

    private void validateMaterialConfigExists(String id) {
        if (materialConfigMapper.selectById(id) == null) {
            throw exception(MATERIAL_TYPE_NOT_EXISTS);
        }
    }

    /*private void validateParentMaterialConfig(String id, String materialParentId) {
        if (materialParentId == null || MaterialConfigDO.MATERIAL_PARENT_ID_ROOT.equals(materialParentId)) {
            return;
        }
        // 1. 不能设置自己为父物料类型
        if (Objects.equals(id, materialParentId)) {
            throw exception(MATERIAL_TYPE_PARENT_ERROR);
        }
        // 2. 父物料类型不存在
        MaterialConfigDO parentMaterialConfig = materialConfigMapper.selectById(materialParentId);
        if (parentMaterialConfig == null) {
            throw exception(MATERIAL_TYPE_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父物料类型，如果父物料类型是自己的子物料类型，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            materialParentId = parentMaterialConfig.getMaterialParentId();
            if (Objects.equals(id, materialParentId)) {
                throw exception(MATERIAL_TYPE_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父物料类型
            if (materialParentId == null || MaterialConfigDO.MATERIAL_PARENT_ID_ROOT.equals(materialParentId)) {
                break;
            }
            parentMaterialConfig = materialConfigMapper.selectById(materialParentId);
            if (parentMaterialConfig == null) {
                break;
            }
        }
    }*/

    /*private void validateMaterialParentIdMaterialNumberUnique(String id, String materialParentId, String materialNumber) {
        MaterialConfigDO materialConfig = materialConfigMapper.selectByMaterialParentIdAndMaterialNumber(materialParentId, materialNumber);
        if (materialConfig == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的物料类型
        if (id == null) {
            throw exception(MATERIAL_TYPE_MATERIAL_PARENT_ID_DUPLICATE);
        }
        if (!Objects.equals(materialConfig.getId(), id)) {
            throw exception(MATERIAL_TYPE_MATERIAL_PARENT_ID_DUPLICATE);
        }
    }*/

    @Override
    public MaterialConfigDO getMaterialConfig(String id) {
        return materialConfigMapper.selectById(id);
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigList() {
        return materialConfigMapper.selectList();
    }

    @Override
    public PageResult<MaterialConfigDO> getMaterialConfigPage(MaterialConfigPageReqVO listReqVO) {
        return materialConfigMapper.getMaterialConfigPage(listReqVO);
    }

    @Override
    public MaterialConfigDO getMaterialConfigByMaterialStockId(String materialStockId) {
        return materialConfigMapper.selectByMaterialStockId(materialStockId);
    }

    @Override
    public List<MaterialConfigDO> getMaterialConfigListByIds(Collection<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return materialConfigMapper.selectBatchIds(ids);
    }

    @Override
    public MaterialConfigDO getMaterialConfigByBarCode(String barCode) {
        return materialConfigMapper.getMaterialConfigByBarcode(barCode);
    }


}

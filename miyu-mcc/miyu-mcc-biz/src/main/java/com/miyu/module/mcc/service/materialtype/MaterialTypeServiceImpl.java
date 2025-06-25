package com.miyu.module.mcc.service.materialtype;

import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.mcc.dal.mysql.materialconfig.MaterialConfigMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.mcc.controller.admin.materialtype.vo.*;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.materialtype.MaterialTypeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;

/**
 * 编码类别属性表(树形结构) Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class MaterialTypeServiceImpl implements MaterialTypeService {

    @Resource
    private MaterialTypeMapper materialTypeMapper;


    @Resource
    private MaterialConfigMapper materialConfigMapper;

    @Override
    public String createMaterialType(MaterialTypeSaveReqVO createReqVO) {
        // 校验父类型id的有效性
        validateParentMaterialType(null, createReqVO.getParentId());
        // 校验名称的唯一性
        validateMaterialTypeNameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // 插入
        MaterialTypeDO materialType = BeanUtils.toBean(createReqVO, MaterialTypeDO.class);


        //判断位数超限制
       Integer length =  materialType.getCode().getBytes().length;
//       if (length>materialType.getBitNumber()){
//           throw exception(ENCODING_TYPE_NUMBER_LIMIT);
//       }
        materialType.setBitNumber(length);
        if (materialType.getParentId().equals("0")){
            materialType.setLevel(0);
        }else {
            MaterialTypeDO materialTypeDO = materialTypeMapper.selectById(materialType.getParentId());
            if (materialTypeDO.getLevel().equals(materialTypeDO.getLevelLimit())){
                throw exception(ENCODING_TYPE_LEVEL_LIMIT);
            }
            materialType.setLevel(materialTypeDO.getLevel()+1);
            materialType.setLevelLimit(materialTypeDO.getLevelLimit());

        }
        materialTypeMapper.insert(materialType);
        // 返回
        return materialType.getId();
    }

    @Override
    public void updateMaterialType(MaterialTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialTypeExists(updateReqVO.getId());
        // 校验父类型id的有效性
        validateParentMaterialType(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验名称的唯一性
        validateMaterialTypeNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        MaterialTypeDO updateObj = BeanUtils.toBean(updateReqVO, MaterialTypeDO.class);
        Integer length =  updateReqVO.getCode().getBytes().length;
        updateObj.setBitNumber(length);
        materialTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterialType(String id) {
        // 校验存在
        validateMaterialTypeExists(id);
        // 校验是否有子编码类别属性表(树形结构)
        if (materialTypeMapper.selectCountByParentId(id) > 0) {
            throw exception(ENCODING_TYPE_EXITS_CHILDREN);
        }
        // 删除
        materialTypeMapper.deleteById(id);
    }

    private void validateMaterialTypeExists(String id) {
        if (materialTypeMapper.selectById(id) == null) {
            throw exception(ENCODING_TYPE_NOT_EXISTS);
        }
    }

    private void validateParentMaterialType(String id, String parentId) {
        if (parentId == null || MaterialTypeDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父编码类别属性表(树形结构)
        if (Objects.equals(id, parentId)) {
            throw exception(ENCODING_TYPE_PARENT_ERROR);
        }
        // 2. 父编码类别属性表(树形结构)不存在
        MaterialTypeDO parentMaterialType = materialTypeMapper.selectById(parentId);
        if (parentMaterialType == null) {
            throw exception(ENCODING_TYPE_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父编码类别属性表(树形结构)，如果父编码类别属性表(树形结构)是自己的子编码类别属性表(树形结构)，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentMaterialType.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(ENCODING_TYPE_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父编码类别属性表(树形结构)
            if (parentId == null || MaterialTypeDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentMaterialType = materialTypeMapper.selectById(parentId);
            if (parentMaterialType == null) {
                break;
            }
        }
    }

    private void validateMaterialTypeNameUnique(String id, String parentId, String name) {
        MaterialTypeDO materialType = materialTypeMapper.selectByParentIdAndName(parentId, name);
        if (materialType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的编码类别属性表(树形结构)
        if (id == null) {
            throw exception(ENCODING_TYPE_NAME_DUPLICATE);
        }
        if (!Objects.equals(materialType.getId(), id)) {
            throw exception(ENCODING_TYPE_NAME_DUPLICATE);
        }
    }

    @Override
    public MaterialTypeDO getMaterialType(String id) {
        return materialTypeMapper.selectById(id);
    }

    @Override
    public List<MaterialTypeDO> getMaterialTypeList(MaterialListReqVO listReqVO) {
        return materialTypeMapper.selectList(listReqVO);
    }


    @Override
    public List<MaterialTypeDO> getMaterialTypeConfigList(MaterialListReqVO listReqVO) {
        List<MaterialConfigDO> configList  = materialConfigMapper.selectList();
        List<MaterialTypeDO> typeList = configList.stream()
                .map(o -> {
                    MaterialTypeDO type = new MaterialTypeDO();
                    type.setParentId(o.getMaterialTypeId());
                    type.setId(o.getId());
                    type.setName(o.getMaterialName() + "_" +o.getMaterialNumber());
                    return type;
                }).collect(Collectors.toList());
        List<MaterialTypeDO> list = materialTypeMapper.selectList(listReqVO);
        list.addAll(typeList);
        return list;
    }

    @Override
    public List<MaterialTypeDO> getMaterialTypeByCode(String code) {
        return materialTypeMapper.selectList(MaterialTypeDO::getCode,code);
    }

}

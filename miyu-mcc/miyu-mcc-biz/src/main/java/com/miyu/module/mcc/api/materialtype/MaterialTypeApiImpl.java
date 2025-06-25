package com.miyu.module.mcc.api.materialtype;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.mcc.api.materialtype.dto.MaterialTypeReqDTO;
import com.miyu.module.mcc.api.materialtype.dto.MaterialTypeRespDTO;
import com.miyu.module.mcc.controller.admin.materialtype.vo.MaterialTypeSaveReqVO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import com.miyu.module.mcc.dal.mysql.materialtype.MaterialTypeMapper;
import com.miyu.module.mcc.service.materialtype.MaterialTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class MaterialTypeApiImpl implements MaterialTypeApi{
    @Resource
    private MaterialTypeMapper materialTypeMapper;
    @Resource
    private MaterialTypeService materialTypeService;

    @Override
    public CommonResult<MaterialTypeRespDTO> getByCode(String code) {
        LambdaQueryWrapper<MaterialTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialTypeDO::getCode,code);
        List<MaterialTypeDO> materialTypeList = materialTypeMapper.selectList(wrapper);
        if(materialTypeList.size()==0){
            return CommonResult.success(null);
        }
        List<MaterialTypeRespDTO> materialTypeRespDTOS = BeanUtils.toBean(materialTypeList, MaterialTypeRespDTO.class);
        return CommonResult.success(materialTypeRespDTOS.get(0));
    }

    @Override
    public CommonResult<String> createMaterialType(@Valid MaterialTypeReqDTO reqDTO) {
        MaterialTypeSaveReqVO saveReqVO = BeanUtils.toBean(reqDTO,MaterialTypeSaveReqVO.class);
        return CommonResult.success(materialTypeService.createMaterialType(saveReqVO));
    }

    @Override
    public CommonResult<List<MaterialTypeRespDTO>> getMaterialTypeList(Collection<String> ids) {
        List<MaterialTypeDO> materialTypeDOS = materialTypeMapper.selectBatchIds(ids);
        return CommonResult.success(BeanUtils.toBean(materialTypeDOS,MaterialTypeRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialTypeRespDTO>> getMaterialTypeListByProperty(Integer encodingProperty) {

        List<MaterialTypeDO> materialTypeDOS = materialTypeMapper.selectList(MaterialTypeDO::getEncodingProperty, encodingProperty);
        return CommonResult.success(BeanUtils.toBean(materialTypeDOS,MaterialTypeRespDTO.class));
    }
}

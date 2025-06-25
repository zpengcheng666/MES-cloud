package com.miyu.module.pdm.service.stepCategory;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.CustomizedAttributeReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.CustomizedAttributeSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.CustomizedAttributeDO;
import com.miyu.module.pdm.dal.mysql.stepCategory.CustomizedAttributeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class CustomizedAttributeServiceImpl implements CustomizedAttributeService {

    @Resource
    private CustomizedAttributeMapper customizedAttributeMapper;

    @Override
    public List<CustomizedAttributeDO> getCustomizedAttributesByCategoryId(String id) {
        return customizedAttributeMapper.getCustomizedAttributesByCategoryId(id);
    }

    @Override
    public String customizedAttribute(CustomizedAttributeSaveReqVO createReqVO) {

        // 插入
        CustomizedAttributeDO resultDO = BeanUtils.toBean(createReqVO, CustomizedAttributeDO.class)
                .setId(IdUtil.fastSimpleUUID())
                .setCategoryId(createReqVO.getCategoryId())
                .setAttrNameCn(createReqVO.getAttrNameCn())
                .setAttrNameEn(createReqVO.getAttrNameEn())
                .setAttrType(createReqVO.getAttrType())
                .setAttrUnit(createReqVO.getAttrUnit())
                .setAttrOrder(createReqVO.getAttrOrder())
                .setAttrDefaultValue(createReqVO.getAttrDefaultValue())
                .setIsMultvalues(createReqVO.getIsMultvalues())
                .setAttrEffectiveValue(createReqVO.getAttrEffectiveValue())
                ;
        customizedAttributeMapper.insert(resultDO);
        return resultDO.getId();
    }

    @Override
    public void updatecustomizedAttribute(CustomizedAttributeSaveReqVO updateReqVO) {
        customizedAttributeMapper.updateById(updateReqVO);
    }

    @Override
    public void deletecustomizedAttribute(String id) {
        customizedAttributeMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateIndex(CustomizedAttributeReqVO updateReqVO) {

    }

    @Override
    public CustomizedAttributeDO getCustomizedAttribute(String id) {
        return customizedAttributeMapper.selectById(id);
    }

    @Override
    public List<CustomizedAttributeDO> getCustomizedAttributeList(CustomizedAttributeSaveReqVO listReqVO) {
        return customizedAttributeMapper.selectList();
    }
}

package com.miyu.module.pdm.service.stepCategory;

import com.miyu.module.pdm.controller.admin.stepCategory.vo.CustomizedAttributeReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.CustomizedAttributeSaveReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategoryListReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.CustomizedAttributeDO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.StepCategoryDO;

import java.util.List;

public interface CustomizedAttributeService {
    List<CustomizedAttributeDO> getCustomizedAttributesByCategoryId(String id);

    String customizedAttribute(CustomizedAttributeSaveReqVO createReqVO);

    void updatecustomizedAttribute(CustomizedAttributeSaveReqVO updateReqVO);

    void deletecustomizedAttribute(String id);

    void updateIndex(CustomizedAttributeReqVO updateReqVO);

    CustomizedAttributeDO getCustomizedAttribute(String id);

    List<CustomizedAttributeDO> getCustomizedAttributeList(CustomizedAttributeSaveReqVO listReqVO);
}
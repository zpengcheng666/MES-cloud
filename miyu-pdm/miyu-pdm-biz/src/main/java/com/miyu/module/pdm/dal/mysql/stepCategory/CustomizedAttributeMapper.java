package com.miyu.module.pdm.dal.mysql.stepCategory;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.CustomizedAttributeSaveReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.CustomizedAttributeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CustomizedAttributeMapper extends BaseMapperX<CustomizedAttributeDO> {


        @Select("<script>" +
                "SELECT CA.id AS id, " +
                "CA.category_id AS categoryId, " +
                "CA.attr_name_cn AS attrNameCn, " +
                "CA.attr_name_en AS attrNameEn, " +
                "CA.attr_type AS attrType, " +
                "CA.attr_unit AS attrUnit, " +
                "CA.attr_order AS attrOrder, " +
                "CA.attr_default_value AS attrDefaultValue, " +
                        "CA.is_multvalues AS isMultvalues, " +
                "CA.attr_effective_value AS attrEffectiveValue, " +
                "SA.name AS name " +
                "FROM capp_customized_attribute CA " +
                "LEFT JOIN capp_step_category SA ON SA.id = CA.category_id " +
                "WHERE CA.category_id = #{category_id} " +
                "AND CA.deleted = 0 " +
                "ORDER BY CA.attr_order ASC " +
                "</script>")
        List<CustomizedAttributeDO> getCustomizedAttributesByCategoryId(String id);

}

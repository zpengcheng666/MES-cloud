package com.miyu.module.es.dal.mysql.visitlicense;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.miyu.module.es.controller.admin.visit.vo.VisitPageReqVO;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import com.miyu.module.es.dal.dataobject.visitlicense.VisitLicenseDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 访客记录 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface VisitLicenseMapper extends BaseMapperX<VisitLicenseDO> {

    @TenantIgnore
    @Select("select * FROM `es_visit_license` WHERE visit_record_id = #{id} ")
    List<VisitLicenseDO> selectByVisitRecordId(@Param("id") String id);

}
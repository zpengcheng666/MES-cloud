package com.miyu.cloud.macs.dal.mysql.visitorRegion;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 访客区域权限 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface VisitorRegionMapper extends BaseMapperX<VisitorRegionDO> {

    default PageResult<VisitorRegionDO> selectPage(VisitorRegionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VisitorRegionDO>()
                .eqIfPresent(VisitorRegionDO::getVisitorId, reqVO.getVisitorId())
                .eqIfPresent(VisitorRegionDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(VisitorRegionDO::getApplicationId, reqVO.getApplicationId())
                .betweenIfPresent(VisitorRegionDO::getEffectiveDate, reqVO.getEffectiveDate())
                .betweenIfPresent(VisitorRegionDO::getInvalidDate, reqVO.getInvalidDate())
                .orderByDesc(VisitorRegionDO::getId));
    }

    @Select("SELECT vr.*,aa.application_number applicationNumber,r.`code` regionCode,r.`name` regionName, " +
            "CASE WHEN aa.`status`IS NULL THEN 0  WHEN aa.`status`='0' THEN 0 WHEN aa.`status`='4' THEN 3 WHEN aa.`status`='3' THEN 3 " +
            "WHEN vr.invalid_date< CURDATE() THEN 2 WHEN vr.effective_date> CURDATE() THEN 0 ELSE 1 END `STATUS` " +
            "FROM (SELECT*FROM macs_visitor_region WHERE visitor_id= #{visitorId} ) vr " +
            "LEFT JOIN macs_access_application aa ON vr.application_id=aa.id " +
            "LEFT JOIN macs_region r ON vr.region_id=r.id")
    List<VisitorRegionRespVO> selectPageByVisitor(Page<VisitorRegionRespVO> page, @Param("visitorId") String visitorId);

    @Select("SELECT vr.*,aa.application_number applicationNumber,r.`code` regionCode,r.`name` regionName, " +
            "CASE WHEN aa.`status`IS NULL THEN 0  WHEN aa.`status`='0' THEN 0 WHEN aa.`status`='2' THEN 3 WHEN aa.`status`='3' THEN 3 " +
            "WHEN vr.invalid_date< CURDATE() THEN 2 WHEN vr.effective_date> CURDATE() THEN 0 ELSE 1 END `STATUS` " +
            "FROM (SELECT*FROM macs_visitor_region WHERE visitor_id= #{visitorId} ) vr " +
            "LEFT JOIN macs_access_application aa ON vr.application_id=aa.id " +
            "LEFT JOIN macs_region r ON vr.region_id=r.id " +
            "WHERE aa.id = #{applicationId} ")
    List<VisitorRegionRespVO> selectPageByVisitorAndApplication(Page<VisitorRegionRespVO> page, @Param("visitorId") String visitorId, @Param("applicationId") String applicationId);

    @Select("SELECT vr.*FROM `macs_visitor_region` vr LEFT JOIN (" +
            "SELECT*FROM `macs_visitor` WHERE `status`=1 AND deleted=0) v ON vr.visitor_id=v.id " +
            "WHERE vr.region_id= #{regionId} " +
            "AND vr.effective_date< NOW() AND vr.invalid_date> NOW()")
    List<VisitorRegionDO> getAuthorizedVisitorByRegion(@Param("regionId") String regionId);

    @Select("SELECT vr.*,r.`code` regionCode,r.`name` regionName FROM ( " +
            "SELECT*FROM `macs_visitor_region` WHERE application_id= #{applicationId} AND visitor_id= #{visitorId} ) vr LEFT JOIN macs_region r ON vr.region_id=r.id")
    List<VisitorRegionRespVO> regionShowList(@Param("applicationId") String applicationId, @Param("visitorId") String visitorId);
}

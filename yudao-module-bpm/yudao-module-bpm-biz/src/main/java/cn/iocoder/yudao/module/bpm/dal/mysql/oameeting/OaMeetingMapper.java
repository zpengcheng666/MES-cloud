package cn.iocoder.yudao.module.bpm.dal.mysql.oameeting;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oameeting.OaMeetingDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OA 会议申请 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OaMeetingMapper extends BaseMapperX<OaMeetingDO> {

    default PageResult<OaMeetingDO> selectPage(OaMeetingPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaMeetingDO>()
                .eqIfPresent(OaMeetingDO::getTitle, reqVO.getTitle())
//                .betweenIfPresent(OaMeetingDO::getMDate, reqVO.getMDate())
                .eqIfPresent(OaMeetingDO::getStaff, reqVO.getStaff())
                .betweenIfPresent(OaMeetingDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(OaMeetingDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(OaMeetingDO::getMroom, reqVO.getMroom())
                .eqIfPresent(OaMeetingDO::getDescription, reqVO.getDescription())
                .eqIfPresent(OaMeetingDO::getDocument, reqVO.getDocument())
                .betweenIfPresent(OaMeetingDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(OaMeetingDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaMeetingDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .orderByDesc(OaMeetingDO::getId));
    }

}

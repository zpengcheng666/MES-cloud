package com.miyu.module.qms.dal.mysql.inspectiontoolverificationrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import com.miyu.module.qms.dal.dataobject.inspectiontoolverificationrecord.InspectionToolVerificationRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo.*;

/**
 * 检验工具校准记录 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionToolVerificationRecordMapper extends BaseMapperX<InspectionToolVerificationRecordDO> {

    default PageResult<InspectionToolVerificationRecordDO> selectPage(InspectionToolVerificationRecordPageReqVO reqVO) {
        MPJLambdaWrapperX<InspectionToolVerificationRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionToolDO.class, InspectionToolDO::getId, InspectionToolVerificationRecordDO::getToolId)
                .selectAs(InspectionToolDO::getName, InspectionToolVerificationRecordDO::getToolName)
                .selectAll(InspectionToolVerificationRecordDO.class)
                .like(reqVO.getToolName()!=null, InspectionToolDO::getName, reqVO.getToolName());


        wrapperX.eq(reqVO.getVerificationDateBegin() != null,"DATE_FORMAT (t.verification_date_begin,'%Y-%m-%d')", reqVO.getVerificationDateBegin());
        wrapperX.eq(reqVO.getVerificationDateBeginAct() != null, "DATE_FORMAT (t.verification_date_begin_act,'%Y-%m-%d')", reqVO.getVerificationDateBeginAct());
        wrapperX.eq(reqVO.getVerificationDateEnd() !=null,"DATE_FORMAT (t.verification_date_end,'%Y-%m-%d')", reqVO.getVerificationDateEnd());

        return selectPage(reqVO, wrapperX
                .betweenIfPresent(InspectionToolVerificationRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionToolVerificationRecordDO::getStockId, reqVO.getStockId())
                .eqIfPresent(InspectionToolVerificationRecordDO::getStatus, reqVO.getStatus())
                .orderByDesc(InspectionToolVerificationRecordDO::getId));
    }

    default InspectionToolVerificationRecordDO selectToolVerificationRecordById(String id) {
        MPJLambdaWrapperX<InspectionToolVerificationRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionToolDO.class, InspectionToolDO::getId, InspectionToolVerificationRecordDO::getToolId)
                .selectAs(InspectionToolDO::getName, InspectionToolVerificationRecordDO::getToolName)
                .selectAll(InspectionToolVerificationRecordDO.class);
        wrapperX.eq(InspectionToolVerificationRecordDO ::getId, id);
        return selectOne(wrapperX);
    }

    /**
     * 获取待完成的送检任务集合
     * @return
     */
    default PageResult<InspectionToolVerificationRecordDO> selectVerificationTaskPage(InspectionToolVerificationRecordPageReqVO reqVO) {
        MPJLambdaWrapperX<InspectionToolVerificationRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionToolDO.class, InspectionToolDO::getId, InspectionToolVerificationRecordDO::getToolId)
                .selectAs(InspectionToolDO::getName, InspectionToolVerificationRecordDO::getToolName)
                .selectAll(InspectionToolVerificationRecordDO.class)
                .and(w -> w.eq(InspectionToolVerificationRecordDO::getStatus, 0).or()
                .eq(InspectionToolVerificationRecordDO::getStatus, 1));
        return selectPage(reqVO, wrapperX);
    }
}

package com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetask;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetask.InspectionSheetGenerateTaskDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo.*;

/**
 * 检验单 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetGenerateTaskMapper extends BaseMapperX<InspectionSheetGenerateTaskDO> {

    default PageResult<InspectionSheetGenerateTaskDO> selectPage(InspectionSheetGenerateTaskPageReqVO reqVO) {

        MPJLambdaWrapperX<InspectionSheetGenerateTaskDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAs("(select count(1) from qms_inspection_sheet_generate_task_detail d where d.task_id = t.id)", InspectionSheetGenerateTaskDO::getQuantity)
                .selectAll(InspectionSheetGenerateTaskDO.class);

        return selectPage(reqVO, wrapper
                .betweenIfPresent(InspectionSheetGenerateTaskDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(InspectionSheetGenerateTaskDO::getSheetName, reqVO.getSheetName())
                .eqIfPresent(InspectionSheetGenerateTaskDO::getSheetNo, reqVO.getSheetNo())
                .eqIfPresent(InspectionSheetGenerateTaskDO::getRecordNumber, reqVO.getRecordNumber())
                .eqIfPresent(InspectionSheetGenerateTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InspectionSheetGenerateTaskDO::getSourceType, reqVO.getSourceType())
                .orderByDesc(InspectionSheetGenerateTaskDO::getId));
    }

    default PageResult<InspectionSheetGenerateTaskDO> selectInspectionSheetGenerateTaskPage(InspectionSheetGenerateTaskPageReqVO reqVO) {

        MPJLambdaWrapperX<InspectionSheetGenerateTaskDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.eq(InspectionSheetGenerateTaskDO::getStatus, 0);
        return selectPage(reqVO, wrapper);
    }

}

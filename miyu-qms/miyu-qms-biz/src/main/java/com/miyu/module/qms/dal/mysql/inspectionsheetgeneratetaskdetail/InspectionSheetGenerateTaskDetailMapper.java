package com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetaskdetail;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail.vo.*;

/**
 * 检验单生成任务明细 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetGenerateTaskDetailMapper extends BaseMapperX<InspectionSheetGenerateTaskDetailDO> {

    default PageResult<InspectionSheetGenerateTaskDetailDO> selectPage(InspectionSheetGenerateTaskDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionSheetGenerateTaskDetailDO>()
                .betweenIfPresent(InspectionSheetGenerateTaskDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionSheetGenerateTaskDetailDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(InspectionSheetGenerateTaskDetailDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(InspectionSheetGenerateTaskDetailDO::getBarCode, reqVO.getBarCode())
                .orderByDesc(InspectionSheetGenerateTaskDetailDO::getId));
    }

}

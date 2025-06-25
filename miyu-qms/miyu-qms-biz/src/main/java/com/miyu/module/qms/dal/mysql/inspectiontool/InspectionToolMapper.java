package com.miyu.module.qms.dal.mysql.inspectiontool;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectiontool.vo.*;

/**
 * 检测工具 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionToolMapper extends BaseMapperX<InspectionToolDO> {

    default PageResult<InspectionToolDO> selectPage(InspectionToolPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionToolDO>()
                .betweenIfPresent(InspectionToolDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(InspectionToolDO::getName, reqVO.getName())
                .likeIfPresent(InspectionToolDO::getManufacturer, reqVO.getManufacturer())
                .likeIfPresent(InspectionToolDO::getManufacturerNumber, reqVO.getManufacturerNumber())
                .eqIfPresent(InspectionToolDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(InspectionToolDO::getStatus, reqVO.getStatus())
                .orderByDesc(InspectionToolDO::getId));
    }

}
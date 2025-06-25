package com.miyu.module.qms.dal.mysql.inspectionitemconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.inspectionitemconfig.InspectionItemConfigDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionitemconfig.vo.*;

/**
 * 检测项配置表（检测内容名称） Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InspectionItemConfigMapper extends BaseMapperX<InspectionItemConfigDO> {

    default PageResult<InspectionItemConfigDO> selectPage(InspectionItemConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionItemConfigDO>()
                .betweenIfPresent(InspectionItemConfigDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(InspectionItemConfigDO::getName, reqVO.getName())
                .eqIfPresent(InspectionItemConfigDO::getNo, reqVO.getNo())
                .orderByDesc(InspectionItemConfigDO::getId));
    }

}
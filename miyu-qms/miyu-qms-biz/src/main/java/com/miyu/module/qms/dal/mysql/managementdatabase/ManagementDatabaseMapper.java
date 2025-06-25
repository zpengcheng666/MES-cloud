package com.miyu.module.qms.dal.mysql.managementdatabase;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.managementdatabase.ManagementDatabaseDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.managementdatabase.vo.*;

/**
 * 质量管理资料库 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ManagementDatabaseMapper extends BaseMapperX<ManagementDatabaseDO> {

    default PageResult<ManagementDatabaseDO> selectPage(ManagementDatabasePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ManagementDatabaseDO>()
                .eqIfPresent(ManagementDatabaseDO::getTreeId, reqVO.getTreeId())
                .eqIfPresent(ManagementDatabaseDO::getType, reqVO.getType())
                .eqIfPresent(ManagementDatabaseDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ManagementDatabaseDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ManagementDatabaseDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(ManagementDatabaseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ManagementDatabaseDO::getId));
    }

}
package com.miyu.module.qms.dal.mysql.managementtree;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.managementtree.ManagementTreeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.managementtree.vo.*;

/**
 * 质量管理关联树 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ManagementTreeMapper extends BaseMapperX<ManagementTreeDO> {

    default PageResult<ManagementTreeDO> selectPage(ManagementTreePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ManagementTreeDO>()
                .eqIfPresent(ManagementTreeDO::getParent, reqVO.getParent())
                .likeIfPresent(ManagementTreeDO::getNodeName, reqVO.getNodeName())
                .eqIfPresent(ManagementTreeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ManagementTreeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ManagementTreeDO::getId));
    }

}
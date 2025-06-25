package com.miyu.cloud.macs.dal.mysql.user;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.dal.dataobject.user.UserDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.user.vo.*;

/**
 * 门禁用户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserMapper extends BaseMapperX<UserDO> {

    default PageResult<UserDO> selectPage(UserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserDO>()
                .eqIfPresent(UserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(UserDO::getFacePicture, reqVO.getFacePicture())
                .eqIfPresent(UserDO::getFingerprintPicture, reqVO.getFingerprintPicture())
                .orderByDesc(UserDO::getId));
    }

}

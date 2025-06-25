package com.miyu.cloud.macs.service.user;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.controller.admin.user.vo.*;
import com.miyu.cloud.macs.dal.dataobject.user.UserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.macs.restServer.entity.MacsRestUser;

/**
 * 门禁用户 Service 接口
 *
 * @author 芋道源码
 */
public interface UserService {

    /**
     * 创建门禁用户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createUser(@Valid UserSaveReqVO createReqVO);

    /**
     * 更新门禁用户
     *
     * @param updateReqVO 更新信息
     */
    void updateUser(@Valid UserSaveReqVO updateReqVO);

    /**
     * 删除门禁用户
     *
     * @param id 编号
     */
    void deleteUser(String id);

    /**
     * 获得门禁用户
     *
     * @param id 编号
     * @return 门禁用户
     */
    UserDO getUser(String id);

    /**
     * 获得门禁用户分页
     *
     * @param pageReqVO 分页查询
     * @return 门禁用户分页
     */
    PageResult<UserDO> getUserPage(UserPageReqVO pageReqVO);

    UserDO getByUserId(String userId);

    List<UserDO> list();

    List<UserDO> list(QueryWrapper<UserDO> wrapper);

//    AdminUserRespDTO getUserByCharacter(MacsRestUser restUser);

    AdminUserRespDTO getUserByUserCode(String userCode);

    AdminUserRespDTO getUserByFacialFeatureString(String facialFeatureString);

    AdminUserRespDTO getUserByFingerprintString(String fingerprintString);

    void update(UpdateWrapper<UserDO> wrapper);

    UserDO getByName(String userCode, String name);
}

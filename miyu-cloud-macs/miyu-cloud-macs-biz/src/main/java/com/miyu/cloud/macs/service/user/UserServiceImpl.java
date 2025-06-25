package com.miyu.cloud.macs.service.user;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.restServer.entity.MacsRestUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.user.vo.*;
import com.miyu.cloud.macs.dal.dataobject.user.UserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.user.UserMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 门禁用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public String createUser(UserSaveReqVO createReqVO) {
        // 插入
        UserDO user = BeanUtils.toBean(createReqVO, UserDO.class);
        userMapper.insert(user);
        // 返回
        return user.getId();
    }

    @Override
    public void updateUser(UserSaveReqVO updateReqVO) {
        // 校验存在
        validateUserExists(updateReqVO.getId());
        // 更新
        UserDO updateObj = BeanUtils.toBean(updateReqVO, UserDO.class);
        userMapper.updateById(updateObj);
    }

    @Override
    public void deleteUser(String id) {
        // 校验存在
        validateUserExists(id);
        // 删除
        userMapper.deleteById(id);
    }

    private void validateUserExists(String id) {
        if (userMapper.selectById(id) == null) {
            throw exception(USER_NOT_EXISTS);
        }
    }

    @Override
    public UserDO getUser(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public PageResult<UserDO> getUserPage(UserPageReqVO pageReqVO) {
        return userMapper.selectPage(pageReqVO);
    }

    @Override
    public UserDO getByUserId(String userId) {
        Wrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("user_id", userId);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserDO> list() {
        return userMapper.selectList();
    }

    @Override
    public List<UserDO> list(QueryWrapper<UserDO> wrapper) {
        return userMapper.selectList(wrapper);
    }

    /*@Override
    public AdminUserRespDTO getUserByCharacter(MacsRestUser visitor) {
        String userCode = visitor.getCode();
        String facialFeatureString = visitor.getFacialFeatureString();
        String fingerprintString = visitor.getFingerprintString();
        if (userCode != null) {
            return getUserByUserCode(userCode);
        } else if (facialFeatureString != null) {
            return getUserByFacialFeatureString(facialFeatureString);
        } else if (fingerprintString != null) {
            return getUserByFingerprintString(fingerprintString);
        }
        return null;
    }*/

    @Override
    public AdminUserRespDTO getUserByUserCode(String userCode) {
        List<UserDO> macsUsers = userMapper.selectList(new QueryWrapper<UserDO>().eq("code", userCode));
        if (macsUsers.size() > 0) {
            if (macsUsers.size() > 1) throw new RuntimeException("当前卡号存在多名用户!");
            CommonResult<AdminUserRespDTO> userResult = adminUserApi.getUser(Long.valueOf(macsUsers.get(0).getUserId()));
            return userResult.getData();
        }
        return null;
    }

    @Override
    public AdminUserRespDTO getUserByFacialFeatureString(String facialFeatureString) {
        return null;
    }

    @Override
    public AdminUserRespDTO getUserByFingerprintString(String fingerprintString) {
        return null;
    }

    @Override
    public void update(UpdateWrapper<UserDO> wrapper) {
        userMapper.update(wrapper);
    }

    @Override
    public UserDO getByName(String userCode, String name) {
        CommonResult<AdminUserRespDTO> byName = adminUserApi.getByUsername(userCode.trim());
        AdminUserRespDTO user = byName.getData();

        UserDO userDO = null;
        if (user != null) {
            if (name != null && !user.getNickname().equals(name.trim())) {
                return userDO;
            }
            userDO = getByUserId(user.getId()+"");
            userDO.setPhone(user.getMobile());
        }
        return userDO;
    }
}

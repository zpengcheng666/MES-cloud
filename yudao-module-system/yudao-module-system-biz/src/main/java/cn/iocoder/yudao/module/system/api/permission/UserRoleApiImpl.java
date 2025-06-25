package cn.iocoder.yudao.module.system.api.permission;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.UserRoleDO;
import cn.iocoder.yudao.module.system.dal.mysql.permission.UserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class UserRoleApiImpl implements UserRoleApi {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public CommonResult<List<String>> getByUserId(String userId) {
        List<UserRoleDO> userRoleDOS = userRoleMapper.selectList(new QueryWrapper<UserRoleDO>().eq("user_id", userId).eq("deleted", '0'));
        List<String> list = userRoleDOS.stream().map(userRoleDO -> userRoleDO.getRoleId() + "").collect(Collectors.toList());
        return CommonResult.success(list);
    }

    @Override
    public CommonResult<List<String>> getUserIdByRole(String roleId) {
        List<UserRoleDO> userRoleDOS = userRoleMapper.selectList(new QueryWrapper<UserRoleDO>().eq("role_id", roleId).eq("deleted", '0'));
        List<String> list = userRoleDOS.stream().map(userRoleDO -> userRoleDO.getUserId() + "").collect(Collectors.toList());
        return CommonResult.success(list);
    }
}

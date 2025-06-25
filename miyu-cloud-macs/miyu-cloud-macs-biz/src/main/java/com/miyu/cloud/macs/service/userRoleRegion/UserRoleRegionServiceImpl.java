package com.miyu.cloud.macs.service.userRoleRegion;

import cn.iocoder.yudao.module.system.api.permission.UserRoleApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.userRoleRegion.UserRoleRegionDO;
import com.miyu.cloud.macs.dal.mysql.userRoleRegion.UserRoleRegionMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class UserRoleRegionServiceImpl implements UserRoleRegionService {

    @Resource
    private UserRoleRegionMapper userRoleRegionMapper;
    @Resource
    private UserRoleApi userRoleApi;

    @Override
    public List<UserRoleRegionDO> getUserRoleRegion(String id) {
        return userRoleRegionMapper.selectList(new QueryWrapper<UserRoleRegionDO>().eq("region_id",id));
    }

    @Override
    public void updateForAuthority(List<String> regionKeyList, List<String> roleKeys, List<String> userKeys) {
        Map<String, String> diff = getDiff(regionKeyList, roleKeys, userKeys);
        addForAuthority(diff);
        deleteForAuthority(diff);
    }

    public void addForAuthority(List<String> regionKeyList, List<String> roleKeys, List<String> userKeys) {
        Map<String, String> diff = getDiff(regionKeyList, roleKeys, userKeys);
        addForAuthority(diff);
    }

    private void addForAuthority(Map<String, String> diff) {
        Set<UserRoleRegionDO> add = new HashSet<>();
        for (Map.Entry<String, String> entry : diff.entrySet()) {
            if ("1".equals(entry.getValue())) {
                String[] ids = entry.getKey().split("_");
                add.add(new UserRoleRegionDO(ids[0],ids[1],ids[2]));
            }
        }
        userRoleRegionMapper.insertBatch(add);
    }

    public void deleteForAuthority(List<String> regionKeyList, List<String> roleKeys, List<String> userKeys) {
        Map<String, String> diff = getDiff(regionKeyList, roleKeys, userKeys);
        deleteForAuthority(diff);
    }

    private void deleteForAuthority(Map<String, String> diff) {
        Set<String> delete = diff.values().stream().filter(value -> value.length() > 1).collect(Collectors.toSet());
        userRoleRegionMapper.deleteBatchIds(delete);
    }

    private Map<String, String> getDiff(List<String> regionKeyList, List<String> roleKeys, List<String> userKeys) {
        List<UserRoleRegionDO> list = userRoleRegionMapper.selectList();
        Map<String, String> map = new HashMap<>();
        for (UserRoleRegionDO userRoleRegion : list) {
            if (!regionKeyList.contains(userRoleRegion.getRegionId())) continue;
            String key = userRoleRegion.getRegionId() + "_" + userRoleRegion.getRoleId() + "_" + userRoleRegion.getUserId();
            map.put(key, userRoleRegion.getId());
        }
        for (String region : regionKeyList) {
            for (String roleKey : roleKeys) {
                String key = region + "_" + roleKey + "_" + null;
                if (map.remove(key) == null) map.put(key, "1");
            }
            for (String userKey : userKeys) {
                String key = region + "_" + null + "_" + userKey;
                if (map.remove(key) == null) map.put(key, "1");
            }
        }
        return map;
    }

    @Override
    public boolean validateAccess(AdminUserRespDTO user, RegionDO region) {
        List<String> roleList = userRoleApi.getByUserId(user.getId() + "").getData();
        QueryWrapper<UserRoleRegionDO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId()).or().in("role_id", roleList);
        List<UserRoleRegionDO> macsUserRoleRegions = userRoleRegionMapper.selectList(wrapper);
        if (macsUserRoleRegions.size() == 0) throw new RuntimeException("未查询到相应权限");
        return true;
    }

    @Override
    public List<UserRoleRegionDO> list(QueryWrapper<UserRoleRegionDO> queryWrapper) {
        return userRoleRegionMapper.selectList(queryWrapper);
    }

    @Override
    public void updateForUserAuthority(List<String> regionKeyList, List<String> userIds) {
        Set<String> deleteList = new HashSet<>();
        Set<UserRoleRegionDO> addList = new HashSet<>();
        for (String userId : userIds) {
            List<UserRoleRegionDO> userRoleRegions = userRoleRegionMapper.selectList(new QueryWrapper<UserRoleRegionDO>().eq("user_id", userId));
            for (UserRoleRegionDO userRoleRegion : userRoleRegions) {
                if (!regionKeyList.remove(userRoleRegion.getRegionId())) {
                    deleteList.add(userRoleRegion.getId());
                }
            }
            for (String id : regionKeyList) {
                addList.add(new UserRoleRegionDO(id,null,userId));
            }
        }
        if (!addList.isEmpty())
            userRoleRegionMapper.insertBatch(addList);
        if (!deleteList.isEmpty())
        userRoleRegionMapper.deleteBatchIds(deleteList);
    }
}

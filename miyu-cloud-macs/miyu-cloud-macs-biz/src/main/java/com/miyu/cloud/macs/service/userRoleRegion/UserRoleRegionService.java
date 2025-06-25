package com.miyu.cloud.macs.service.userRoleRegion;

import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.userRoleRegion.UserRoleRegionDO;

import java.util.List;

public interface UserRoleRegionService {


    List<UserRoleRegionDO> getUserRoleRegion(String id);

    void updateForAuthority(List<String> regionKeyList, List<String> roleKeys, List<String> userKeys);

    void addForAuthority(List<String> regionKeyList, List<String> roleKeys, List<String> userKeys);

    boolean validateAccess(AdminUserRespDTO user, RegionDO region);

    List<UserRoleRegionDO> list(QueryWrapper<UserRoleRegionDO> user_id);

    void updateForUserAuthority(List<String> regionKeyList, List<String> userIds);
}

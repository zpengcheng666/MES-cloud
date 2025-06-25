package com.miyu.cloud.macs.controller.admin.userRoleRegion;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.userRoleRegion.UserRoleRegionDO;
import com.miyu.cloud.macs.service.region.RegionService;
import com.miyu.cloud.macs.service.userRoleRegion.UserRoleRegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户角色")
@RestController
@RequestMapping("/macs/userRoleRegion")
@Validated
public class UserRoleRegionController {

    @Resource
    private UserRoleRegionService userRoleRegionService;
    @Resource
    private RegionService regionService;

    @GetMapping("/getPermissionsByRegionId")
    @Operation(summary = "获得用户角色")
    public CommonResult<Map<String,Object>> getUserRoleRegion(@RequestParam("id") String id) {
        List<UserRoleRegionDO> userRoleRegionList = userRoleRegionService.getUserRoleRegion(id);
        List<String> userSelection = new ArrayList<>();
        List<String> roleSelection = new ArrayList<>();
        for (UserRoleRegionDO userRoleRegion : userRoleRegionList) {
            if (userRoleRegion.getUserId() != null) userSelection.add(userRoleRegion.getUserId());
            if (userRoleRegion.getRoleId() != null) roleSelection.add(userRoleRegion.getRoleId());
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("userSelection", userSelection);
        data.put("roleSelection", roleSelection);
        return success(data);
    }

    @PostMapping("/updateUserRoleForAuthority")
    @Operation(summary="更新用户角色权限")
    public CommonResult<?> updateUserRoleForAuthority(@RequestBody Map<String, List<String>> params) {
        List<String> regionKeys = params.get("regionKeys");
        List<String> regionKeyList = getRegionKeyAndParents(regionKeys);
        List<String> roleKeys = params.get("roleKeys");
        List<String> userKeys = params.get("userKeys");
        userRoleRegionService.updateForAuthority(regionKeyList, roleKeys, userKeys);
        return success(true);
    }

    @PostMapping("/addUserRoleForAuthority")
    @Operation(summary="添加用户角色权限")
    public CommonResult<?> addUserRoleForAuthority(@RequestBody Map<String, List<String>> params) {
        List<String> regionKeys = params.get("regionKeys");
        List<String> regionKeyList = getRegionKeyAndParents(regionKeys);
        List<String> roleKeys = params.get("roleKeys");
        List<String> userKeys = params.get("userKeys");
        userRoleRegionService.addForAuthority(regionKeyList, roleKeys, userKeys);
        return success(true);
    }

    private List<String> getRegionKeyAndParents(List<String> regionKeys) {
        List<RegionDO> list = regionService.list();
        Map<String, RegionDO> map = list.stream().collect(Collectors.toMap(RegionDO::getId, region -> region, (a, b) -> b));
        for (int i = 0; i < regionKeys.size(); i++) {
            RegionDO region = map.get(regionKeys.get(i));
            if (region != null && region.getParentId() != null) {
                if (!regionKeys.contains(region.getParentId())) regionKeys.add(region.getParentId());
            }
        }
        return regionKeys;
    }

    @GetMapping(value = "/userRegion")
    @Operation(summary="获取用户权限")
    public CommonResult<?> getUserRegion(@RequestParam(name="userId",required=true) String userId) {
        List<RegionDO> regionList = regionService.list();
        List<UserRoleRegionDO> userRegionList = userRoleRegionService.list(new QueryWrapper<UserRoleRegionDO>().eq("user_id", userId));
        Map<String,Object> result = new HashMap<>();
        List<String> allTreeKeys = new ArrayList<>();
        Map<String,Map<String,Object>> treeListMap = new HashMap<>();
        List<Map<String,Object>> treeList = new ArrayList<>();
        for (RegionDO region : regionList) {
            allTreeKeys.add(region.getId());
            Map<String,Object> regionMap = new HashMap<>();
            regionMap.put("key", region.getId());
            regionMap.put("title", region.getName());
            regionMap.put("children", new ArrayList<>());
            if (region.getParentId() != null) {
                regionMap.put("parentId", region.getParentId());
            } else {
                treeList.add(regionMap);
            }
            treeListMap.put(region.getId(), regionMap);
        }
        for (Map.Entry<String, Map<String, Object>> entry : treeListMap.entrySet()) {
            Map<String, Object> map = entry.getValue();
            String parentId = (String) map.get("parentId");
            if (parentId != null) {
                Map<String, Object> parent = treeListMap.get(parentId);
                if (parent != null) {
                    List children = (List) parent.get("children");
                    children.add(map);
                }
            }
        }
        List<String> checkedKeys = userRegionList.stream().map(UserRoleRegionDO::getRegionId).collect(Collectors.toList());
        result.put("treeList",treeList);
        result.put("allTreeKeys",allTreeKeys);
        result.put("checkedKeys",checkedKeys);
        return success(result);
    }

    @PostMapping("/userRegionUpdate")
    @Operation(summary="更新用户权限")
    public CommonResult<?>updateUserRegion(@RequestBody Map<String, List<String>> params) {
        List<String> userIds = params.get("userIds");
        List<String> regionIds = params.get("regionIds");
        if (userIds == null || regionIds == null || userIds.isEmpty())
            return CommonResult.error(500,"违规操作");
        List<String> regionKeyList = getRegionKeyAndParents(regionIds);
        userRoleRegionService.updateForUserAuthority(regionKeyList, userIds);
        return success(true);
    }

}

package com.miyu.cloud.macs.restServer.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.VisitorRegionRespVO;
import com.miyu.cloud.macs.dal.dataobject.accessApplication.AccessApplicationDO;
import com.miyu.cloud.macs.dal.dataobject.accessRecords.AccessRecordsDO;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.dal.dataobject.collectorStrategy.CollectorStrategyDO;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.dal.dataobject.file.MacsFileDO;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.strategy.StrategyDO;
import com.miyu.cloud.macs.dal.dataobject.user.UserDO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import com.miyu.cloud.macs.restServer.api.MacsInteractiveApi;
import com.miyu.cloud.macs.restServer.entity.*;
import com.miyu.cloud.macs.restServer.service.MacsInteractiveService;
import com.miyu.cloud.macs.restServer.service.MacsRestService;
import com.miyu.cloud.macs.restServer.webSocket.WebSocket;
import com.miyu.cloud.macs.service.accessApplication.AccessApplicationService;
import com.miyu.cloud.macs.service.accessRecords.AccessRecordsService;
import com.miyu.cloud.macs.service.collector.CollectorService;
import com.miyu.cloud.macs.service.collectorStrategy.CollectorStrategyService;
import com.miyu.cloud.macs.service.device.DeviceService;
import com.miyu.cloud.macs.service.door.DoorService;
import com.miyu.cloud.macs.service.file.MacsFileService;
import com.miyu.cloud.macs.service.region.RegionService;
import com.miyu.cloud.macs.service.strategy.StrategyService;
import com.miyu.cloud.macs.service.user.UserService;
import com.miyu.cloud.macs.service.userRoleRegion.UserRoleRegionService;
import com.miyu.cloud.macs.service.visitor.VisitorService;
import com.miyu.cloud.macs.service.visitorRegion.VisitorRegionService;
import com.mzt.logapi.starter.annotation.LogRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "门禁接口")
@Slf4j
@RestController
@RequestMapping("/macs/rest")
public class MacsRestController {

    @Resource
    private WebSocket webSocket;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CollectorService collectorService;
    @Autowired
    private DoorService doorService;
    @Autowired
    private UserRoleRegionService userRoleRegionService;
    @Autowired
    private AccessRecordsService accessRecordsService;
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private VisitorRegionService visitorRegionService;
    @Autowired
    private MacsInteractiveApi macsInteractiveApi;
    @Autowired
    private UserService userService;
    @Autowired
    private MacsInteractiveService macsInteractiveService;
    @Autowired
    private CollectorStrategyService collectorStrategyService;
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private MacsRestService macsRestService;
    @Autowired
    private MacsFileService macsFileService;
    @Autowired
    private AccessApplicationService accessApplicationService;
    @Resource
    private AdminUserApi adminUserApi;

    private final Map<String,Map<String,Integer>> lastDeviceStatusMap = new HashMap<>();

//    @PostMapping("/pushInformation")
//    @Operation(summary = "设备推送采集信息")
    /*public CommonResult<?> receiveInformation(@RequestBody MacsRestCollector collector) {
        String code = collector.getCode();
        MacsRestUser user = collector.getUser();
        String userIdByCollect = webSocket.getUserIdByCollect(code);
        if (userIdByCollect != null) {
            return collect(user, userIdByCollect);
        } else {
            return validateAccess(collector);
        }
    }*/
    @PostMapping("/pushInformation")
    @Operation(summary = "设备推送采集信息")
    public CommonResult<?> receiveInformation(@RequestBody MacsRestCollector collector) {
        saveAccessRecord(collector);
        return CommonResult.success(true);
    }

    private void saveAccessRecord(MacsRestCollector restCollector) {
        String collectorCode = restCollector.getCode();
        MacsRestUser restUser = restCollector.getUser();
        CollectorDO collector = collectorService.getOne(new QueryWrapper<CollectorDO>().eq("code", collectorCode));
        DoorDO door = doorService.getById(collector.getDoorId());
        DeviceDO device = deviceService.getById(door.getDeviceId());
        RegionDO region = regionService.getById(device.getRegionId());
        AccessRecordsDO records = new AccessRecordsDO().setRegion(region).setDoor(door).setCollector(collector).setDevice(device);
        List<UserDO> userDOS = userService.list(new QueryWrapper<UserDO>().eq("id", restUser.getId()));
        if (userDOS.size() > 0) {
            AdminUserRespDTO userRespDTO = adminUserApi.getUser(Long.valueOf(userDOS.get(0).getUserId())).getData();
            records.setUser(userRespDTO);
        } else {
            List<VisitorDO> visitorDOS = visitorService.list(new QueryWrapper<VisitorDO>().eq("id", restUser.getId()));
            if (visitorDOS.size() > 0) {
                records.setVisitor(visitorDOS.get(0));
            }
        }
        accessRecordsService.add(records, AccessRecordsDO.InstructType.general, null);
        if (records.getUserId() != null) {
            UpdateWrapper<UserDO> wrapper = new UpdateWrapper<>();
            wrapper.set("region_id", collector.getLocationCode().equals("0")? region.getParentId() : region.getId());
            wrapper.eq("user_id", records.getUserId());
            userService.update(wrapper);
        }
        if (records.getVisitorId() != null) {
            UpdateWrapper<VisitorDO> wrapper = new UpdateWrapper<>();
            wrapper.set("region_id", collector.getLocationCode().equals("0")? region.getParentId() : region.getId());
            wrapper.eq("id", records.getVisitorId());
            visitorService.update(wrapper);
        }
    }

    /*private CommonResult<?> collect(MacsRestUser user, String userIdByCollect) {
        Map<String,Object> msgObj = new HashMap<>();
        if (user.getCode() != null) {
            msgObj.put("code", user.getCode());
        }
        if (user.getFacialFeatureString() != null) {
            //todo 测试
            msgObj.put("facePicture", user.getFacialFeatureString());
            msgObj.put("facialFeatureString", user.getFacialFeatureString());
        }
        if (user.getFingerprintString() != null) {
            //todo 测试
            msgObj.put("fingerprintPicture", user.getFingerprintString());
            msgObj.put("fingerprintString", user.getFingerprintString());
        }
        webSocket.sendOneMessage(userIdByCollect, JsonUtils.toJsonString(msgObj));
        return CommonResult.success(true);
    }*/
    /**
     * 校验通行流程 转移到设备
     */
//    @PostMapping("/validateAccess")
//    @Operation(summary="通行校验")
    /*public CommonResult<?> validateAccess(@RequestBody MacsRestCollector restCollector) {
        String collectorCode = restCollector.getCode();
        MacsRestUser restUser = restCollector.getUser();
        CollectorDO collector = collectorService.getOne(new QueryWrapper<CollectorDO>().eq("code", collectorCode));
        DoorDO door = doorService.getById(collector.getDoorId());
        RegionDO region = regionService.getById(door.getRegionId());
        DeviceDO device = deviceService.getById(collector.getDeviceId());
        AccessRecordsDO records = new AccessRecordsDO().setRegion(region).setDoor(door).setCollector(collector).setDevice(device);
        boolean validateFlag = !region.getPublicStatus();
        List<StrategyDO> strategyList = strategyService.list(new QueryWrapper<StrategyDO>().eq("code", "noVerification"));
        if (strategyList.size() == 1) {
            QueryWrapper<CollectorStrategyDO> queryWrapper = new QueryWrapper<CollectorStrategyDO>();
            queryWrapper.eq("collector_id", collector.getId());
            queryWrapper.eq("strategy_id", strategyList.get(0).getId());
            List<CollectorStrategyDO> csIds = collectorStrategyService.list(queryWrapper);
            if (csIds.size() > 0) validateFlag = false;
        }
        try {
            //查询用户
            AdminUserRespDTO user = userService.getUserByCharacter(restUser);
            if (!region.getPublicStatus()) {
                if (user != null) {
                    records.setUser(user);
                    if (validateFlag) {
                        if (user.getStatus() == 2) {
                            accessRecordsService.add(records, AccessRecordsDO.InstructType.validate, "当前用户已冻结!");
                            throw new RuntimeException("当前用户已冻结!");
                        }
                        //记录 校验记录
                        accessRecordsService.add(records, AccessRecordsDO.InstructType.validate, null);
                        //用户校验
                        userRoleRegionService.validateAccess(user, region);
                    }
                } else {
                    //访客校验
                    VisitorDO visitor = visitorService.getVisitorByCharacter(restUser);
                    if (visitor == null) {
                        if (restUser.getCode() !=null) throw new RuntimeException("当前用户不存在!"+restUser.getCode());
                        else throw new RuntimeException("未识别当前用户!");
                    }
                    records.setVisitor(visitor);
                    if (validateFlag) {
                        if (visitor.getStatus() == 2) {
                            accessRecordsService.add(records, AccessRecordsDO.InstructType.validate, "当前访客已冻结!");
                            throw new RuntimeException("当前访客已冻结!");
                        }
                        accessRecordsService.add(records, AccessRecordsDO.InstructType.validate, null);
                        visitorRegionService.validateAccess(visitor, region);
                    }
                }
            }
            //指令生成发送
            ThreadUtil.execAsync(() -> {
                try {
                    sendAccessInstruct(records, door, collector);
                } catch (URISyntaxException e) {
                    log.error(e.getMessage());
                }
            });

            restUser.setName(records.getUserName());
            restUser.setAvatar(records.getAvatar());
            restUser.setUserCode(records.getUserCode());
            restUser.setVerifyStatus(true);
            return CommonResult.success(restUser);
        } catch (Exception e) {
            accessRecordsService.add(records, AccessRecordsDO.InstructType.validate, e.getMessage());
            restUser.setCode(records.getUserCode());
            restUser.setName(records.getUserName());
            restUser.setAvatar(records.getAvatar());
            restUser.setVerifyStatus(false);
            restUser.setVerifyMsg(e.getMessage());
            CommonResult<MacsRestUser> result = CommonResult.error(500,e.getMessage());
            result.setData(restUser);
            return result;
        }
    }*/

    private void sendAccessInstruct(AccessRecordsDO records, DoorDO door, CollectorDO collector) throws URISyntaxException {
        RegionDO region = regionService.getById(door.getRegionId());
        DeviceDO device = deviceService.getById(collector.getDeviceId());
        MacsRestDevice restInstruct = new MacsRestDevice();
        restInstruct.createInstruct(device, door, collector, MacsRestDoor.instructType.general);

        URI uri = macsInteractiveService.getUriByDeviceCode(device.getCode());
        if (uri == null) log.error("未连接门禁设备");
        boolean flag = macsInteractiveApi.executeInstruct(uri, restInstruct);
        log.info(JsonUtils.toJsonString(restInstruct));
        if (flag) {
            log.info("指令发送成功");
            accessRecordsService.add(records, AccessRecordsDO.InstructType.general, null);
            String regionId = null;
            if ("0".equals(collector.getLocationCode())) regionId= region.getParentId();
            else if ("1".equals(collector.getLocationCode())) regionId= region.getId();
            if (records.getUserId() != null) {
                UpdateWrapper<UserDO> wrapper = new UpdateWrapper<>();
                wrapper.set("region_id", regionId);
                wrapper.eq("user_id", records.getUserId());
                userService.update(wrapper);
            }
            if (records.getVisitorId() != null) {
                UpdateWrapper<VisitorDO> wrapper = new UpdateWrapper<>();
                wrapper.set("region_id", regionId);
                wrapper.eq("id", records.getVisitorId());
                visitorService.update(wrapper);
            }
        }
        else log.error("指令发送失败");
    }

    @PostMapping("/pushDeviceStatus")
    @Operation(summary = "更新设备状态")
    CommonResult<?> getAllDeviceStatus(@RequestBody MacsRestDevice device) {
        String deviceCode = device.getCode();
        int deviceStatus = device.getStatus();
        List<MacsRestDoor> doors = device.getDoors();
        Integer lastDeviceStatus = getLastStatus("device", deviceCode);
        if (lastDeviceStatus == null || deviceStatus != lastDeviceStatus) {
            UpdateWrapper<DeviceDO> deviceWrapper = new UpdateWrapper<DeviceDO>().set("status", lastDeviceStatus).eq("code", deviceCode);
            deviceService.update(deviceWrapper);
            setLastStatus("device", deviceCode, lastDeviceStatus);
        }
        for (MacsRestDoor door : doors) {
            String doorCode = door.getCode();
            int doorStatus = door.getDoorStatus();
            List<MacsRestCollector> collectors = door.getCollectors();
            Integer lastDoorStatus = getLastStatus("door", doorCode);
            if (lastDoorStatus == null || doorStatus != lastDoorStatus) {
                UpdateWrapper<DoorDO> doorWrapper = new UpdateWrapper<DoorDO>().set("door_status", lastDoorStatus).eq("code", doorCode);
                doorService.update(doorWrapper);
                setLastStatus("door", doorCode, lastDoorStatus);
            }
            for (MacsRestCollector collector : collectors) {
                String collectorCode = collector.getCode();
                int collectorStatus = collector.getStatus();
                Integer lastCollectorStatus = getLastStatus("collector", collectorCode);
                if (lastCollectorStatus == null || collectorStatus != lastCollectorStatus) {
                    UpdateWrapper<CollectorDO> collectorWrapper = new UpdateWrapper<CollectorDO>().set("status", lastCollectorStatus).eq("code", collectorCode);
                    collectorService.update(collectorWrapper);
                    setLastStatus("collector", collectorCode, lastCollectorStatus);
                }
            }
        }
        return CommonResult.success(true);
    }

    private void setLastStatus(String type, String code, Integer status) {
        lastDeviceStatusMap.putIfAbsent(type, new HashMap<>());
        lastDeviceStatusMap.get(type).put(code, status);
    }

    private Integer getLastStatus(String type, String code) {
        if (lastDeviceStatusMap.get(type) != null) {
            return lastDeviceStatusMap.get(type).get(code);
        }
        return null;
    }


    /**
     * 根据 姓名,账号 查用户
     */
    @GetMapping("/getUserByNameAndUserCode")
    public CommonResult<MacsRestUser> getUser(MacsRestUser user) {
        String name = user.getName();
        String userCode = user.getUserCode();
        UserDO userDO = userService.getByName(userCode,name);
        if (userDO != null) {
            user.setId(userDO.getId());
            user.setCode(userDO.getCode());
            user.setPhone(userDO.getPhone());
            return CommonResult.success(user);
        }
        return CommonResult.error(500,"查无此人");
    }

    /**
     * 根据 姓名,身份证号码 查访客
     */
    @GetMapping("/getVisitorByNameAndIdCard")
    public CommonResult<MacsRestUser> getVisitor(MacsRestUser user) {
        String name = user.getName();
        String idCard = user.getIdCard();
        QueryWrapper<VisitorDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("id_card", idCard);
        List<VisitorDO> visitorList = visitorService.list(queryWrapper);
        if (visitorList.size() == 1) {
            VisitorDO visitor = visitorList.get(0);
            user.setId(visitor.getId());
            user.setAvatar(visitor.getAvatar());
            user.setCode(visitor.getCode());
            user.setPhone(visitor.getPhone());
            List<AccessApplicationDO> applicationList = accessApplicationService.effectiveListByVisitor(visitor.getId());
            for (AccessApplicationDO accessApplicationDO : applicationList) {
                List<VisitorRegionRespVO> list = visitorRegionService.regionShowList(accessApplicationDO.getId(), visitor.getId());
                accessApplicationDO.setRegionList(list);
            }
            String jsonString = JsonUtils.toJsonString(applicationList);
            user.setApplicationList(JsonUtils.parseArray(jsonString, Map.class));
            return CommonResult.success(user);
        }
        return CommonResult.error(500,"查无此人");
    }

    @PostMapping("/saveInformation")
    @Operation(summary = "接收采集信息")
    @LogRecord(type = "门禁", subType = "更新人员信息", bizNo = "{{#imageUpload.id}}", success = "人员信息已更新")
    public CommonResult<?> saveInformation(@RequestBody MacsRestImageUploadVO imageUpload, HttpServletRequest request) {
        try {
            macsRestService.saveInformation(imageUpload, request.getRemoteAddr());
        } catch (Exception e) {
            return CommonResult.error(500,e.getMessage());
        }
        return CommonResult.success(true);
    }

    @GetMapping("/getImageByName/{fileId}")
    @Operation(summary = "下载文件")
    public void getImageByName(HttpServletResponse response, @PathVariable("fileId") String fileId) throws IOException {
        MacsFileDO fileDO = macsFileService.getFile(fileId);
        String filePath = fileDO.getPath() + fileDO.getName();
        byte[] content = FileUtil.readBytes(filePath);
        if (content == null) {
            log.warn("downImage:"+filePath + "文件不存在");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        byte[] encryptedBytes = SecureUtil.aes(fileDO.getSalt().getBytes(CharsetUtil.CHARSET_UTF_8)).decrypt(content);
//        ServletUtils.writeAttachment(response, fileDO.getName(), encryptedBytes); //下载
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=" + fileDO.getName());
        response.getOutputStream().write(encryptedBytes); //浏览
    }

    @RequestMapping(value = "/inlineUser",method = RequestMethod.GET )
    public Boolean getInlineUser(@RequestParam(name="region") String region){

        RegionDO macsRegion = regionService.getOne(new QueryWrapper<RegionDO>().eq("code",region));
        if(macsRegion == null){
            throw new RuntimeException("region null");
        }
        List<UserDO> macsUsers = userService.list(new QueryWrapper<UserDO>().eq("region_id",macsRegion.getId()));

        List<VisitorDO> macsVisitors =visitorService.list(new QueryWrapper<VisitorDO>().eq("region_id",macsRegion.getId()));

        return macsUsers.size() + macsVisitors.size() == 0;
    }
}

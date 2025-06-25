package com.miyu.cloud.macs.service.door;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.cloud.macs.dal.dataobject.accessRecords.AccessRecordsDO;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.mysql.device.DeviceMapper;
import com.miyu.cloud.macs.restServer.api.MacsInteractiveApi;
import com.miyu.cloud.macs.restServer.entity.MacsRestCollector;
import com.miyu.cloud.macs.restServer.entity.MacsRestDevice;
import com.miyu.cloud.macs.service.accessRecords.AccessRecordsService;
import com.miyu.cloud.macs.restServer.entity.MacsRestDoor;
import com.miyu.cloud.macs.restServer.service.MacsInteractiveService;
import com.miyu.cloud.macs.service.collector.CollectorService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.miyu.cloud.macs.controller.admin.door.vo.*;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.door.DoorMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 门 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DoorServiceImpl implements DoorService {

    @Resource
    private DoorMapper doorMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private MacsInteractiveService macsInteractiveService;
    @Resource
    private AccessRecordsService accessRecordsService;
    @Resource
    private MacsInteractiveApi macsInteractiveApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private CollectorService collectorService;

    @Override
    public String createDoor(DoorSaveReqVO createReqVO) {
        // 插入
        DoorDO door = BeanUtils.toBean(createReqVO, DoorDO.class);
        doorMapper.insert(door);
        // 返回
        return door.getId();
    }

    @Override
    public void updateDoor(DoorSaveReqVO updateReqVO) {
        // 校验存在
        validateDoorExists(updateReqVO.getId());
        // 更新
        DoorDO updateObj = BeanUtils.toBean(updateReqVO, DoorDO.class);
        doorMapper.updateById(updateObj);
    }

    @Override
    public void deleteDoor(String id) {
        // 校验存在
        validateDoorExists(id);
        // 删除
        doorMapper.deleteById(id);
    }

    private void validateDoorExists(String id) {
        if (doorMapper.selectById(id) == null) {
            throw exception(DOOR_NOT_EXISTS);
        }
    }

    @Override
    public DoorDO getDoor(String id) {
        return doorMapper.selectById(id);
    }

    @Override
    public PageResult<DoorDO> getDoorPage(DoorPageReqVO pageReqVO) {
        Wrapper<DoorDO> queryWrapper = new LambdaQueryWrapperX<DoorDO>()
                .likeIfPresent(DoorDO::getName, pageReqVO.getName())
                .eqIfPresent(DoorDO::getRegionId, pageReqVO.getRegionId())
                .eqIfPresent(DoorDO::getDeviceId, pageReqVO.getDeviceId())
                .likeIfPresent(DoorDO::getCode, pageReqVO.getCode())
                .eqIfPresent(DoorDO::getDoorStatus, pageReqVO.getDoorStatus())
                .orderByDesc(DoorDO::getId);
        IPage<DoorDO> mpPage = MyBatisUtils.buildPage(pageReqVO, null);
        doorMapper.selectPageList(mpPage, queryWrapper);
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    // TODO: 2024/4/18 指令发送
    @Override
    public void openDoor(LoginUser loginUser, String id) throws URISyntaxException {
        DoorDO door = doorMapper.selectById(id);
        DeviceDO device = deviceMapper.selectById(door.getDeviceId());
        List<CollectorDO> collectors = collectorService.list(new QueryWrapper<CollectorDO>().eq("door_id", door.getId()));
        if (collectors.size() == 0) throw new RuntimeException("未找到当前门的控制器");
        URI uri = macsInteractiveService.getUriByDeviceCode(device.getCode());
        MacsRestDevice restInstruct = new MacsRestDevice();
        restInstruct.createInstruct(device,door,collectors.get(0),MacsRestDoor.instructType.open);
        macsInteractiveApi.executeInstruct(uri,restInstruct);
        AdminUserRespDTO user1 = adminUserApi.getUser(loginUser.getId()).getData();
        AccessRecordsDO records = new AccessRecordsDO().setDoor(door).setDevice(device).setOperator(user1);
        accessRecordsService.add(records, AccessRecordsDO.InstructType.open, null);
    }

    @Override
    public void closeDoor(LoginUser loginUser, String id) throws URISyntaxException {
        DoorDO door = doorMapper.selectById(id);
        DeviceDO device = deviceMapper.selectById(door.getDeviceId());
        List<CollectorDO> collectors = collectorService.list(new QueryWrapper<CollectorDO>().eq("door_id", door.getId()));
        if (collectors.size() == 0) throw new RuntimeException("未找到当前门的控制器");
        URI uri = macsInteractiveService.getUriByDeviceCode(device.getCode());
        MacsRestDevice restInstruct = new MacsRestDevice();
        restInstruct.createInstruct(device,door,collectors.get(0),MacsRestDoor.instructType.close);
        macsInteractiveApi.executeInstruct(uri, restInstruct);
        AdminUserRespDTO user1 = adminUserApi.getUser(loginUser.getId()).getData();
        AccessRecordsDO records = new AccessRecordsDO().setDoor(door).setDevice(device).setOperator(user1);
        accessRecordsService.add(records, AccessRecordsDO.InstructType.close, null);
    }

    @Override
    public List<DoorDO> getDoorList(DoorPageReqVO listReqVO) {
        return doorMapper.selectList();
    }

    @Override
    public DoorDO getById(String doorId) {
        return doorMapper.selectById(doorId);
    }

    @Override
    public int update(UpdateWrapper<DoorDO> doorWrapper) {
        return doorMapper.update(doorWrapper);
    }
}

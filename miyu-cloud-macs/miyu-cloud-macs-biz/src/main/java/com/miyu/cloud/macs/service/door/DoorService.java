package com.miyu.cloud.macs.service.door;

import java.net.URISyntaxException;
import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.controller.admin.door.vo.*;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 门 Service 接口
 *
 * @author 芋道源码
 */
public interface DoorService {

    /**
     * 创建门
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createDoor(@Valid DoorSaveReqVO createReqVO);

    /**
     * 更新门
     *
     * @param updateReqVO 更新信息
     */
    void updateDoor(@Valid DoorSaveReqVO updateReqVO);

    /**
     * 删除门
     *
     * @param id 编号
     */
    void deleteDoor(String id);

    /**
     * 获得门
     *
     * @param id 编号
     * @return 门
     */
    DoorDO getDoor(String id);

    /**
     * 获得门分页
     *
     * @param pageReqVO 分页查询
     * @return 门分页
     */
    PageResult<DoorDO> getDoorPage(DoorPageReqVO pageReqVO);

    void openDoor(LoginUser loginUser, String id) throws URISyntaxException;

    void closeDoor(LoginUser loginUser, String id) throws URISyntaxException;

    List<DoorDO> getDoorList(DoorPageReqVO listReqVO);

    DoorDO getById(String doorId);

    int update(UpdateWrapper<DoorDO> doorWrapper);
}

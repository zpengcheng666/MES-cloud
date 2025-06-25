package com.miyu.cloud.macs.service.accessApplication;

import javax.validation.*;

import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.controller.admin.accessApplication.vo.*;
import com.miyu.cloud.macs.dal.dataobject.accessApplication.AccessApplicationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 通行申请 Service 接口
 *
 * @author 芋道源码
 */
public interface AccessApplicationService {

    /**
     * 创建通行申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAccessApplication(@Valid AccessApplicationSaveReqVO createReqVO);

    /**
     * 更新通行申请
     *
     * @param updateReqVO 更新信息
     */
    void updateAccessApplication(@Valid AccessApplicationSaveReqVO updateReqVO);

    /**
     * 删除通行申请
     *
     * @param id 编号
     */
    void deleteAccessApplication(String id);

    /**
     * 获得通行申请
     *
     * @param id 编号
     * @return 通行申请
     */
    AccessApplicationDO getAccessApplication(String id);

    /**
     * 获得通行申请分页
     *
     * @param pageReqVO 分页查询
     * @return 通行申请分页
     */
    PageResult<AccessApplicationDO> getAccessApplicationPage(AccessApplicationPageReqVO pageReqVO);

    //重置
    void reset();

    //刷新设备连接
    void resetDeviceConnection();

    //添加连接新服务
    boolean reconnectServer(String ip, String port);

    //服务断连
    boolean serverDestroy(String uri);

    Map<String, List<String>> currentDeviceInstancesGet();

    //禁用删除设备
    boolean removeDevice(String deviceCode) throws NacosException;

    //添加新设备
    boolean addDevices(List<String> deviceCodes);

    String createVisitorApplication(AccessApplicationSaveReqVO createReqVO);

    void updateApplicationStatus(String applicationId, Integer status);

    List<AccessApplicationDO> listByVisitor(String id);

    List<AccessApplicationDO> effectiveListByVisitor(String id);
}

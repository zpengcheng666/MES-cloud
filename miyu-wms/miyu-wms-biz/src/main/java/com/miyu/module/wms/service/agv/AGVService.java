package com.miyu.module.wms.service.agv;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.agv.vo.*;
import com.miyu.module.wms.dal.dataobject.agv.AGVDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * AGV 信息 Service 接口
 *
 * @author 上海弥彧
 */
public interface AGVService {

    /**
     * 创建AGV 信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAGV(@Valid AGVSaveReqVO createReqVO);

    /**
     * 更新AGV 信息
     *
     * @param updateReqVO 更新信息
     */
    void updateAGV(@Valid AGVSaveReqVO updateReqVO);

    /**
     * 删除AGV 信息
     *
     * @param id 编号
     */
    void deleteAGV(String id);

    /**
     * 获得AGV 信息
     *
     * @param id 编号
     * @return AGV 信息
     */
    AGVDO getAGV(String id);

    /**
     * 获得AGV 信息分页
     *
     * @param pageReqVO 分页查询
     * @return AGV 信息分页
     */
    PageResult<AGVDO> getAGVPage(AGVPageReqVO pageReqVO);

    AGVDO getAGVByCarNo(String carNo);
}
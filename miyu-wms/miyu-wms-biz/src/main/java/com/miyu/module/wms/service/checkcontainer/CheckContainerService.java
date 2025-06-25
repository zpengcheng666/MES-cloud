package com.miyu.module.wms.service.checkcontainer;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.checkcontainer.vo.*;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;

/**
 * 库存盘点容器 Service 接口
 *
 * @author QianJy
 */
public interface CheckContainerService {

    /**
     * 创建库存盘点容器
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCheckContainer(@Valid CheckContainerSaveReqVO createReqVO);

    /**
     * 更新库存盘点容器
     *
     * @param updateReqVO 更新信息
     */
    void updateCheckContainer(@Valid CheckContainerSaveReqVO updateReqVO);


    /**
     * 生成盘点容器列表
     *
     */
    void insertCheckContainer(String checkPlanId, List<MaterialStockDO> materialStockList);

    /**
     * 删除库存盘点容器
     *
     * @param id 编号
     */
    void deleteCheckContainer(String id);

    /**
     * 获得库存盘点容器
     *
     * @param id 编号
     * @return 库存盘点容器
     */
    CheckContainerDO getCheckContainer(String id);

    /**
     * 获得库存盘点容器分页
     *
     * @param pageReqVO 分页查询
     * @return 库存盘点容器分页
     */
    PageResult<CheckContainerDO> getCheckContainerPage(CheckContainerPageReqVO pageReqVO);

    /**
     * 获得库存盘点容器列表
     *
     * @param checkPlanId 盘点计划编号
     * @return 库存盘点容器列表
     */
    List<CheckContainerDO> getCheckContainerByCheckPlanId(String checkPlanId);

    List<CheckContainerDO> getCheckContainerAndLocationIdByCheckPlanId(String checkPlanId);

    int updateCheckContainerStatus(String checkContainerId, Integer checkStatus);

    Boolean updateBatchCheckContainer(List<CheckContainerDO> updateDOList);
}
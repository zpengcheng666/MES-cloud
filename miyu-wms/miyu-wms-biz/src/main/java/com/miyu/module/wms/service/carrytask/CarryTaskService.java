package com.miyu.module.wms.service.carrytask;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.controller.admin.carrytask.vo.*;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 搬运任务 Service 接口
 *
 * @author 技术部长
 */
public interface CarryTaskService {

    /**
     * 创建搬运任务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCarryTask(@Valid CarryTaskSaveReqVO createReqVO);

    /**
     * 更新搬运任务
     *
     * @param updateReqVO 更新信息
     */
    void updateCarryTask(@Valid CarryTaskSaveReqVO updateReqVO);

    void updateCarryTask(CarryTaskDO carryTaskDO);

    void updateCarrySubTask(CarrySubTaskDO carrySubTask);


    /**
     * 删除搬运任务
     *
     * @param id 编号
     */
    void deleteCarryTask(String id);

    /**
     * 获得搬运任务
     *
     * @param id 编号
     * @return 搬运任务
     */
    CarryTaskDO getCarryTask(String id);

    CarryTaskDO getCarryTaskByTaskCode(String taskCode);


    /**
     * 获得搬运任务何其子表数据
     */
    CarryTaskDO getCarryTaskWithSubTask(String id);


    CarryTaskDO getCarryTaskBySubTaskId(String subId);


    /**
     * 获得搬运任务分页
     *
     * @param pageReqVO 分页查询
     * @return 搬运任务分页
     */
    PageResult<CarryTaskDO> getCarryTaskPage(CarryTaskPageReqVO pageReqVO);

    /**
     * 查询挂起的搬运任务
     */
    List<CarryTaskDO> getHangingCarryTask();

    /**
     * 保存搬运任务主子表数据
     * @param carryTaskDO
     */
    void saveCarryTask(CarryTaskDO carryTaskDO);

    List<CarryTaskDO> getUnfinishedCarryTaskByReflectStockId(String reflectStockId);



    /**
     * 创建刀具配送搬运任务
     * @param carryTask
     * @param trayStock
     * @param targetLocationIdsStr
     */
    void createToolDistributionCarryTask(CarryTaskDO carryTask, MaterialStockDO trayStock, String targetLocationIdsStr);


    /**
     * 下发搬运任务
     * @param carryTask
     * @return
     */
    CommonResult<String> dispatchCarryTask(CarryTaskDO carryTask);

    // ==================== 子表（搬运任务子表） ====================

    /**
     * 根据父id和执行顺序 获取子表
     * @param parentId
     * @param executeOrder
     */
    CarrySubTaskDO getCarrySubTaskByParentIdAndExecuteOrder(String parentId, Integer executeOrder);
    /**
     * 根据父id和执行顺序 更新子表
     * @param parentId
     * @param executeOrder
     */
    Boolean updateCarrySubTaskByParentIdAndExecuteOrder(String parentId, Integer executeOrder, Integer taskStatus);


    /**
     * 获得搬运任务子表列表
     *
     * @param parentId 搬运任务id
     * @return 搬运任务子表列表
     */
    List<CarrySubTaskDO> getCarrySubTaskListByParentId(String parentId);

    /**
     * 获取所有未完成的搬运任务主表
     */
    List<CarryTaskDO> getUnfinishedCarryTask();

    /**
     * 获取所有未完成的搬运任务子表
     */
    List<CarrySubTaskDO> getUnfinishedCarrySubTask();

    // 校验物料是否已存在任务
    void checkCarryTask(String containerStockId);

    // 校验物料是否已存在任务
    void checkSubCarryTask(String materialStockId);

    /**
     * 下发下一个搬运任务
     * @param insId
     * @return
     */
    CarryTaskResult<?> dispatchNextCarryTask(String insId);

    /**
     * 移动
     */
    void moveCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList);
    /**
     * 取
     */
    void takeCarrySubTask(int executeOrder, String materialId, String locationId,boolean trigEvent, List<CarrySubTaskDO> carrySubTaskList);
    /**
     * 放
     */
    void putCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList);
    /**
     * 放 ---特殊标记
     */
    void putSpecialMarkCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList);
    /**
     * 上架
     */
    void upCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList);
    /**
     * 下架
     */
    void downCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList);


    /**
     * 呼叫托盘子任务
     * @param carrySubTaskList 子任务集合
     * @param tray 呼叫的托盘
     * @param trayDownLocation 托盘的下架库位 可以为null
     * @param targetLocation 目标库位
     */
    void createCallTrayCarrySubTask(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO tray, WarehouseLocationDO trayDownLocation, WarehouseLocationDO targetLocation);

    /**
     * 物料配送子任务
     * @param carrySubTaskList 子任务集合
     * @param containerStockId 容器id
     * @param trayId 托盘id 来取容器的托盘 也可能和容器是同一个托盘
     * @param startLocationId 起始库位
     * @param targetLocation 目标库位
     * @param materialAtAreaType 物料所在区域类型
     * @param upMaterialId 上架物料id
     * @param upEmptyLocation 上架物料的空库位
     */
    void createMaterialDistributionCarrySubTask(List<CarrySubTaskDO> carrySubTaskList, String containerStockId, String trayId, String startLocationId , WarehouseLocationDO targetLocation, Integer materialAtAreaType, String upMaterialId, WarehouseLocationDO upEmptyLocation);

    /**
     * 托盘回库子任务
     * @param carrySubTaskList 子任务集合
     * @param tray 托盘
     * @param returnLocationId 回库库位
     * @param trayUpLocation 托盘上架库位
     * @param takeLocationId 取件库位
     */
    void createTrayReturnCarrySubTask(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO tray, String returnLocationId, WarehouseLocationDO trayUpLocation, String takeLocationId);
}

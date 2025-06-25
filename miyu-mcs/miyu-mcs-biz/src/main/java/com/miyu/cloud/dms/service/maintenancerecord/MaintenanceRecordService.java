package com.miyu.cloud.dms.service.maintenancerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordAddSaveReqVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordDO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordToSparePartDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备保养维护记录 Service 接口
 *
 * @author miyu
 */
public interface MaintenanceRecordService {

    /**
     * 创建设备保养维护记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaintenanceRecord(@Valid MaintenanceRecordSaveReqVO createReqVO);

    /**
     * 更新设备保养维护记录
     *
     * @param updateReqVO 更新信息
     */
    void updateMaintenanceRecord(@Valid MaintenanceRecordSaveReqVO updateReqVO);

    /**
     * 删除设备保养维护记录
     *
     * @param id 编号
     */
    void deleteMaintenanceRecord(String id);

    /**
     * 获得设备保养维护记录
     *
     * @param id 编号
     * @return 设备保养维护记录
     */
    MaintenanceRecordDO getMaintenanceRecord(String id);

    /**
     * 获得设备保养维护记录分页
     *
     * @param pageReqVO 分页查询
     * @return 设备保养维护记录分页
     */
    PageResult<MaintenanceRecordDO> getMaintenanceRecordPage(MaintenanceRecordPageReqVO pageReqVO);

    /**
     * 通过计划id添加设备保养维护记录
     *
     * @param addSaveReqVO
     */
    void addMaintenanceRecord(@Valid MaintenanceRecordAddSaveReqVO addSaveReqVO);

    /**
     * 超期检查服务,每天运行一次
     */
    void expirationShutdownService();

    /*****************************************************************************************************/
    /********                                  使用备件                                           **********/
    /*****************************************************************************************************/

    /**
     * 添加使用备件(同时减去相应备件数量)
     *
     * @param recordId   记录id
     * @param insertList 添加数据
     */
    void addSparePart(String recordId, List<MaintenanceRecordToSparePartDO> insertList);

    /**
     * 根据保养维护记录获得使用备件
     *
     * @param recordId 保养维护记录id
     */
    List<MaintenanceRecordToSparePartDO> getSparePartByRecordId(String recordId);

    /**
     * 根据备件获得使用记录
     *
     * @param partId 备件id
     * @return 使用记录
     */
    List<MaintenanceRecordToSparePartDO> getSparePartByPartId(String partId);
}

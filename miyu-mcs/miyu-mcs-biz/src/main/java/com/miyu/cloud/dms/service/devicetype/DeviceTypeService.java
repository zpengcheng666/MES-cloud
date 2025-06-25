package com.miyu.cloud.dms.service.devicetype;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypePageReqVO;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 设备类型 Service 接口
 *
 * @author 王正浩
 */
public interface DeviceTypeService {

    /**
     * 创建设备类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createDeviceType(@Valid DeviceTypeSaveReqVO createReqVO);

    /**
     * 更新设备类型
     *
     * @param updateReqVO 更新信息
     */
    void updateDeviceType(@Valid DeviceTypeSaveReqVO updateReqVO);

    /**
     * 删除设备类型
     *
     * @param id 编号
     */
    void deleteDeviceType(String id);

    /**
     * 获得设备类型
     *
     * @param id 编号
     * @return 设备类型
     */
    DeviceTypeDO getDeviceType(String id);

    /**
     * 获得设备类型分页
     *
     * @param pageReqVO 分页查询
     * @return 设备类型分页
     */
    PageResult<DeviceTypeDO> getDeviceTypePage(DeviceTypePageReqVO pageReqVO);

    /**
     * 列出所有设备类型
     *
     * @return 设备类型列表
     */
    List<DeviceTypeDO> listDeviceType(Integer type);

    /**
     * 查询所有产线类型/单机设备
     */
    List<CommonDevice> getLineTypeList(CommonDevice commonDevice);

    /**
     * 根据设备类型id 匹配设备类型
     */
    List<DeviceTypeDataRespDTO> getDeviceTypeListByIds(Collection<String> ids);

    /**
     * 根据产线类型id 查询设备类型
     */
    List<DeviceTypeDataRespDTO> getDeviceTypeListByLineType(Collection<String> ids);

    /**
     * 根据类型查询 产线或设备类型
     * @param ids 设备类型/产线类型 id
     */
    List<CommonDevice> getUnitListByLineType(Collection<String> ids);

    /**
     * 查询所有 产线/组/单机设备
     */
    List<CommonDevice> getAllUnitList();

    /**
     * 通过 产线/单机设备类型 ids 查询设备
     */
    List<LedgerDO> getLedgerListByLineList(Collection<String> ids);

    List<DeviceTypeDO> getAllTypeList(CommonDevice commonDevice);

    List<CommonDevice> getDeviceListByUserId(String userId);

    List<DeviceTypeDO> getDeviceTypeList();

    DeviceTypeDO getDeviceTypeListByCode(String deviceTypeCode);
}

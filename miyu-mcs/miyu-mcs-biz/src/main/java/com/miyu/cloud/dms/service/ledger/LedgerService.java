package com.miyu.cloud.dms.service.ledger;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerPageReqVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设备台账 Service 接口
 *
 * @author miyu
 */
public interface LedgerService {

    /**
     * 创建设备台账
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createLedger(@Valid LedgerSaveReqVO createReqVO);

    /**
     * 更新设备台账
     *
     * @param updateReqVO 更新信息
     */
    void updateLedger(@Valid LedgerSaveReqVO updateReqVO);

    /**
     * 删除设备台账
     *
     * @param id 编号
     */
    void deleteLedger(String id);

    /**
     * 获得设备台账
     *
     * @param id 编号
     * @return 设备台账
     */
    LedgerDO getLedger(String id);

    /**
     * 获得设备台账分页
     *
     * @param pageReqVO 分页查询
     * @return 设备台账分页
     */
    PageResult<LedgerDO> getLedgerPage(LedgerPageReqVO pageReqVO);

    /**
     * 获得设备台账列表
     *
     * @return 设备台账列表
     * @return
     */
    List<LedgerDO> getLedgerList();

    /**
     * 通过 产线/组 ids 查询设备
     *
     * @return
     */
    List<LedgerDO> getUnitListByRecordIds(Collection<String> recordIds);

    /**
     * 通过IP获得台账数据
     *
     * @param ip 指定的ip
     * @return
     */
    LedgerDO getLedgerByIp(String ip);

    List<LedgerDO> list(Wrapper<LedgerDO> wrapper);

    /**
     * 通过设备id 查询仓库
     * @param deviceId 设备id(不包含产线)
     * @return 仓库id
     */
    String getWarehouseByDevice(String deviceId);
    /**
     * 通过设备id 查询刀具库位
     * @param deviceId 设备id(不包含产线)
     * @return 库位id
     */
    String getCuttingLocationByDevice(String deviceId);
    Map<String,String> getCuttingLocationListByDeviceIds(Collection<String> deviceIds);

    String getToolLocationByDevice(String deviceId);


    List<LedgerDO> listByIds(Collection<String> deviceIds);

    List<LedgerDO> getLedgerListByIds(Collection<String> deviceIds);

    List<LedgerDO> getByLineTypeAndDeviceType(Collection<String> unitSet, Collection<String> typeSet);

    LedgerDO getLedgerByNumber(String deviceNumber);

    int save(LedgerDO ledgerDO);

    int UpdateById(LedgerDO ledgerDO);


    CommonResult<String> getDeviceCodeByLocationId(String locationId);
}

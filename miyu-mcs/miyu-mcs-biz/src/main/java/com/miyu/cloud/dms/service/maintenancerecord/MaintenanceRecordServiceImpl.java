package com.miyu.cloud.dms.service.maintenancerecord;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordAddSaveReqVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordDO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordToSparePartDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.maintenanceplan.MaintenancePlanMapper;
import com.miyu.cloud.dms.dal.mysql.maintenancerecord.MaintenanceRecordMapper;
import com.miyu.cloud.dms.dal.mysql.maintenancerecord.MaintenanceRecordToSparePartMapper;
import com.miyu.cloud.dms.service.sparepart.SparePartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.*;

/**
 * 设备保养维护记录 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    @Resource
    private MaintenanceRecordMapper maintenanceRecordMapper;

    @Override
    public String createMaintenanceRecord(MaintenanceRecordSaveReqVO createReqVO) {
        // 插入
        MaintenanceRecordDO maintenanceRecord = BeanUtils.toBean(createReqVO, MaintenanceRecordDO.class);
        maintenanceRecordMapper.insert(maintenanceRecord);
        // 返回
        return maintenanceRecord.getId();
    }

    @Override
    public void updateMaintenanceRecord(MaintenanceRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateMaintenanceRecordExists(updateReqVO.getId());
        // 更新
        MaintenanceRecordDO updateObj = BeanUtils.toBean(updateReqVO, MaintenanceRecordDO.class);
        maintenanceRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaintenanceRecord(String id) {
        // 校验存在
        validateMaintenanceRecordExists(id);
        // 删除
        maintenanceRecordMapper.deleteById(id);
    }

    private void validateMaintenanceRecordExists(String id) {
        if (maintenanceRecordMapper.selectById(id) == null) {
            throw exception(MAINTENANCE_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public MaintenanceRecordDO getMaintenanceRecord(String id) {
        expirationShutdownService();
        return maintenanceRecordMapper.selectById(id);
    }

    @Override
    public PageResult<MaintenanceRecordDO> getMaintenanceRecordPage(MaintenanceRecordPageReqVO pageReqVO) {
        return maintenanceRecordMapper.selectPage(pageReqVO);
    }

    @Resource
    private MaintenancePlanMapper maintenancePlanMapper;

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void addMaintenanceRecord(@Valid MaintenanceRecordAddSaveReqVO addSaveReqVO) {
        if (StrUtil.isBlank(addSaveReqVO.getId())) {
            throw exception(MAINTENANCE_RECORD_NOT_EXISTS);
        }

        if (addSaveReqVO.getStatus() == null) {
            throw exception(MAINTENANCE_RECORD_STATUS_EMPTY);
        }
        if (addSaveReqVO.getStartTime() == null) {
            throw exception(MAINTENANCE_RECORD_START_EMPTY);
        }
        if (addSaveReqVO.getEndTime() == null) {
            throw exception(MAINTENANCE_RECORD_END_EMPTY);
        }

        MaintenanceRecordDO record = maintenanceRecordMapper.selectById(addSaveReqVO.getId());
        //获取保养维护记录
        if (record == null) {
            throw exception(MAINTENANCE_RECORD_NOT_EXISTS);
        }

        MaintenancePlanDO plan = maintenancePlanMapper.selectById(record.getCode());
        //获取维护保养计划
        if (plan == null) {
            throw exception(MAINTENANCE_PLAN_NOT_EXISTS);
        }

        record.setRecordStatus(1);

        Long userId = WebFrameworkUtils.getLoginUserId();
        record.setMaintenanceBy(userId.toString());

        record.setStatus(addSaveReqVO.getStatus());
        record.setRemarks(addSaveReqVO.getRemarks());
        record.setContent(addSaveReqVO.getContent());
        record.setStartTime(addSaveReqVO.getStartTime());
        record.setEndTime(addSaveReqVO.getEndTime());
        //更新记录
        maintenanceRecordMapper.updateById(record);

        plan.setLastStatus(addSaveReqVO.getStatus());
        plan.setLastTime(addSaveReqVO.getEndTime());
        //更新维护保养计划中的字段
        maintenancePlanMapper.updateById(plan);

        if (addSaveReqVO.getSpareParts() != null && !addSaveReqVO.getSpareParts().isEmpty()) {
            addSparePart(addSaveReqVO.getId(), addSaveReqVO.getSpareParts());
        }
    }

    @Resource
    private LedgerMapper ledgerMapper;

    @Override
    public void expirationShutdownService() {
        List<MaintenanceRecordDO> list = maintenanceRecordMapper.selectPreExpirationShutdown();
        if (list == null || list.isEmpty()) {
            return;
        }
        LedgerDO ledger = null;
        LocalDateTime now = LocalDateTime.now();

        for (MaintenanceRecordDO maintenanceRecord : list) {
            if (maintenanceRecord.getCreateTime().plusDays(maintenanceRecord.getExpirationTime().longValue()).isAfter(now)) {
                continue;
            }
            ledger = ledgerMapper.selectById(maintenanceRecord.getDevice());
            if (ledger == null) {
                continue;
            }
            ledger.setStatus(1); //设备状态改为关闭
            ledgerMapper.updateById(ledger);

        }
    }

    /*****************************************************************************************************/
    /********                                  使用备件                                           **********/
    /*****************************************************************************************************/

    @Resource
    private MaintenanceRecordToSparePartMapper maintenanceRecordToSparePartMapper;
    @Resource
    private SparePartService sparePartService;

    /**
     * 添加使用备件(同时减去相应备件数量)
     *
     * @param recordId   记录id
     * @param insertList 添加数据
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void addSparePart(String recordId, List<MaintenanceRecordToSparePartDO> insertList) {
        if (insertList == null || insertList.isEmpty()) {
            return;
        }
        for (MaintenanceRecordToSparePartDO data : insertList) {
            data.setRecordId(recordId);
            sparePartService.useSparePart(data.getPartId(), data.getNumber());
        }
        maintenanceRecordToSparePartMapper.insertBatch(insertList);
    }

    /**
     * 根据保养维护记录获得使用备件
     *
     * @param recordId 保养维护记录id
     */
    @Override
    public List<MaintenanceRecordToSparePartDO> getSparePartByRecordId(String recordId) {
        return maintenanceRecordToSparePartMapper.selectList(new LambdaQueryWrapperX<MaintenanceRecordToSparePartDO>()
                .eqIfPresent(MaintenanceRecordToSparePartDO::getRecordId, recordId)
        );
    }

    /**
     * 根据备件获得使用记录
     *
     * @param partId 备件id
     * @return 使用记录
     */
    public List<MaintenanceRecordToSparePartDO> getSparePartByPartId(String partId) {
        return maintenanceRecordToSparePartMapper.selectList(new LambdaQueryWrapperX<MaintenanceRecordToSparePartDO>()
                .eqIfPresent(MaintenanceRecordToSparePartDO::getPartId, partId)
        );
    }

}

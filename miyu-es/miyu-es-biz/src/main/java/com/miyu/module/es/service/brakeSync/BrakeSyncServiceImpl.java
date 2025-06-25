package com.miyu.module.es.service.brakeSync;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncRespVO;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncSaveReqVO;
import com.miyu.module.es.dal.dataobject.brakeSync.BrakeSyncDO;
import com.miyu.module.es.dal.mysql.brakeSync.BrakeSyncMapper;
import com.miyu.module.es.service.xxlJobInfo.XxlJobInfoServiceImpl;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.LogRecordConstants.*;
import static com.miyu.cloud.es.enums.ErrorCodeConstants.BRAKESYNC_NOT_EXISTS;
import static com.miyu.cloud.es.enums.LogRecordConstants.*;

@Service
@Validated
public class BrakeSyncServiceImpl implements BrakeSyncService {

    @Resource
    private BrakeSyncMapper brakeSyncMapper;

    @Resource
    private XxlJobInfoServiceImpl xxlJobService;

    @Override
    @DSTransactional
    @LogRecord(type = BRAKE_SYNC_CLUE_TYPE, subType = BRAKE_SYNC_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = BRAKE_SYNC_SUBMIT_SUCCESS)
    public void updateBrakeSync(BrakeSyncSaveReqVO updateReqVO) {
        // 校验存在
        validateBrakeExists(updateReqVO.getId());
        //更新
        BrakeSyncDO brakeSyncDO = BeanUtils.toBean(updateReqVO, BrakeSyncDO.class);
        brakeSyncMapper.updateSyncById(brakeSyncDO.getId(),brakeSyncDO.getAutomatic(),brakeSyncDO.getCycle(),brakeSyncDO.getSync());
        //同步至xxl-job
        xxlJobService.xxlJobUpdate(updateReqVO);

        // 4. 记录日志
        LogRecordContext.putVariable("id", updateReqVO.getId());
    }

    @Override
    public BrakeSyncRespVO getBrakeSync(String id) {
        BrakeSyncDO brakeSyncDO = brakeSyncMapper.selectSyncById(id);
        return BeanUtils.toBean(brakeSyncDO, BrakeSyncRespVO.class);
    }


    private void validateBrakeExists(String id) {
        if (brakeSyncMapper.selectSyncById(id) == null) {
            throw exception(BRAKESYNC_NOT_EXISTS);
        }
    }

}

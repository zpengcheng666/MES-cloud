package com.miyu.module.es.service.brakeSync;

import com.miyu.module.es.controller.admin.brake.vo.BrakeSaveReqVO;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncRespVO;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncSaveReqVO;

import javax.validation.Valid;

public interface BrakeSyncService {

    /**
     * 更新配置信息
     *
     * @param updateReqVO 更新信息
     */
    void updateBrakeSync(@Valid BrakeSyncSaveReqVO updateReqVO);

    /**
     * 获取配置信息
     */
    BrakeSyncRespVO getBrakeSync(String id);

}

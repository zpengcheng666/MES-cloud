package com.miyu.module.dc.service.offlinealarm;

import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmPageReqVO;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmResVO;

import java.util.List;

public interface OfflineAlarmService {

    List<OfflineAlarmResVO> queryOlineStatusDetailPage(OfflineAlarmPageReqVO offlineAlarmPageReqVO);

    Number queryOlineStatusDetailCount(OfflineAlarmPageReqVO offlineAlarmPageReqVO);

    List<OfflineAlarmResVO> queryNormStatusDetailPage(OfflineAlarmPageReqVO offlineAlarmPageReqVO);

    Number queryNormStatusDetailCount(OfflineAlarmPageReqVO offlineAlarmPageReqVO);
}

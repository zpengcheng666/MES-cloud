package com.miyu.module.dc.service.offlinecollect;

import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectPageReqVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectResVO;

import java.util.List;

public interface OfflineCollectService {

    List<OfflineCollectResVO> queryCollectList(OfflineCollectPageReqVO reqVO);

    Number queryCollectCount(OfflineCollectPageReqVO reqVO);


}

package com.miyu.module.dc.service.offlineerror;

import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorPageReqVO;
import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorResVO;

import java.util.List;

public interface OfflineErrorService {

    List<OfflineErrorResVO> queryOfflineError(OfflineErrorPageReqVO reqVO);

    Number queryOfflineErrorCount(OfflineErrorPageReqVO reqVO);

}

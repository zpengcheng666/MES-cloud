package com.miyu.module.pdm.service.processRoute;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRoutePageReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteRespVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;

import javax.validation.Valid;

public interface ProcessRouteService {
    //创建加工路线
    String createProcessRoute(@Valid ProcessRouteSaveReqVO createReqVO);

    //更新加工路线
    void updateProcessRoute(@Valid ProcessRouteSaveReqVO updateReqVO);

    //删除加工路线
    void deleteProcessRoute(String id);

    //获得加工路线
    ProcessRouteDO getProcessRoute(String id);

    //获得加工路线分页
    PageResult<ProcessRouteDO> getProcessRoutePage(ProcessRoutePageReqVO reqVO);
    
}

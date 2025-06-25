package com.miyu.module.pdm.service.processRouteTypical;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalPageReqVO;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processRouteTypical.ProcessRouteTypicalDO;

import javax.validation.Valid;

public interface ProcessRouteTypicalService {
    //创建典型工艺路线
    String createProcessRouteTypical(@Valid ProcessRouteTypicalSaveReqVO createReqVO);

    //更新典型工艺路线
    void updateProcessRouteTypical(@Valid ProcessRouteTypicalSaveReqVO updateReqVO);

    //删除典型工艺路线
    void deleteProcessRouteTypical(String id);

    //获得典型工艺路线
    ProcessRouteTypicalDO getProcessRouteTypical(String id);

    //获得典型工艺路线分页
    PageResult<ProcessRouteTypicalDO> getProcessRouteTypicalPage(ProcessRouteTypicalPageReqVO reqVO);

}

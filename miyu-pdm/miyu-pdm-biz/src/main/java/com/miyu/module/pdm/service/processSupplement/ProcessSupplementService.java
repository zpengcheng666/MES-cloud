package com.miyu.module.pdm.service.processSupplement;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementPageReqVO;
import com.miyu.module.pdm.controller.admin.processSupplement.vo.ProcessSupplementSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processSupplement.ProcessSupplementDO;

import javax.validation.Valid;
import java.util.List;

public interface ProcessSupplementService {
    //创建补加工工艺规程
    String createProcessSupplement(@Valid ProcessSupplementSaveReqVO createReqVO);

    //更新补加工工艺规程
    void updateProcessSupplement(@Valid ProcessSupplementSaveReqVO updateReqVO);

    //删除补加工工艺规程
    void deleteProcessSupplement(String id);

    //获得补加工工艺规程
    ProcessSupplementDO getProcessSupplement(String id);

    //获得补加工工艺规程分页
    PageResult<ProcessSupplementDO> getProcessSupplementPage(ProcessSupplementPageReqVO reqVO);

    //获得补加工工艺规程结构树
    List<ProcessPlanDetailRespVO> getProcessSupplementTreeList(ProcessPlanDetailReqVO reqVO);
    void startProcessInstance(@Valid ProcessSupplementSaveReqVO reqVO);
    void updateProcessSupplementInstanceStatus(String id,Integer status);
}

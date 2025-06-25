package com.miyu.module.pdm.service.process;

import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.QuotaPerPartSaveReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteListReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.*;
import com.miyu.module.pdm.controller.admin.project.vo.ProjectListReqVO;
import com.miyu.module.pdm.dal.dataobject.process.ProcessDO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.dal.dataobject.project.ProjectDO;
import com.miyu.module.pdm.dal.mysql.processRoute.ProcessRouteMapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.Valid;
import java.util.List;

public interface processService {

//    ProcessDO getProcess(ProcessReqVO reqVO);
    String createProcess(@Valid ProcessSaveReqVO createReqVO);

    void updateProcess(@Valid ProcessSaveReqVO updateReqVO);
    List<ProcessRouteDO> getRouteList(ProcessRouteListReqVO listReqVO);

    void deleteProcess(@Valid ProcessSaveReqVO deleteReqVO);
    ProcessRespVO selectProcessById(@Param("id") String id);

    Integer getProcessCountByPartVersionId(@Param("partVersionId") String partVersionId,@Param("processCondition") String processCondition);
    List<ProcessRespVO> getProcessListByPartVersionId(@Param("partVersionId") String partVersionId,@Param("processCondition") String processCondition);
    void saveSelectedProcess(ProcessSelectedReqVO reqVO);
    void saveProcessUp(ProcessSelectedReqVO reqVO);
    void startProcessInstance(@Valid ProcessTaskReqVO updateReqVO);
    void updateProcessInstanceStatus(String id,Integer status);


}

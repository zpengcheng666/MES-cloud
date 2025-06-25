package com.miyu.cloud.mcs.restServer.service.technology;

import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;

public interface TechnologyRestService {

    ProcessPlanDetailRespDTO getTechnologyById(String id);

    ProcedureRespDTO getProcess(String technologyId, String processId);

    ProcessPlanDetailRespDTO getTechnologyByIdCache(String orderId, String technologyId);

    ProcedureRespDTO getProcessCache(String orderId, String technologyId, String processId);

    String getPreProcessIdCache(String orderId, String technologyId, String processId);

    String getNextProcessIdCache(String orderId, String technologyId, String processId);
}

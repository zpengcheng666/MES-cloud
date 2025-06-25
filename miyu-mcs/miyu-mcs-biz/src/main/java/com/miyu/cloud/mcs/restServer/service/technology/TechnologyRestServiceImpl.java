package com.miyu.cloud.mcs.restServer.service.technology;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Validated
public class TechnologyRestServiceImpl implements TechnologyRestService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;

    @Override
    public ProcessPlanDetailRespDTO getTechnologyById(String id) {
        CommonResult<String> result = processPlanDetailApi.getProcessPlanDetail(id);
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        return JsonUtils.parseObject(result.getData(), ProcessPlanDetailRespDTO.class);
    }

    @Override
    public ProcedureRespDTO getProcess(String technologyId, String processId) {
        if (StringUtils.isBlank(technologyId) || StringUtils.isBlank(processId)) {
            throw new ServiceException(5009, "请校验参数正确!");
        }
        ProcessPlanDetailRespDTO technology = getTechnologyById(technologyId);
        List<ProcedureRespDTO> procedureList = technology.getProcedureList();
        Optional<ProcedureRespDTO> first = procedureList.stream().filter(item -> processId.equals(item.getId())).findFirst();
        if (!first.isPresent()) throw new ServiceException(5004, "未找到工序!");
        return first.get();
    }

    @Override
    public ProcessPlanDetailRespDTO getTechnologyByIdCache(String orderId, String technologyId) {
        String key = orderId + "_" + technologyId;
        String technologyString = (String) redisTemplate.opsForValue().get(key);
        if (technologyString == null) {
            CommonResult<String> result = processPlanDetailApi.getProcessPlanDetail(technologyId);
            if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
            technologyString = result.getData();
            redisTemplate.opsForValue().set(key, technologyString);
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        return JsonUtils.parseObject(technologyString, ProcessPlanDetailRespDTO.class);
    }

    @Override
    public ProcedureRespDTO getProcessCache(String orderId, String technologyId, String processId) {
        if (StringUtils.isBlank(technologyId) || StringUtils.isBlank(processId)) {
            throw new ServiceException(5009, "请校验参数正确!");
        }
        ProcessPlanDetailRespDTO technology = getTechnologyByIdCache(orderId, technologyId);
        List<ProcedureRespDTO> procedureList = technology.getProcedureList();
        Optional<ProcedureRespDTO> first = procedureList.stream().filter(item -> processId.equals(item.getId())).findFirst();
        if (!first.isPresent()) throw new ServiceException(5004, "未找到工序!");
        return first.get();
    }

    @Override
    public String getPreProcessIdCache(String orderId, String technologyId, String processId) {
        if (processId == null) throw new ServiceException(5004, "参数异常!processId为null");
        String preProcessId = null;
        ProcessPlanDetailRespDTO technologyByIdCache = getTechnologyByIdCache(orderId, technologyId);
        List<ProcedureRespDTO> procedureList = technologyByIdCache.getProcedureList();
        for (ProcedureRespDTO procedureRespDTO : procedureList) {
            if (ProcedureRespDTO.isIgnoreProcedure(procedureRespDTO)) continue;
            if (processId.equals(procedureRespDTO.getId())) {
                return preProcessId;
            }
            preProcessId = procedureRespDTO.getId();
        }
        throw new ServiceException(5004, "未找到相匹配的工序");
    }

    @Override
    public String getNextProcessIdCache(String orderId, String technologyId, String processId) {
        if (processId == null) throw new ServiceException(5004, "参数异常!processId为null");
        ProcessPlanDetailRespDTO technologyByIdCache = getTechnologyByIdCache(orderId, technologyId);
        List<ProcedureRespDTO> procedureList = technologyByIdCache.getProcedureList();
        boolean flag = false;
        for (ProcedureRespDTO procedureRespDTO : procedureList) {
            if (flag) {
                if (ProcedureRespDTO.isIgnoreProcedure(procedureRespDTO)) continue;
                return procedureRespDTO.getId();
            }
            if (processId.equals(procedureRespDTO.getId())) {
                flag = true;
            }
        }
        return null;
    }
}

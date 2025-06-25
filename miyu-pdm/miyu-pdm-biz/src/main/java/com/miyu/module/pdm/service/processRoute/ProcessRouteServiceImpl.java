package com.miyu.module.pdm.service.processRoute;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.google.common.annotations.VisibleForTesting;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRoutePageReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.dal.mysql.process.ProcedureMapper;
import com.miyu.module.pdm.dal.mysql.processRoute.ProcessRouteMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;

@Service
@Slf4j
public class ProcessRouteServiceImpl implements ProcessRouteService{

    @Resource
    private ProcessRouteMapper processRouteMapper;
    @Resource
    private ProcedureMapper procedureMapper;

    @Override
    public String createProcessRoute(ProcessRouteSaveReqVO createReqVO) {
        // 检验加工路线名称唯一
        validateProcessRouteNameUnique(null, createReqVO.getName());
        ProcessRouteDO processRoute= BeanUtils.toBean(createReqVO,ProcessRouteDO.class);
        processRouteMapper.insert(processRoute);
        return processRoute.getId();
    }

    @Override
    @LogRecord(type = PDM_PROCESS_ROUTE_TYPE, subType = PDM_PROCESS_ROUTE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_PROCESS_ROUTE_UPDATE_SUCCESS)
    public void updateProcessRoute(ProcessRouteSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessRouteExists(updateReqVO.getId());
        // 检验加工路线名称唯一
        validateProcessRouteNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 更新
        ProcessRouteDO updateObj = BeanUtils.toBean(updateReqVO, ProcessRouteDO.class);
        processRouteMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("processRouteName", updateReqVO.getName());
    }

    /**
     * 检验加工路线名称是否唯一
     */
    @VisibleForTesting
    void validateProcessRouteNameUnique(String id, String name) {
        if (StrUtil.isBlank(name)) {
            return;
        }
        ProcessRouteDO routeDO = processRouteMapper.selectByProcessRouteName(name);
        if (routeDO == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id
        if (id == null || id.equals("")) {
            throw exception(PROCESSROUTE_EXISTS);
        }
        if (!routeDO.getId().equals(id)) {
            throw exception(PROCESSROUTE_EXISTS);
        }
    }

    private void validateProcessRouteExists(String id) {
        if (processRouteMapper.selectById(id) == null) {
            throw exception(PROCESSROUTE_NOT_EXISTS);
        }
    }
    @Override
    @LogRecord(type = PDM_PROCESS_ROUTE_TYPE, subType = PDM_PROCESS_ROUTE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_PROCESS_ROUTE_DELETE_SUCCESS)
    public void deleteProcessRoute(String id) {
        // 校验存在
        validateProcessRouteExists(id);
        ProcessRouteDO processRoute = processRouteMapper.selectById(id);
        if (procedureMapper.selectCountByProcedureName(processRoute.getName() )> 0){
            throw exception(PROCESSROUTE_HASED_EXISTS);
        }
        // 删除
        processRouteMapper.deleteById(id);
        // 记录日志
        LogRecordContext.putVariable("processRouteName", processRoute.getName());
    }

    @Override
    public ProcessRouteDO getProcessRoute(String id) {
        return processRouteMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessRouteDO> getProcessRoutePage(ProcessRoutePageReqVO reqVO) {
        return processRouteMapper.selectPage(reqVO);
    }

}

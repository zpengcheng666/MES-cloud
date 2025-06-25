package com.miyu.module.pdm.service.processRouteTypical;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.google.common.annotations.VisibleForTesting;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalPageReqVO;
import com.miyu.module.pdm.controller.admin.processRouteTypical.vo.ProcessRouteTypicalSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.processRouteTypical.ProcessRouteTypicalDO;
import com.miyu.module.pdm.dal.mysql.process.ProcedureMapper;
import com.miyu.module.pdm.dal.mysql.processRouteTypical.ProcessRouteTypicalMapper;
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
public class ProcessRouteTypicalServiceImpl implements ProcessRouteTypicalService {

    @Resource
    private ProcessRouteTypicalMapper processRouteTypicalMapper;
    @Resource
    private ProcedureMapper procedureMapper;

    @Override
    public String createProcessRouteTypical(ProcessRouteTypicalSaveReqVO createReqVO) {
        // 检验典型工艺路线名称唯一
        validateProcessRouteNameUnique(null, createReqVO.getName());
        ProcessRouteTypicalDO processRouteTypical= BeanUtils.toBean(createReqVO,ProcessRouteTypicalDO.class);
        processRouteTypicalMapper.insert(processRouteTypical);
        return processRouteTypical.getId();
    }

    @Override
    @LogRecord(type = PDM_PROCESS_ROUTE_TYPICAL_TYPE, subType = PDM_PROCESS_ROUTE_TYPICAL_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_PROCESS_ROUTE_TYPICAL_UPDATE_SUCCESS)
    public void updateProcessRouteTypical(ProcessRouteTypicalSaveReqVO updateReqVO) {
        // 校验存在
        validateProcessRouteExists(updateReqVO.getId());
        // 检验典型工艺路线名称唯一
        validateProcessRouteNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 更新
        ProcessRouteTypicalDO updateObj = BeanUtils.toBean(updateReqVO, ProcessRouteTypicalDO.class);
        processRouteTypicalMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("processRouteTypicalName", updateReqVO.getName());
    }

    /**
     * 检验典型工艺路线名称是否唯一
     */
    @VisibleForTesting
    void validateProcessRouteNameUnique(String id, String name) {
        if (StrUtil.isBlank(name)) {
            return;
        }
        ProcessRouteTypicalDO routeDO = processRouteTypicalMapper.selectByProcessRouteName(name);
        if (routeDO == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id
        if (id == null || id.equals("")) {
            throw exception(PROCESSROUTE_TYPICAL_EXISTS);
        }
        if (!routeDO.getId().equals(id)) {
            throw exception(PROCESSROUTE_TYPICAL_EXISTS);
        }
    }

    private void validateProcessRouteExists(String id) {
        if (processRouteTypicalMapper.selectById(id) == null) {
            throw exception(PROCESSROUTE_TYPICAL_NOT_EXISTS);
        }
    }
    @Override
    @LogRecord(type = PDM_PROCESS_ROUTE_TYPICAL_TYPE, subType = PDM_PROCESS_ROUTE_TYPICAL_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_PROCESS_ROUTE_TYPICAL_DELETE_SUCCESS)
    public void deleteProcessRouteTypical(String id) {
        // 校验存在
        validateProcessRouteExists(id);
        ProcessRouteTypicalDO processRoute = processRouteTypicalMapper.selectById(id);
        if (procedureMapper.selectCountByTypicalId(id )> 0){
            throw exception(PROCESSROUTE_TYPICAL_HASED_EXISTS);
        }
        // 删除
        processRouteTypicalMapper.deleteById(id);
        // 记录日志
        LogRecordContext.putVariable("processRouteTypicalName", processRoute.getName());
    }

    @Override
    public ProcessRouteTypicalDO getProcessRouteTypical(String id) {
        return processRouteTypicalMapper.selectById(id);
    }

    @Override
    public PageResult<ProcessRouteTypicalDO> getProcessRouteTypicalPage(ProcessRouteTypicalPageReqVO reqVO) {
        return processRouteTypicalMapper.selectPage(reqVO);
    }

}

package com.miyu.module.pdm.service.processTask;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.dal.mysql.processTask.ProcessTaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Validated
public class ProcessTaskServiceImpl implements ProcessTaskService {
    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Override
    public String addProcessTask(ProcessTaskReqVO addReqVO) {
        List<String> partVersionIdArr = addReqVO.getPartVersionIdArr();
        partVersionIdArr.forEach(item -> {
            addReqVO.setPartVersionId(item);
            ProcessTaskDO processTask= BeanUtils.toBean(addReqVO,ProcessTaskDO.class)
                    .setId(IdUtil.fastSimpleUUID());
            processTaskMapper.insert(processTask);
        });
        return addReqVO.getProjectCode();
    }

    @Override
    public void updateProcessTask(ProcessTaskReqVO updateReqVO) {
        ProcessTaskDO updateObj = BeanUtils.toBean(updateReqVO, ProcessTaskDO.class);
        processTaskMapper.updateById(updateObj);
    }
    @Override
    public ProcessTaskDO getProcessTask(String id) {
        return processTaskMapper.selectById(id);
    }

    @Override
    public List<ProcessTaskDO> getProcessTaskList(ProcessTaskReqVO reqVO) {
        return processTaskMapper.selectList(reqVO);
    }

    @Override
    public List<ProcessTaskRespVO> getPartListByProjectCode(ProcessTaskReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String status=reqVO.getStatus();
        String partNumber = reqVO.getPartNumber();
        return processTaskMapper.selectProcessTaskList(projectCode,partNumber,status);
    }

    @Override
    public List<ProcessTaskRespVO> getPartListByProjectCodeNew(ProcessTaskRespVO reqVO) {
            String projectCode = reqVO.getProjectCode();
            String status=reqVO.getStatus();
            String partNumber = reqVO.getPartNumber();
            return processTaskMapper.selectPartListByProjectCodeNew(projectCode,partNumber,status);
    }


    @Override
    public String addProcessTaskNew(ProcessTaskReqVO addReqVO) {
        List<String> taskIdArr = addReqVO.getTaskIdArr();
        taskIdArr.forEach(item -> {
                ProcessTaskDO updateObj= BeanUtils.toBean(addReqVO,ProcessTaskDO.class)
                    .setId(item);
            processTaskMapper.updateById(updateObj);

        });
        return addReqVO.getProjectCode();
    }

    @Override
    public void updateProjectstatus(String projectCode) {
        processTaskMapper.updateProjectstatus(projectCode);
    }
}

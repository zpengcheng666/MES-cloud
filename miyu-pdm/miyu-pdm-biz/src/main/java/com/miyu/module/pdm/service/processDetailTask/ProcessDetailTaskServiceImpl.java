package com.miyu.module.pdm.service.processDetailTask;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskRespVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.processDetailTask.ProcessDetailTaskDO;
import com.miyu.module.pdm.dal.mysql.processDetailTask.ProcessDetailTaskMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProcessDetailTaskServiceImpl implements ProcessDetailTaskService {

    @Resource
    private ProcessDetailTaskMapper processDetailTaskMapper;

    @Override
    public List<ProjPartBomTreeRespVO> getProjPartBomTreeList(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        return processDetailTaskMapper.selectPartTreeList(projectCode, partNumber, status);
    }
    @Override
    public List<ProcessDetailTaskRespVO> getPartListByProjectCode(ProcessDetailTaskReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        Integer status=reqVO.getStatus();
        String partNumber = reqVO.getPartNumber();
        return processDetailTaskMapper.selectProcessDetailTaskList(projectCode,partNumber,status);
    }

    @Override
    public ProcessDetailTaskDO getProcessDetailTask(String id) {
        return processDetailTaskMapper.selectById(id);
    }

    @Override
    public String assignProcessTask(ProcessDetailTaskReqVO addReqVO) {
        List<String> partVersionIdArr = addReqVO.getPartVersionIdArr();
        List<String> procedureIdArr = addReqVO.getProcedureIdArr();
        List<String> processVersionIdArr = addReqVO.getProcessVersionIdArr();
        //根据partVersionId数组以及procedureId数组大小设定限制循环次数的条件，将Req中接收的数组逐个拆分，
        //这两个数组缺一不可
        int limit = Math.min(partVersionIdArr.size(), procedureIdArr.size());
        for (int i = 0; i < limit; i++) {
            String partVersionId = partVersionIdArr.get(i);
            String procedureId = procedureIdArr.get(i);
            String processVersionId = processVersionIdArr.get(i);

            addReqVO.setPartVersionId(partVersionId);
            addReqVO.setProcedureId(procedureId);
            addReqVO.setProcessVersionId(processVersionId);
            ProcessDetailTaskDO processDetailTask = BeanUtils.toBean(addReqVO, ProcessDetailTaskDO.class)
                    .setId(IdUtil.fastSimpleUUID());
            processDetailTaskMapper.insert(processDetailTask);
        }
        return addReqVO.getProjectCode();
    }
    @Override
    public void updateProcessTask(ProcessDetailTaskReqVO updateReqVO) {
        ProcessDetailTaskDO updateObj = BeanUtils.toBean(updateReqVO, ProcessDetailTaskDO.class);
        processDetailTaskMapper.updateById(updateObj);
    }

}

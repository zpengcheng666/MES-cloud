package com.miyu.module.pdm.service.feasibilityTask;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.mysql.feasibilityTask.FeasibilityTaskMapper;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


@Service
@Validated
public class FeasibilityTaskServiceImpl implements FeasibilityTaskService{

    @Resource
    private FeasibilityTaskMapper feasibilityTaskMapper;

    @Override
    public String addFeasibilityTask(FeasibilityTaskReqVO addReqVO) {
        List<String> partVersionIdArr = addReqVO.getPartVersionIdArr();
        partVersionIdArr.forEach(item -> {
            addReqVO.setPartVersionId(item);
            FeasibilityTaskDO feasibilityTask= BeanUtils.toBean(addReqVO,FeasibilityTaskDO.class)
                    .setId(IdUtil.fastSimpleUUID());
            feasibilityTaskMapper.insert(feasibilityTask);
        });
        return addReqVO.getProjectCode();
    }

    @Override
    public void updateFeasibilityTask(FeasibilityTaskReqVO updateReqVO) {
        FeasibilityTaskDO updateObj = BeanUtils.toBean(updateReqVO, FeasibilityTaskDO.class);
        feasibilityTaskMapper.updateById(updateObj);
    }
    @Override
    public FeasibilityTaskDO getFeasibilityTask(String id) {
        return feasibilityTaskMapper.selectById(id);
    }

    @Override
    public List<FeasibilityTaskDO> getFeasibilityTaskList(FeasibilityTaskReqVO reqVO) {

        return feasibilityTaskMapper.selectList(reqVO);
    }

    @Override
    public List<FeasibilityTaskRespVO> getPartListByProjectCode(FeasibilityTaskReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String status=reqVO.getStatus();
        String partNumber = reqVO.getPartNumber();
        return feasibilityTaskMapper.selectFeasibilityTaskList(projectCode,partNumber,status);
    }

    @Override
    public List<FeasibilityTaskRespVO> getPartListByProjectCodeNew(FeasibilityTaskReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String status=reqVO.getStatus();
        String partNumber = reqVO.getPartNumber();
        return feasibilityTaskMapper.selectPartListByProjectCodeNew(projectCode,partNumber,status);
    }

    @Override
    public String addFeasibilityTaskNew(FeasibilityTaskReqVO addReqVO) {
        List<String> taskIdArr = addReqVO.getTaskIdArr();
        taskIdArr.forEach(item -> {
            FeasibilityTaskDO updateObj= BeanUtils.toBean(addReqVO,FeasibilityTaskDO.class)
                    .setId(item);
            feasibilityTaskMapper.updateById(updateObj);

        });
        return addReqVO.getProjectCode();
    }

}

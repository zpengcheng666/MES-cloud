package com.miyu.module.tms.service.assembletask;

import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.mysql.toolinfo.ToolInfoMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.tms.controller.admin.assembletask.vo.*;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.assembletask.AssembleTaskMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具装配任务 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class AssembleTaskServiceImpl implements AssembleTaskService {

    @Resource
    private AssembleTaskMapper assembleTaskMapper;
    @Resource
    public ToolInfoMapper toolInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAssembleTask(AssembleTaskSaveReqVO createReqVO) {
        // 插入
        AssembleTaskDO assembleTask = BeanUtils.toBean(createReqVO, AssembleTaskDO.class);
        assembleTaskMapper.insert(assembleTask);
        // 返回
        return assembleTask.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAssembleTask(AssembleTaskSaveReqVO updateReqVO) {
        // 校验存在
        validateAssembleTaskExists(updateReqVO.getId());
        // 更新
        AssembleTaskDO updateObj = BeanUtils.toBean(updateReqVO, AssembleTaskDO.class);
        assembleTaskMapper.updateById(updateObj);

        if (updateReqVO.getStatus().equals(2)){//接单
            List<ToolInfoDO> toolInfoDOS = new ArrayList<>();
            for (int i=0 ; i<updateReqVO.getNeedCount();i++){
                ToolInfoDO toolInfoDO = new ToolInfoDO();
                toolInfoDO.setAssembleTaskId(updateReqVO.getId());
                toolInfoDO.setMaterialConfigId(updateReqVO.getMaterialConfigId());
                toolInfoDO.setStatus(1);
                toolInfoDOS.add(toolInfoDO);
            }
            toolInfoMapper.insertBatch(toolInfoDOS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssembleTask(String id) {
        // 校验存在
        validateAssembleTaskExists(id);
        // 删除
        assembleTaskMapper.deleteById(id);
    }

    private void validateAssembleTaskExists(String id) {
        if (assembleTaskMapper.selectById(id) == null) {
            throw exception(ASSEMBLE_TASK_NOT_EXISTS);
        }
    }

    @Override
    public AssembleTaskDO getAssembleTask(String id) {
        return assembleTaskMapper.selectById(id);
    }

    @Override
    public PageResult<AssembleTaskDO> getAssembleTaskPage(AssembleTaskPageReqVO pageReqVO) {
        return assembleTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AssembleTaskDO> getAssembleTaskListByOrderNumbers(Collection<String> orderNumbers) {
        return assembleTaskMapper.selectList(AssembleTaskDO::getOrderNumber, orderNumbers);
    }


}
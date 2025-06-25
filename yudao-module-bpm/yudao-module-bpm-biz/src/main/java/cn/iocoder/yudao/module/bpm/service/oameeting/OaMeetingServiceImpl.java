package cn.iocoder.yudao.module.bpm.service.oameeting;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oameeting.OaMeetingDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.oameeting.OaMeetingMapper;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.OA_MEETING_NOT_EXISTS;

/**
 * OA 会议申请 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OaMeetingServiceImpl implements OaMeetingService {

    @Resource
    private OaMeetingMapper oaMeetingMapper;

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "boa_meeting";

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    public Long createOaMeeting(Long userId,OaMeetingSaveReqVO createReqVO) {
        // 插入
        OaMeetingDO oaMeeting = BeanUtils.toBean(createReqVO, OaMeetingDO.class)
                .setStatus(BpmTaskStatusEnum.RUNNING.getStatus());;
        oaMeetingMapper.insert(oaMeeting);

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(oaMeeting.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();
        // 将工作流的编号，更新到 OA 请假单中
        oaMeetingMapper.updateById(new OaMeetingDO().setId(oaMeeting.getId()).setProcessInstanceId(processInstanceId));
        // 返回
        return oaMeeting.getId();
    }

    @Override
    public void updateOaMeeting(OaMeetingSaveReqVO updateReqVO) {
        // 校验存在
        validateOaMeetingExists(updateReqVO.getId());
        // 更新
        OaMeetingDO updateObj = BeanUtils.toBean(updateReqVO, OaMeetingDO.class);
        oaMeetingMapper.updateById(updateObj);
    }

    @Override
    public void deleteOaMeeting(Long id) {
        // 校验存在
        validateOaMeetingExists(id);
        // 删除
        oaMeetingMapper.deleteById(id);
    }

    private void validateOaMeetingExists(Long id) {
        if (oaMeetingMapper.selectById(id) == null) {
            throw exception(OA_MEETING_NOT_EXISTS);
        }
    }

    @Override
    public OaMeetingDO getOaMeeting(Long id) {
        return oaMeetingMapper.selectById(id);
    }

    @Override
    public PageResult<OaMeetingDO> getOaMeetingPage(OaMeetingPageReqVO pageReqVO) {
        return oaMeetingMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateLeaveStatus(Long id, Integer status) {
        validateLeaveExists(id);
        oaMeetingMapper.updateById(new OaMeetingDO().setId(id).setStatus(status));
    }

    private void validateLeaveExists(Long id) {
        if (oaMeetingMapper.selectById(id) == null) {
            throw exception(OA_MEETING_NOT_EXISTS);
        }
    }

}

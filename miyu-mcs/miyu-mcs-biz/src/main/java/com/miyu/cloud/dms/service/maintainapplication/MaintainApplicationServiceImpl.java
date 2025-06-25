package com.miyu.cloud.dms.service.maintainapplication;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import com.miyu.cloud.dms.controller.admin.maintainapplication.vo.MaintainApplicationPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintainapplication.vo.MaintainApplicationSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintainapplication.MaintainApplicationDO;
import com.miyu.cloud.dms.dal.mysql.maintainapplication.MaintainApplicationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.*;

/**
 * 设备维修申请 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class MaintainApplicationServiceImpl implements MaintainApplicationService {

    private void check(MaintainApplicationSaveReqVO data) {
        if (StringUtils.isBlank(data.getDevice())) {
            throw exception(MAINTAIN_APPLICATION_DEVICE_EMPTY);
        }
        if (data.getImportant() == null) {
            throw exception(MAINTAIN_APPLICATION_IMPORTANT_EMPTY);
        }
        if (data.getType() == null) {
            throw exception(MAINTAIN_APPLICATION_TYPE_EMPTY);
        }
        if (StringUtils.isBlank(data.getDescribe1())) {
            throw exception(MAINTAIN_APPLICATION_DESCRIBE_EMPTY);
        }
        if (data.getDuration() == null) {
            throw exception(MAINTAIN_APPLICATION_DURATION_EMPTY);
        }
    }

    public static final String PROCESS_KEY = "dms_maintain_application";

    @Resource
    private MaintainApplicationMapper maintainApplicationMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public String createMaintainApplication(MaintainApplicationSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        MaintainApplicationDO maintainApplication = BeanUtils.toBean(createReqVO, MaintainApplicationDO.class);
        maintainApplication.setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        maintainApplication.setApplicationTime(LocalDateTime.now());

        Long userId = WebFrameworkUtils.getLoginUserId();
        maintainApplication.setApplicant(userId.toString());

        maintainApplicationMapper.insert(maintainApplication);
        // 返回


        Map<String, Object> processInstanceVariables = new HashMap<>();

        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY).setBusinessKey(maintainApplication.getId()).setVariables(processInstanceVariables)

        ).getCheckedData();

        maintainApplication.setProcessInstanceId(processInstanceId);

        maintainApplicationMapper.updateById(maintainApplication);

        return maintainApplication.getId();
    }

    @Override
    public void updateMaintainApplication(MaintainApplicationSaveReqVO updateReqVO) {
        // 校验存在
        validateMaintainApplicationExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        MaintainApplicationDO updateObj = BeanUtils.toBean(updateReqVO, MaintainApplicationDO.class);
        maintainApplicationMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaintainApplication(String id) {
        // 校验存在
        validateMaintainApplicationExists(id);
        // 删除
        maintainApplicationMapper.deleteById(id);
    }

    private void validateMaintainApplicationExists(String id) {
        if (maintainApplicationMapper.selectById(id) == null) {
            throw exception(MAINTAIN_APPLICATION_NOT_EXISTS);
        }
    }

    @Override
    public MaintainApplicationDO getMaintainApplication(String id) {
        return maintainApplicationMapper.selectById(id);
    }

    @Override
    public PageResult<MaintainApplicationDO> getMaintainApplicationPage(MaintainApplicationPageReqVO pageReqVO) {
        return maintainApplicationMapper.selectPage(pageReqVO);
    }

}
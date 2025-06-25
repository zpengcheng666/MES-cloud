package cn.iocoder.yudao.module.bpm.service.oaclock;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oaclock.OaClockDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.oaclock.OaClockMapper;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.OA_CLOCK_IN_EXISTS;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.OA_CLOCK_NOT_EXISTS;

/**
 * OA 打卡 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OaClockServiceImpl implements OaClockService {

    @Resource
    private OaClockMapper oaClockMapper;

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "boa_clock";

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    public Long createOaClock(OaClockSaveReqVO createReqVO) {
        // 插入
        OaClockDO oaClock = BeanUtils.toBean(createReqVO, OaClockDO.class)
                .setStatus(BpmTaskStatusEnum.RUNNING.getStatus());

        //获取打卡状态判定
        int clockStatus = getClockStatus(oaClock);

        //这部分是已经打过卡的情况,上班卡禁止重打，下班卡可以更新
        QueryWrapper<OaClockDO> wrapper = new QueryWrapper<>();
        wrapper.like("clock_time",LocalDate.now());
        wrapper.eq("type",oaClock.getType());
        List<OaClockDO> oaClockDOS = oaClockMapper.selectList(wrapper);
        if(oaClockDOS.size()>0&&oaClock.getType()==1){
            throw exception(OA_CLOCK_IN_EXISTS);
        }
        if(oaClockDOS.size()>0&&oaClock.getType()==2){
            // 更新下班打卡时间
            oaClockMapper.updateById(new OaClockDO()
                    .setId(oaClockDOS.get(0).getId())
                    .setClockTime(oaClock.getClockTime())
                    .setClockStatus(clockStatus));
            return oaClockDOS.get(0).getId();
        }

        oaClockMapper.insert(oaClock);

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("type",oaClock.getType());
        processInstanceVariables.put("clock",clockStatus);
        String processInstanceId = processInstanceApi.createProcessInstance(createReqVO.getUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(oaClock.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();
        // 将工作流的编号，更新到 打卡记录中
        oaClockMapper.updateById(new OaClockDO().setId(oaClock.getId()).setProcessInstanceId(processInstanceId).setClockStatus(clockStatus));
        // 返回
        return oaClock.getId();
    }

    @Override
    public void updateOaClock(OaClockSaveReqVO updateReqVO) {
        // 校验存在
        validateOaClockExists(updateReqVO.getId());
        // 更新
        OaClockDO updateObj = BeanUtils.toBean(updateReqVO, OaClockDO.class);
        oaClockMapper.updateById(updateObj);
    }

    @Override
    public void deleteOaClock(Long id) {
        // 校验存在
        validateOaClockExists(id);
        // 删除
        oaClockMapper.deleteById(id);
    }

    private void validateOaClockExists(Long id) {
        if (oaClockMapper.selectById(id) == null) {
            throw exception(OA_CLOCK_NOT_EXISTS);
        }
    }

    @Override
    public OaClockDO getOaClock(Long id) {
        return oaClockMapper.selectById(id);
    }

    @Override
    public PageResult<OaClockDO> getOaClockPage(OaClockPageReqVO pageReqVO) {
        return oaClockMapper.selectPage(pageReqVO);
    }

    public int getClockStatus(OaClockDO oaClock){
        LocalDate now = LocalDate.now();
        int clock_status = 0;
        if(oaClock.getType()==1){
            //今天的上班时间
            LocalDateTime localDateTime = LocalDateTime.of(now, LocalTime.of(8, 30, 0));
            boolean before = oaClock.getClockTime().isBefore(localDateTime);
            //在上班时间前,打卡成功,之后迟到
            if(before){
                //正常
                clock_status = 1;
            }else {
                //迟到
                clock_status = 2;
            }
        }else {
            //今天下班时间
            LocalDateTime localDateTime = LocalDateTime.of(now, LocalTime.of(17, 30, 0));
            boolean after = oaClock.getClockTime().isAfter(localDateTime);
            //在下班时间后,打卡成功,之前迟到
            if(after){
                //正常
                clock_status = 3;
            }else {
                //迟到
                clock_status = 4;
            }
        }
        return clock_status;
    }

    @Override
    public void updateLeaveStatus(Long id, Integer status) {
        validateLeaveExists(id);
        oaClockMapper.updateById(new OaClockDO().setId(id).setStatus(status));
    }

    private void validateLeaveExists(Long id) {
        if (oaClockMapper.selectById(id) == null) {
            throw exception(OA_CLOCK_NOT_EXISTS);
        }
    }

}

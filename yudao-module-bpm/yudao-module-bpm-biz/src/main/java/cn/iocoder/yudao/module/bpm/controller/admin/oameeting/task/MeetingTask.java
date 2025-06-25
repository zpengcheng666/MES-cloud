package cn.iocoder.yudao.module.bpm.controller.admin.oameeting.task;

import cn.iocoder.yudao.module.bpm.dal.mysql.oameeting.OaMeetingMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 任务执行器
 */
@Component
@Slf4j
public class MeetingTask implements JavaDelegate {
    @Resource
    private OaMeetingMapper oaMeetingMapper;

    @Override
    public void execute(DelegateExecution execution) {
//        System.out.println("会议申请的任务执行器执行了");
//        System.out.println(execution.getId());
//        //这个才是实例id
//        System.out.println(execution.getProcessInstanceId());
//        String id = execution.getId();
//        String ids = execution.getProcessInstanceId();
//        System.out.println(id);
//        System.out.println(ids);
//        QueryWrapper<OaMeetingDO> wrapper = new QueryWrapper<>();
//        wrapper.eq("process_instance_id",execution.getProcessInstanceId());
//        OaMeetingDO oaMeetingDO = oaMeetingMapper.selectOne(wrapper);
//        System.out.println(oaMeetingDO);
//
//        System.out.println(execution.getCurrentFlowableListener().getFieldExtensions());
    }
}

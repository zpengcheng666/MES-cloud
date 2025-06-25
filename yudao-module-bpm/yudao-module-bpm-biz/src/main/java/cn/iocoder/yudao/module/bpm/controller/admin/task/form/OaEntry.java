package cn.iocoder.yudao.module.bpm.controller.admin.task.form;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.bpm.framework.flowable.core.util.FlowableUtils;
import cn.iocoder.yudao.module.bpm.service.definition.BpmProcessDefinitionService;
import cn.iocoder.yudao.module.bpm.service.task.BpmProcessInstanceService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.vo.UserSaveReqVOTemp;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务执行器
 */
@Component
@Slf4j
public class OaEntry implements JavaDelegate {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @Resource
    private BpmProcessDefinitionService processDefinitionService;

    @Resource
    private AdminUserApi adminUserApi;


    @Override
    public void execute(DelegateExecution execution) {
        //这个才是实例id
        System.out.println(execution.getProcessInstanceId());

        String id = execution.getProcessInstanceId();
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(id);
        Map<String, Object> map = FlowableUtils.getProcessInstanceFormVariable(processInstance);
        UserSaveReqVOTemp reqVOTemp = new UserSaveReqVOTemp();
        //填充数据
        fillData(reqVOTemp,map);

        CommonResult<Long> user = adminUserApi.createUser(reqVOTemp);
    }

    /**
     * 填充创建新用户需要的数据
     * @param reqVOTemp
     * @param map
     */
    public void fillData(UserSaveReqVOTemp reqVOTemp,Map<String, Object> map){
        if(map.containsKey("postIds")){
            List<Long> list = (List<Long>) map.get("postIds");
            Set<Long> postIds = new HashSet<>();
            postIds.addAll(list);
            reqVOTemp.setPostIds(postIds);
        }
        if(map.containsKey("username")){
            reqVOTemp.setUsername(map.get("username").toString());
            reqVOTemp.setNickname(map.get("username").toString());
        }
        if(map.containsKey("deptId")){
            reqVOTemp.setDeptId(Long.valueOf(map.get("deptId").toString()));
        }

        if(map.containsKey("email")){
            reqVOTemp.setEmail(map.get("email").toString());
        }

        if(map.containsKey("mobile")){
            reqVOTemp.setMobile(map.get("mobile").toString());
        }

        if(map.containsKey("sex")){
            reqVOTemp.setSex(Integer.valueOf(map.get("sex").toString()));
        }
        reqVOTemp.setPassword("123456");
    }
}

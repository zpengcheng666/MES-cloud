package com.miyu.module.pdm.api.feasibilityDetail;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.mysql.feasibilityTask.FeasibilityTaskMapper;
import com.miyu.module.pdm.dal.mysql.processTask.ProcessTaskMapper;
import com.miyu.module.pdm.service.feasibilityDetail.FeasibilityDetailService;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.service.processTask.ProcessTaskService;
import jodd.util.StringUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Validated
public class FeasibilityDetailApiImpl implements FeasibilityDetailApi {

    @Resource
    private FeasibilityDetailService feasibilityDetailService;
    @Resource
    private ProcessTaskService processTaskService;
    @Resource
    private FeasibilityTaskMapper feasibilityTaskMapper;


    @Override
    public CommonResult<String> updateProjectstatus(String projectCode) {
        if(StringUtil.isEmpty(projectCode)) {
            return CommonResult.error(500, "参数不能为空");
        }
        feasibilityDetailService.updateProjectstatus(projectCode);
        processTaskService.updateProjectstatus(projectCode);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<String> updateFeasilityTaskStatus(String businessKey, Integer status) {
        feasibilityDetailService.updateProcessInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<String> pushFeasibility(String projectCode, String partNumber, String partName, String processCondition) {
        if(StringUtil.isEmpty(projectCode) || StringUtil.isEmpty(partNumber) || StringUtil.isEmpty(partName) || StringUtil.isEmpty(processCondition)) {
            return CommonResult.error(500, "参数不能为空");
        }
        feasibilityTaskMapper.deleteMessage(projectCode, partNumber, partName, processCondition);
        //加工状态：①A状态-粗加工 ②A状态 ③锻件粗加工
        String partProcessCondition = processCondition;
        if(processCondition.indexOf("-")>=0) {
            partProcessCondition = processCondition.substring(0,processCondition.indexOf("-"));
        } else {
            if(processCondition.indexOf("状态")>=0) {
                partProcessCondition = processCondition.substring(0,processCondition.indexOf("状态")+2);
            } else if(processCondition.indexOf("件")>=0) {
                partProcessCondition = processCondition.substring(0,processCondition.indexOf("件")+1);
            }
        }
        List<FeasibilityTaskDO> list = feasibilityTaskMapper.selectTaskList(partNumber, partProcessCondition);
        if (list.size() > 0) {
            FeasibilityTaskDO firstFeasibilityTask = list.get(0);
            String partVersionId = firstFeasibilityTask.getPartVersionId();
            String id = IdUtil.fastSimpleUUID();
            feasibilityTaskMapper.insertFeasibilityTask(id, projectCode, partNumber, partName, processCondition,partVersionId);
        }
        return CommonResult.success("ok");
    }


}
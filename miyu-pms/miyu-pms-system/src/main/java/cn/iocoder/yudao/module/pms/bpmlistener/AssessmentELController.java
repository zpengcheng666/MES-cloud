package cn.iocoder.yudao.module.pms.bpmlistener;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.enums.AssessmentConstans;
import cn.iocoder.yudao.module.pms.service.assessment.AssessmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("pms/assessment/listen")
public class AssessmentELController {

    @Resource
    private AssessmentService assessmentService;

    @PostMapping("/createCostChild")
    public CommonResult<String> createCostChild(@RequestBody Map<String,String> map){
        map.put("assessment_type", AssessmentConstans.CostAssessment);
        String assessmentReplenish = assessmentService.createAssessmentReplenish(map);

        return success(assessmentReplenish);

    }
    @PostMapping("/createTechnologyChild")
    public CommonResult<String> createTechnologyChild(@RequestBody Map<String,String> map){
        map.put("assessment_type", AssessmentConstans.TechnologyAssessment);
        String assessmentReplenish = assessmentService.createAssessmentReplenish(map);
        return success(assessmentReplenish);

    }
    @PostMapping("/createCapacityChild")
    public CommonResult<String> createCapacityChild(@RequestBody Map<String,String> map){
        map.put("assessment_type", AssessmentConstans.CapacityAssessment);
        String assessmentReplenish = assessmentService.createAssessmentReplenish(map);
        return success(assessmentReplenish);

    }
    @PostMapping("/createStrategyChild")
    public CommonResult<String> createStrategyChild(@RequestBody Map<String,String> map){
        map.put("assessment_type", AssessmentConstans.StrategyAssessment);
        String assessmentReplenish = assessmentService.createAssessmentReplenish(map);
        return success(assessmentReplenish);

    }


}


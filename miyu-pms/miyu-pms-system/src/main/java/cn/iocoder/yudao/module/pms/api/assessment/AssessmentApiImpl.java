package cn.iocoder.yudao.module.pms.api.assessment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import cn.iocoder.yudao.module.pms.service.assessment.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
public class AssessmentApiImpl implements AssessmentApi{
    @Resource
    private AssessmentService assessmentService;

    @Override
    public CommonResult<String> updateProcessStatus(String businessKey, Integer status) {
        assessmentService.updateAssessmentStatus(businessKey,status);
        return success("ok");
    }
}

package cn.iocoder.yudao.module.pms.bpmlistener;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.enums.AssessmentConstans;
import cn.iocoder.yudao.module.pms.service.assessment.AssessmentService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("pms/approval/listen")
public class ApprovalELController {

    @Resource
    private PmsApprovalService service;

    @PostMapping("/appoint")
    public CommonResult<String> appoint(@RequestBody Map<String,String> map){
        String apponit = service.apponit(map);
        return success(apponit);
    }
}


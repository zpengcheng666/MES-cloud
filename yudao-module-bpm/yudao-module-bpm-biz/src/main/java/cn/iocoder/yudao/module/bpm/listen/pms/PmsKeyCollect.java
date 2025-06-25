package cn.iocoder.yudao.module.bpm.listen.pms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface PmsKeyCollect {
    String APPROVAL_KEY = "pms_approval";//立项key
    String ASSESSMENT_KEY = "pms_assessment";//评审key
    String PLAN_KEY = "pms_plan";//评审key

}

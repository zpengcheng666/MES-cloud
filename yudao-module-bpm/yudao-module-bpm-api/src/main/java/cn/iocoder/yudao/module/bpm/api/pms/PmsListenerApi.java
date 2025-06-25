package cn.iocoder.yudao.module.bpm.api.pms;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("pms-server")
//@FeignClient("pms-server")
@Component
public interface PmsListenerApi {
    /**
     * 监听立项结束后的审批状态
     * @param id
     * @param status
     */
    @PostMapping("/pms/updateStatus/approval")
    void updateStatus(@RequestParam("id") String id, @RequestParam("status") Integer status);

    /**
     * 监听评审结束后的审批状态
     * @param id
     * @param status
     */
    @PostMapping("/pms/updateStatus/assessment")
    void updateAssessmentStatus(@RequestParam("id") String id, @RequestParam("status") Integer status);

    /**
     * 监听评审结束后的审批状态
     * @param id
     * @param status
     */
    @PostMapping("/pms/updateStatus/plan")
    void updatePlanStatus(@RequestParam("id") String id, @RequestParam("status") Integer status);

//    @PostMapping("/xia/test/feignTest")
//    void updateStatus(@RequestParam("id")String id, @RequestParam("status")Integer status);

    /**
     * 补充成本评估
     * @param map
     * @return
     */
    @PostMapping("/pms/assessment/listen/createCostChild")
    public CommonResult<String> createCostChild(@RequestBody Map<String,String> map);

    /**
     * 补充技术评估
     * @param map
     * @return
     */
    @PostMapping("/pms/assessment/listen/createTechnologyChild")
    public CommonResult<String> createTechnologyChild(@RequestBody Map<String,String> map);

    /**
     * 补充效能评估
     * @param map
     * @return
     */
    @PostMapping("/pms/assessment/listen/createCapacityChild")
    public CommonResult<String> createCapacityChild(@RequestBody Map<String,String> map);

    /**
     * 补充战略评估
     * @param map
     * @return
     */
    @PostMapping("/pms/assessment/listen/createStrategyChild")
    public CommonResult<String> createStrategyChild(@RequestBody Map<String,String> map);

    /**
     * 立项负责人任命
     * @param map
     * @return
     */
    @PostMapping("/pms/approval/listen/appoint")
    public CommonResult<String> appoint(@RequestBody Map<String,String> map);
}


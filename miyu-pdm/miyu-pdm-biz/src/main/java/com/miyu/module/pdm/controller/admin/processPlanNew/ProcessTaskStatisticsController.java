package com.miyu.module.pdm.controller.admin.processPlanNew;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.processPlanNew.vo.ProcessTaskStatisticsReqVO;
import com.miyu.module.pdm.controller.admin.processPlanNew.vo.ProcessTaskStatisticsRespVO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.service.processPlanNew.ProcessTaskStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM -工艺任务统计 ")
@RestController
@RequestMapping("pdm/process-plan-new")
@Validated
public class ProcessTaskStatisticsController {

    @Resource
    private ProcessTaskStatisticsService processTaskStatisticsService;

    @GetMapping("/list")
    @Operation(summary = "获得加工路线分页")
    public CommonResult<List<ProcessTaskStatisticsRespVO>> getProcessTaskStatistics(@Valid  ProcessTaskStatisticsReqVO ReqVO) {
        List<ProcessTaskDO> list = processTaskStatisticsService.getProcessTaskStatistics(ReqVO);
        //System.out.println(ReqVO);
        return success(BeanUtils.toBean(list, ProcessTaskStatisticsRespVO.class));
    }

}

package com.miyu.module.qms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.api.inspectionsheetcreatetask.InspectionSheetTaskApi;
import com.miyu.module.qms.api.inspectionsheetcreatetask.dto.TaskDTO;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo.InspectionSheetGenerateTaskSaveReqVO;
import com.miyu.module.qms.service.inspectionsheetgeneratetask.InspectionSheetGenerateTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class InspectionSheetGenerateTaskApiImpl implements InspectionSheetTaskApi {

    @Resource
    private InspectionSheetGenerateTaskService inspectionSheetGenerateTaskService;

    @Override
    public CommonResult<String> createInspectionSheetTask(TaskDTO reqDTO) {
        return CommonResult.success(inspectionSheetGenerateTaskService.createInspectionSheetGenerateTask(BeanUtils.toBean(reqDTO, InspectionSheetGenerateTaskSaveReqVO.class)));
    }
}

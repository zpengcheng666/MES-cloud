package com.miyu.module.wms.controller.admin.instruction;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionPageReqVO;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionRespVO;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionSaveReqVO;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.instruction.InstructionService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@Tag(name = "管理后台 - 指令")
@RestController
@RequestMapping("/wms/instruction")
@Validated
public class InstructionController {

    @Resource
    private InstructionService instructionService;
    @Resource
    private MaterialStockService materialStockService;

    @PostMapping("/create")
    @Operation(summary = "创建指令")
    @PreAuthorize("@ss.hasPermission('wms:instruction:create')")
    public CommonResult<String> createInstruction(@Valid @RequestBody InstructionSaveReqVO createReqVO) {
        return success(instructionService.createInstruction(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新指令")
    @PreAuthorize("@ss.hasPermission('wms:instruction:update')")
    public CommonResult<Boolean> updateInstruction(@Valid @RequestBody InstructionSaveReqVO updateReqVO) {
        instructionService.updateInstruction(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除指令")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:instruction:delete')")
    public CommonResult<Boolean> deleteInstruction(@RequestParam("id") String id) {
        instructionService.deleteInstruction(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得指令")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:instruction:query')")
    public CommonResult<InstructionRespVO> getInstruction(@RequestParam("id") String id) {
        InstructionDO instruction = instructionService.getInstruction(id);
        return success(BeanUtils.toBean(instruction, InstructionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得指令分页")
    @PreAuthorize("@ss.hasPermission('wms:instruction:query')")
    public CommonResult<PageResult<InstructionRespVO>> getInstructionPage(@Valid InstructionPageReqVO pageReqVO) {
        PageResult<InstructionDO> pageResult = instructionService.getInstructionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InstructionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出指令 Excel")
    @PreAuthorize("@ss.hasPermission('wms:instruction:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInstructionExcel(@Valid InstructionPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InstructionDO> list = instructionService.getInstructionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "指令.xls", "数据", InstructionRespVO.class, BeanUtils.toBean(list, InstructionRespVO.class));
    }

    /*                                               新增内容                                                          */
    /******************************************************************************************************************/

    @GetMapping("/handle-complete")
    @Operation(summary = "指令完成测试接口")
    public CommonResult<Boolean> handleComplete(String id) {
        InstructionDO instruction = instructionService.getInstruction(id);
        if(DictConstants.WMS_INSTRUCTION_STATUS_NOT_START == instruction.getInsStatus()
                || DictConstants.WMS_INSTRUCTION_STATUS_RUNNING == instruction.getInsStatus()) {
            if(DictConstants.WMS_INSTRUCTION_TYPE_ON == instruction.getInsType()){
                instructionService.stockonshelfComplete(instruction.getInsCode());
            }else if(DictConstants.WMS_INSTRUCTION_TYPE_OFF == instruction.getInsType()) {
                instructionService.stockoffshelfComplete(instruction.getInsCode());
            }
        }
        return success(true);
    }


    /**
     * 任务执行反馈
     */
    @PostMapping("/taskExecutionFeedback")
    @Operation(summary = "任务执行反馈")
    public CommonResult<Boolean> taskExecutionFeedback(@RequestBody JSONObject jsonObject) {
        String taskNo = jsonObject.getString("TaskNo");
        String taskStatus = jsonObject.getString("TaskStatus");
        String time = jsonObject.getString("Time");
        String Message = jsonObject.getString("Message");
        LocalDateTime parse = null;
        try {
            parse = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }catch (Exception e){
            throw exception(DATE_FORMAT_ERROR);
        }
        System.out.println("任务执行反馈：" + taskNo + "，状态：" + taskStatus + "，时间：" + parse + "，消息：" + Message);

        instructionService.updateInsStatusByInsCodeAndInsStatus(taskNo, taskStatus, parse, Message);
        return success(true);
    }

    @GetMapping("/onShelfInstruction")
    @Operation(summary = "上架指令")
    @PreAuthorize("@ss.hasPermission('wms:instruction:create')")
    public CommonResult<InstructionDO> onShelfInstruction(String materialStockId, String targetLocationId){
        InstructionDO instructionDO = instructionService.onShelfInstruction(materialStockId, targetLocationId);
        return success(instructionDO);
    }


    @GetMapping("/offShelfInstruction")
    @Operation(summary = "下架指令")
    @PreAuthorize("@ss.hasPermission('wms:instruction:create')")
    public CommonResult<InstructionDO> offShelfInstruction(String materialStockId, String targetLocationId){
        InstructionDO instructionDO = instructionService.offShelfInstruction(materialStockId, targetLocationId);
        return success(instructionDO);
    }
    
    
}
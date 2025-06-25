package cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 简易版,项目计划提醒用 Response VO")
@Data
@ExcelIgnoreUnannotated
public class NotifyMessageRespVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22320")
    @ExcelProperty("用户ID")
    private Long id;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20685")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "模版发送人名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("模版发送人名称")
    private String templateNickname;

    @Schema(description = "模版内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("模版内容")
    private String templateContent;

    @Schema(description = "通知类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("通知类型")
    private Integer type;

    @Schema(description = "是否已读", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("是否已读")
    private Boolean readStatus;

    @Schema(description = "阅读时间")
    @ExcelProperty("阅读时间")
    private LocalDateTime readTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

package cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 简易版,项目计划提醒用新增/修改 Request VO")
@Data
public class NotifyMessageSaveReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22320")
    private Long id;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20685")
//    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "模版发送人名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
//    @NotEmpty(message = "模版发送人名称不能为空")
    private String templateNickname;

    @Schema(description = "模版内容", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotEmpty(message = "模版内容不能为空")
    private String templateContent;

    @Schema(description = "通知类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
//    @NotNull(message = "通知类型不能为空")
    private Integer type;

    @Schema(description = "是否已读", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "是否已读不能为空")
    private Boolean readStatus;

    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

}

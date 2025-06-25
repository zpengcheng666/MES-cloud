package cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 简易版,项目计划提醒用新增/修改 Request VO")
@Data
public class NotifyMessageReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22320")
    private Long id;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20685")
    private Long userId;

    @Schema(description = "模版发送人名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String templateNickname;

    @Schema(description = "模版内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String templateContent;

    @Schema(description = "通知类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "是否已读", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Boolean readStatus;

    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

}

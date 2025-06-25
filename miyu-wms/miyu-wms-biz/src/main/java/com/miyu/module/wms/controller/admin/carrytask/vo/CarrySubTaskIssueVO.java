package com.miyu.module.wms.controller.admin.carrytask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 搬运任务下发子任务 VO")
@Data
public class CarrySubTaskIssueVO {
    /**
     * 任务点位顺序
     */
    private Integer Sort;
    /**
     * 任务点位
     */
    private String Site;
    /**
     * JackLoad 举升 JackUnLoad下降
     * Finish任务结束 其它值则保持当前车
     * 状态移动到当前点
     */
    private String TrigEvent;
    /**
     * Yes 需要触发 No 不需要触发
     */
    private String Operation;
}

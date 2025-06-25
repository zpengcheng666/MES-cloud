package com.miyu.module.wms.controller.admin.carrytask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 搬运任务接收状态 VO")
@Data
public class CarryTaskReceiveVO {
    /**
     * 任务编号
     */
    private String TaskNo;
    /**
     * 任务点位顺序
     */
    private String Sort;
    /**
     * 点位
     */
    private String Site;
    /**
     * 状态
     * Arrived 已到达 Finish 已完成
     */
    private String Status;
    /**
     * 时间
     */
    private String StatusTime;

    /**
     * AGV编号
     */
    private String CarNo;

}

package com.miyu.module.wms.controller.admin.carrytask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 搬运任务设备状态 VO")
@Data
public class CarryTaskStatusVO {
    /**
     * 机器人运行模式，手动模式=0，自动模式=1
     */
    private Integer mode;
    /**
     * 机器人的 x 坐标, 单位 m
     */
    private String x;
    /**
     * 机器人的 y坐标, 单位 m
     */
    private String y;
    /**
     * 机器人当前所在站点的 id（该判断比
     * 较严格，机器人必须很靠近某一个站
     * 点(<30cm， 这个距离可以通过参数
     * 配置中的 CurrentPointDist 修改)，否
     * 则为空字符，即不处于任何站点
     */
    private String current_station;
    /**
     * 机器人上一个所在站点的 id
     */
    private String last_station;
    /**
     * 机器人底盘是否静止（行走电机）
     */
    private boolean is_stop;
    /**
     * 机器人是否被阻挡
     */
    private boolean blocked;
    /**
     * true 表示急停按钮处于激活状态(按
     * 下), false 表示急停按钮处于非激活状
     * 态(拔起)
     */
    private boolean emergency;
    /**
     * 0 = NONE, 1 = WAITING(目前不可能
     * 出现该状态), 2 = RUNNING, 3 =
     * SUSPENDED, 4 = COMPLETED, 5 =
     * FAILED, 6 = CANCELED
     */
    private String task_status;
}

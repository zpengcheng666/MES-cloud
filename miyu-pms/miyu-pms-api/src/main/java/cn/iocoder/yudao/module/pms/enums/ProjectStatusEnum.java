package cn.iocoder.yudao.module.pms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatusEnum {
    NotStart(0L, "未开始"),
    Start(1L, "待审核"),
    StayAssessment(2L,"待评审"),
    Prepare(3L, "准备中"),
    Process(4L, "生产中(加工中)"),
    DeliveryCompleted(5L, "出库完成"),
    Finish(6L, "已结束"),
    Terminate(7L, "异常终止");

    /**
     * 状态
     */
    private final Long status;
    /**
     * 名字
     */
    private final String name;
}

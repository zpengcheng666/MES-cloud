package com.miyu.module.ppm.enums.purchaseconsignment;

import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PpmAuditStatusEnum {
    DRAFT(0,"未提交"),
    PROCESS(1, "审批中"),
    APPROVE(2, "审批通过"),
    REJECT(3, "审批不通过"),
    CANCEL(4, "已取消"),


    /**
     * 使用场景：
     * 1. 任务被向后【加签】时，它在审批通过后，会变成 APPROVING 这个状态，然后等到【加签】出来的任务都被审批后，才会变成 APPROVE 审批通过
     */
    APPROVING(7, "审批通过中"),
    /**
     * 使用场景：
     * 1. 任务被向前【加签】时，它会变成 WAIT 状态，需要等待【加签】出来的任务被审批后，它才能继续变为 RUNNING 继续审批
     * 2. 任务被向后【加签】时，【加签】出来的任务处于 WAIT 状态，它们需要等待该任务被审批后，它们才能继续变为 RUNNING 继续审批
     */
    WAIT(0, "待审批");

    /**
     * 状态
     * <p>
     * 如果新增时，注意 {@link #isEndStatus(Integer)} 是否需要变更
     */
    private final Integer status;
    /**
     * 名字
     */
    private final String name;

    /**
     * 判断该状态是否已经处于 End 最终状态
     * <p>
     * 主要用于一些状态更新的逻辑，如果已经是最终状态，就不再进行更新
     *
     * @param status 状态
     * @return 是否
     */
    public static boolean isEndStatus(Integer status) {
        return ObjectUtils.equalsAny(status,
                APPROVE.getStatus(), REJECT.getStatus(), CANCEL.getStatus(), APPROVING.getStatus());
    }
}

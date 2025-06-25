package com.miyu.module.es.dal.dataobject.brakeSync;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("es_brake_sync")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrakeSyncDO extends BaseDO {

    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *自动同步(1.开启 2.关闭)
     */
    private Integer automatic;

    /**
     *周期
     */
    private String cycle;

    /**
     * 厂区同步方式(1.不互通 2.仅新厂同步旧厂 3.仅旧厂同步新厂 4.互通）
     */
    private Integer sync;
}

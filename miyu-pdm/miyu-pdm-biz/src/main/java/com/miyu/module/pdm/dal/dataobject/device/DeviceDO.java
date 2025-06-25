package com.miyu.module.pdm.dal.dataobject.device;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * PDM 设备-临时 DO
 *
 * @author liuy
 */
@TableName("dms_device_type")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String code;

    private String name;

    private Integer type;

    private Integer enable;

    private String specification;

    private String manufacturer;

    private String countryRegion;

    private String contacts;

    private String contactPhone;

    private String remark;
}

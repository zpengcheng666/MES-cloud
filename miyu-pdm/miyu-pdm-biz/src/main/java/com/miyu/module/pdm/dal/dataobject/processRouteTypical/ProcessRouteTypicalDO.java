package com.miyu.module.pdm.dal.dataobject.processRouteTypical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

//典型工艺路线DO
@TableName(value="capp_process_route_typical",autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRouteTypicalDO extends BaseDO {
    //典型工艺路线ID
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //典型工艺路线名
    private String name;
    //工序名称
    private String procedureName;
    //工艺路线
    private String processRouteName;
    //典型工艺路线描述
    private String description;

}

package com.miyu.module.pdm.dal.dataobject.processRoute;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

//加工路线DO
@TableName(value="capp_process_route",autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRouteDO extends BaseDO {
    //加工路线ID
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    //加工路线名
    private String name;
    //加工路线描述
    private String description;
    //加工路线
//    @TableField(exist = false)
//    List processRouteDetailDOList;

}

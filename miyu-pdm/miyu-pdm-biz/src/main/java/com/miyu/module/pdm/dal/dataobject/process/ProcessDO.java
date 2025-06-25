package com.miyu.module.pdm.dal.dataobject.process;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@TableName("capp_process")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDO extends BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String partVersionId;

    private String processCode;

    private String processSchemeCode;

    private String materialId;

    private String materialNumber;

    private String materialDesg;

    private String materialCode;

    private String materialName;

    private String materialCondition;

    private String materialSpec;

    private String materialSpecification;

    private String singleSize;

    private String groupSize;

    private String processRouteName;

    private Integer singleQuatity;

    private Integer isValid;

    private String processCondition;

//    @TableField(exist = false)
//    private String processId;
}

package com.miyu.module.qms.dal.dataobject.unqualifiedmaterial;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.util.List;

/**
 * 不合格品产品 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_unqualified_material")
@KeySequence("qms_unqualified_material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnqualifiedMaterialDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 检验单方案任务ID
     */
    private String inspectionSheetSchemeId;
    /**
     * 检验单产品ID
     */
    private String schemeMaterialId;

    /**
     * 缺陷等级
     */
    private Integer defectiveLevel;

    /**
     * 不合格品处理方式 字段放到不合格产品表中
     */
     private Integer handleMethod;

    /**
     * 缺陷描述
     */
    private String content;

    /**
     * 不合格品ID
     */
    @TableField(exist = false)
    private List<String> defectiveCode;

    @TableField(exist = false)
    private String defectiveCodesStr;


    /**
     * 物料库存ID
     */
    @TableField(exist = false)
    private String materialId;

    /**
     * 物料条码
     */
    @TableField(exist = false)
    private String barCode;
}

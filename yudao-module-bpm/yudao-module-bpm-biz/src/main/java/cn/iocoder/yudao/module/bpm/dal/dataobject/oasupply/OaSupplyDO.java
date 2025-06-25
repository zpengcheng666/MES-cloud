package cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * OA 物品领用 DO
 *
 * @author 芋道源码
 */
@TableName("bpm_oa_supply")
@KeySequence("bpm_oa_supply_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaSupplyDO extends BaseDO {

    /**
     * 请假表单主键
     */
    @TableId
    private Long id;
    /**
     * 申请人部门
     */
    private String dept;
    /**
     * 申请人
     */
    private String applicant;
    /**
     * 申请部门领导
     */
    private String leader;
    /**
     * 仓库管理员
     */
    private String warehouseman;
    /**
     * 申请原因
     */
    private String reason;
    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}

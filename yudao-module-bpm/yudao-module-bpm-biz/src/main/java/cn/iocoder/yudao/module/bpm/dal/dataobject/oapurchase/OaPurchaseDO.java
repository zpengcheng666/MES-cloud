package cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * OA 采购申请 DO
 *
 * @author 芋道源码
 */
@TableName("bpm_oa_purchase")
@KeySequence("bpm_oa_purchase_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaPurchaseDO extends BaseDO {

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
     * 采购人
     */
    private String purchaseAgent;
    /**
     * 供应商
     */
    private String supplier;

    /**
     * 供应商编码
     */
    private String supplyCode;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}

package cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * OA 采购申请 DO
 *
 * @author 芋道源码
 */
@TableName("bpm_oa_purchase_list")
@KeySequence("bpm_oa_purchase_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaPurchaseListDO extends BaseDO {

    /**
     * 请假表单主键
     */
    @TableId
    private Long id;
    /**
     * 品名规格
     */
    private String nameSpe;
    /**
     * 用途
     */
    private String intention;
    /**
     * 数量
     */
    private Long amount;
    /**
     * 需求时间
     */
    private LocalDateTime requireTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 采购父表id
     */
    private Long purchaseId;

}

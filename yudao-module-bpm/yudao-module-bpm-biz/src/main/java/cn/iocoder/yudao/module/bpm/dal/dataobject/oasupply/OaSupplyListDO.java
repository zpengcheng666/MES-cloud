package cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * OA 物品领用表-物品清单 DO
 *
 * @author 芋道源码
 */
@TableName("bpm_oa_supply_list")
@KeySequence("bpm_oa_supply_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaSupplyListDO extends BaseDO {

    /**
     * 请假表单主键
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 规格型号
     */
    private String specification;
    /**
     * 数量
     */
    private Long amount;
    /**
     * 单位
     */
    private String unit;
    /**
     * 库存
     */
    private Long inventory;
    /**
     * 备注
     */
    private String remark;
    /**
     * 采购父表id
     */
    private Long supplyId;

}

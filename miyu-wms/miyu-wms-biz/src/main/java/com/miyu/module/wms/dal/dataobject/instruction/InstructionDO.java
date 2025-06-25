package com.miyu.module.wms.dal.dataobject.instruction;

import com.miyu.module.wms.enums.DictConstants;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;


/**
 * 指令 DO
 *
 * @author 王正浩
 */
@TableName("wms_instruction")
@KeySequence("wms_instruction_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructionDO extends BaseDO {

    // 生成指令流水号 前缀
    private final static String INSTRUCTION_PREFIX = "INS-";

    /**
     * 构造器
     *
     * @param insCode 指令编码
     * @param carryStockId 被搬运的托盘id
     * @param insType 指令类型
     * @param startLocationId 起始库位id
     * @param targetLocationId 目标库位id
     * @param startLocationCode 起始库位编码
     * @param targetLocationCode 目标库位编码
     * @param insDescription 指令描述
     */
    public InstructionDO(String insCode, String carryStockId, Integer insType, String startLocationId, String targetLocationId, String startLocationCode, String targetLocationCode, String insDescription){
        if(StringUtils.isAnyBlank(insCode, carryStockId, startLocationId, startLocationCode)){
            throw new IllegalArgumentException("参数不能为空");
        }
        this.insCode = INSTRUCTION_PREFIX + insCode;
        this.materialStockId = carryStockId;
        this.insStatus = DictConstants.WMS_INSTRUCTION_STATUS_NOT_START;
        this.startLocationId = startLocationId;
        this.targetLocationId = targetLocationId;
        this.insType= insType;
        String insContent = "";
        if(DictConstants.WMS_INSTRUCTION_TYPE_ON == insType){
            insContent = "上架指令，上架起始库位：" + startLocationCode + "，至目标库位：" + targetLocationCode;
        }else if(DictConstants.WMS_INSTRUCTION_TYPE_OFF == insType){
            insContent = "下架指令，下架起始库位：" + startLocationCode + "，至目标库位：" + targetLocationCode;
        }
        this.insContent = insContent;
        this.insDescription = insDescription;
    }

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 指令编码
     */
    private String insCode;
    /**
     * 物料库存id
     */
    private String materialStockId;
    /**
     * 指令类型（上架指令、下架指令）
     *
     * 枚举 {@link TODO wms_instruction_type 对应的类}
     */
    private Integer insType;
    /**
     * 指令状态（未开始、进行中、已完成、已取消）
     *
     * 枚举 {@link TODO wms_instruction_status 对应的类}
     */
    private Integer insStatus;
    /**
     * 起始库位id
     */
    private String startLocationId;
    /**
     * 目标库位id
     */
    private String targetLocationId;
    /**
     * 指令内容
     */
    private String insContent;
    /**
     * 指令描述
     */
    private String insDescription;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
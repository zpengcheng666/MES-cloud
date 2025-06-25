package com.miyu.module.tms.enums;

/**
 * tms 字典类型的枚举类
 *
 * @author Qianjy
 */
public interface DictConstants {

    // 刀具装配记录类型 // 装、卸、当前装、作废
    Integer ASSEMBLE_RECORD_TYPE_ASSEMBLE = 1;//装
    Integer ASSEMBLE_RECORD_TYPE_SELL = 2;//卸
    Integer ASSEMBLE_RECORD_TYPE_CURRENT_ASSEMBLE = 3;//当前装
    Integer ASSEMBLE_RECORD_TYPE_INVALID = 4;//作废


    //成品刀具状态(1装配中、2完成、3使用中、4拆卸中、5无效)
    Integer PARTS_STATUS_ASSEMBLING = 1;//装配中
    Integer PARTS_STATUS_COMPLETED = 2;//完成
    Integer PARTS_STATUS_USING = 3;//使用中
    Integer PARTS_STATUS_DISASSEMBLING = 4;//拆卸中
    Integer PARTS_STATUS_INVALID = 5;//无效

}

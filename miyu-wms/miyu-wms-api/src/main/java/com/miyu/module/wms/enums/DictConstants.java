package com.miyu.module.wms.enums;

/**
 * wms 字典类型的枚举类
 *
 * @author Qianjy
 */
public interface DictConstants {

    /**
     *订单类型 采购入库 外协入库 生产入库 退料入库 检验入库 其他入库
     * 销售出库 外协出库 生产出库 检验出库 报损出库 采购退货出库 调拨出库 其他出库
     * 库存移动
     */
    Integer WMS_ORDER_TYPE_PURCHASE_IN = 1; //订单类型 采购入库
    Integer WMS_ORDER_TYPE_OUTSOURCE_IN = 2; //订单类型 外协入库
    Integer WMS_ORDER_TYPE_PRODUCE_IN = 3; //订单类型 生产入库
    Integer WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN = 4; //订单类型 采购退货入库
    Integer WMS_ORDER_TYPE_CHECK_IN = 5; //订单类型 检验入库
    Integer WMS_ORDER_TYPE_OTHER_IN = 6; //订单类型 其他入库
    Integer WMS_ORDER_TYPE_SALE_OUT = 7; //订单类型 销售出库
    Integer WMS_ORDER_TYPE_OUTSOURCE_OUT = 8; //订单类型 外协出库
    Integer WMS_ORDER_TYPE_PRODUCE_OUT = 9; //订单类型 生产出库
    Integer WMS_ORDER_TYPE_CHECK_OUT = 10; //订单类型 检验出库
    Integer WMS_ORDER_TYPE_DAMAGE_OUT = 11; //订单类型 报损出库
    Integer WMS_ORDER_TYPE_PURCHASE_RETURN_OUT = 12; //订单类型 采购退货出库
    Integer WMS_ORDER_TYPE_TRANSFER_OUT = 13; //订单类型 调拨出库
    Integer WMS_ORDER_TYPE_OTHER_OUT = 14; //订单类型 其他出库
    Integer WMS_ORDER_TYPE_PRODUCE_MOVE = 15; //订单类型 生产移库
    Integer WMS_ORDER_TYPE_CHECK_MOVE = 16; //订单类型 检验移库
    Integer WMS_ORDER_TYPE_TRANSFER_MOVE = 17; //订单类型 调拨移库
    Integer WMS_ORDER_TYPE_MATERIAL_IN = 18; //订单类型 原材料入库 ---用于带货加工的订单
    Integer WMS_ORDER_TYPE_PROFIT_IN = 19; //订单类型 盘盈入库
    Integer WMS_ORDER_TYPE_LOSS_OUT = 20; //订单类型 盘亏出库


    Integer INFRA_BOOLEAN_TINYINT_NO = 0; // 否
    Integer INFRA_BOOLEAN_TINYINT_YES = 1; // 是

    Integer WMS_BIND_TYPE_UNBIND = 1; // 绑定类型 未绑定
    Integer WMS_BIND_TYPE_LOCATION = 2; // 绑定类型 库位
    Integer WMS_BIND_TYPE_STORAGE = 3; //  绑定类型 储位

    String WMS_BIND_TYPE_LOCATION_NAME = "库位"; // 绑定类型 库位
    String WMS_BIND_TYPE_STORAGE_NAME = "储位"; //  绑定类型 储位

    Integer WMS_WAREHOUSE_TYPE_1 = 1; // 仓库类型 自动化立体库
    Integer WMS_WAREHOUSE_TYPE_2 = 2; // 仓库类型 人工立体库
    Integer WMS_WAREHOUSE_TYPE_3 = 3; // 仓库类型 半自动线体库
    Integer WMS_WAREHOUSE_TYPE_4 = 4; // 仓库类型 AGV虚拟库
    Integer WMS_WAREHOUSE_TYPE_5 = 5; // 仓库类型 自动化线体库

    Integer WMS_WAREHOUSE_AREA_TYPE_1 = 1; // 库区类型 存储区
    Integer WMS_WAREHOUSE_AREA_TYPE_2 = 2; // 库区类型 暂存区
    Integer WMS_WAREHOUSE_AREA_TYPE_3 = 3; // 库区类型 物料接驳区
    Integer WMS_WAREHOUSE_AREA_TYPE_4 = 4; // 库区类型 拣选区
    Integer WMS_WAREHOUSE_AREA_TYPE_5 = 5; // 库区类型 收货区
    Integer WMS_WAREHOUSE_AREA_TYPE_6 = 6; // 库区类型 虚拟暂存区
    Integer WMS_WAREHOUSE_AREA_TYPE_7 = 7; // 库区类型 打包区
    Integer WMS_WAREHOUSE_AREA_TYPE_8 = 8; // 库区类型 质检区
    Integer WMS_WAREHOUSE_AREA_TYPE_9 = 9; // 库区类型 机加功能区  手动上下架使用
    Integer WMS_WAREHOUSE_AREA_TYPE_10 = 10; // 库区类型 加工缓存库区
    Integer WMS_WAREHOUSE_AREA_TYPE_11 = 11; // 库区类型 刀具接驳区

    Integer WMS_WAREHOUSE_AREA_PROPERTY_NOT_AUTO = 1; // 库区属性 人工库区
    Integer WMS_WAREHOUSE_AREA_PROPERTY_AUTO = 2; // 库区属性 自动库区
    Integer WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO = 3; // 库区属性 半自动库区  需要人工将物料放入接驳位
    Integer WMS_WAREHOUSE_AREA_PROPERTY_AGV = 4; // 库区属性 AGV虚拟暂存库区


    Integer WMS_IN_WAREHOUSE_TYPE_1 = 1; // 入库类型 自建入库
    Integer WMS_IN_WAREHOUSE_TYPE_2 = 2; // 入库类型 采购入库
    Integer WMS_IN_WAREHOUSE_TYPE_3 = 3; // 入库类型 外协入库
    Integer WMS_IN_WAREHOUSE_TYPE_4 = 4; // 入库类型 生产入库
    Integer WMS_IN_WAREHOUSE_TYPE_5 = 5; // 入库类型 采购退货入库
    Integer WMS_IN_WAREHOUSE_TYPE_6 = 6; // 入库类型 检验入库
    Integer WMS_IN_WAREHOUSE_TYPE_7 = 7; // 入库类型 其他入库
    Integer WMS_IN_WAREHOUSE_TYPE_8 = 8; // 入库类型 原材料入库 ---用于带货加工的订单
    Integer WMS_IN_WAREHOUSE_TYPE_9 = 9; // 入库类型 盘盈入库

//    Integer WMS_IN_WAREHOUSE_DETAIL_STATE_1 = 1; // 入库详情状态 待入库
//    Integer WMS_IN_WAREHOUSE_DETAIL_STATE_2 = 2; // 入库详情状态 待上架
//    Integer WMS_IN_WAREHOUSE_DETAIL_STATE_3 = 3; // 入库详情状态 已完成



    Integer WMS_OUT_WAREHOUSE_TYPE_1 = 1; // 出库类型 自建出库
    Integer WMS_OUT_WAREHOUSE_TYPE_2 = 2; // 出库类型 销售出库
    Integer WMS_OUT_WAREHOUSE_TYPE_3 = 3; // 出库类型 外协出库
    Integer WMS_OUT_WAREHOUSE_TYPE_4 = 4; // 出库类型 生产出库
    Integer WMS_OUT_WAREHOUSE_TYPE_5 = 5; // 出库类型 检验出库
    Integer WMS_OUT_WAREHOUSE_TYPE_6 = 6; // 出库类型 报损出库
    Integer WMS_OUT_WAREHOUSE_TYPE_7 = 7; // 出库类型 采购退货出库
    Integer WMS_OUT_WAREHOUSE_TYPE_8 = 8; // 出库类型 调拨出库
    Integer WMS_OUT_WAREHOUSE_TYPE_9 = 9; // 出库类型 其他出库
    Integer WMS_OUT_WAREHOUSE_TYPE_10 = 10; // 出库类型 盘亏出库


    Integer WMS_MOVE_WAREHOUSE_TYPE_1 = 1; // 移库类型 生产移库
    Integer WMS_MOVE_WAREHOUSE_TYPE_2 = 2; // 移库类型 检验移库
    Integer WMS_MOVE_WAREHOUSE_TYPE_3 = 3; // 移库类型 调拨移库


//    Integer WMS_INSTRUCTION_STATUS_NOT_ACTIVATED = 0; // 指令状态 待激活
    Integer WMS_INSTRUCTION_STATUS_NOT_START = 1; // 指令状态 未开始 （此状态的指令需要被定时触发）
    Integer WMS_INSTRUCTION_STATUS_RUNNING = 2; // 指令状态 进行中
    Integer WMS_INSTRUCTION_STATUS_FINISHED = 3; // 指令状态 已完成
    Integer WMS_INSTRUCTION_STATUS_CANCEL = 4; // 指令状态 已取消
    Integer WMS_INSTRUCTION_STATUS_EXCEPTION = 5; // 指令状态 异常

    String WMS_INSTRUCTION_STATUS_START = "Start";
    String WMS_INSTRUCTION_STATUS_END = "End";
    String WMS_INSTRUCTION_STATUS_ALARM = "Alarm";
    String WMS_INSTRUCTION_STATUS_CANCELl = "Cancel";

    Integer WMS_INSTRUCTION_TYPE_ON = 0; // 指令类型 上架指令
    Integer WMS_INSTRUCTION_TYPE_OFF = 1; // 指令类型 下架指令


    // 物料类型 零件 托盘 工装 夹具 刀具
    String WMS_MATERIAL_TYPE_TP = "TP"; //物料类型 托盘
    String WMS_MATERIAL_TYPE_GJ = "GJ"; //物料类型 零件
    String WMS_MATERIAL_TYPE_GZ = "GZ"; //物料类型 工装
    String WMS_MATERIAL_TYPE_JJ = "JJ"; //物料类型 夹具
    String WMS_MATERIAL_TYPE_DJ = "DJ"; //物料类型 刀具



    Integer WMS_CARRY_TASK_TYPE_IN = 1; // 任务类型 入库搬运
    Integer WMS_CARRY_TASK_TYPE_OUT = 2; // 任务类型 出库搬运
    Integer WMS_CARRY_TASK_TYPE_ADJUST = 3; // 任务类型 库存移交
    Integer WMS_CARRY_TASK_TYPE_TRAY = 4; // 任务类型 呼叫托盘


    Integer WMS_CARRY_TASK_STATUS_HOLD = 0; // 搬运任务状态 挂起
    Integer WMS_CARRY_TASK_STATUS_NOT_START = 1; // 搬运任务状态 未开始
    Integer WMS_CARRY_TASK_STATUS_ISSUED = 2; // 搬运任务状态 已下发
    Integer WMS_CARRY_TASK_STATUS_RUNNING = 3; // 搬运任务状态 进行中
    Integer WMS_CARRY_TASK_STATUS_FINISHED = 4; // 搬运任务状态 已完成
    Integer WMS_CARRY_TASK_STATUS_EXCEPTION = 5; // 搬运任务状态 异常
    Integer WMS_CARRY_TASK_STATUS_CANCEL = 6; // 搬运任务状态 已取消

    Integer WMS_CARRY_SUB_TASK_STATUS_HOLD = 0; // 搬运子任务状态 挂起
    Integer WMS_CARRY_SUB_TASK_STATUS_NOT_START = 1; // 搬运子任务状态 未开始
//    Integer WMS_CARRY_SUB_TASK_STATUS_ISSUED = 2; // 搬运子任务状态 已下发  废弃此状态
    Integer WMS_CARRY_SUB_TASK_STATUS_RUNNING = 2; // 搬运子任务状态 进行中
    Integer WMS_CARRY_TASK_CALL_BACK_STATUS_FINISHED = 3; // 搬运子任务状态 已完成
    Integer WMS_CARRY_SUB_TASK_STATUS_EXCEPTION = 4; // 搬运子任务状态 异常
    Integer WMS_CARRY_SUB_TASK_STATUS_CANCEL = 5; // 搬运子任务状态 已取消

//    String WMS_CARRY_TASK_CALL_BACK_STATUS_ARRIVED = "Arrived";// AGV 反馈状态 已到达
    String WMS_CARRY_TASK_CALL_BACK_STATUS_FINISH = "Finish";// AGV 反馈状态 已完成

    // 搬运任务指令类型  移动 取 放 上架 下架
    Integer WMS_CARRY_TASK_INSTRUCTION_TYPE_MOVE = 1; // 移动
    Integer WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE = 2; // 取
    Integer WMS_CARRY_TASK_INSTRUCTION_TYPE_PUT = 3; // 放
    Integer WMS_CARRY_TASK_INSTRUCTION_TYPE_ON = 4; // 上架
    Integer WMS_CARRY_TASK_INSTRUCTION_TYPE_OFF = 5; // 下架

    // 统一任务单状态(出库、入库、移库)   不能再改了 新增状态往后加
    Integer WMS_ORDER_DETAIL_STATUS_0 = 0; // 入库状态 待质检
    Integer WMS_ORDER_DETAIL_STATUS_1 = 1; // 入库状态 待入库 // 出库状态 待出库 // 移库状态状态 待出库
    Integer WMS_ORDER_DETAIL_STATUS_2 = 2; // 入库状态 待送达 // 出库状态 待送达 // 移库状态状态 待送达
    Integer WMS_ORDER_DETAIL_STATUS_3 = 3; // 入库状态 待上架 // 出库状态 待签收 // 移库状态状态 待签收
    Integer WMS_ORDER_DETAIL_STATUS_4 = 4; // 入库状态 已完成 // 出库状态 已完成 // 移库状态状态 已完成
    Integer WMS_ORDER_DETAIL_STATUS_5 = 5; // 入库状态 已关闭 // 出库状态 已关闭 // 移库状态状态 已关闭
    Integer WMS_ORDER_DETAIL_STATUS_6 = 6; // 入库状态 待审批 // 出库状态 待审批 // 移库状态状态 待审批
//    Integer WMS_ORDER_DETAIL_STATUS_7 = 7; // 入库状态 待签入 // 出库状态 无此状态 // 移库状态状态 带签入

    String WMS_CARRY_TASK_OPERATION_MOVE = "";// 移动
    String WMS_CARRY_TASK_OPERATION_TAKE = "JackLoad";//举升
    String WMS_CARRY_TASK_OPERATION_PUT = "JackUnLoad";//下降
    String WMS_CARRY_TASK_TRIGEVENT_YES = "Yes";//需要触发
    String WMS_CARRY_TASK_TRIGEVENT_NO = "No";//不需要触发

    Integer WMS_CHECK_STATUS_CHECK = 1;  // 盘点状态 待审核
    Integer WMS_CHECK_STATUS_NOT_START = 2;  // 盘点状态 未开始
    Integer WMS_CHECK_STATUS_RUNNING = 3;  // 盘点状态 进行中
    Integer WMS_CHECK_STATUS_FINISHED = 4;  // 盘点状态 已完成
    Integer WMS_CHECK_STATUS_CLOSED = 5;  // 盘点状态 已关闭

    // 盘点详情状态  待盘点 已盘点
    Integer WMS_CHECK_DETAIL_STATUS_WAITCHECK = 1; // 盘点详情状态 待盘点
    Integer WMS_CHECK_DETAIL_STATUS_CHECKED = 2; // 盘点详情状态 已盘点
    Integer WMS_CHECK_DETAIL_STATUS_SUBMITTED = 3; // 盘点详情状态 已提交

    Integer WMS_CHECK_TYPE_NORMAL = 1; // 盘点类型 默认
    Integer WMS_CHECK_TYPE_PROFIT = 2; // 盘点类型 盘盈新增


    // 物料状态 1待质检 2合格 3不合格
    Integer WMS_MATERIAL_STATUS_WAITCHECK = 1; // 物料状态 待质检
    Integer WMS_MATERIAL_STATUS_QUALIFIED = 2; // 物料状态 合格
    Integer WMS_MATERIAL_STATUS_UNQUALIFIED = 3; // 物料状态 不合格


    // 呼叫托盘搬运状态 1 未知，2 搬运中，3 已抵达
    Integer WMS_CARRY_TRAY_STATUS_UNKNOWN = 1; // 呼叫托盘搬运状态 未知
    Integer WMS_CARRY_TRAY_STATUS_MOVING = 2; // 呼叫托盘搬运状态 搬运中
    Integer WMS_CARRY_TRAY_STATUS_ARRIVED = 3; // 呼叫托盘搬运状态 已抵达

    // 报警级别 1 提示 2 警告 3 错误
    String WMS_ALARM_LEVEL_INFO = "1"; // 报警级别 1 提示
    String WMS_ALARM_LEVEL_WARNING = "2"; // 报警级别 2 警告
    String WMS_ALARM_LEVEL_ERROR = "3"; // 报警级别 3 错误
    // 报警类型 1 系统 2 AGV
    String WMS_ALARM_TYPE_SYSTEM = "1"; // 报警类型 1 系统
    String WMS_ALARM_TYPE_AGV = "2"; // 报警类型 2 AGV
    // 报警状态 1 未解决 2 已解决 3 已忽略
    String WMS_ALARM_STATUS_NOT_SOLVED = "1"; // 报警状态 1 未解决
    String WMS_ALARM_STATUS_SOLVED = "2"; // 报警状态 2 已解决
    String WMS_ALARM_STATUS_IGNORED = "3"; // 报警状态 3 已忽略

    // 库存维护记录 1报废 2装配 3拆卸 4分拣 5加工
    Integer WMS_INVENTORY_MAINTENANCE_TYPE_SCRAP = 1; // 库存维护记录 1报废
    Integer WMS_INVENTORY_MAINTENANCE_TYPE_ASSEMBLY = 2; // 库存维护记录 2装配
    Integer WMS_INVENTORY_MAINTENANCE_TYPE_DISASSEMBLY = 3; // 库存维护记录 3拆卸
    Integer WMS_INVENTORY_MAINTENANCE_TYPE_SORTING = 4; // 库存维护记录 4分拣
    Integer WMS_INVENTORY_MAINTENANCE_TYPE_PROCESSING = 5; // 库存维护记录 5加工
}

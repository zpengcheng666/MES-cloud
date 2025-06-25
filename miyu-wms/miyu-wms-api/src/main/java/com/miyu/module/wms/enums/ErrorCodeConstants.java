package com.miyu.module.wms.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 错误码枚举类
 * wms 系统，使用 2-001-000-000 段
 *
 * @author Qinyu
 * Created on 2021/1/25 11:10
 * 错误码枚举类
 * 1. 仓库表不存在
 * 2. 库位表不存在
 * 3. 货位表不存在
 * 4. 库存表不存在
 * 5. 出入库单表不存在
 * 6. 出入库单明细表不存在
 * 7. 库存盘点单表不存在
 * 8. 库存盘点单明细表不存在
 * 9. 库存调整单表不存在
 * 10. 库存调整单明细表不存在
 * 11. 库存盘点任务表不存在
 * 12. 库存调整任务表不存在
 * 13. 库存盘点任务明细表不存在
 * 14. 库存调整任务明细表不存在
 */
public interface ErrorCodeConstants {
    // 引用此异常枚举的地方 都有存在bug的风险，需要注意 发生后解决bug 并更新为其他种类提示抛出。
    ErrorCode BUG = new ErrorCode(2_001_000_001, "系统存在业务逻辑错误，请联系管理员处理");
    // 未知状态
    ErrorCode UNKNOWN_STATUS = new ErrorCode(2_001_000_002, "未知状态");
    // 未知类型
    ErrorCode UNKNOWN_TYPE = new ErrorCode(2_001_000_003, "未知类型");
    // 参数不能为空
    ErrorCode PARAM_NOT_NULL = new ErrorCode(2_001_000_004, "参数不能为空");
    // 用户未登录
    ErrorCode USER_NOT_LOGIN = new ErrorCode(2_001_000_005, "用户未登录");
    // 登录用户未绑定签收库位，请联系管理员
    ErrorCode USER_NOT_BIND_SIGN_AREA = new ErrorCode(2_001_000_006, "登录用户未绑定签收库位，请联系管理员");
    // 登录用户未绑定此仓库的签收库位，请联系管理员
    ErrorCode USER_NOT_BIND_THIS_WAREHOUSE_SIGN_AREA = new ErrorCode(2_001_000_007, "登录用户未绑定此仓库的签收库位，请联系管理员");
    // 登录用户在此仓库绑定多个签收库位，请联系管理员
    ErrorCode USER_BIND_MULTI_SIGN_AREA_IN_THIS_WAREHOUSE = new ErrorCode(2_001_000_008, "登录用户在此仓库绑定多个签收库位，请联系管理员");
    // 物料条码生成失败
    ErrorCode MATERIAL_BARCODE_GENERATE_FAILED = new ErrorCode(2_001_000_009, "物料条码生成失败");
    // 物料批次码生成失败
    ErrorCode MATERIAL_BATCH_CODE_GENERATE_FAILED = new ErrorCode(2_001_000_010, "物料批次码生成失败");
    // 出入库编码生成失败
    ErrorCode WAREHOUSE_ORDER_CODE_GENERATE_FAILED = new ErrorCode(2_001_000_011, "出入库编码生成失败");
    // 物料库存未审批
    ErrorCode MATERIAL_STOCK_NOT_APPROVED = new ErrorCode(2_001_000_012, "物料库存未审批");
    // 时间格式错误
    ErrorCode DATE_FORMAT_ERROR = new ErrorCode(2_001_000_013, "时间格式错误");





    ErrorCode WAREHOUSE_NOT_EXISTS = new ErrorCode(2_001_001_001, "仓库表不存在");
    // 此仓库未配置接驳库区，请先配置
    ErrorCode WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA = new ErrorCode(2_001_001_002, "此仓库未配置接驳库区，请先配置");

    ErrorCode WAREHOUSE_AREA_NOT_EXISTS = new ErrorCode(2_001_002_001, "库区不存在");
    ErrorCode WAREHOUSE_AREA_ALL_NULL_CGLS = new ErrorCode(2_001_002_002, "库区的通道、组、层、位不能全为空");

    ErrorCode WAREHOUSE_LOCATION_NOT_EXISTS = new ErrorCode(2_001_002_003, "库位不存在");
    ErrorCode WAREHOUSE_LOCATION_IS_EXISTS = new ErrorCode(2_001_002_004, "库位已存在，不能重复创建");
    ErrorCode WAREHOUSE_LOCATION_LOCKED = new ErrorCode(2_001_002_005, "库位已被锁定，不能进行操作");
    // 刀具接驳库位只能绑定刀具托盘
    ErrorCode WAREHOUSE_LOCATION_CAN_ONLY_BIND_TOOL_TRAY = new ErrorCode(2_001_002_006, "刀具接驳库位只能绑定刀具托盘");


    ErrorCode IN_WAREHOUSE_NOT_EXISTS = new ErrorCode(2_001_003_001, "入库不存在");
    ErrorCode IN_WAREHOUSE_CONTAINER_IS_EXISTS_MATERIAL = new ErrorCode(2_001_003_002, "该容器已绑定物料，请先解绑");
    ErrorCode IN_WAREHOUSE_LOCATION_NOT_EXISTS = new ErrorCode(2_001_003_003, "不存在呼叫库位");
    ErrorCode IN_WAREHOUSE_LOCATION_IS_OCCUPIED = new ErrorCode(2_001_003_004, "库位已被占用");
    ErrorCode IN_WAREHOUSE_CONTAINER_NOT_IN_LOCATION = new ErrorCode(2_001_003_005, "容器不在库位上");
    ErrorCode IN_WAREHOUSE_LOCATION_HAS_UNFINISHED_DOWN_INSTRUCTION_ORDER = new ErrorCode(2_001_003_006, "库位存在未完成的下架指令");
    ErrorCode IN_WAREHOUSE_AGV_CANNOT_REACH = new ErrorCode(2_001_003_007, "选择的物料容器，AGV无法抵达，请选择其他物料容器");
    ErrorCode IN_WAREHOUSE_CONTAINER_NOT_SELECTED = new ErrorCode(2_001_003_008, "未选择物料容器");
    ErrorCode IN_WAREHOUSE_CONTAINER_NO_AVAILABLE_DOWN_LOCATION = new ErrorCode(2_001_003_009, "所选物料容器暂无可用下架库位，请更换呼叫物料容器");
    ErrorCode IN_WAREHOUSE_UNFINISHED_INSTRUCTION_ORDER = new ErrorCode(2_001_003_010, "存在未完成的指令单");
    ErrorCode IN_WAREHOUSE_UNFINISHED_CARRYING_TASK_ORDER = new ErrorCode(2_001_003_011, "存在未完成的搬运任务单");
    ErrorCode IN_WAREHOUSE_INSTRUCTION_NOT_EXISTS = new ErrorCode(2_001_003_012, "指令单不存在");
    ErrorCode IN_WAREHOUSE_LOCATION_UPDATE_ERROR = new ErrorCode(2_001_003_013, "物料库位更新失败");
    ErrorCode IN_WAREHOUSE_CARRYING_TASK_NOT_EXISTS = new ErrorCode(2_001_003_014, "搬运任务单不存在");
    ErrorCode IN_WAREHOUSE_AGV_NOT_EXISTS = new ErrorCode(2_001_003_015, "AGV不存在");
    ErrorCode IN_WAREHOUSE_LOCATION_MATERIAL_NOT_IN_WAREHOUSE_ORDER = new ErrorCode(2_001_003_016, "库位上绑定的物料不存在入库单中，无入库权限");
    ErrorCode IN_WAREHOUSE_DUPLICATE_STOCK_PENDING_ORDER_NUMBER = new ErrorCode(2_001_003_017, "存在重复的入库单号");
    ErrorCode IN_WAREHOUSE_AGV_CANNOT_REACH_LOCATION = new ErrorCode(2_001_003_018, "此库位AGV无法抵达，请更换库位进行物料入库");
    ErrorCode IN_WAREHOUSE_LOCATION_NOT_EXISTS_MATERIAL = new ErrorCode(2_001_003_019, "库位为空，无需进行入库操作");
    ErrorCode IN_WAREHOUSE_CONTAINER_NOT_CONTAINER = new ErrorCode(2_001_003_020, "入库物料必须使用容器装载");
    ErrorCode IN_WAREHOUSE_DEFAULT_STORAGE_AREA_NOT_EXISTS = new ErrorCode(2_001_003_021, "默认存放库区不存在");
    ErrorCode IN_WAREHOUSE_AGV_NOT_AVAILABLE_TARGET_LOCATION = new ErrorCode(2_001_003_022, "暂无可用AGV接驳位，请稍后入库");
    ErrorCode IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_ORDER = new ErrorCode(2_001_003_023, "存在无入库单的物料，请核实准确后再入库");
    ErrorCode IN_WAREHOUSE_MATERIAL_IN_WAREHOUSE_ORDER_NOT_IN_EQUAL_WAREHOUSE = new ErrorCode(2_001_003_024, "存在入库仓库不一致的物料，请核实准确后再入库");
    ErrorCode IN_WAREHOUSE_DETAIL_UPDATE_FAILED = new ErrorCode(2_001_003_025, "入库详情单更新失败");
    // 此托盘未在此入库仓库配置接驳库区，请先配置
    ErrorCode IN_WAREHOUSE_TRAY_NOT_IN_CONFIG_AREA = new ErrorCode(2_001_003_026, "此托盘未在此入库仓库配置接驳库区，请先配置");
    ErrorCode IN_WAREHOUSE_DETAIL_NOT_MATCH_IN_WAREHOUSE_ORDER = new ErrorCode(2_001_003_027, "存在入库仓库不一致的入库详情单，请核实准确后再入库");
    // 此物料不满足入库状态，需人工分拣后再入库
    ErrorCode IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_STATUS = new ErrorCode(2_001_003_028, "此物料不满足入库状态，需人工分拣后再入库");
    ErrorCode IN_WAREHOUSE_CARRY_TASK_NOT_EXISTS = new ErrorCode(2_001_003_029, "搬运任务不存在");
    ErrorCode IN_WAREHOUSE_DETAIL_NOT_EXISTS = new ErrorCode(2_001_004_001, "入库详情不存在");


    ErrorCode OUT_WAREHOUSE_DETAIL_NOT_EXISTS = new ErrorCode(2_001_006_001, "出库详情单不存在");
    ErrorCode OUT_WAREHOUSE_TYPE_NOT_MATCH = new ErrorCode(2_001_006_002, "出库类型与出库详情单不匹配");
    ErrorCode OUT_WAREHOUSE_CALL_LOCATION_NOT_EMPTY = new ErrorCode(2_001_006_003, "呼叫库位不为空，请先清空库位");
    ErrorCode OUT_WAREHOUSE_DETAIL_UPDATE_FAILED = new ErrorCode(2_001_006_004, "出库详情单更新失败");
    ErrorCode OUT_WAREHOUSE_AGV_CANNOT_REACH_LOCATION = new ErrorCode(2_001_006_005, "此库位AGV无法抵达，请更换库位进行呼叫");
    // 此物料不存在待签收的出库详情单，无法签收
    ErrorCode OUT_WAREHOUSE_MATERIAL_NOT_FOUND_DELIVERY_ORDER = new ErrorCode(2_001_006_006, "此物料不存在待签收的出库详情单，无法签收");
    ErrorCode OUT_WAREHOUSE_MATERIAL_NOT_OUT_OUT_WAREHOUSE_ORDER = new ErrorCode(2_001_006_007, "存在无出库单的物料，请核实准确后再出库");
    // 出库详情单不存在出库仓库，数据错误
    ErrorCode OUT_WAREHOUSE_DETAIL_NOT_EXISTS_OUT_WAREHOUSE_ORDER = new ErrorCode(2_001_006_008, "出库详情单不存在目标仓库，数据错误");
    ErrorCode OUT_WAREHOUSE_ORDER_NOT_MATCH_CALL_LOCATION = new ErrorCode(2_001_006_009, "出库详情单与呼叫库位不匹配");
    // 存在目标仓库不一致的出库详情单，请核实准确后再出库
    ErrorCode OUT_WAREHOUSE_DETAIL_NOT_MATCH_OUT_WAREHOUSE_ORDER = new ErrorCode(2_001_006_010, "存在目标仓库不一致的出库详情单，请核实准确后再出库");
    // 存在出库单号不一致的出库详情单，请核实准确后再出库
    ErrorCode OUT_WAREHOUSE_DETAIL_NOT_MATCH_OUT_WAREHOUSE_ORDER_NUMBER = new ErrorCode(2_001_006_011, "存在出库单号不一致的出库详情单，请核实准确后再出库");
    // 此物料不满足出库状态，需人工分拣后再出库
    ErrorCode OUT_WAREHOUSE_MATERIAL_NOT_MATCH_OUT_WAREHOUSE_STATUS = new ErrorCode(2_001_006_012, "此物料不满足出库状态，需人工分拣后再出库");
    // 只能创建在存储库区的出库单
    ErrorCode OUT_WAREHOUSE_ORDER_NOT_IN_STORAGE_AREA = new ErrorCode(2_001_006_013, "只能创建在存储库区的出库单");


    ErrorCode STOCK_CHECK_NOT_EXISTS = new ErrorCode(2_001_007_001, "库存盘点单不存在");
    ErrorCode MATERIAL_TYPE_NOT_EXISTS = new ErrorCode(2_001_007_001, "物料类型不存在");
    ErrorCode MATERIAL_TYPE_EXITS_CHILDREN = new ErrorCode(2_001_007_002, "存在存在子物料类型，无法删除");
    ErrorCode MATERIAL_TYPE_PARENT_NOT_EXITS = new ErrorCode(2_001_007_003, "父级物料类型不存在");
    ErrorCode MATERIAL_TYPE_PARENT_ERROR = new ErrorCode(2_001_007_004, "不能设置自己为父物料类型");
    ErrorCode MATERIAL_TYPE_MATERIAL_PARENT_ID_DUPLICATE = new ErrorCode(2_001_007_005, "已经存在该父物料类型的物料类型");
    ErrorCode MATERIAL_TYPE_PARENT_IS_CHILD = new ErrorCode(2_001_007_006, "不能设置自己的子MaterialConfig为父MaterialConfig");
    ErrorCode MATERIAL_TYPE_ALL_NULL_LRC = new ErrorCode(2_001_007_006, "物料类型数据错误，为容器类型并且非单储位时，层、行、列不能全部为空");
    ErrorCode MATERIAL_TYPE_CONTAINER_ERROR = new ErrorCode(2_001_007_007, "批量管理的物料不能设置为容器");
    //此物料所属类型未配置默认存放仓库，请先配置默认存放仓库再入库，或填写默认存放仓库
    ErrorCode MATERIAL_TYPE_DEFAULT_STORAGE_AREA_NOT_EXISTS = new ErrorCode(2_001_007_008, "此物料所属类型未配置默认存放仓库，请先配置默认存放仓库再入库");
    // 不能生成容器类型的半成品类型
    ErrorCode MATERIAL_TYPE_HALF_PRODUCT_CONTAINER_ERROR = new ErrorCode(2_001_007_009, "不能生成容器类型的半成品类型");
    // 此容器非此物料的指定容器，请核实准确后再签入
    ErrorCode STOCK_CHECK_CONTAINER_NOT_MATCH_MATERIAL = new ErrorCode(2_001_007_010, "此容器非此物料的指定容器，请核实准确后再签入");



    ErrorCode MATERIAL_STOCK_NOT_EXISTS = new ErrorCode(2_001_008_001, "物料库存不存在");
    ErrorCode MATERIAL_STOCK_BIND_POSITION_ERROR = new ErrorCode(2_001_008_002, "物料绑定位置必须有且只有一项");
    ErrorCode MATERIAL_STOCK_TOTAL_LESS_THAN_LOCKED = new ErrorCode(2_001_008_003, "总库存不能小于锁定库存");
    ErrorCode MATERIAL_MANAGE_MODE_SINGLE_MATERIAL_TOTALITY_ERROR = new ErrorCode(2_001_008_004, "物料管理模式为单件的物料， 数量只能为1");
    ErrorCode MATERIAL_MANAGE_MODE_BATCH_MATERIAL_TOTALITY_ERROR = new ErrorCode(2_001_008_005, "物料管理模式为批量的物料， 数量必须大于0");
    ErrorCode MATERIAL_STOCK_NO_MOVE_PERMISSION = new ErrorCode(2_001_008_006, "物料无移库权限，不可绑定");
    ErrorCode MATERIAL_STOCK_NOT_BIND_POSITION = new ErrorCode(2_001_008_007, "物料未绑定位置");
    // 物料所在位置不支持搬运，请选择其他物料
    ErrorCode MATERIAL_STOCK_NOT_SUPPORT_CARRYING = new ErrorCode(2_001_008_008, "物料所在位置不支持搬运，请选择其他物料");
    // 下架库位不合法
    ErrorCode MATERIAL_STOCK_INVALID_DOWN_LOCATION = new ErrorCode(2_001_008_009, "下架库位不合法");
    // 此物料无法上架
    ErrorCode MATERIAL_STOCK_CANNOT_PUT_ON = new ErrorCode(2_001_008_010, "此物料无法上架");
    // 所选物料不符合装配规则，请选择其他物料，或调整类型配置
    ErrorCode MATERIAL_STOCK_NOT_MATCH_ASSEMBLY_RULE = new ErrorCode(2_001_008_011, "所选物料不符合装配规则，请选择其他物料，或调整类型配置");
    ErrorCode MATERIAL_STOCK_LOCK_AVAILABLE_ERROR = new ErrorCode(2_001_008_012, "锁定库存数量不能大于可用库存数量");
    ErrorCode MATERIAL_STOCK_UNLOCK_LOCKED_ERROR = new ErrorCode(2_001_008_013, "解锁库存数量不能大于锁定库存数量");
    // 存在无出库单的物料，无法签出
    ErrorCode MATERIAL_STOCK_NO_ORDER_TO_SIGN = new ErrorCode(2_001_008_014, "存在无出库单的物料，无法签出");
    // 物料更新库位失败
    ErrorCode MATERIAL_STOCK_UPDATE_LOCATION_ERROR = new ErrorCode(2_001_008_015, "物料更新库位失败");
    // 物料更新储位失败
    ErrorCode MATERIAL_STOCK_UPDATE_BIN_ERROR = new ErrorCode(2_001_008_016, "物料更新储位失败");
    ErrorCode MATERIAL_STOCK_NOT_ENOUGH_STOCK = new ErrorCode(2_001_008_017, "物料库存不足");
    ErrorCode MATERIAL_STOCK_LOCATION_OR_STORAGE_NOT_EXISTS = new ErrorCode(2_001_008_018, "物料库位或储位不存在");
    ErrorCode MATERIAL_STOCK_BARCODE_EXISTS = new ErrorCode(2_001_008_019, "条码已存在");
    // 此物料不存在库存出入库单，无法进行拣选操作，请核实后再拣选
    ErrorCode MATERIAL_STOCK_NO_STOCK_IN_OUT_ORDER = new ErrorCode(2_001_008_020, "此物料不存在库存出入库单，无法进行拣选操作，请核实后再拣选");
    // 此物料存在库存出入库单数量错误，无法进行创建操作，请核实后再拣选
    ErrorCode MATERIAL_STOCK_STOCK_IN_OUT_ORDER_QUANTITY_ERROR = new ErrorCode(2_001_008_021, "此物料存在库存出入库单数量错误，无法进行创建操作，请核实后再拣选");
    // 此物料不存出入库单，无法进行签入操作，请核实后再操作
    ErrorCode MATERIAL_STOCK_NO_STOCK_IN_OUT_ORDER_TO_SIGN = new ErrorCode(2_001_008_022, "此物料不存出入库单，无法进行签入操作，请核实后再操作");
    // 未找到此物料，请核实此操作库位上的物料信息
    ErrorCode MATERIAL_STOCK_NOT_FOUND = new ErrorCode(2_001_008_023, "未找到此物料，请核实此操作库位上的物料信息");
    // 此库位上存在多个相同条码的物料，物料库存数据错误！
    ErrorCode MATERIAL_STOCK_BARCODE_DUPLICATE = new ErrorCode(2_001_008_024, "此库位上存在多个相同条码的物料，物料库存数据错误！");
    // 不能签入托盘！！
    ErrorCode MATERIAL_STOCK_CANNOT_SIGN_IN_TRAY = new ErrorCode(2_001_008_025, "不能签入托盘！！");
    ErrorCode MATERIAL_STOCK_CANNOT_SIGN_OUT_TRAY = new ErrorCode(2_001_008_025, "不能签出托盘！！");
    // 存在多个出入库单据，请传入出入库单号再进行分拣
    ErrorCode MATERIAL_STOCK_MULTIPLE_STOCK_IN_OUT_ORDER = new ErrorCode(2_001_008_026, "存在多个出入库单据，请先通过分拣操作界面进行分拣");
    // 此托盘刀具架的配送目的地存在未配置的库区，请先配置再进行配送
    ErrorCode MATERIAL_STOCK_TRAY_NOT_CONFIG_DESTINATION = new ErrorCode(2_001_008_027, "此托盘刀具架的配送目的地存在未配置的库区，请先配置再进行配送");
    // 此托盘刀具架的配送目的地存在未配置的库位，请先查看系统库存配置再进行配送
    ErrorCode MATERIAL_STOCK_TRAY_NOT_CONFIG_BIN = new ErrorCode(2_001_008_028, "此托盘刀具架的配送目的地存在未配置的库位，请先查看系统库存配置再进行配送");
    // 物料更新失败
    ErrorCode MATERIAL_STOCK_UPDATE_ERROR = new ErrorCode(2_001_008_029, "物料更新失败");
    // 存在无配送目的地的刀具，请先配置配送目的地再进行出库配送
    ErrorCode MATERIAL_STOCK_TRAY_NOT_CONFIG_DESTINATION_ERROR = new ErrorCode(2_001_008_030, "存在无配送目的地的刀具，请先配置配送目的地再进行出库配送");
    // 只能签收托盘上的物料
    ErrorCode MATERIAL_STOCK_CANNOT_SIGN_OUT_NOT_TRAY_MATERIAL = new ErrorCode(2_001_008_031, "只能签出托盘上的物料");
    // 无可签出物料
    ErrorCode MATERIAL_STOCK_CANNOT_SIGN_OUT_MATERIAL = new ErrorCode(2_001_008_032, "无可签出物料");
    // 物料签出更新出入库单失败
    ErrorCode MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR = new ErrorCode(2_001_008_033, "物料签出更新出入库单失败");
    // 此物料已存在仓库内，无法收货，请核实
    ErrorCode MATERIAL_STOCK_ALREADY_IN_WAREHOUSE = new ErrorCode(2_001_008_034, "此物料已存在仓库内，无法收货，请核实");
    // 物料收货失败
    ErrorCode MATERIAL_STOCK_TAKE_DELIVERY_ERROR = new ErrorCode(2_001_008_035, "物料收货失败");
    // 物料发货失败
    ErrorCode MATERIAL_STOCK_SEND_DELIVERY_ERROR = new ErrorCode(2_001_008_036, "物料发货失败");
    // 非容器物料不能直接绑定在库位上，数据错误！！！
    ErrorCode MATERIAL_STOCK_NOT_CONTAINER_MATERIAL_CANNOT_BIND_BIN = new ErrorCode(2_001_008_037, "非容器物料不能直接绑定在库位上，数据错误！！！");
    // 物料不能绑定在自己身上
    ErrorCode MATERIAL_STOCK_CANNOT_BIND_SELF = new ErrorCode(2_001_008_038, "物料不能绑定在自己身上");
    // 扫描的储位编码不正确，请核实
    ErrorCode MATERIAL_STOCK_SCAN_BIN_CODE_ERROR = new ErrorCode(2_001_008_039, "扫描的储位编码不正确，请核实");
    // 签出仓库于出库仓库不一致，请核实
    ErrorCode MATERIAL_STOCK_SIGN_OUT_WAREHOUSE_NOT_MATCH_OUT_WAREHOUSE = new ErrorCode(2_001_008_040, "签出仓库与出库仓库不一致，请核实");
    // 此物料已指定签收库位，非目标签收库位的操作人员不可签收
    ErrorCode MATERIAL_STOCK_SIGN_OUT_NOT_MATCH_TARGET_LOCATION = new ErrorCode(2_001_008_041, "此物料已指定签收库位，非目标签收库位的操作人员不可签收");
    // 物料库位绑定信息错误，请核实
    ErrorCode MATERIAL_STOCK_BIN_NOT_BIND_POSITION = new ErrorCode(2_001_008_042, "物料库位绑定信息错误，请核实");

    ErrorCode MATERIAL_STORAGE_NOT_EXISTS = new ErrorCode(2_001_009_001, "物料储位不存在");
    // 此托盘无可用储位，请选择其他托盘
    ErrorCode MATERIAL_STORAGE_TRAY_NOT_FOUND = new ErrorCode(2_001_009_002, "此托盘无可用储位，请选择其他托盘");
    // 此储位号不存在，请核实
    ErrorCode MATERIAL_STORAGE_BIN_NOT_EXISTS = new ErrorCode(2_001_009_003, "此储位号不存在，请核实");
    // 储位信息错误,数据错误！！！
    ErrorCode MATERIAL_STORAGE_BIN_INFO_ERROR = new ErrorCode(2_001_009_004, "储位信息错误，数据错误！！！");


    ErrorCode UNIT_NOT_EXISTS = new ErrorCode(2_001_010_001, "物料单位不存在");

    ErrorCode TAKE_DELIVERY_NOT_EXISTS = new ErrorCode(2_001_011_001, "物料收货不存在");

    ErrorCode MATERIAL_CONFIG_AREA_NOT_EXISTS = new ErrorCode(2_001_012_001, "物料库区配置不存在");
    ErrorCode MATERIAL_CONFIG_AREA_EXISTS = new ErrorCode(2_001_012_001, "物料库区配置已存在");
    // 物料所在库区正在进行库存盘点，请等待盘点完成后再进行操作
    ErrorCode MATERIAL_CONFIG_AREA_IN_STOCK_CHECK = new ErrorCode(2_001_012_002, "物料所在库区正在进行库存盘点，请等待盘点完成后再进行操作");
    // 物料库存库位绑定信息错误，请核实
    ErrorCode MATERIAL_CONFIG_BIN_NOT_BIND_POSITION = new ErrorCode(2_001_012_003, "物料库存库位绑定信息错误，请核实");
    // 出入库仓库一致，请核实
    ErrorCode MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH = new ErrorCode(2_001_012_004, "出入库仓库一致，请核实");


    ErrorCode INSTRUCTION_NOT_EXISTS = new ErrorCode(2_001_013_001, "指令不存在");
    // 指令状态更新失败
    ErrorCode INSTRUCTION_STATUS_UPDATE_ERROR = new ErrorCode(2_001_013_002, "指令状态更新错误");

    ErrorCode CARRYING_TASK_NOT_EXISTS = new ErrorCode(2_001_014_001, "搬运任务不存在");
    ErrorCode CARRYING_TASK_NOT_EXISTS_START_LOCATION = new ErrorCode(2_001_014_002, "搬运任务不存在起始库位");
    ErrorCode CARRYING_TASK_NOT_EXISTS_TARGET_LOCATION = new ErrorCode(2_001_014_003, "搬运任务不存在目标库位");
    // 搬运状态更新错误
    ErrorCode CARRYING_TASK_STATUS_UPDATE_ERROR = new ErrorCode(2_001_014_004, "搬运状态更新错误");
    // 此物料已生成任务 请勿重复操作
    ErrorCode CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK = new ErrorCode(2_001_014_005, "此物料已生成任务 请勿重复操作");
    // 未找到此此物料可用于接驳的库位，请配置此物料的物料接驳库区配置
    ErrorCode CARRYING_TASK_MATERIAL_NOT_FOUND_CARRYING_AREA = new ErrorCode(2_001_014_006, "未找到此物料可用于接驳的库位，请配置此物料的物料接驳库区配置");
    // 呼叫库位未配置物料容器的接驳库区，请先配置
    ErrorCode CARRYING_TASK_CALL_LOCATION_NOT_CONFIG_CARRYING_AREA = new ErrorCode(2_001_014_007, "呼叫库位未配置物料容器的接驳库区，请先配置");
    //搬运物料必须使用容器装载
    ErrorCode CARRYING_TASK_MATERIAL_NOT_CONTAINER = new ErrorCode(2_001_014_008, "搬运物料必须使用容器装载");
    ErrorCode CARRYING_TASK_MATERIAL_NOT_TRAY = new ErrorCode(2_001_014_009, "搬运物料必须使用托盘装载");
    // 请呼叫非容器类物料
    ErrorCode CARRYING_TASK_MATERIAL_NOT_CONTAINER_CALL = new ErrorCode(2_001_014_010, "请选择非托盘类型的物料呼叫");
    // 暂无可用托盘承载物料，请查看物料库区配置，或稍后重试
    ErrorCode CARRYING_TASK_TRAY_NOT_FOUND = new ErrorCode(2_001_014_011, "暂无可用托盘承载物料，请查看物料库区配置，或稍后重试");
    // 未找到此此物料可用于上架的库位，请配置此物料的物料存储库区配置
    ErrorCode CARRYING_TASK_MATERIAL_NOT_FOUND_STORAGE_AREA = new ErrorCode(2_001_014_012, "未找到此物料可用于上架的库位，请配置此物料的物料存储库区配置");
    // 只能呼叫托盘，请查验后再呼叫
    ErrorCode CARRYING_TASK_MATERIAL_NOT_TRAY_CALL = new ErrorCode(2_001_014_013, "只能呼叫空托盘，请查验后再呼叫");
    // 库位绑定多个物料，请查验后再操作
    ErrorCode CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL = new ErrorCode(2_001_014_014, "库位绑定多个物料，请查验后再操作");
    // 此托盘未配置下架接驳库区，请先配置
    ErrorCode CARRYING_TASK_TRAY_NOT_CONFIG_CARRYING_AREA = new ErrorCode(2_001_014_015, "此托盘未配置下架接驳库区，请先配置");
    // 呼叫库位未配置此工装的指定承载的托盘类型，请先配置
    ErrorCode CARRYING_TASK_CALL_LOCATION_NOT_CONFIG_TRAY_TYPE = new ErrorCode(2_001_014_016, "呼叫库位未配置此工装的指定承载的托盘类型，请先配置");
    // 此任务已下发，请勿重复下发
    ErrorCode CARRYING_TASK_HAS_DISPATCH = new ErrorCode(2_001_014_017, "此任务已下发，请勿重复下发");
    // 托盘所在位置不支持呼叫，请选择其他托盘
    ErrorCode CARRYING_TASK_TRAY_NOT_SUPPORT_CALL = new ErrorCode(2_001_014_018, "托盘所在位置不支持呼叫，请选择其他托盘");
    // 此物料所在库位绑定物料数据异常，不在物料或存在多个，请核实
    ErrorCode CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL = new ErrorCode(2_001_014_019, "此物料所在库位绑定物料数据异常，不存在物料或存在多个，请核实");
    // 此库位已生成搬运任务，请勿重复下发
    ErrorCode CARRYING_TASK_LOCATION_HAS_TRANSPORT_TASK = new ErrorCode(2_001_014_020, "此库位已生成搬运任务，请勿重复下发");
    // 此接驳库区未配置可以搬运的托盘类型，请先配置
    ErrorCode CARRYING_TASK_CARRYING_AREA_NOT_CONFIG_TRAY_TYPE = new ErrorCode(2_001_014_021, "此接驳库区未配置可以搬运的托盘类型，请先配置");
    // 目标库位非接驳库位，不支持搬运
    ErrorCode CARRYING_TASK_TARGET_LOCATION_NOT_SUPPORT_CARRYING = new ErrorCode(2_001_014_022, "目标库位非接驳库位，不支持搬运");
    // 无空闲的起始库位
    ErrorCode CARRYING_TASK_START_LOCATION_NOT_FREE = new ErrorCode(2_001_014_023, "无空闲的起始库位");
    // 无空闲的目标库位
    ErrorCode CARRYING_TASK_TARGET_LOCATION_NOT_FREE = new ErrorCode(2_001_014_024, "无空闲的目标库位");
    // 目标库位暂时不可用
    ErrorCode CARRYING_TASK_TARGET_LOCATION_NOT_FREE_CALL = new ErrorCode(2_001_014_025, "目标库位暂时不可用");
    // 暂无可用上架库位
    ErrorCode CARRYING_TASK_STORAGE_LOCATION_NOT_FREE = new ErrorCode(2_001_014_026, "暂无可用上架库位");
    // 暂无可用的接驳库位
    ErrorCode CARRYING_TASK_CARRYING_LOCATION_NOT_FREE = new ErrorCode(2_001_014_027, "暂无可用的接驳库位");
    // 暂无可用的托盘类型
    ErrorCode CARRYING_TASK_TRAY_TYPE_NOT_FREE = new ErrorCode(2_001_014_028, "暂无可用的托盘类型");
    // 托盘上应该有且仅有一个物料
    ErrorCode CARRYING_TASK_TRAY_HAS_MULTIPLE_MATERIAL = new ErrorCode(2_001_014_029, "托盘上应该有且仅有一个物料");
    /// 接驳库位上只能存放托盘
    ErrorCode CARRYING_TASK_CARRYING_LOCATION_HAS_MATERIAL = new ErrorCode(2_001_014_030, "接驳库位上只能存放托盘");
    // 此托盘指定的签入物料非此物料，请更换物料签入托盘
    ErrorCode CARRYING_TASK_TRAY_NOT_MATCH_MATERIAL = new ErrorCode(2_001_014_031, "此托盘指定的签入物料非此物料，请更换物料签入托盘");
    // 搬运任务生成失败
    ErrorCode CARRYING_TASK_GENERATE_ERROR = new ErrorCode(2_001_014_032, "搬运任务生成失败");
    // 收货区不能存放托盘
    ErrorCode CARRYING_TASK_DELIVERY_LOCATION_HAS_TRAY = new ErrorCode(2_001_014_033, "收货区不能存放托盘");
    // 创建搬运任务失败
    ErrorCode CARRYING_TASK_CREATE_ERROR = new ErrorCode(2_001_014_034, "创建搬运任务失败");
    // 托盘上无刀具，无需配送
    ErrorCode CARRYING_TASK_TRAY_NO_TOOL = new ErrorCode(2_001_014_035, "托盘上无刀具，无需配送");



    ErrorCode STOCK_MOVE_DETAIL_NOT_EXISTS = new ErrorCode(2_001_015_001, "库存移动详情不存在");
    //空托盘不能进行移库操作
    ErrorCode MOVE_WAREHOUSE_TRAY_EMPTY = new ErrorCode(2_001_015_002, "空托盘不能进行移库操作");
    ErrorCode MOVE_WAREHOUSE_MATERIAL_NOT_MOVE_MOVE_WAREHOUSE_ORDER = new ErrorCode(2_001_015_003, "存在无移库单的物料，请核实准确后再移库");
    ErrorCode MOVE_WAREHOUSE_MATERIAL_NOT_FOUND_DELIVERY_ORDER = new ErrorCode(2_001_015_004, "此物料不存在待签收的移库详情单，无法签收");
    // 此物料不满足移库状态，需人工分拣后再移库
    ErrorCode MOVE_WAREHOUSE_MATERIAL_NOT_MATCH_MOVE_WAREHOUSE_STATUS = new ErrorCode(2_001_015_005, "此物料不满足移库状态，需人工分拣后再移库");
    //存在目标仓库不一致的移库详情单，请核实准确后再操作
    ErrorCode MOVE_WAREHOUSE_MATERIAL_NOT_MATCH_DEST_WAREHOUSE = new ErrorCode(2_001_015_006, "存在目标仓库不一致的移库详情单，请核实准确后再操作");
    // 此托盘未在此移库仓库配置接驳库区，请先配置
    ErrorCode MOVE_WAREHOUSE_TRAY_NOT_CONFIG_CARRYING_AREA = new ErrorCode(2_001_015_007, "此托盘未在此移库仓库配置接驳库区，请先配置");
    //移库详情单更新失败
    ErrorCode MOVE_WAREHOUSE_DETAIL_UPDATE_ERROR = new ErrorCode(2_001_015_008, "移库详情单更新失败");


    // 此物料储位编码信息错误，请核实
    ErrorCode MATERIAL_STOCK_BIN_CODE_ERROR = new ErrorCode(2_001_016_001, "此物料储位编码信息错误，请核实");

    ErrorCode AGV_NOT_EXISTS = new ErrorCode(2_001_016_001, "AGV 信息不存在");

    //不满足物料指定的容器类型
    ErrorCode MATERIAL_NOT_MATCH_CONTAINER_TYPE = new ErrorCode(2_001_017_001, "不满足物料指定的容器类型");

    ErrorCode CHECK_CONTAINER_NOT_EXISTS = new ErrorCode(2_001_018_001, "库存盘点容器不存在");

    ErrorCode CHECK_DETAIL_NOT_EXISTS = new ErrorCode(2_001_019_001, "库存盘点详情不存在");
    // 盘点详情信息更新失败
    ErrorCode CHECK_DETAIL_UPDATE_ERROR = new ErrorCode(2_001_019_002, "库存盘点详情信息更新失败");


    ErrorCode CHECK_PLAN_NOT_EXISTS = new ErrorCode(2_001_020_001, "库存盘点计划不存在");
    // 盘点任务信息更新失败
    ErrorCode CHECK_PLAN_UPDATE_ERROR = new ErrorCode(2_001_020_002, "库存盘点任务信息更新失败");
    // 存在未盘点的的库位，请先盘点
    ErrorCode CHECK_PLAN_NOT_CHECK_ALL_LOCATION = new ErrorCode(2_001_020_003, "存在未盘点的的库位，请先盘点");
    //已存在未完成的盘点计划，无法创建新的盘点计划
    ErrorCode CHECK_PLAN_EXIST_NOT_FINISHED = new ErrorCode(2_001_020_004, "已存在未完成的盘点计划，无法创建新的盘点计划");



    ErrorCode CHECK_CONTAINER_INSERT_ERROR = new ErrorCode(2_001_021_001, "库存盘点容器插入失败");
    // 盘点容器信息更新失败
    ErrorCode CHECK_CONTAINER_UPDATE_ERROR = new ErrorCode(2_001_021_002, "库存盘点容器信息更新失败");
    // 盘点数量错误，请核实
    ErrorCode CHECK_CONTAINER_COUNT_ERROR = new ErrorCode(2_001_021_003, "盘点数量错误，请核实");


    // 单据已存在，请勿重复创建
    ErrorCode BILL_ALREADY_EXISTS = new ErrorCode(2_001_022_001, "单据已存在，请勿重复创建");
    // 获取刀具配送单失败
    ErrorCode GET_TOOL_DISTRIBUTION_ORDER_ERROR = new ErrorCode(2_001_023_001, "获取刀具配送单失败");

    ErrorCode ALARM_NOT_EXISTS = new ErrorCode(2_001_024_001, "异常不存在");

    ErrorCode MATERIAL_MAINTENANCE_NOT_EXISTS = new ErrorCode(2_001_025_001, "物料维护记录不存在");
}
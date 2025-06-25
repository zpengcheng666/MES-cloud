package com.miyu.module.pdm.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 错误码枚举类
 * pdm 系统，使用 2-002-000-000 段
 *
 * @author Liuy
 */
public interface ErrorCodeConstants {
    // ========== PDM 产品 2-002-000-000 ==========
    ErrorCode PRODUCT_NOT_EXISTS = new ErrorCode(2_002_000_001, "产品不存在");
    ErrorCode PRODUCT_NUMBER_EXISTS = new ErrorCode(2_002_000_001, "产品图号已存在");

    // ========== PDM 产品分类 2-002-001-000 ==========
    ErrorCode PRODUCT_CATEGORY_NOT_EXISTS = new ErrorCode(2_002_001_000, "产品分类不存在");
    ErrorCode PRODUCT_CATEGORY_EXITS_CHILDREN = new ErrorCode(2_002_001_001, "存在子产品分类，无法删除");
    ErrorCode PRODUCT_CATEGORY_PARENT_NOT_EXITS = new ErrorCode(2_002_001_002,"父级产品分类不存在");
    ErrorCode PRODUCT_CATEGORY_PARENT_ERROR = new ErrorCode(2_002_001_003, "不能设置自己为父产品分类");
    ErrorCode PRODUCT_CATEGORY_NAME_DUPLICATE = new ErrorCode(2_002_001_004, "已经存在该分类名称的产品分类");
    ErrorCode PRODUCT_CATEGORY_PARENT_IS_CHILD = new ErrorCode(2_002_001_005, "不能设置自己的子分类为父分类");
    ErrorCode PRODUCT_CATEGORY_EXITS_PRODUCT = new ErrorCode(2_002_001_006, "存在产品使用该分类，无法删除");
    ErrorCode RECEIVE_INFO_NOT_EXISTS = new ErrorCode(2_002_001_007, "设计数据包接收记录不存在");
    // ========== PDM 数据包结构 2-002-002-000 ==========
    ErrorCode STRUCTURE_NOT_EXISTS = new ErrorCode(2_002_002_000, "数据包结构不存在");
    ErrorCode STRUCTURE_EXITS_CHILDREN = new ErrorCode(2_002_002_001, "存在子数据包结构，无法删除");
    ErrorCode STRUCTURE_PARENT_NOT_EXITS = new ErrorCode(2_002_002_002,"设计数据包接收记录不存在");

    // ========== PDM 数据包导入 2-002-003-000 ==========
    ErrorCode DATA_IMPORT_SUFFIX_ERROR = new ErrorCode(2_002_003_000, "数据包格式错误，仅允许导入zip格式文件！");
    ErrorCode DATA_IMPORT_CONNECT_ERROR = new ErrorCode(2_002_003_001, "连接服务器异常，请检查服务是否启动");
    ErrorCode DATA_IMPORT_MATCH_ERROR0 = new ErrorCode(2_002_003_002, "解析失败，zip包内未解析出CATPart文件");
    ErrorCode DATA_IMPORT_MATCH_ERROR1 = new ErrorCode(2_002_003_003, "解析失败，zip包内未解析出pdf文件");
    ErrorCode DATA_IMPORT_MATCH_ERROR2 = new ErrorCode(2_002_003_004, "解析失败，zip包内未解析出xlsx文件");
    ErrorCode DATA_IMPORT_MATCH_ERROR3 = new ErrorCode(2_002_003_005, "解析失败，zip包内未解析出xml文件");
    ErrorCode DATA_IMPORT_MATCH_PATH_ERROR0 = new ErrorCode(2_002_003_006, "解析失败，zip包内CATPart文件路径与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_PATH_ERROR1 = new ErrorCode(2_002_003_007, "解析失败，zip包内pdf文件路径与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_PATH_ERROR2 = new ErrorCode(2_002_003_008, "解析失败，zip包内xlsx文件路径与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_PATH_ERROR3 = new ErrorCode(2_002_003_009, "解析失败，zip包内xml文件路径与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_NUM_ERROR2 = new ErrorCode(2_002_003_010, "解析失败，zip包内xlsx文件只允许有一个");
    ErrorCode DATA_IMPORT_MATCH_NUM_ERROR3 = new ErrorCode(2_002_003_011, "解析失败，zip包内xml文件只允许有一个");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR0 = new ErrorCode(2_002_003_020, "解析失败，xlsx内图号行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR1 = new ErrorCode(2_002_003_021, "解析失败，xlsx内单机数量行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR2 = new ErrorCode(2_002_003_022, "解析失败，xlsx内数模版次行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR3 = new ErrorCode(2_002_003_023, "解析失败，xlsx内类别行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR4 = new ErrorCode(2_002_003_024, "解析失败，xlsx内架次行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR5 = new ErrorCode(2_002_003_025, "解析失败，xlsx内EDRN编号行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR6 = new ErrorCode(2_002_003_026, "解析失败，xlsx内EDRN版次行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR7 = new ErrorCode(2_002_003_027, "解析失败，xlsx内状态表行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_MATCH_EXCEL_ERROR8 = new ErrorCode(2_002_003_028, "解析失败，xlsx内状态表更改单号行列与数据包结构内不匹配");
    ErrorCode DATA_IMPORT_EXITS = new ErrorCode(2_002_003_030, "数据包名称已存在，无法导入");
    ErrorCode PART_TASK_EXITS = new ErrorCode(2_002_003_061,"零件已下发工艺任务，无法删除");

    // ========== PDM 加工路线 2-002-003-040 ==========
    ErrorCode PROCESSROUTE_NOT_EXISTS=new ErrorCode(2_002_003_040,"加工路线不存在");
    ErrorCode PROCESSROUTE_EXISTS = new ErrorCode(2_002_000_001, "加工路线名称已存在");
    ErrorCode PROCESSROUTE_TYPICAL_NOT_EXISTS=new ErrorCode(2_002_003_041,"典型工艺路线不存在");
    ErrorCode PROCESSROUTE_TYPICAL_EXISTS = new ErrorCode(2_002_003_042, "典型工艺路线名称已存在");
    ErrorCode DATA_OBJECT_EXISTS = new ErrorCode(2_002_003_031, "产品数据对象已存在");
    ErrorCode FEASIBILITY_TASK_NOT_EXISTS = new ErrorCode(2_002_003_032, "技术评估任务不存在");
    ErrorCode PROCESSROUTE_HASED_EXISTS = new ErrorCode(2_002_003_033, "存在工艺方案使用该加工路线，无法删除");
    ErrorCode PROCESSROUTE_TYPICAL_HASED_EXISTS = new ErrorCode(2_002_003_034, "存在工序使用该典型工艺路线，无法删除");

    // ========== PDM 工艺规程 2-002-003-050 ==========

    ErrorCode PRODURENUMBER_EXISTS = new ErrorCode(2_002_003_034, "该工序序号已被使用");

    ErrorCode PRODURENUMBER_ISNOT_EXISTS  = new ErrorCode(2_002_003_035, "该工序序号已被使用,不能更改");

//    ErrorCode PROCEDURE_NOT_EXISTS=new ErrorCode(2_002_003_050,"工序不存在");
//    ErrorCode PROCEDURE_EXISTS = new ErrorCode(2_002_000_051, "工序已存在");
//    ErrorCode STEP_NOT_EXISTS=new ErrorCode(2_002_003_052,"工步不存在");
//    ErrorCode STEP_EXISTS = new ErrorCode(2_002_003_053, "工步已存在");

    ErrorCode STEP_NAME_DUPLICATE = new ErrorCode(2_002_003_054, "已经存在该工步");

    ErrorCode STEP_EXITS_CHILDREN = new ErrorCode(2_002_003_055, "存在下属工步，无法删除");

//    ErrorCode STEP_PARENT_NOT_EXITS = new ErrorCode(2_002_003_056,"父级工序不存在");
//    ErrorCode STEP_PARENT_ERROR = new ErrorCode(2_002_003_057, "不能设置自己为父产品分类");
//    ErrorCode STEP_PARENT_IS_CHILD = new ErrorCode(2_002_003_058, "不能设置自己的子分类为父分类");
    ErrorCode ORDER_CHANGE_IS_EXISTS  = new ErrorCode(2_002_003_059, "更改单号已存在");
    ErrorCode ORDER_CHANGE_DEATIL_NULL  = new ErrorCode(2_002_003_060, "请添加更改详情信息");
    ErrorCode STEP_CATEGORY_NOT_EXISTS = new ErrorCode(2_002_001_000, "工步分类不存在");
    ErrorCode STEP_CATEGORY_PARENT_ERROR = new ErrorCode(2_002_001_003, "不能设置自己为父工步分类");
    ErrorCode STEP_CATEGORY_EXITS_CHILDREN = new ErrorCode(2_002_001_001, "存在子工步分类，无法删除");
    ErrorCode STEP_CATEGORY_PARENT_NOT_EXITS = new ErrorCode(2_002_001_002,"父级工步分类不存在");
    ErrorCode STEP_CATEGORY_EXITS_STEP = new ErrorCode(2_002_001_006, "存在工步使用该分类，无法删除");
    ErrorCode STEP_CATEGORY_NAME_DUPLICATE = new ErrorCode(2_002_001_004, "已经存在该分类名称的工步分类");
    ErrorCode STEP_CATEGORY_PARENT_IS_CHILD = new ErrorCode(2_002_001_005, "不能设置自己的子分类为父分类");

    // ========== PDM 工装设计 2-002-003-060 ==========
    ErrorCode TOOLING_CATEGORY_NOT_EXISTS = new ErrorCode(2_002_001_000, "工装分类不存在");
    ErrorCode TOOLING_CATEGORY_EXITS_CHILDREN = new ErrorCode(2_002_001_001, "存在子工装分类，无法删除");
    ErrorCode TOOLING_CATEGORY_PARENT_NOT_EXITS = new ErrorCode(2_002_001_002,"父级工装分类不存在");
    ErrorCode TOOLING_CATEGORY_PARENT_ERROR = new ErrorCode(2_002_001_003, "不能设置自己为父工装分类");
    ErrorCode TOOLING_CATEGORY_NAME_DUPLICATE = new ErrorCode(2_002_001_004, "已经存在该分类名称的工装分类");
    ErrorCode TOOLING_CATEGORY_PARENT_IS_CHILD = new ErrorCode(2_002_001_005, "不能设置自己的子分类为父分类");
    ErrorCode TOOLING_CATEGORY_EXITS_PRODUCT = new ErrorCode(2_002_001_006, "存在工装使用该分类，无法删除");
    ErrorCode TOOLING_NUMBER_DUPLICATE = new ErrorCode(2_002_001_007, "已存在该工装申请");

    // ========== PDM 补加工工艺规程 2-002-004-000 ==========
    ErrorCode PROCESSSUPPLEMENT_NOT_EXISTS=new ErrorCode(2_002_004_001,"补加工工艺规程不存在");
    ErrorCode PROCESSSUPPLEMENT_EXISTS = new ErrorCode(2_002_004_002, "补加工工艺规程编号已存在");
}

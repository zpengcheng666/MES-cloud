package com.miyu.module.pdm.enums;

/**
 * pdm 字典类型的枚举类
 *
 * @author Liuy
 */
public interface DictConstants {

    Integer FILE_TYPE_0 = 0; // 文件类型 CATPart
    Integer FILE_TYPE_1 = 1; // 文件类型 pdf
    Integer FILE_TYPE_2 = 2; // 文件类型 xlsx
    Integer FILE_TYPE_3 = 3; // 文件类型 xml

    String FILE_SUFFIX_0 = "CATPart"; // 文件后缀
    String FILE_SUFFIX_1 = "pdf"; // 文件后缀
    String FILE_SUFFIX_2 = "xlsx"; // 文件后缀
    String FILE_SUFFIX_3 = "xml"; // 文件后缀

    Integer CONTENT_TYPE_0 = 0; // 文件内容 图号
    Integer CONTENT_TYPE_1 = 1; // 文件内容 单机数量
    Integer CONTENT_TYPE_2 = 2; // 文件内容 数模版次
    Integer CONTENT_TYPE_3 = 3; // 文件内容 类别
    Integer CONTENT_TYPE_4 = 4; // 文件内容 架次
    Integer CONTENT_TYPE_5 = 5; // 文件内容 EDRN编号
    Integer CONTENT_TYPE_6 = 6; // 文件内容 EDRN版次
    Integer CONTENT_TYPE_7 = 7; // 文件内容 状态表
    Integer CONTENT_TYPE_8 = 8; // 文件内容 状态表更改单号

    String EXCEL_CONTENT_0 = "图号"; // excel内容
    String EXCEL_CONTENT_1 = "单机数量"; // excel内容
    String EXCEL_CONTENT_2 = "数模版次"; // excel内容
    String EXCEL_CONTENT_3 = "类别"; // excel内容
    String EXCEL_CONTENT_4 = "架次"; // excel内容
    String EXCEL_CONTENT_5 = "EDRN编号"; // excel内容
    String EXCEL_CONTENT_6 = "EDRN版次"; // excel内容
    String EXCEL_CONTENT_7 = "状态表"; // excel内容
    String EXCEL_CONTENT_8 = "状态表更改单号"; // excel内容
}

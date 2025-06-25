package com.miyu.module.es.dal.dataobject.monthlyCar;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyData {

    //通知
    private Integer NoticeType;

    //通知数据集合
    private List<MonthlyCarData> Items;

}

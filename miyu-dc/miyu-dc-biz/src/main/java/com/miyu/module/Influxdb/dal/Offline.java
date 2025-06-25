package com.miyu.module.Influxdb.dal;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

@Data
public class Offline extends BaseDO {

    /**
     * 基础sql
     */
    private String selectSQL;

    /**
     * 分页
     */
    private String limit;

}

package com.miyu.module.dc.dal.dataobject.mqtt;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

@Data
public class ReceiveDO {

//    private Integer type;  //类别（1.设备注册  2.数据存储）

    private Object value;  //数据值  当type为1时，暂传入匹配标签码。         当type为2时，传入数据值
}

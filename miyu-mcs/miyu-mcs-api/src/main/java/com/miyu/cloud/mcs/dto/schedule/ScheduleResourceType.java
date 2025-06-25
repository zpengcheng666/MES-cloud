package com.miyu.cloud.mcs.dto.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ScheduleResourceType {

    //制造资源类型：1设备 2刀具 3工装
    private Integer resourcesType;
    //制造资源类型id(设备、刀具、工装等)
    private String resourcesTypeId;
    //设备编号
    private String code;
    //设备名称
    private String name;
    //刀简号
    private String cutterNum;
    //刀组编码
    private String cutterGroupCode;
    //刀柄类型
    private String taperTypeName;
    //工装编号
    private String materialNumber;
    //工装名称
    private String materialName;
    //最大数量
    private Integer count;
}

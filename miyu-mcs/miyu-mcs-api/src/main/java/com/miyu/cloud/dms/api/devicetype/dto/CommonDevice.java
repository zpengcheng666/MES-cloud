package com.miyu.cloud.dms.api.devicetype.dto;

import com.alibaba.ttl.threadpool.agent.internal.javassist.compiler.ast.StringL;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "dms 服务 - 设备类型通用")
@Data
public class CommonDevice {

    @Schema(description = "类型id")
    private String id;

    @Schema(description = "类型编码")
    private String code;

    @Schema(description = "类型名称")
    private String name;

    @Schema(description = "规格")
    private String specification;

    //0 设备, 1 工位, 2 产线
    @Schema(description = "类型" )
    private Integer type;

    @Schema(description = "位置id")
    private String location;

    @Schema(description = "位置id")
    private String locationId;
    public String getLocationId() {return location;}
    public void setLocationId(String locationId) {this.location = locationId;}
}

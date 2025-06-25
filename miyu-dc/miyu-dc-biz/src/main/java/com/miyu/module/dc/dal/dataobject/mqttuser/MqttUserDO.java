package com.miyu.module.dc.dal.dataobject.mqttuser;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class MqttUserDO {

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     *账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密加盐方式(mqtt客户端定义)
     */
    private String salt;

    /**
     * 权限
     */
    private String isSuperuser;

    /**
     * 创建时间
     */
    private String created;

}

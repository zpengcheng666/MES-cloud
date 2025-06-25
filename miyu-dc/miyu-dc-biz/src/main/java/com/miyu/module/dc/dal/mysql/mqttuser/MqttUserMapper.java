package com.miyu.module.dc.dal.mysql.mqttuser;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.miyu.module.dc.dal.dataobject.mqttuser.MqttUserDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MqttUserMapper extends BaseMapperX<MqttUserDO> {

    /**
     * mqtt用户表新增（加盐加密）
     * @param mqttUserDO
     */
    @TenantIgnore
    void insertUser(@Param("mqttUserDO") MqttUserDO mqttUserDO);

    @TenantIgnore
    @Delete("delete from dc_mqtt_user where username = #{userName}")
    void deleteUser(@Param("userName") String userName);



}

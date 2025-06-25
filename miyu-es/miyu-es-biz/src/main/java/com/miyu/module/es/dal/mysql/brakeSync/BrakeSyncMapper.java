package com.miyu.module.es.dal.mysql.brakeSync;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import com.miyu.module.es.dal.dataobject.brakeSync.BrakeSyncDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BrakeSyncMapper extends BaseMapperX<BrakeSyncDO> {

    @TenantIgnore
    @Select("select * FROM `es_brake_sync` WHERE id = #{id} ")
    BrakeSyncDO selectSyncById( @Param("id") String id);

    @TenantIgnore
    @Update("UPDATE `es_brake_sync` SET automatic = #{automatic},cycle = #{cycle},sync=#{sync} WHERE id = #{id} ")
    void updateSyncById(@Param("id") String id , @Param("automatic") Integer automatic , @Param("cycle") String cycle , @Param("sync") Integer sync);


}

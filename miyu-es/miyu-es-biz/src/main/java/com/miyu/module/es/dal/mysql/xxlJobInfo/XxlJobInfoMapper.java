package com.miyu.module.es.dal.mysql.xxlJobInfo;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.miyu.module.es.dal.dataobject.xxlJobInfo.XxlJobInfoDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import static cn.iocoder.yudao.framework.datasource.core.enums.DataSourceEnum.SLAVE;

@Slave
public interface XxlJobInfoMapper extends BaseMapperX<XxlJobInfoDO> {


    @TenantIgnore
    @DS(SLAVE)
    @Update("UPDATE `xxl_job_info` SET trigger_status = #{triggerStatus} WHERE id = #{id} ")
    void updateOpen( @Param("id") Integer id , @Param("triggerStatus") Integer triggerStatus);

    @TenantIgnore
    @DS(SLAVE)
    @Update("UPDATE `xxl_job_info` SET trigger_status = #{triggerStatus}, schedule_conf = #{scheduleConf} WHERE id = #{id} ")
    void updateEnd(@Param("id") Integer id , @Param("triggerStatus") Integer triggerStatus , @Param("scheduleConf") String scheduleConf );
}

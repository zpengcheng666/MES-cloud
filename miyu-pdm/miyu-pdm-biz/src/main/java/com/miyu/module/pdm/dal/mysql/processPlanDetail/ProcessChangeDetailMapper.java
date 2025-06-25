package com.miyu.module.pdm.dal.mysql.processPlanDetail;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessChangeDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProcessChangeDetailMapper  extends BaseMapperX<ProcessChangeDetailDO> {

    @Update("UPDATE capp_process_change_detail SET deleted = 1 WHERE id=#{id} AND deleted = 0 ")
    void deleteChangeOrderById(String id);
}

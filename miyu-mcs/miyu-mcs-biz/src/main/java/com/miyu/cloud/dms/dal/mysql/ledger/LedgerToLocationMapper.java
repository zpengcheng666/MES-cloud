package com.miyu.cloud.dms.dal.mysql.ledger;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToLocationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LedgerToLocationMapper extends BaseMapperX<LedgerToLocationDO> {
}

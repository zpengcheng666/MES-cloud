package com.miyu.cloud.dms.dal.mysql.ledger;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LedgerToUserMapper extends BaseMapperX<LedgerToUserDO> {

    default List<LedgerToUserDO> getUsersByLedger(String id){
        return selectList(new LambdaQueryWrapperX<LedgerToUserDO>()
                .eq(LedgerToUserDO::getLedger, id));
    }

}

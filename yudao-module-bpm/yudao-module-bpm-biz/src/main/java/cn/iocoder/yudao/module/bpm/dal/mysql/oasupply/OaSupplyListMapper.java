package cn.iocoder.yudao.module.bpm.dal.mysql.oasupply;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyListDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * OA 物品领用表-物品清单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OaSupplyListMapper extends BaseMapperX<OaSupplyListDO> {

    default List<OaSupplyListDO> selectListBySupplyId(Long supplyId) {
        return selectList(OaSupplyListDO::getSupplyId, supplyId);
    }

    default int deleteBySupplyId(Long supplyId) {
        return delete(OaSupplyListDO::getSupplyId, supplyId);
    }

}

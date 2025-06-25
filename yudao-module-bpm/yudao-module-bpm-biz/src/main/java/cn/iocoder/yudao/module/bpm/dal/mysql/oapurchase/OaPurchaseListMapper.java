package cn.iocoder.yudao.module.bpm.dal.mysql.oapurchase;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseListDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * OA 采购申请 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OaPurchaseListMapper extends BaseMapperX<OaPurchaseListDO> {

    default List<OaPurchaseListDO> selectListByPurchaseId(Long purchaseId) {
        return selectList(OaPurchaseListDO::getPurchaseId, purchaseId);
    }

    default int deleteByPurchaseId(Long purchaseId) {
        return delete(OaPurchaseListDO::getPurchaseId, purchaseId);
    }

}

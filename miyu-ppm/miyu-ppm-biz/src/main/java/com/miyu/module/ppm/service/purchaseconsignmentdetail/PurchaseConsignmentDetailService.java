package com.miyu.module.ppm.service.purchaseconsignmentdetail;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 收货明细 Service 接口
 *
 * @author 芋道源码
 */
public interface PurchaseConsignmentDetailService {

    /**
     * 创建收货明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createPurchaseConsignmentDetail(@Valid PurchaseConsignmentDetailSaveReqVO createReqVO);

    /**
     * 更新收货明细
     *
     * @param updateReqVO 更新信息
     */
    void updatePurchaseConsignmentDetail(@Valid PurchaseConsignmentDetailSaveReqVO updateReqVO);

    /**
     * 删除收货明细
     *
     * @param id 编号
     */
    void deletePurchaseConsignmentDetail(String id);

    /**
     * 获得收货明细
     *
     * @param id 编号
     * @return 收货明细
     */
    ConsignmentDetailDO getPurchaseConsignmentDetail(String id);

    /**
     * 获得收货明细分页
     *
     * @param pageReqVO 分页查询
     * @return 收货明细分页
     */
    PageResult<ConsignmentDetailDO> getPurchaseConsignmentDetailPage(PurchaseConsignmentDetailPageReqVO pageReqVO);

    /***
     * 查询合同下 有效的收库单
     * @param contractId
     * @return
     */
    List<ConsignmentDetailDO> getInboundOderByContractId(String contractId, List<Integer> status);

    /**
     * 通过产品编号查询收货单明细Id
     */
    ConsignmentDetailDO queryConsignmentDetailIdByConfigId(String materialConfigId , String consignmentId);

    /**
     * 通过产品编号查询收货单明细数据
     */
    List<ConsignmentDetailDO> queryConsignmentDetailIdById(String id);

    /***
     * 根据 项目  类型  合同  查询入库详情
     * @param projectId
     * @param contract
     * @param consignmentType
     * @return
     */
    List<ConsignmentDetailDO> getDetailListByProjectId(String projectId,String contract,Integer consignmentType);

    /**
     * 通过收货单查询收货单明细
     */
    List<ConsignmentDetailDO> queryConsignmentDetailIdByConsignmentId(String consignmentId);


    /***
     * 根据发货单详情 查询对应的收货单
     * @param shippingDetailIds
     * @return
     */
    List<ConsignmentDetailDO> getDetailListByShippingDetailIds(Collection<String> shippingDetailIds);
    List<ConsignmentDetailDO> getDetailListByIds(Collection<String> consignmentIds);


    List<ConsignmentDetailDO> getDetailListByInfoId(String consignmentInfoId);

    List<ConsignmentDetailDO> getPurchaseDetailByProjectOrderId(String id);

    List<ConsignmentDetailDO> getPurchaseDetailByPurchaseId(String id);
}

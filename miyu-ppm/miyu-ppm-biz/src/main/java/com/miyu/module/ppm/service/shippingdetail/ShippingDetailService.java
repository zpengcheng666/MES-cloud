package com.miyu.module.ppm.service.shippingdetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售发货明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ShippingDetailService {

    /**
     * 创建销售发货明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShippingDetail(@Valid ShippingDetailSaveReqVO createReqVO);

    /**
     * 更新销售发货明细
     *
     * @param updateReqVO 更新信息
     */
    void updateShippingDetail(@Valid ShippingDetailSaveReqVO updateReqVO);

    /**
     * 删除销售发货明细
     *
     * @param id 编号
     */
    void deleteShippingDetail(String id);

    /**
     * 获得销售发货明细
     *
     * @param id 编号
     * @return 销售发货明细
     */
    ShippingDetailDO getShippingDetail(String id);

    /**
     * 获得销售发货明细分页
     *
     * @param pageReqVO 分页查询
     * @return 销售发货明细分页
     */
    PageResult<ShippingDetailDO> getShippingDetailPage(ShippingDetailPageReqVO pageReqVO);


    /***
     * 查询合同下 有效的出库单
     * @param contractId
     * @return
     */
    List<ShippingDetailDO> getOutboundOderByContractId(String contractId,List<Integer> status);

    /***
     * 根据项目获取发货详情
     * @param projectId
     * @return
     */
    List<ShippingDetailDO> getDetailByProjectId(String projectId,String projectOrderId,Integer shippingType);


    List<ShippingDetailDO> getDetailByProjectIds(Collection<String> projectId);


    /****
     *  根据发货单  或者发货单产品表ID 查询发货单详情
     * @param shippingId  发货单Id
     * @param infoId  发货单产品表Id
     * @return
     */
    List<ShippingDetailDO> getDetailsById(String shippingId,String infoId);


    List<ShippingDetailDO> getDetailByConsignmentDetailIds(Collection<String> consignmentDetailIds);

    List<ShippingDetailDO> getShippingDetailByProjectOrderId(String id);

    List<ShippingDetailDO> getShippingDetailByPurchaseId(String id);
}

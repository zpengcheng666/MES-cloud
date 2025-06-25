package com.miyu.module.ppm.service.consignmentreturndetail;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.ConsignmentReturnRespVO;
import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;

/**
 * 销售退货单详情 Service 接口
 *
 * @author 芋道源码
 */
public interface ConsignmentReturnDetailService {

    /**
     * 创建销售退货单详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createConsignmentReturnDetail(@Valid ConsignmentReturnDetailSaveReqVO createReqVO);

    /**
     * 更新销售退货单详情
     *
     * @param updateReqVO 更新信息
     */
    void updateConsignmentReturnDetail(@Valid ConsignmentReturnDetailSaveReqVO updateReqVO);

    /**
     * 删除销售退货单详情
     *
     * @param id 编号
     */
    void deleteConsignmentReturnDetail(String id);

    /**
     * 获得销售退货单详情
     *
     * @param id 编号
     * @return 销售退货单详情
     */
    ConsignmentReturnDetailDO getConsignmentReturnDetail(String id);

    /**
     * 获得销售退货单详情分页
     *
     * @param pageReqVO 分页查询
     * @return 销售退货单详情分页
     */
    PageResult<ConsignmentReturnDetailDO> getConsignmentReturnDetailPage(ConsignmentReturnDetailPageReqVO pageReqVO);

    /**
     * 查询采购退货单明细信息
     * @param ids
     * @return
     */
    List<ConsignmentReturnDetailDO> getConsignmentReturnDetails( List<String> ids , String contractId);

    /**
     * 退货单金额计算
     */
    List<ConsignmentReturnRespVO> queryConsignmentReturnPrice(List<ShippingDO> consignmentReturnDOS , List<ShippingDetailDO> consignmentReturnDetailDOS);


}
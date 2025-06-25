package com.miyu.module.ppm.service.shippingreturndetail;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailRetraceDTO;
import com.miyu.module.ppm.controller.admin.shippingreturndetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售退货单详情 Service 接口
 *
 * @author miyudmA
 */
public interface ShippingReturnDetailService {

    /**
     * 创建销售退货单详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShippingReturnDetail(@Valid ShippingReturnDetailSaveReqVO createReqVO);

    /**
     * 更新销售退货单详情
     *
     * @param updateReqVO 更新信息
     */
    void updateShippingReturnDetail(@Valid ShippingReturnDetailSaveReqVO updateReqVO);

    /**
     * 删除销售退货单详情
     *
     * @param id 编号
     */
    void deleteShippingReturnDetail(String id);

    /**
     * 获得销售退货单详情
     *
     * @param id 编号
     * @return 销售退货单详情
     */
    ShippingReturnDetailDO getShippingReturnDetail(String id);

    /**
     * 获得销售退货单详情分页
     *
     * @param pageReqVO 分页查询
     * @return 销售退货单详情分页
     */
    PageResult<ShippingReturnDetailDO> getShippingReturnDetailPage(ShippingReturnDetailPageReqVO pageReqVO);

    /***
     * 根据合同和状态 查询退货单详情
     * @param contractId  合同
     * @param shippingReturnStatus  退货单状态
     * @return
     */
    List<ShippingReturnDetailDO> getShippingReturnDetails(String contractId,List<Integer> shippingReturnStatus);

    List<ShippingReturnDetailDO> getShippingReturnDetails(List<String> shippingReturnIds);

    List<ShippingReturnDetailRetraceDTO> getShippingReturnListByBarcode(String barCode);



    /***
     * 根据发货单详情查询退货单详情
     * @param detailIds
     * @return
     */
    List<ShippingReturnDetailDO> getShippingReturnDetailListByShippingDetailIds(List<String> detailIds);


}
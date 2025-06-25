package com.miyu.module.ppm.service.shippinginfo;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.controller.admin.shippinginfo.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售发货产品 Service 接口
 *
 * @author 上海弥彧
 */
public interface ShippingInfoService {

    /**
     * 创建销售发货产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShippingInfo(@Valid ShippingInfoSaveReqVO createReqVO);

    /**
     * 更新销售发货产品
     *
     * @param updateReqVO 更新信息
     */
    void updateShippingInfo(@Valid ShippingInfoSaveReqVO updateReqVO);

    /**
     * 删除销售发货产品
     *
     * @param id 编号
     */
    void deleteShippingInfo(String id);

    /**
     * 获得销售发货产品
     *
     * @param id 编号
     * @return 销售发货产品
     */
    ShippingInfoDO getShippingInfo(String id);

    /**
     * 获得销售发货产品分页
     *
     * @param pageReqVO 分页查询
     * @return 销售发货产品分页
     */
    PageResult<ShippingInfoDO> getShippingInfoPage(ShippingInfoPageReqVO pageReqVO);



    List<ShippingInfoDO> getShippingInfoByShippingId(String shippingId);


    List<ShippingInfoDO> getShippingInfoByContractId(String contractId);
    List<ShippingInfoDO> getShippingInfoByContractIds(List<String> contractId);

    /***
     * 获取采购退货数量
     * @return
     */
    List<ConsignmentCompanyNumberRespVO>  getConsignmentCompanyReturnNumber(LocalDateTime[] createTimeRange);

    /***
     * 根据收货单查询退货单
     * @param consignmentIds
     * @return
     */
    List<ShippingInfoDO> getShippingInfoByConsignmentIds(List<String> consignmentIds);
    List<ShippingInfoDO> getShippingInfoByOrderIds(List<String> orderIds);
}
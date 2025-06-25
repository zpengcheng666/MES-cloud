package com.miyu.module.ppm.service.consignmentinfo;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.*;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 收货产品 Service 接口
 *
 * @author 上海弥彧
 */
public interface ConsignmentInfoService {

    /**
     * 创建收货产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createConsignmentInfo(@Valid ConsignmentInfoSaveReqVO createReqVO);

    /**
     * 更新收货产品
     *
     * @param updateReqVO 更新信息
     */
    void updateConsignmentInfo(@Valid ConsignmentInfoSaveReqVO updateReqVO);

    /***
     * 签收
     * @param updateReqVO
     */
    void signInfo(@Valid ConsignmentInfoSaveReqVO updateReqVO);

    /***
     * 条码签收
     * @param updateReqVO
     */
    void signMaterial(@Valid ConsignmentInfoSaveReqVO updateReqVO);

    /**
     * 删除收货产品
     *
     * @param id 编号
     */
    void deleteConsignmentInfo(String id);

    /**
     * 获得收货产品
     *
     * @param id 编号
     * @return 收货产品
     */
    ConsignmentInfoDO getConsignmentInfo(String id);

    /**
     * 获得收货产品分页
     *
     * @param pageReqVO 分页查询
     * @return 收货产品分页
     */
    PageResult<ConsignmentInfoDO> getConsignmentInfoPage(ConsignmentInfoPageReqVO pageReqVO);

    /***
     *根据合同查询收货列表
     *
     * @param contractId  合同ID
     * @param consignmentId  收货单ID （排除用）
     * @return
     */
    List<ConsignmentInfoDO> getConsignmentInfoByContractId(String contractId,String consignmentId);
    List<ConsignmentInfoDO> getConsignmentInfoByConsignmentId(String consignmentId);

    List<ConsignmentInfoDO> getConsignmentInfos(ConsignmentInfoQueryReqVO reqVO);


    List<ConsignmentCompanyNumberRespVO> getCompanyConsignmentAmount(LocalDateTime[] createTimeRange);


    /***
     * 根据合同订单查询收货单
     * @param orderIds
     * @return
     */
    List<ConsignmentInfoDO> getConsignmentInfoByOrderIds(List<String> orderIds);

    /***
     * 根据发货单查询退货单
     * @param shippingIds
     * @return
     */
    List<ConsignmentInfoDO> getConsignmentInfoByShippingIds(List<String> shippingIds);


    /***
     * 根据合同查询结束的收货单
     * @param contractIds
     * @return
     */
    List<ConsignmentInfoDO> getConsignmentInfoByContractIds(List<String> contractIds);
}
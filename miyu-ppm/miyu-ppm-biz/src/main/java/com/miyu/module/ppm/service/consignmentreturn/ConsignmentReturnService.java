package com.miyu.module.ppm.service.consignmentreturn;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;

/**
 * 采购退货单 Service 接口
 *
 * @author 芋道源码
 */
public interface ConsignmentReturnService {


    /**
     *  通过合同查询采购退货及其明细
     * @param ids
     * @return
     */
    List<ConsignmentReturnDTO> getConsignmentReturnDetailByContractIds(Collection<String> ids);

    // ==================== 子表（采购退货单详情） ====================

    /**
     * 更新退货审批状态
     * @param id
     * @param status
     */
    void updateConsignmentProcessInstanceStatus(String id,Integer status);


    /**
     * 查询退货单详细信息
     */
    List<ShippingDetailDO>  queryConsignmentReturnDetailById(String consignmentReturnId);

    /**
     * 获取合同下的退货单
     */
    List<ShippingDO> getConsignmentReturnByContract(String contractId, List<Integer> statusList,Integer shippingType);


    /**
     * 合同主键查询退货单集合
     * @param id
     * @return
     */
    List<ConsignmentReturnDTO> getConsignmentReturnListByContractId(String id);

    /***
     * 监听退货出库的状态
     */
    void checkOutBoundInfo();



    Boolean outBound(ConsignmentReturnDO consignmentReturnDO,List<ConsignmentReturnDetailDO> detailDOS);


    List<ShippingDetailDO> getReturnByBarCodes(Collection<String> barCodes);

}

package com.miyu.module.ppm.service.shippingreturn;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.controller.admin.shippingreturn.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 销售退货单 Service 接口
 *
 * @author miyudmA
 */
public interface ShippingReturnService {


    /**
     * 通过合同id查询销售退货单及明细
     * @param ids
     * @return
     */
    List<ShippingReturnDTO> getShippingReturnListByContractIds(Collection<String> ids);

    /***
     * 更新审批状态
     * @param id
     * @param status
     */
    void updateShippingProcessInstanceStatus(String id,Integer status);

    /***
     * 根据合同获取退货单
     * @param contractId
     * @param status
     * @return
     */
    List<ConsignmentDO> getShippingReturnByContract(String contractId, List<Integer> status);

    /***
     * 根据id获取退货单
     * @param ids
     * @return
     */
    List<ConsignmentDO> getShippingReturnByIds(List<String> ids);



}

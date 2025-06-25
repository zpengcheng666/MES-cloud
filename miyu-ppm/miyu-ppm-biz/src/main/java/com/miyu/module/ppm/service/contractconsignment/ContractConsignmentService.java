package com.miyu.module.ppm.service.contractconsignment;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.contractconsignment.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 外协发货 Service 接口
 *
 * @author 上海弥彧
 */
public interface ContractConsignmentService {


    /***
     * 出库
     * @param consignmentDO
     * @param detailDOS
     * @return
     */
    Boolean outBoundContractConsignment(ContractConsignmentDO consignmentDO,List<ContractConsignmentDetailDO> detailDOS);


    /***
     * 监听外协出库的状态
     */
    void checkOutBoundInfo();


    /***
     * 外协发货审批
     * @param businessKey
     * @param status
     */
    void updateContractConsignmentStatus(String businessKey, Integer status);

    /***
     * 外协退货审批
     * @param businessKey
     * @param status
     */
    void updateContractConsignmentReturnStatus(String businessKey, Integer status);


    /***
     * 验证数量是否超过限制
     * @param reqVO
     */
    void validateContractConsignment(ContractConsignmentSaveReqVO reqVO);
}
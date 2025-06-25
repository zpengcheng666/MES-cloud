package com.miyu.module.ppm.service.contractconsignmentdetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 外协发货单详情 Service 接口
 *
 * @author 上海弥彧
 */
public interface ContractConsignmentDetailService {

    /**
     * 创建外协发货单详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractConsignmentDetail(@Valid ContractConsignmentDetailSaveReqVO createReqVO);

    /**
     * 更新外协发货单详情
     *
     * @param updateReqVO 更新信息
     */
    void updateContractConsignmentDetail(@Valid ContractConsignmentDetailSaveReqVO updateReqVO);

    /**
     * 删除外协发货单详情
     *
     * @param id 编号
     */
    void deleteContractConsignmentDetail(String id);

    /**
     * 获得外协发货单详情
     *
     * @param id 编号
     * @return 外协发货单详情
     */
    ContractConsignmentDetailDO getContractConsignmentDetail(String id);

    /**
     * 获得外协发货单详情分页
     *
     * @param pageReqVO 分页查询
     * @return 外协发货单详情分页
     */
    PageResult<ContractConsignmentDetailDO> getContractConsignmentDetailPage(ContractConsignmentDetailPageReqVO pageReqVO);


    /***
     * 获取项目已经外协发货的订单
     * @param projectIds
     * @return
     */
    List<ContractConsignmentDetailDO> getContractConsignmentDetails(Collection<String> projectIds);

    List<ContractConsignmentDetailDO> getContractConsignmentDetailsByContractId(String contractId);
}
package com.miyu.module.ppm.service.warehousedetail;

import java.util.*;
import javax.validation.*;

import com.miyu.module.ppm.api.purchaseConsignment.dto.WarehouseRespDTO;
import com.miyu.module.ppm.controller.admin.warehousedetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.wms.api.materialconfig.dto.WarehouseDetailDTO;

/**
 * 入库详情表 对应仓库库存 来源WMS Service 接口
 *
 * @author 芋道源码
 */
public interface WarehouseDetailService {

    /**
     * 创建入库详情表 对应仓库库存 来源WMS
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarehouseDetail(@Valid WarehouseDetailSaveReqVO createReqVO);

    /**
     * 更新入库详情表 对应仓库库存 来源WMS
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouseDetail(@Valid WarehouseDetailSaveReqVO updateReqVO);

    /**
     * 删除入库详情表 对应仓库库存 来源WMS
     *
     * @param id 编号
     */
    void deleteWarehouseDetail(String id);

    /**
     * 获得入库详情表 对应仓库库存 来源WMS
     *
     * @param id 编号
     * @return 入库详情表 对应仓库库存 来源WMS
     */
    WarehouseDetailDO getWarehouseDetail(String id);

    /**
     * 获得入库详情表 对应仓库库存 来源WMS分页
     *
     * @param pageReqVO 分页查询
     * @return 入库详情表 对应仓库库存 来源WMS分页
     */
    PageResult<WarehouseDetailDO> getWarehouseDetailPage(WarehouseDetailPageReqVO pageReqVO);

    /**
     * 新增入库明细表数据
     */
    void addWarehouseDetail(List<WarehouseDetailDO>  warehouseDetailDO);

    /**
     * 查询退货单明细对应的入库详情
     */
    List<WarehouseDetailDO> queryWareHouseList(String consignmentReturnId);

    /**
     * 查询收货单对应的产品信息
     */
    List<WarehouseDetailDO> queryWareHouseListByConsignmentReturnNo(String consignmentReturnNo);

}
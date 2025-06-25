package com.miyu.module.wms.service.stockactive;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.mysql.stockactive.StockActiveMapper;

public interface StockActiveService {

    /**
     * 获得物料上架分页
     *
     * @param pageReqVO 分页查询
     * @return 物料库存分页
     */
    PageResult<MaterialStockDO> getOnshelfPage(MaterialStockPageReqVO pageReqVO);

    PageResult<MaterialStockDO> getOffshelfPage(MaterialStockPageReqVO pageReqVO);

    /**
     * 待签收物料签收   出库单或移库单签收
     *
     * @param signForMaterialStock 签收的物料id
     * @param signForLocationId 签收的物料的库位id
     * @return
     */
    void signForAllWaitSignForMaterial(MaterialStockDO signForMaterialStock, String signForLocationId);

    /**
     * 待出库刀具签收   出库单签收
     * @param cutterStock
     * @param locationCode
     */
    void signForAllWaitSignForCutter(MaterialStockDO cutterStock, String locationCode);

    /**
     * 物料自动签收   出库单或移库单自动签收
     *  对于不能承载托盘的库位，自动签收---只签收托盘上的物料且后续的搬运任务将继续将托盘送回默认仓库
     * @param materialStockId
     * @param locationId
     */
//    void signAheadAutoSignForMaterial(String materialStockId, String locationId);


    /**
     * 物料签入   入库单或移库单签入
     * @param checkInMaterialStock
     * @param storageCode
     */
    void checkInMaterial(MaterialStockDO checkInMaterialStock, String storageCode);

    void checkInCutter(MaterialStockDO checkInMaterialStock, String storageCode, String trayId);

    void checkInCutter(MaterialStockDO checkInMaterialStock, String storageId);

}



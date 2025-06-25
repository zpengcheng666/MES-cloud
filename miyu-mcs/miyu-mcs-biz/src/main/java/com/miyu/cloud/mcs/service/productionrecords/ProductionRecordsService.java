package com.miyu.cloud.mcs.service.productionrecords;

import javax.validation.*;
import com.miyu.cloud.mcs.controller.admin.productionrecords.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 现场作业记录 Service 接口
 *
 * @author miyu
 */
public interface ProductionRecordsService {

    /**
     * 创建现场作业记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createProductionRecords(@Valid ProductionRecordsSaveReqVO createReqVO);

    /**
     * 更新现场作业记录
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionRecords(@Valid ProductionRecordsSaveReqVO updateReqVO);

    /**
     * 删除现场作业记录
     *
     * @param id 编号
     */
    void deleteProductionRecords(String id);

    /**
     * 获得现场作业记录
     *
     * @param id 编号
     * @return 现场作业记录
     */
    ProductionRecordsDO getProductionRecords(String id);

    /**
     * 获得现场作业记录分页
     *
     * @param pageReqVO 分页查询
     * @return 现场作业记录分页
     */
    PageResult<ProductionRecordsDO> getProductionRecordsPage(ProductionRecordsPageReqVO pageReqVO);

}

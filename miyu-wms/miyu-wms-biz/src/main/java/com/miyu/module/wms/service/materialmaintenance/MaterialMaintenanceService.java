package com.miyu.module.wms.service.materialmaintenance;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.materialmaintenance.vo.*;
import com.miyu.module.wms.dal.dataobject.materialmaintenance.MaterialMaintenanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料维护记录 Service 接口
 *
 * @author QianJy
 */
public interface MaterialMaintenanceService {

    /**
     * 创建物料维护记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialMaintenance(@Valid MaterialMaintenanceSaveReqVO createReqVO);

    /**
     * 更新物料维护记录
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialMaintenance(@Valid MaterialMaintenanceSaveReqVO updateReqVO);

    /**
     * 删除物料维护记录
     *
     * @param id 编号
     */
    void deleteMaterialMaintenance(String id);

    /**
     * 获得物料维护记录
     *
     * @param id 编号
     * @return 物料维护记录
     */
    MaterialMaintenanceDO getMaterialMaintenance(String id);

    /**
     * 获得物料维护记录分页
     *
     * @param pageReqVO 分页查询
     * @return 物料维护记录分页
     */
    PageResult<MaterialMaintenanceDO> getMaterialMaintenancePage(MaterialMaintenancePageReqVO pageReqVO);


    /**
     * 生成库存维护记录
     */
    void generateMaterialMaintenance(String materialStockId, Integer type, String description);
    void generateMaterialMaintenance(String materialStockId, Integer quantity, Integer type, String description);
}
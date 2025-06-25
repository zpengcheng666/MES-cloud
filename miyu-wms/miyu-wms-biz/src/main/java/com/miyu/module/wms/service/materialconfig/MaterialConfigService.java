package com.miyu.module.wms.service.materialconfig;

import java.util.*;
import javax.validation.*;
import javax.validation.constraints.NotEmpty;

import com.miyu.module.wms.controller.admin.materialconfig.vo.*;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;

/**
 * 物料类型 Service 接口
 *
 * @author QianJy
 */
public interface MaterialConfigService {

    /**
     * 创建物料类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialConfig(@Valid MaterialConfigSaveReqVO createReqVO);

    /**
     * 更新物料类型
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialConfig(@Valid MaterialConfigSaveReqVO updateReqVO);

    /**
     * 删除物料类型
     *
     * @param id 编号
     */
    void deleteMaterialConfig(String id);

    int insert(MaterialConfigDO entity);

    int updateById(MaterialConfigDO entity);

    /**
     * 获得物料类型
     *
     * @param id 编号
     * @return 物料类型
     */
    MaterialConfigDO getMaterialConfig(String id);

    /**
     * 获得物料类型列表
     *
     * @param listReqVO 查询条件
     * @return 物料类型列表
     */
    List<MaterialConfigDO> getMaterialConfigList();

    //  分页
    PageResult<MaterialConfigDO> getMaterialConfigPage(MaterialConfigPageReqVO listReqVO);

    /**
     * 根据物料ID获取物料类型
     * @param materialStockId
     * @return
     */
    MaterialConfigDO getMaterialConfigByMaterialStockId(String materialStockId);

    /**
     * 获得物料类型集合
     * @param ids
     * @return
     */
    List<MaterialConfigDO> getMaterialConfigListByIds(Collection<String> ids);


    MaterialConfigDO getMaterialConfigByBarCode(String barCode);
}

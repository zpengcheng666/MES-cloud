package com.miyu.module.mcc.service.materialconfig;

import java.util.*;
import javax.validation.*;

import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.controller.admin.materialconfig.vo.*;
import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 物料类型 Service 接口
 *
 * @author 上海弥彧
 */
public interface MaterialConfigService {

    /**
     * 创建物料类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialConfig(@Valid MaterialConfigSaveReqVO createReqVO);
    String createMaterialConfigNoSubmit(@Valid MaterialConfigSaveReqVO createReqVO);

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

    /**
     * 获得物料类型
     *
     * @param id 编号
     * @return 物料类型
     */
    MaterialConfigDO getMaterialConfig(String id);

    /**
     * 获得物料类型分页
     *
     * @param pageReqVO 分页查询
     * @return 物料类型分页
     */
    PageResult<MaterialConfigDO> getMaterialConfigPage(MaterialConfigPageReqVO pageReqVO);


    /**
     * 物料类别ID获取物料类型集合
     * @param materialTypeId
     * @return
     */
    List<MaterialConfigDO> getMaterialConfigListByTypeId(String materialTypeId);

    /**
     * 获得物料类型集合
     * @param ids
     * @return
     */
    List<MaterialConfigDO> getMaterialConfigListByIds(Collection<String> ids);
    List<MaterialConfigDO> getMaterialConfigListByCodes(Collection<String> codes);


    List<MaterialConfigDO> getMaterialConfigListByCode(String code);

    List<MaterialConfigDO> getMaterialConfigListByTypeCode(MaterialConfigReqDTO reqDTO);
    List<MaterialConfigDO> getMaterialConfigList();

    /**
     * 物料审批
     * @param bussinessKey
     * @param status
     */
    void updateAudit(String bussinessKey, Integer status);
}

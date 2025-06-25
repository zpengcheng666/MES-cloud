package com.miyu.module.tms.service.toolconfig;

import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolconfig.vo.*;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 刀具类型 Service 接口
 *
 * @author QianJy
 */
public interface ToolConfigService {

    /**
     * 创建刀具类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolConfig(@Valid ToolConfigSaveReqVO createReqVO);

    /**
     * 更新刀具类型
     *
     * @param updateReqVO 更新信息
     */
    void updateToolConfig(@Valid ToolConfigSaveReqVO updateReqVO);

    /**
     * 删除刀具类型
     *
     * @param id 编号
     */
    void deleteToolConfig(String id);

    /**
     * 获得刀具类型
     *
     * @param id 编号
     * @return 刀具类型
     */
    ToolConfigDO getToolConfig(String id);

    /**
     * 获得刀具类型分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具类型分页
     */
    PageResult<ToolConfigDO> getToolConfigPage(ToolConfigPageReqVO pageReqVO);

    /**
     * 物料类型ID获取刀具类型
     * @param materialConfigId
     * @return
     */
    ToolConfigDO getToolConfigByMaterialConfigId(String materialConfigId);

    /**
     * 获得适配刀具类型分页
     * @param pageReqVO
     * @return
     */
    PageResult<ToolConfigDO> getFitToolConfigPageByType(ToolConfigPageReqVO pageReqVO);

    /**
     * 获得刀具类型分页
     * @param pageReqVO
     * @return
     */
    PageResult<ToolConfigDO> getToolConfigPageByType(ToolConfigPageReqVO pageReqVO);
}

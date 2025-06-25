package com.miyu.module.tms.service.toolconfigparameter;

import java.util.*;
import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolconfigparameter.vo.*;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 刀具参数信息 Service 接口
 *
 * @author 上海弥彧
 */
public interface ToolConfigParameterService {

    /**
     * 创建刀具参数信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolConfigParameter(@Valid ToolConfigParameterSaveReqVO createReqVO);

    /**
     * 更新刀具参数信息
     *
     * @param updateReqVO 更新信息
     */
    void updateToolConfigParameter(@Valid ToolConfigParameterSaveReqVO updateReqVO);

    /**
     * 删除刀具参数信息
     *
     * @param id 编号
     */
    void deleteToolConfigParameter(Long id);

    /**
     * 获得刀具参数信息
     *
     * @param id 编号
     * @return 刀具参数信息
     */
    ToolConfigParameterDO getToolConfigParameter(Long id);

    /**
     * 获得刀具参数信息分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具参数信息分页
     */
    PageResult<ToolConfigParameterDO> getToolConfigParameterPage(ToolConfigParameterPageReqVO pageReqVO);

}

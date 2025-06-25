package com.miyu.module.tms.service.toolgroup;

import java.util.*;
import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolgroup.vo.*;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailPageReqVO;
import com.miyu.module.tms.dal.dataobject.toolgroup.ToolGroupDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;

/**
 * 刀具组装 Service 接口
 *
 * @author zhangyunfei
 */
public interface ToolGroupService {

    /**
     * 创建刀具组装
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolGroup(@Valid ToolGroupSaveReqVO createReqVO);

    /**
     * 更新刀具组装
     *
     * @param updateReqVO 更新信息
     */
    void updateToolGroup(@Valid ToolGroupSaveReqVO updateReqVO);

    /**
     * 删除刀具组装
     *
     * @param id 编号
     */
    void deleteToolGroup(String id);

    /**
     * 获得刀具组装
     *
     * @param id 编号
     * @return 刀具组装
     */
    ToolGroupDO getToolGroup(String id);

    /**
     * 获得刀具组装分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具组装分页
     */
    PageResult<ToolGroupDO> getToolGroupPage(ToolGroupPageReqVO pageReqVO);

    /**
     * 获得刀具组装列表
     *
     * @param pageReqVO
     * @return
     */
    List<ToolGroupDetailDO> getToolGroupDetailList(ToolGroupDetailPageReqVO pageReqVO);

    /**
     * 刀具组装id查询刀具组装
     * @param id
     * @return
     */
    ToolGroupDO getToolGroupById(String id);

    /**
     * 成品刀具类型id获取刀具组装
     * @param mainConfigId
     * @return
     */
    List<ToolGroupDO> getGroupByMainConfigId(String mainConfigId);
}

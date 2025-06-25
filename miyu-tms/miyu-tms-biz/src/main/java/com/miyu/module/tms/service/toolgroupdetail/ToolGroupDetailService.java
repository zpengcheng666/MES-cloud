package com.miyu.module.tms.service.toolgroupdetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.*;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 刀具组装 Service 接口
 *
 * @author zhangyunfei
 */
public interface ToolGroupDetailService {

    /**
     * 创建刀具组装
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolGroupDetail(@Valid ToolGroupDetailSaveReqVO createReqVO);

    /**
     * 更新刀具组装
     *
     * @param updateReqVO 更新信息
     */
    void updateToolGroupDetail(@Valid ToolGroupDetailSaveReqVO updateReqVO);

    /**
     * 删除刀具组装
     *
     * @param id 编号
     */
    void deleteToolGroupDetail(String id);

    /**
     * 获得刀具组装
     *
     * @param id 编号
     * @return 刀具组装
     */
    ToolGroupDetailDO getToolGroupDetail(String id);

    /**
     * 获得刀具组装分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具组装分页
     */
    PageResult<ToolGroupDetailDO> getToolGroupDetailPage(ToolGroupDetailPageReqVO pageReqVO);

    /**
     * 刀具组装id获取刀具组装详情
     * @param groupId
     * @return
     */
    List<ToolGroupDetailDO> getToolGroupDetailListByGroupId(String groupId);
}

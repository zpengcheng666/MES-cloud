package com.miyu.module.pdm.service.toolingApply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingApplyReqVO;
import com.miyu.module.pdm.dal.dataobject.toolingApply.ToolingApplyDO;

import javax.validation.Valid;
import java.util.List;


public interface ToolingApplyService {
    PageResult<ToolingApplyDO> getToolingApplyPage(ToolingApplyReqVO reqVO);

    List<ToolingApplyDO> getToolingApplyList(ToolingApplyReqVO reqVO);

    ToolingApplyReqVO getToolingApply(String id);

    String createToolingApply(@Valid ToolingApplyReqVO createReqVO);

    void deleteToolingApply(String id);

    void updateToolingApply(@Valid ToolingApplyReqVO updateReqVO);

    void startApplyInstance(@Valid ToolingApplyReqVO updateReqVO);

    void updateApplyInstanceStatus(String id,Integer status);

}

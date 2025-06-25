package com.miyu.module.pdm.service.toolingApply;



import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailReqVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailTreeRespVO;

import java.util.List;

public interface ToolingDetailService {

    List<ToolingDetailTreeRespVO> getTreeList(ToolingDetailReqVO reqVO);
}

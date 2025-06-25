package com.miyu.module.pdm.service.part;

import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;

import java.util.List;

public interface NewPartService {

    List<NewPartRespVO> getTreeList(NewPartReqVO reqVO);
}

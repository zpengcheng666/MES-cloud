package com.miyu.module.pdm.service.processTask;

import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessDetailReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessDetailRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.FeasibilityResultDO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.QuotaPerPartDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessDetailDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

public interface ProcessDetailService {
    List<ProjPartBomRespVO> getProjPartBomListByProjectCode(ProjPartBomReqVO reqVO);
    List<ProjPartBomTreeRespVO> getProjPartBomTreeList(ProjPartBomReqVO reqVO);
    List<ProjPartBomTreeRespVO> getProjPartBomTreeByPartVersionId(ProjPartBomReqVO reqVO);
    ProjPartBomRespVO getPartDetailByTaskId(String id);

    /**
     * 保存选中资源信息
     */
    void saveSelectedResource(ResourceSelectedReqVO reqVO);

    /**
     * 删除选中资源信息
     */
    void deleteSelectedDevice(ProcessDetailReqVO reqVO);

    List<ProcessDetailRespVO> getResourceListByPart(ResourceSelectedReqVO reqVO);

    FeasibilityResultDO getFeasibilityResult(FeasibilityResultReqVO reqVO);

    void updateProcessTaskStatus(@Valid ProcessTaskReqVO updateReqVO);

    QuotaPerPartDO getQuotaPerPart(QuotaPerPartReqVO reqVO);

    List<ProjPartBomTreeRespVO> getProjPartBomTreeListNew(ProjPartBomReqVO reqVO);
    List<ProjPartBomTreeRespVO> getProcessListByProjectCodes(Collection<String> projectCodes);
    ProjPartBomRespVO getPartDetailByTaskIdNew(String id);

    List<ProjPartBomRespVO> getPartDetailNewHome(ProjPartBomReqVO reqVO);
}

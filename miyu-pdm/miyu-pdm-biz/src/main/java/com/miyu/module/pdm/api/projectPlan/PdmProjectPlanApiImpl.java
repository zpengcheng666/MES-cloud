package com.miyu.module.pdm.api.projectPlan;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.api.projectAssessment.dto.CombinationRespDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProcedureDetailRespDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomReqDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureDetailRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.material.MaterialDO;
import com.miyu.module.pdm.service.material.MaterialService;
import com.miyu.module.pdm.service.processPlanDetail.ProcessPlanDetailService;
import com.miyu.module.pdm.service.processTask.ProcessDetailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class PdmProjectPlanApiImpl implements PdmProjectPlanApi {
    @Resource
    private ProcessDetailService processDetailService;

    @Resource
    private ProcessPlanDetailService processPlanDetailService;

    @Resource
    private MaterialService materialService;

    /**
     * 获取工艺编程树
     * @param req
     * @return
     */
    @Override
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomPlanList(ProjPartBomReqDTO req) {
        //ProjPartBomReqVO projPartBomReqVO = new ProjPartBomReqVO();
        ProjPartBomReqVO projPartBomReqVO = BeanUtils.toBean(req, ProjPartBomReqVO.class);
        //List<ProjPartBomTreeRespVO> projPartBomTreeList = processDetailService.getProjPartBomTreeList(projPartBomReqVO);
        //现在使用新版
        List<ProjPartBomTreeRespVO> projPartBomTreeListNew = processDetailService.getProjPartBomTreeListNew(projPartBomReqVO);
        List<ProjPartBomTreeRespDTO> projPartBomTreeRespDTOS = BeanUtils.toBean(projPartBomTreeListNew, ProjPartBomTreeRespDTO.class);
//        List<ProjPartBomTreeRespDTO> projPartBomTreeRespDTOS2 = projPartBomTreeRespDTOS.stream().filter(item -> item.getStatus().equals("5")).collect(Collectors.toList());
        return success(projPartBomTreeRespDTOS);
    }

    /**
     * 获取工艺详细设计结构树
     * @param projectCode
     * @param viewSelf
     * @return
     */
    @Override
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomProcessDetailDesignList(String projectCode, boolean viewSelf) {
        ProjPartBomReqVO projPartBomReqVO = new ProjPartBomReqVO();
        projPartBomReqVO.setProjectCode(projectCode);
        projPartBomReqVO.setViewSelf(viewSelf);
        List<ProjPartBomTreeRespVO> list = processPlanDetailService.getProjPartBomTreeList(projPartBomReqVO);
        List<ProjPartBomTreeRespDTO> projPartBomTreeRespDTOS = BeanUtils.toBean(list, ProjPartBomTreeRespDTO.class);
        return success(projPartBomTreeRespDTOS);
    }

    @Override
    public CommonResult<List<ProcedureDetailRespDTO>> getResourceListByProcedure(String processVersionId, String procedureId, String partVersionId) {
        ResourceSelectedReqVO reqVO = new ResourceSelectedReqVO();
        reqVO.setProcessVersionId(processVersionId).setProcedureId(procedureId).setPartVersionId(partVersionId);
        List<ProcedureDetailRespVO> list = processPlanDetailService.getResourceListByProcedure(reqVO);
        List<ProcedureDetailRespDTO> procedureDetailRespDTOS = BeanUtils.toBean(list, ProcedureDetailRespDTO.class);
        return success(procedureDetailRespDTOS);
    }

    @Override
    public CommonResult<List<CombinationRespDTO>> getResourceCombinationListByIds(List<String> ids) {
        List<MaterialDO> list = materialService.getMaterialListByMaterialIds(ids);
        return success(BeanUtils.toBean(list, CombinationRespDTO.class));
    }

    @Override
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomTreeListNew(String projectCode, String partNumber) {
        ProjPartBomReqVO reqVO = new ProjPartBomReqVO();
        reqVO.setProjectCode(projectCode);
        reqVO.setPartNumber(partNumber);
        List<ProjPartBomTreeRespVO> list = processDetailService.getProjPartBomTreeListNew(reqVO);
        List<ProjPartBomTreeRespDTO> projPartBomTreeRespDTOS = BeanUtils.toBean(list, ProjPartBomTreeRespDTO.class);
        return success(projPartBomTreeRespDTOS);
    }

    @Override
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProcessListByProjectCodes(List<String> projectCodes) {
        List<ProjPartBomTreeRespVO> list = processDetailService.getProcessListByProjectCodes(projectCodes);
        List<ProjPartBomTreeRespDTO> projPartBomTreeRespDTOS = BeanUtils.toBean(list, ProjPartBomTreeRespDTO.class);
        return success(projPartBomTreeRespDTOS);
    }
}

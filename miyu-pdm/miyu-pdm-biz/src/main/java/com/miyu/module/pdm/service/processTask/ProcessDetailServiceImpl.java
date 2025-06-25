package com.miyu.module.pdm.service.processTask;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessDetailReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessDetailRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.FeasibilityResultDO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.QuotaPerPartDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessDetailDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.dal.mysql.feasibilityDetail.FeasibilityResultMapper;
import com.miyu.module.pdm.dal.mysql.feasibilityDetail.QuotaPerPartMapper;
import com.miyu.module.pdm.dal.mysql.processDetail.ProcessDetailMapper;
import com.miyu.module.pdm.dal.mysql.processTask.ProcessTaskMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessDetailServiceImpl implements ProcessDetailService {
    @Resource
    private ProcessDetailMapper processDetailMapper;
    @Resource
    private FeasibilityResultMapper feasibilityResultMapper;
    @Resource
    private ProcessTaskMapper processTaskMapper;
    @Resource
    private QuotaPerPartMapper quotaPerPartMapper;
    @Override
    public List<ProjPartBomRespVO> getProjPartBomListByProjectCode(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = "";
        if (reqVO.getViewSelf()) {//只看我的
            reviewedBy = loginUser.getId().toString();
        }
        return processDetailMapper.selectPartList(projectCode, partNumber, status, reviewedBy);
    }
    @Override
    public List<ProjPartBomTreeRespVO> getProjPartBomTreeList(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = "";
        if (reqVO.getViewSelf() != null && reqVO.getViewSelf()) {//只看我的
            reviewedBy = loginUser.getId().toString();
        }
        return processDetailMapper.selectPartTreeList(projectCode, partNumber, status, reviewedBy);
    }
    @Override
    public List<ProjPartBomTreeRespVO> getProjPartBomTreeByPartVersionId(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partVersionId = reqVO.getPartVersionId();
        return processDetailMapper.selectPartTreeListByPartVersionId(projectCode, partVersionId);
    }
    @Override
    public ProjPartBomRespVO getPartDetailByTaskId(String id) {
        return processDetailMapper.selectPartDetail(id);
    }

    @Override
    public void saveSelectedResource(ResourceSelectedReqVO reqVO) {
        processDetailMapper.deleteByProjectCode(reqVO);
        List<String> ids = reqVO.getIds();
        ids.forEach(id -> {
            ProcessDetailDO detailDO = BeanUtils.toBean(reqVO, ProcessDetailDO.class)
                    .setId(IdUtil.fastSimpleUUID())
                    .setResourcesTypeId(id)

                    ;
            processDetailMapper.insert(detailDO);
        });
    }

    @Override
    public void deleteSelectedDevice(ProcessDetailReqVO reqVO) {
        processDetailMapper.deleteByResourceId(reqVO);
    }

    @Override
    public List<ProcessDetailRespVO> getResourceListByPart(ResourceSelectedReqVO reqVO) {
        List<ProcessDetailDO> list = processDetailMapper.selectResourceList(reqVO);
        return BeanUtils.toBean(list, ProcessDetailRespVO.class);
    }

    @Override
    public FeasibilityResultDO getFeasibilityResult(FeasibilityResultReqVO reqVO) {
        return feasibilityResultMapper.selectFeasibilityResult(reqVO);
    }

    @Override
    public void updateProcessTaskStatus(ProcessTaskReqVO updateReqVO) {
        LambdaUpdateWrapper<ProcessTaskDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ProcessTaskDO::getStatus, updateReqVO.getStatus());
        updateWrapper.eq(ProcessTaskDO::getProjectCode, updateReqVO.getProjectCode());
        updateWrapper.eq(ProcessTaskDO::getPartVersionId, updateReqVO.getPartVersionId());
        processTaskMapper.update(updateWrapper);
    }

    @Override
    public QuotaPerPartDO getQuotaPerPart(QuotaPerPartReqVO reqVO) {
        return quotaPerPartMapper.selectQuotaPerPart(reqVO);
    }

    @Override
    public List<ProjPartBomTreeRespVO> getProjPartBomTreeListNew(ProjPartBomReqVO reqVO) {
        String projectCode = reqVO.getProjectCode();
        String partNumber = reqVO.getPartNumber();
        Integer status = reqVO.getStatus();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = "";
        if (reqVO.getViewSelf() != null && reqVO.getViewSelf()) {//只看我的
            reviewedBy = loginUser.getId().toString();
        }
        //重新组织结构树，先取一二级节点(零件+工艺规程节点)
        List<ProjPartBomTreeRespVO> listAll = processDetailMapper.selectPartTreeListNew(projectCode, partNumber, status, reviewedBy, reqVO.getProjectStatus(), reqVO.getPartVersionId());
        //过滤工艺规程节点(去重)，一层层处理工序及工步节点
        List<ProjPartBomTreeRespVO> processList = listAll.stream().filter(item -> item.getType() == 2).collect(Collectors.toList());
        List<String> processVersionIds = processList.stream().map(ProjPartBomTreeRespVO::getId).distinct().collect(Collectors.toList());
        if(processVersionIds != null && processVersionIds.size() > 0) {
            for(String processVersionId : processVersionIds) {
                List<ProjPartBomTreeRespVO> procedureList = processDetailMapper.selectProcedureListByProcessVersionId(processVersionId);
                if(procedureList != null && procedureList.size() > 0) {
                    for(ProjPartBomTreeRespVO procedure : procedureList) {
                        List<ProjPartBomTreeRespVO> stepList = processDetailMapper.selectStepListByProcedureId(procedure.getId());
                        if(stepList != null && stepList.size() > 0) {
                            listAll.addAll(stepList);
                        }
                    }
                    listAll.addAll(procedureList);
                }
            }
        }
        return listAll;
    }

    @Override
    public List<ProjPartBomTreeRespVO> getProcessListByProjectCodes(Collection<String> projectCodes) {
        if (CollUtil.isEmpty(projectCodes)) {
            return Collections.emptyList();
        }
        String projectCodeStr = "";
        for(String projectCode : projectCodes) {
            projectCodeStr += "'"+ projectCode+"'" + ",";
        }
        if(projectCodeStr.endsWith(",")) {
            projectCodeStr = projectCodeStr.substring(0, projectCodeStr.length() - 1);
        }
        List<ProjPartBomTreeRespVO> listAll = processDetailMapper.selectProcessListByProjectCodes(projectCodeStr);
        return listAll;
    }

    @Override
    public ProjPartBomRespVO getPartDetailByTaskIdNew(String id) {
        return processDetailMapper.selectPartDetailNew(id);
    }


    @Override
    public List<ProjPartBomRespVO> getPartDetailNewHome(ProjPartBomReqVO reqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        String reviewedBy = loginUser.getId().toString();
        return processDetailMapper.selectPartDetailNewHome(reviewedBy);
    }
}

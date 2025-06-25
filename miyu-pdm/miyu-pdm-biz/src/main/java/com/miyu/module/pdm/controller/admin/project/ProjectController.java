package com.miyu.module.pdm.controller.admin.project;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import com.miyu.module.pdm.controller.admin.project.vo.ProjectListReqVO;
import com.miyu.module.pdm.controller.admin.project.vo.ProjectRespVO;
import com.miyu.module.pdm.dal.dataobject.project.ProjectDO;
import com.miyu.module.pdm.service.project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "管理后台 - PDM 项目")
@RestController
@RequestMapping("/pdm/project")
@Validated
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @Resource
    private PmsApi pmsApi;

    @GetMapping("/list1")
    @Operation(summary = "获得项目列表")
    public CommonResult<List<ProjectRespVO>> getProjectList1(@Valid ProjectListReqVO listReqVO) {
        List<ProjectDO> list = projectService.getProjectList(listReqVO);
        //重新组装项目名
        return success(convertList(list, project -> new ProjectRespVO()
                .setId(project.getId().toString()).setCode(project.getCode()).setName(project.getCode()+"("+project.getName()+")")));
    }

    @GetMapping("/list")
    @Operation(summary = "获得项目列表")
    public CommonResult<List<ProjectRespVO>> getProjectList(@Valid ProjectListReqVO listReqVO) {
        String projectCode = "";
        if(listReqVO.getCode() != null) {
            projectCode = listReqVO.getCode();
        }
        List<ProjectRespVO> projectRespVOList = new ArrayList<>();
        CommonResult<List<PmsApprovalDto>> projectList = pmsApi.getApprovalList(projectCode);
        for(PmsApprovalDto approval : projectList.getData()) {
            ProjectRespVO projectRespVO = new ProjectRespVO();
            projectRespVO.setId(approval.getId());
            projectRespVO.setCode(approval.getProjectCode());
            //重新组装项目名
            projectRespVO.setName(approval.getProjectCode()+"("+approval.getProjectName()+")");
            projectRespVOList.add(projectRespVO);
        }
        return success(projectRespVOList);
    }
}

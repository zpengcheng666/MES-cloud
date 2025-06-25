package com.miyu.module.pdm.service.project;

import com.miyu.module.pdm.controller.admin.project.vo.ProjectListReqVO;
import com.miyu.module.pdm.dal.dataobject.project.ProjectDO;

import java.util.List;

/**
 * PDM 项目 Service 接口
 *
 * @author 芋道源码
 */
public interface ProjectService {

    /**
     * 获得项目列表
     *
     * @param listReqVO 查询条件
     * @return 项目列表
     */
    List<ProjectDO> getProjectList(ProjectListReqVO listReqVO);
}

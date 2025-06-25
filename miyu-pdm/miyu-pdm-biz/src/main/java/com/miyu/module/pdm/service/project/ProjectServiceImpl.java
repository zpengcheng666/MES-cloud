package com.miyu.module.pdm.service.project;

import com.miyu.module.pdm.controller.admin.project.vo.ProjectListReqVO;
import com.miyu.module.pdm.dal.dataobject.project.ProjectDO;
import com.miyu.module.pdm.dal.mysql.project.ProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * PDM 产品分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public List<ProjectDO> getProjectList(ProjectListReqVO listReqVO) {
        return projectMapper.selectList(listReqVO);
    }
}

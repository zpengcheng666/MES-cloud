package com.miyu.module.pdm.dal.mysql.project;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.project.vo.ProjectListReqVO;
import com.miyu.module.pdm.dal.dataobject.project.ProjectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * PDM 项目 Mapper
 *
 * @author liuy
 */
@Mapper
public interface ProjectMapper extends BaseMapperX<ProjectDO> {

    default List<ProjectDO> selectList(ProjectListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProjectDO>()
                .likeIfPresent(ProjectDO::getCode, reqVO.getCode())
                .likeIfPresent(ProjectDO::getName, reqVO.getName())
                .orderByDesc(ProjectDO::getId));
    }
}

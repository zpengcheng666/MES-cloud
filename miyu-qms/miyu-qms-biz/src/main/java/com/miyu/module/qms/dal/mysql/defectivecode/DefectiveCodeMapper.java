package com.miyu.module.qms.dal.mysql.defectivecode;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.defectivecode.DefectiveCodeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.defectivecode.vo.*;

/**
 * 缺陷代码 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface DefectiveCodeMapper extends BaseMapperX<DefectiveCodeDO> {

    default PageResult<DefectiveCodeDO> selectPage(DefectiveCodePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DefectiveCodeDO>()
                .betweenIfPresent(DefectiveCodeDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(DefectiveCodeDO::getName, reqVO.getName())
                .eqIfPresent(DefectiveCodeDO::getCode, reqVO.getCode())
                .orderByDesc(DefectiveCodeDO::getId));
    }

}
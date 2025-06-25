package com.miyu.cloud.macs.dal.mysql.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.controller.admin.file.vo.MacsFilePageReqVO;
import com.miyu.cloud.macs.dal.dataobject.file.MacsFileDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件 Mapper
 *
 * @author miyu
 */
@Mapper
public interface MacsFileMapper extends BaseMapperX<MacsFileDO> {

    default PageResult<MacsFileDO> selectPage(MacsFilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MacsFileDO>()
                .likeIfPresent(MacsFileDO::getName, reqVO.getName())
                .eqIfPresent(MacsFileDO::getPath, reqVO.getPath())
                .eqIfPresent(MacsFileDO::getType, reqVO.getType())
                .eqIfPresent(MacsFileDO::getSize, reqVO.getSize())
                .eqIfPresent(MacsFileDO::getUserId, reqVO.getUserId())
                .eqIfPresent(MacsFileDO::getVisitorId, reqVO.getVisitorId())
                .orderByDesc(MacsFileDO::getId));
    }

}

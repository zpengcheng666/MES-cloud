package com.miyu.module.pdm.dal.mysql.part;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.part.vo.PartInstanceListReqVO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



@Mapper
public interface PartInstanceMapper extends BaseMapperX<PartInstanceDO> {

    default List<PartInstanceDO> selectList(PartInstanceListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PartInstanceDO>()
                .eqIfPresent(PartInstanceDO::getName, reqVO.getName())
                .eqIfPresent(PartInstanceDO::getRootproductId, reqVO.getRootproductId())
                .eqIfPresent(PartInstanceDO::getPartVersionId, reqVO.getPartVersionId())
                .eqIfPresent(PartInstanceDO::getParentId, reqVO.getParentId())
                .eqIfPresent(PartInstanceDO::getCustomizedIndex, reqVO.getCustomizedIndex())
                .eqIfPresent(PartInstanceDO::getSerialNumber, reqVO.getSerialNumber())
                .eqIfPresent(PartInstanceDO::getVmatrix01, reqVO.getVmatrix01())
                .eqIfPresent(PartInstanceDO::getVmatrix02, reqVO.getVmatrix02())
                .eqIfPresent(PartInstanceDO::getVmatrix03, reqVO.getVmatrix03())
                .eqIfPresent(PartInstanceDO::getVmatrix04, reqVO.getVmatrix04())
                .eqIfPresent(PartInstanceDO::getVmatrix05, reqVO.getVmatrix05())
                .eqIfPresent(PartInstanceDO::getVmatrix06, reqVO.getVmatrix06())
                .eqIfPresent(PartInstanceDO::getVmatrix07, reqVO.getVmatrix07())
                .eqIfPresent(PartInstanceDO::getVmatrix08, reqVO.getVmatrix08())
                .eqIfPresent(PartInstanceDO::getVmatrix09, reqVO.getVmatrix09())
                .eqIfPresent(PartInstanceDO::getVmatrix10, reqVO.getVmatrix10())
                .eqIfPresent(PartInstanceDO::getVmatrix11, reqVO.getVmatrix11())
                .eqIfPresent(PartInstanceDO::getVmatrix12, reqVO.getVmatrix12())
                .eqIfPresent(PartInstanceDO::getType, reqVO.getType())
                .eqIfPresent(PartInstanceDO::getTargetId, reqVO.getTargetId())
                .orderByDesc(PartInstanceDO::getId));
    }
}

//    default PartInstanceDO selectByParentIdAndInstanceNumber(Long parentId, String instanceNumber) {
//        return selectOne(PartInstanceDO::getParentId, parentId, PartInstanceDO::getInstanceNumber, instanceNumber);
//    }
//
//    default Long selectCountByParentId(String parentId) {
//        return selectCount(PartInstanceDO::getParentId, parentId);
//    }
//
//}
package com.miyu.module.pdm.dal.mysql.dataStatistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsPageReqVO;
import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataStatisticsMapper extends BaseMapperX<DataStatisticsDO> {
    default PageResult<DataStatisticsDO> selectPage(DataStatisticsPageReqVO reqVO) {
        return selectPage(reqVO,new LambdaQueryWrapperX<DataStatisticsDO>()
                //如果reqvo.getname 不为空，则模糊查询
                .likeIfPresent(DataStatisticsDO::getName, reqVO.getName())
                //下面的一系列的调用的含义是 对于每一个属性，如果reqvo中相应的字段为空，
                // 则添加一个等于(精确匹配)的条件到查询中，涉及到的字段则是以下eq的字段
                    .likeIfPresent(DataStatisticsDO::getName, reqVO.getName())
                    .eqIfPresent(DataStatisticsDO::getSize, reqVO.getSize())
                    .eqIfPresent(DataStatisticsDO::getType, reqVO.getType())
                    .eqIfPresent(DataStatisticsDO::getMd5, reqVO.getMd5())
                    .eqIfPresent(DataStatisticsDO::getVaultUrl, reqVO.getVaultUrl())
                    .eqIfPresent(DataStatisticsDO::getStatus, reqVO.getStatus())
                    .eqIfPresent(DataStatisticsDO::getProductNumber, reqVO.getRootProductId())
                    .eqIfPresent(DataStatisticsDO::getProjectCode, reqVO.getProjectCode())
                    .eqIfPresent(DataStatisticsDO::getCompanyId, reqVO.getCompanyId())
                    .likeIfPresent(DataStatisticsDO::getProjectName, reqVO.getProjectName())
                    .likeIfPresent(DataStatisticsDO::getReceiveCode, reqVO.getReceiveCode())
                    .eqIfPresent(DataStatisticsDO::getStructureId, reqVO.getStructureId())
                    .orderByDesc(DataStatisticsDO::getId));
        }

    default DataStatisticsDO selectByName(String name) {
        return selectOne(DataStatisticsDO::getName, name);
    }

    default List<DataStatisticsDO> selectList(DataStatisticsPageReqVO reqVO) {
        LambdaQueryWrapperX<DataStatisticsDO> queryWrapper = new LambdaQueryWrapperX<DataStatisticsDO>()
                .likeIfPresent(DataStatisticsDO::getReceiveCode, reqVO.getReceiveCode())
                .orderByDesc(DataStatisticsDO::getReceiveCode);
        return selectList(queryWrapper);
    }
}


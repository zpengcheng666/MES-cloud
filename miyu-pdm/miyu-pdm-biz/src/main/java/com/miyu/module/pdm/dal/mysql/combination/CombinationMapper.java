package com.miyu.module.pdm.dal.mysql.combination;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.combination.vo.CombinationListReqVO;
import com.miyu.module.pdm.dal.dataobject.combination.CombinationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * PDM 刀组-临时 Mapper
 *
 * @author liuy
 */
@Mapper
public interface CombinationMapper extends BaseMapperX<CombinationDO> {

    default List<CombinationDO> selectList(CombinationListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CombinationDO>()
                .likeIfPresent(CombinationDO::getCutterGroupCode, reqVO.getCutterGroupCode())
                .likeIfPresent(CombinationDO::getTaperTypeName, reqVO.getTaperTypeName())
                .likeIfPresent(CombinationDO::getHiltMark, reqVO.getHiltMark())
                .orderByDesc(CombinationDO::getId));
    }

    default List<CombinationDO> selectListByCombinationIds(Collection<String> combinationIds) {
        return selectList(CombinationDO::getId, combinationIds);
    }

}

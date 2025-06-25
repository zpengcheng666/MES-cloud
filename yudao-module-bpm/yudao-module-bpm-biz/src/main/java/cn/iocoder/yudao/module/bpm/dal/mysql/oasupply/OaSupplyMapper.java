package cn.iocoder.yudao.module.bpm.dal.mysql.oasupply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * OA 物品领用 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OaSupplyMapper extends BaseMapperX<OaSupplyDO> {

    default PageResult<OaSupplyDO> selectPage(OaSupplyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaSupplyDO>()
                .eqIfPresent(OaSupplyDO::getDept, reqVO.getDept())
                .eqIfPresent(OaSupplyDO::getApplicant, reqVO.getApplicant())
                .eqIfPresent(OaSupplyDO::getLeader, reqVO.getLeader())
                .eqIfPresent(OaSupplyDO::getWarehouseman, reqVO.getWarehouseman())
                .eqIfPresent(OaSupplyDO::getReason, reqVO.getReason())
                .eqIfPresent(OaSupplyDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaSupplyDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(OaSupplyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaSupplyDO::getId));
    }

}

package com.miyu.cloud.dms.dal.mysql.ledger;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToLocationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备台账 Mapper
 *
 * @author miyu
 */
@Mapper
public interface LedgerMapper extends BaseMapperX<LedgerDO> {

    default PageResult<LedgerDO> selectPage(LedgerPageReqVO reqVO) {
        LambdaQueryWrapperX<LedgerDO> queryWrapper = new LambdaQueryWrapperX<LedgerDO>()
                .eqIfPresent(LedgerDO::getCode, reqVO.getCode())
                .likeIfPresent(LedgerDO::getName, reqVO.getName())
                .eqIfPresent(LedgerDO::getEquipmentStationType, reqVO.getEquipmentStationType())
                .eqIfPresent(LedgerDO::getType, reqVO.getType())
                .eqIfPresent(LedgerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(LedgerDO::getRunStatus, reqVO.getRunStatus())
                .eqIfPresent(LedgerDO::getOnlineStatus, reqVO.getOnlineStatus())
                .eqIfPresent(LedgerDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(LedgerDO::getIp, reqVO.getIp())
                .eqIfPresent(LedgerDO::getBindEquipment, reqVO.getBindEquipment())
                .likeIfPresent(LedgerDO::getSuperintendent, reqVO.getSuperintendent())
                .betweenIfPresent(LedgerDO::getPurchaseDate, reqVO.getPurchaseDate())
                .betweenIfPresent(LedgerDO::getMaintenanceDate, reqVO.getMaintenanceDate())
                .eqIfPresent(LedgerDO::getMaintenanceBy, reqVO.getMaintenanceBy())
                .betweenIfPresent(LedgerDO::getInspectionDate, reqVO.getInspectionDate())
                .eqIfPresent(LedgerDO::getInspectionBy, reqVO.getInspectionBy())
                .likeIfPresent(LedgerDO::getTechnicalParameter1, reqVO.getTechnicalParameter1())
                .likeIfPresent(LedgerDO::getTechnicalParameter2, reqVO.getTechnicalParameter2())
                .likeIfPresent(LedgerDO::getTechnicalParameter3, reqVO.getTechnicalParameter3())
                .likeIfPresent(LedgerDO::getTechnicalParameter4, reqVO.getTechnicalParameter4())
                .betweenIfPresent(LedgerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(LedgerDO::getCreateTime);

        if (reqVO.getLintStationGroup() != null) {
            queryWrapper.eqIfPresent(LedgerDO::getLintStationGroup, reqVO.getLintStationGroup());
        } else {
            queryWrapper.isNull(LedgerDO::getLintStationGroup);
        }

        return selectPage(reqVO, queryWrapper);
    }

    default List<LedgerDO> getLedgerListByDeviceType(String id){
        return selectList(new LambdaQueryWrapperX<LedgerDO>()
                .eq(LedgerDO::getEquipmentStationType, id));
    }

    default List<LedgerDO> getEgLedger(String id){
        return selectList(new LambdaQueryWrapperX<LedgerDO>()
                .eq(LedgerDO::getEquipmentStationType, id));
    }

    default List<LedgerDO> selectDeviceCodeByLocationId(String locationId){
        MPJLambdaWrapperX<LedgerDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(LedgerToLocationDO.class, LedgerToLocationDO::getLedger, LedgerDO::getId)
                .eq(LedgerDO::getLocationId, locationId)
                .selectAll(LedgerDO.class);
        return selectList(wrapperX);
    }
}

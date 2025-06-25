package com.miyu.module.qms.dal.mysql.inspectionsheetrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetrecord.InspectionSheetRecordDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.enums.InspectionSheetSchemeMaterialStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo.*;
import org.springframework.util.StringUtils;

/**
 * 检验记录 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface InspectionSheetRecordMapper extends BaseMapperX<InspectionSheetRecordDO> {

    default PageResult<InspectionSheetRecordDO> selectPage(InspectionSheetRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionSheetRecordDO>()
                .betweenIfPresent(InspectionSheetRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionSheetRecordDO::getInspectionSchemeItemId, reqVO.getInspectionSchemeItemId())
                .eqIfPresent(InspectionSheetRecordDO::getContent, reqVO.getContent())
                .eqIfPresent(InspectionSheetRecordDO::getInspectionResult, reqVO.getInspectionResult())
                .eqIfPresent(InspectionSheetRecordDO::getSchemeMaterialId, reqVO.getSchemeMaterialId())
                .orderByDesc(InspectionSheetRecordDO::getId));
    }

    default List<InspectionSheetRecordDO> selectListByMaterialId(String materialId) {

        MPJLambdaWrapperX<InspectionSheetRecordDO> wrapperX = new MPJLambdaWrapperX<>();

        wrapperX.leftJoin(InspectionSchemeItemDO.class, InspectionSchemeItemDO::getId, InspectionSheetRecordDO::getInspectionSchemeItemId)
                .leftJoin(InspectionItemDO.class, InspectionItemDO::getId, InspectionSchemeItemDO::getInspectionItemId)
                .leftJoin(InspectionItemTypeDO.class, InspectionItemTypeDO::getId, InspectionItemDO::getItemTypeId)
                .selectAs(InspectionItemDO::getItemName, InspectionSheetRecordDO::getInspectionSchemeItemName)
                .selectAs(InspectionItemTypeDO::getName, InspectionSheetRecordDO::getInspectionItemTypeName)
                .selectAs(InspectionSchemeItemDO::getReferenceType, InspectionSheetRecordDO::getReferenceType)
                .selectAs(InspectionSchemeItemDO::getMaxValue, InspectionSheetRecordDO::getSchemeMaxValue)
                .selectAs(InspectionSchemeItemDO::getMinValue, InspectionSheetRecordDO::getSchemeMinValue)
                .selectAs(InspectionSchemeItemDO::getContent, InspectionSheetRecordDO::getSchemeContent)
                .selectAs(InspectionSchemeItemDO::getJudgement, InspectionSheetRecordDO::getJudgement)
                .selectAll(InspectionSheetRecordDO.class);

        wrapperX.eq(InspectionSheetRecordDO::getSchemeMaterialId, materialId).orderByAsc(InspectionSchemeItemDO::getNumber);

        return selectList(wrapperX);
    }


    default int deleteByInspectionSheetSchemeId(String inspectionSheetSchemeId) {
        return delete(InspectionSheetRecordDO::getInspectionSheetSchemeId, inspectionSheetSchemeId);
    }


    default List<InspectionSheetRecordDO> getInspectionSheetRecords(AnalysisReqVO reqVO){


        MPJLambdaWrapperX<InspectionSheetRecordDO> wrapperX = new MPJLambdaWrapperX<>();

        wrapperX.leftJoin(InspectionSchemeItemDO.class, InspectionSchemeItemDO::getId, InspectionSheetRecordDO::getInspectionSchemeItemId)
                .leftJoin(InspectionItemDO.class, InspectionItemDO::getId, InspectionSchemeItemDO::getInspectionItemId)
                .leftJoin(InspectionSheetSchemeMaterialDO.class,InspectionSheetSchemeMaterialDO::getId,InspectionSheetRecordDO::getSchemeMaterialId)
                .selectAs(InspectionItemDO::getItemName, InspectionSheetRecordDO::getInspectionSchemeItemName)
                .selectAs(InspectionSheetSchemeMaterialDO::getBatchNumber, InspectionSheetRecordDO::getBatchNumber)
                .selectAs(InspectionSheetSchemeMaterialDO::getMaterialConfigId, InspectionSheetRecordDO::getMaterialConfigId);

        if (!StringUtils.isEmpty(reqVO.getBatchNumber())){
            wrapperX.eq(InspectionSheetSchemeMaterialDO::getBatchNumber,reqVO.getBatchNumber());
        }
        if (!StringUtils.isEmpty(reqVO.getMaterialConfigId())){
            wrapperX.eq(InspectionSheetSchemeMaterialDO::getMaterialConfigId,reqVO.getMaterialConfigId());
        }

        // 已完成
        wrapperX.eq(InspectionSheetSchemeMaterialDO::getStatus, InspectionSheetSchemeMaterialStatusEnum.FINISH.getStatus());

        wrapperX.selectAll(InspectionSheetRecordDO.class);
        return selectList(wrapperX.betweenIfPresent(InspectionSheetRecordDO::getUpdateTime,reqVO.getQueryTime())
        .isNotNull(InspectionSheetRecordDO::getInspectionResult));

    }

    default List<InspectionSheetRecordDO> getInspectionSheetRecordList4Terminal(String materialId) {
        MPJLambdaWrapperX<InspectionSheetRecordDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSchemeItemDO.class, InspectionSchemeItemDO::getId, InspectionSheetRecordDO::getInspectionSchemeItemId)
                .leftJoin(InspectionItemDO.class, InspectionItemDO::getId, InspectionSchemeItemDO::getInspectionItemId)
                .leftJoin(InspectionItemTypeDO.class, InspectionItemTypeDO::getId, InspectionItemDO::getItemTypeId)
                .selectAs(InspectionItemDO::getItemName, InspectionSheetRecordDO::getInspectionSchemeItemName)
                .selectAs(InspectionItemTypeDO::getName, InspectionSheetRecordDO::getInspectionItemTypeName)
                .selectAs(InspectionSchemeItemDO::getReferenceType, InspectionSheetRecordDO::getReferenceType)
                .selectAs(InspectionSchemeItemDO::getContent, InspectionSheetRecordDO::getSchemeContent)
                .selectAs(InspectionSchemeItemDO::getJudgement, InspectionSheetRecordDO::getJudgement)
                .selectAll(InspectionSheetRecordDO.class);
        wrapperX.eq(InspectionSheetRecordDO::getSchemeMaterialId, materialId).orderByAsc(InspectionSchemeItemDO::getNumber);

        return selectList(wrapperX);
    }
}

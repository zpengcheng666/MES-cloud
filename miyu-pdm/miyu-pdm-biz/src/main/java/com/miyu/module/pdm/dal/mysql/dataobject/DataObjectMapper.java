package com.miyu.module.pdm.dal.mysql.dataobject;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.pdm.controller.admin.dataobject.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestBody;

@Mapper
public interface DataObjectMapper extends BaseMapperX<DataObjectDO> {

    default PageResult<DataObjectDO> selectPage(DataObjectPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DataObjectDO>()
                .eqIfPresent(DataObjectDO::getRootproductId, reqVO.getRootproductId())
                .eqIfPresent(DataObjectDO::getStdDataObject, reqVO.getStdDataObject())
                .eqIfPresent(DataObjectDO::getCustomizedDataObject, reqVO.getCustomizedDataObject())
                .eqIfPresent(DataObjectDO::getCustomizedIndex, reqVO.getCustomizedIndex())
                .eqIfPresent(DataObjectDO::getCustomizedType, reqVO.getCustomizedType())
                .likeIfPresent(DataObjectDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DataObjectDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DataObjectDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DataObjectDO::getIntrinsicAttrs, reqVO.getIntrinsicAttrs())
                .eqIfPresent(DataObjectDO::getCustomizedAttrs, reqVO.getCustomizedAttrs())
                .eqIfPresent(DataObjectDO::getSerialNumber, reqVO.getSerialNumber())
                .betweenIfPresent(DataObjectDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DataObjectDO::getId));
    }
    //获取客户化标识列表
    @Select("SELECT customized_index  FROM pdm_data_object " +
            "WHERE rootproduct_id = #{rootproductId} " +
            "GROUP BY customized_index")
    List<DataObjectDO> getCustomizedIndicesByRootProductId(String rootproductId);

    int insertSelective(DataObjectDO record);
    int selectCustomizedIndexCount(@Param("customizedIndex") String customizedIndex);
    int delIndex( DataObjectPageReqVO pageReqVO);
    int     updateByPrimaryKeySelective(DataObjectDO record);

    void createMiddleTable(@Param("customizedIndex")String customizedIndex);

    int insertPartInstanceSelective( PartInstanceDO obl);

    List<DataObjectDO> selectAllByRIDAndCID(DataObjectSaveReqVO record);

    void createTable(@Param("tableName") String tableName, @Param("attrsList") List<WinDataObjectAttrs> attrsList);

    Map<String, Object> selectOneById(@Param("id")String id);

    int savePM(Map<String, Object> map);
    int savePV(Map<String, Object> pvMap);
    int savePartMaster(Map<String, Object> pmMap);
    int savePartVersion(Map<String, Object> pvMap);
}
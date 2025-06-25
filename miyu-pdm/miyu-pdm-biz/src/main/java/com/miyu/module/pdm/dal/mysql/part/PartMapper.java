package com.miyu.module.pdm.dal.mysql.part;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.github.yulichang.method.mp.SelectOne;
import com.miyu.module.pdm.controller.admin.part.vo.PartPageReqVO;
import com.miyu.module.pdm.dal.dataobject.part.PartDO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import com.miyu.module.pdm.dal.dataobject.version.PartVersionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PartMapper extends BaseMapperX<PartDO> {
//    default PageResult<PartDO> selectPage(PartPageReqVO reqVO) {
//        return selectPage(reqVO, new LambdaQueryWrapperX<PartDO>()
//                .eqIfPresent(PartDO::getProductNumber, reqVO.getProductNumber())
//                .eqIfPresent(PartDO::getStdDataObject, reqVO.getStdDataObject())
//                .eqIfPresent(PartDO::getCustomizedDataObject, reqVO.getCustomizedDataObject())
//                .eqIfPresent(PartDO::getCustomizedIndex, reqVO.getCustomizedIndex())
//                .eqIfPresent(PartDO::getCustomizedType, reqVO.getCustomizedType())
//                .likeIfPresent(PartDO::getTableName, reqVO.getTableName())
//                .eqIfPresent(PartDO::getDescription, reqVO.getDescription())
//                .eqIfPresent(PartDO::getStatus, reqVO.getStatus())
//                .eqIfPresent(PartDO::getIntrinsicAttrs, reqVO.getIntrinsicAttrs())
//                .eqIfPresent(PartDO::getCustomizedAttrs, reqVO.getCustomizedAttrs())
//                .eqIfPresent(PartDO::getSerialNumber, reqVO.getSerialNumber())
//                .eqIfPresent(PartDO::getRootproductId, reqVO.getRootproductId())
//                .betweenIfPresent(PartDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(PartDO::getId));
//    }


    /**
     * 查找出表名为xxx的字段
     *
     * @return
     */
    String selectTableNameByProductIdAndType(@Param("rootproductId") String rootproductId);

    /**
     * 根据表名字查出数据
     */
    List<PartInstanceDO> selectAllFromDynamicTable(@Param("tableName") String tableName);

    /**
     * 根据产品id获取客户化属性
     */
    @Select("SELECT "
            + "a.customized_attrs as customizedAttrs "
            + "FROM pdm_data_object a "
            + "WHERE a.rootproduct_id = #{rootProductId} AND a.std_data_object = #{stdDataObject} AND a.customized_index = #{customizedIndex} ")
    String selectCustomizedAttrs(@Param("rootProductId") String rootProductId, @Param("stdDataObject") String stdDataObject, @Param("customizedIndex") String customizedIndex);

    /**
     * 根据零部件版本id获取动态pv表属性
     */
    @Select("SELECT * FROM ${tableName} "
            + "WHERE id = #{id}")
    List<Map<String, Object>> selectPvAttrValues(@Param("tableName") String tableNam, @Param("id") String id);

}

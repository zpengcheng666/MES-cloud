package com.miyu.module.pdm.dal.mysql.dataStatistics;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomRespVO;
import com.miyu.module.pdm.dal.dataobject.bom.BomDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface BomMapper extends BaseMapperX<BomDO> {
    //统计零件的数量
    default Long countPart(String id) {
        return selectCount(new LambdaQueryWrapperX<BomDO>()
                .eq(BomDO::getReceiveInfoId, id));

    }

    @Select("<script>" +
            "SELECT d.part_version_id AS partVersionId, pm.part_number as partNumber, pm.part_name as partName, pv.part_version as partVersion, " +
            " pm.root_product_id AS rootProductId,o.customized_index AS customizedIndex" +
            " FROM pdm_datapackage_bom d " +
            " LEFT JOIN pdm_part_version pv ON d.part_version_id = pv.id LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id" +
            " LEFT JOIN pdm_data_object o ON pm.root_product_id = o.rootproduct_id and o.std_data_object = 'PartInstance'" +
            " WHERE 1=1" +
            "<if test='receiveInfoId != null and receiveInfoId != \"\" '>" +
            " and d.receive_info_id = #{receiveInfoId} " +
            "</if>" +
            "<if test='partNumber != null and partNumber != \"\" '>" +
            " and pm.part_number like concat('%',#{partNumber},'%')" +
            "</if>" +
            "</script>")
    List<BomRespVO> selectPartList(@Param("receiveInfoId") String receiveInfoId, @Param("partNumber") String partNumber);
}

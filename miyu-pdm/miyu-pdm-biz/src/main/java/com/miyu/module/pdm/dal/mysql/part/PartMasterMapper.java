package com.miyu.module.pdm.dal.mysql.part;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.part.vo.AddPartReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.AddPartRespVO;
import com.miyu.module.pdm.dal.dataobject.master.PartMasterDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface PartMasterMapper extends BaseMapperX<PartMasterDO> {

    @Select("<script>"+
            "SELECT * FROM pdm_part_master WHERE part_number = #{partNumber} And process_condition = #{processCondition} AND deleted = 0"+
            "</script>")
    List<AddPartRespVO> selectPM(@Param("partNumber") String partNumber, @Param("processCondition") String processCondition);

    @Insert("<script>"+
            "INSERT INTO pdm_part_master (id,part_number,part_name,process_condition,root_product_id,product_type) VALUES (#{id},#{part_number},#{part_name},#{process_condition},#{root_product_id},#{product_type})"+
            "</script>")
    int insertPM(Map<String ,Object> pmMap);

    @Update("<script>"+
            "UPDATE pdm_part_master SET deleted = 1 WHERE id = #{PMID}"+
            "</script>")
    void deletePM(@Param("PMID") String PMID);
}

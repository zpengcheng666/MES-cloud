package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessChangeReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessChangeRespVO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessChangeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProcessChangeMapper extends BaseMapperX<ProcessChangeDO> {

    @Select("<script>"+
            " SELECT "+
            " cpc.* "+
            " FROM capp_process_change cpc "+
            " WHERE 1=1 AND cpc.deleted = 0 "+
            "<if test='projectCode != null and projectCode != \"\"'>" +
            " AND project_code = #{projectCode} "+
            "</if> " +
            "<if test='processVersionId != null and processVersionId != \"\"'>" +
            " AND process_version_id = #{processVersionId} "+
            "</if> " +
            "<if test='status != null'>" +
            " AND status = #{status} "+
            "</if> " +
            " order by change_code "+
            "</script>")
    List<ProcessChangeRespVO> selectChangeOrderList(@Param("projectCode") String projectCode, @Param("processVersionId") String processVersionId, @Param("status") Integer status);


    @Select("<script>"+
            " SELECT "+
            " change_code AS changeCode,id  "+
            " FROM capp_process_change  "+
            " WHERE 1=1 AND deleted = 0 "+
            "<if test='projectCode != null and projectCode != \"\"'>" +
            " AND project_code = #{projectCode} "+
            "</if> " +
            "<if test='processVersionId != null and processVersionId != \"\"'>" +
            " AND process_version_id = #{processVersionId} "+
            "</if> " +
            "<if test='changeCode != null and changeCode != \"\"'>" +
            " AND change_code = #{changeCode} "+
            "</if> " +
            "</script>")
    ProcessChangeDO selectNeed(@Param("projectCode") String projectCode, @Param("processVersionId") String processVersionId,@Param("changeCode") String changeCode);

    @Select("<script>"+
            " SELECT "+
            " cpcd.* "+
            " FROM capp_process_change_detail cpcd "+
            " LEFT JOIN capp_process_change cpc ON  cpcd.process_change_id = cpc.id  "+
            " WHERE 1=1 AND cpcd.deleted = 0 "+
            "<if test='processChangeId != null and processChangeId != \"\"'>" +
            " AND cpcd.process_change_id = #{processChangeId} "+
            "</if> " +
            "</script>")
    List<ProcessChangeRespVO> selectChangeDetailList(@Param("processChangeId") String processChangeId);

    @Select("<script>"+
            "SELECT cpc.project_code,cpc.process_version_id,cpcd.* FROM capp_process_change_detail cpcd LEFT JOIN capp_process_change cpc ON cpcd.process_change_id = cpc.id WHERE 1=1 AND cpcd.deleted = 0"+
            "</script>")
    List<ProcessChangeRespVO> getChangeDetailItemAll(ProcessChangeReqVO reqVO);


    @Select("<script>"+
            "SELECT cpc.*,cpcd.* FROM capp_process_change_detail cpcd LEFT JOIN capp_process_change cpc ON cpcd.process_change_id = cpc.id WHERE 1=1 " +
            " AND cpc.deleted = 0 "+
            "<if test='id != null and id != \"\"'>" +
            " AND cpc.id = #{id} "+
            "</if> " +
            "</script>")
    List<ProcessChangeRespVO> selectProcessChangeDetailList(@Param("id") String id);

    @Select("<script>"+
            "SELECT cpc.*,cp.process_code,cpv.process_version,pm.part_number,pm.part_name,cp.process_condition FROM capp_process_change cpc " +
            " left join capp_process_version cpv on cpv.id = cpc.process_version_id" +
            " left join capp_process cp on cp.id = cpv.process_id" +
            " left join pdm_part_version pv on pv.id = cp.part_version_id" +
            " left join pdm_part_master pm on pm.id = pv.part_master_id" +
            " WHERE 1=1 AND cpc.deleted = 0" +
            "<if test='id != null and id != \"\"'>" +
            " AND cpc.id = #{id} "+
            "</if> " +
            "</script>")
    ProcessChangeRespVO selectProcessChangeById(@Param("id") String id);


    @Select("<script>"+
            " SELECT "+
            " cpc.id  "+
            " FROM capp_process_change cpc "+
            " WHERE 1=1 AND cpc.deleted = 0 "+
            "<if test='projectCode1 != null and projectCode1 != \"\"'>" +
            " AND project_code = #{projectCode1} "+
            "</if> " +
            "<if test='processVersionId1 != null and processVersionId1 != \"\"'>" +
            " AND process_version_id = #{processVersionId1} "+
            "</if> " +
            "<if test='changeCode1 != null and changeCode1 != \"\"'>" +
            " AND change_code = #{changeCode1} "+
            "</if> " +
            "</script>")
    List<ProcessChangeRespVO> selectChangeOne(@Param("projectCode1") String projectCode1, @Param("processVersionId1") String processVersionId1,@Param("changeCode1") String changeCode1);


}

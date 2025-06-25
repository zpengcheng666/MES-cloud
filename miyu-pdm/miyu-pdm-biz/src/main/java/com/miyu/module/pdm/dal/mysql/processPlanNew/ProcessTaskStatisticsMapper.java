package com.miyu.module.pdm.dal.mysql.processPlanNew;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProcessTaskStatisticsMapper extends BaseMapperX<ProcessTaskDO> {


    @Select("<script>" +
            "SELECT "+
            "t.reviewed_by AS reviewedBy, "+
            "t.reviewer, "+
            "IFNULL(s.one, 0) AS one, "+
            "IFNULL(s.two, 0) AS two, "+
            "IFNULL(s.three, 0) AS three, "+
            "IFNULL(s.five, 0) AS five "+
            "FROM ( "+
            "SELECT DISTINCT t.reviewed_by, t.reviewer, t.update_time "+
            "FROM capp_process_task t "+
            "WHERE 1=1 "+
            ") t "+
            "LEFT JOIN ( "+
            "SELECT reviewed_by, "+
            "COUNT(CASE WHEN status = 1 THEN 1 END) AS one, "+
            "COUNT(CASE WHEN status = 2 THEN 1 END) AS two, "+
            "COUNT(CASE WHEN status = 3 THEN 1 END) AS three, "+
            "COUNT(CASE WHEN status = 5 THEN 1 END) AS five "+
            "FROM capp_process_task "+
            "WHERE 1=1 "+
            "<if test='reviewedBy != null and reviewedBy !=\"\" '>" +
            "AND reviewed_by = #{reviewedBy} " +
            "</if> " +
            "<if test='startTime != null and startTime != \"\" and endTime != null and endTime != \"\" '>" +
            "AND (update_time BETWEEN #{startTime} AND #{endTime}) " +
            "</if> " +
            "GROUP BY reviewed_by  "+
            ") s "+
            "ON t.reviewed_by = s.reviewed_by "+
            "GROUP BY t.reviewed_by, t.reviewer, s.one, s.two, s.three, s.five  "+
            "</script>")
    List<ProcessTaskDO> getProcessTaskStatistics(@Param("reviewedBy") String reviewedBy,@Param("startTime") String  startTime,@Param("endTime") String endTime);

}

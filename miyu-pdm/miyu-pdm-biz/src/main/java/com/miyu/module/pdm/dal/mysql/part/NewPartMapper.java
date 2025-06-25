package com.miyu.module.pdm.dal.mysql.part;


import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewPartMapper {

    @Select("<script>"+
            " SELECT * FROM ( "+
            " SELECT "+
            " 0 AS type, "+
            " '10' AS id, "+
            " '0' AS parentId, "+
            " '产品结构树' AS name, "+
            " '1' AS serialnum, "+
            " 'std' AS customizedIndex, "+
            " '' AS partVersionId, "+
            " '' AS rootproductId, "+
            " '' AS datapackageBomId, "+
            " '' AS source "+
            " FROM DUAL UNION "+
            " SELECT "+
            " 1 AS type, "+
            " p.company_id AS id, "+
            " '10' AS parentId, "+
            " p.company_name AS name, "+
            " p.company_name AS serialnum, "+
            " 'std' AS customizedIndex, "+
            " MAX(p.part_version_id) AS partVersionId, "+
            " MAX(pm.root_product_id) AS rootproductId, "+
            " '' AS datapackageBomId, "+
            " '' AS source "+
            " from pdm_proj_part_bom p "+
            " LEFT JOIN pdm_part_version pv ON p.part_version_id = pv.id " +
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id " +
            " WHERE pv.deleted = 0 AND pm.deleted = 0 " +
            " group by p.company_id,p.company_name"+
            " UNION SELECT "+
            " 2 AS type, "+
            " pb.id, "+
            " pb.company_id AS parentId, "+
            " CONCAT( pm.part_number,'(',pm.part_name,')','(',pm.process_condition,')' ) AS name, "+
            " pm.part_number AS serialnum, "+
            " 'std' AS customizedIndex, "+
            " pb.part_version_id AS partVersionId, "+
            " pm.root_product_id AS rootproductId, "+
            " pb.datapackage_bom_id AS datapackageBomId, "+
            " pv.source AS source "+
            " FROM pdm_proj_part_bom pb "+
            " LEFT JOIN pdm_part_version pv ON pb.part_version_id = pv.id "+
            " LEFT JOIN pdm_part_master pm ON pv.part_master_id = pm.id " +
            " WHERE pv.deleted = 0 AND pm.deleted = 0 AND pb.deleted = 0 ) a "+
            " WHERE 1 = 1 ORDER BY serialnum "+
            "</script>")
    List<NewPartRespVO> selectPartTreeList(NewPartReqVO reqVO);
}

package com.miyu.module.pdm.service.toolingApply;

import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailReqVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailTreeRespVO;
import com.miyu.module.pdm.dal.mysql.ToolingDetail.ToolingDetailMapper;
import com.miyu.module.pdm.dal.mysql.toolingApply.ToolingApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

@Service
@Validated
public class ToolingDetailServiceImpl implements ToolingDetailService {

    @Resource
    private ToolingDetailMapper toolingDetailMapper;
    @Autowired
    private ToolingApplyMapper toolingApplyMapper;

    @Override
    public List<ToolingDetailTreeRespVO> getTreeList(ToolingDetailReqVO reqVO) {
        String rootProductId = reqVO.getRootProductId();
        String customizedIndex = toolingApplyMapper.selectCustomzedIndexById(rootProductId);
        String tableName_PI = "pdm_"+customizedIndex+"_part_instance";
        String tableName_PM = "pdm_"+customizedIndex+"_part_master";
        String tableName_PV = "pdm_"+customizedIndex+"_part_version";
        String tableName_DR = "pdm_"+customizedIndex+"_document_revision";
        String tableName_DF = "pdm_"+customizedIndex+"_document_file";
        return toolingDetailMapper.selectTreeList(tableName_PI,tableName_PV,tableName_PM,tableName_DR,tableName_DF,rootProductId);
    }
}

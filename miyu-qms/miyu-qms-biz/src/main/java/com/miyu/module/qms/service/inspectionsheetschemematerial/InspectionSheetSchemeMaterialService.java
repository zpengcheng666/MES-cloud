package com.miyu.module.qms.service.inspectionsheetschemematerial;

import javax.validation.*;

import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionMaterialUpdateReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.List;

/**
 * 检验单产品 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetSchemeMaterialService {

    /**
     * 创建检验单产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheetSchemeMaterial(@Valid InspectionSheetSchemeMaterialSaveReqVO createReqVO);

    /**
     * 更新检验单产品
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheetSchemeMaterial(@Valid InspectionSheetSchemeMaterialSaveReqVO updateReqVO);

    /**
     * 删除检验单产品
     *
     * @param id 编号
     */
    void deleteInspectionSheetSchemeMaterial(String id);

    /**
     * 获得检验单产品
     *
     * @param id 编号
     * @return 检验单产品
     */
    InspectionSheetSchemeMaterialDO getInspectionSheetSchemeMaterial(String id);

    /**
     * 获得检验单产品分页
     *
     * @param pageReqVO 分页查询
     * @return 检验单产品分页
     */
    PageResult<InspectionSheetSchemeMaterialDO> getInspectionSheetSchemeMaterialPage(InspectionSheetSchemeMaterialPageReqVO pageReqVO);


    /**
     * 更新产品检验结果
     * @param updateReqVO
     */
    void updateInspectionMaterialResult(InspectionMaterialUpdateReqVO updateReqVO);

    /**
     *
     * @param id
     * @return
     */
    List<InspectionSheetSchemeMaterialDO> getUnqualifiedMaterialListBySchemeId(String id);

    /**
     * 检验任务ID获取不合格品集合,产品维度查看缺陷代码
     * @param id
     * @return
     */
    List<InspectionSheetSchemeMaterialDO> getUnqualifiedMaterialDefectiveListBySchemeId(String id);


//    /**
//     * 更新不合格品处理方式
//     * @param reqDTOList
//     */
//    void updateUnqualifiedMaterialHandleMethodBatch(List<UnqualifiedMaterialReqDTO> reqDTOList);



    List<InspectionSheetSchemeMaterialDO> getMaterialsByAnalysis(AnalysisReqVO vo);

    /**
     * 检验任务叫料获得检验单产品分页
     * @param pageReqVO
     * @return
     */
    PageResult<InspectionSheetSchemeMaterialDO> getInspectionSheetSchemeMaterialTaskPage(InspectionSheetSchemeMaterialPageReqVO pageReqVO);

    /**
     * 检验任务Id获取检验产品集合
     * @param schemeId
     * @return
     */
    List<InspectionSheetSchemeMaterialDO> getInspectionSheetSchemeMaterialListBySchemeId(String schemeId);
}

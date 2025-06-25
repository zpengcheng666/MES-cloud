package com.miyu.module.qms.service.inspectionscheme;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionscheme.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 检验方案 Service 接口
 *
 * @author 芋道源码
 */
public interface InspectionSchemeService {

    /**
     * 创建检验方案
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionScheme(@Valid InspectionSchemeSaveReqVO createReqVO);

    /**
     * 更新检验方案
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionScheme(@Valid InspectionSchemeSaveReqVO updateReqVO);

    /**
     * 删除检验方案
     *
     * @param id 编号
     */
    void deleteInspectionScheme(String id);

    /**
     * 获得检验方案
     *
     * @param id 编号
     * @return 检验方案
     */
    InspectionSchemeDO getInspectionScheme(String id);


    /***
     * 更改方案生效状态
     * @param id
     * @param isEffective
     */
    void submitEffective(String id,Integer isEffective);

    /**
     * 获得检验方案分页
     *
     * @param pageReqVO 分页查询
     * @return 检验方案分页
     */
    PageResult<InspectionSchemeDO> getInspectionSchemePage(InspectionSchemePageReqVO pageReqVO);

    // ==================== 子表（检验方案检测项目详情） ====================

    /**
     * 获得检验方案检测项目详情列表
     *
     * @param inspectionSchemeId 方案ID
     * @return 检验方案检测项目详情列表
     */
    List<InspectionSchemeItemDO> getInspectionSchemeItemListByInspectionSchemeId(String inspectionSchemeId);

    /**
     * 获得检验方案集合
     * @param reqVO
     * @return
     */
    List<InspectionSchemeDO> getInspectionSchemeList4InspectionSheet(InspectionSchemeReqVO reqVO);

    /**
     * 更改方案生效状态
     * @param technologyId
     * @param isEffective
     */
    void submitEffectiveByTechnologyId(String technologyId, Integer isEffective);

    /**
     * 工艺、工序ID查询检验方案
     * @param technologyId
     * @return
     */
    List<InspectionSchemeDO> getInspectionSchemeByProcessId(String technologyId, String processId);

    /**
     * 工艺ID删除检验方案
     * @param technologyId
     */
    void deleteInspectionSchemeByTechnologyId(String technologyId);
}

package com.miyu.module.qms.service.inspectionschemeitem;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionschemeitem.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 检验方案检测项目详情 Service 接口
 *
 * @author 芋道源码
 */
public interface InspectionSchemeItemService {

    /**
     * 创建检验方案检测项目详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSchemeItem(@Valid InspectionSchemeItemSaveReqVO createReqVO);

    /**
     * 更新检验方案检测项目详情
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSchemeItem(@Valid InspectionSchemeItemSaveReqVO updateReqVO);

    /**
     * 创建检验方案检测项目详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSchemeItemWithDetail(@Valid InspectionSchemeItemSaveReqVO createReqVO);

    /**
     * 更新检验方案检测项目详情
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSchemeItemWithDetail(@Valid InspectionSchemeItemSaveReqVO updateReqVO);


    /**
     * 删除检验方案检测项目详情
     *
     * @param id 编号
     */
    void deleteInspectionSchemeItem(String id);

    /**
     * 获得检验方案检测项目详情
     *
     * @param id 编号
     * @return 检验方案检测项目详情
     */
    InspectionSchemeItemDO getInspectionSchemeItem(String id);

    /**
     * 获得检验方案检测项目详情分页
     *
     * @param pageReqVO 分页查询
     * @return 检验方案检测项目详情分页
     */
    PageResult<InspectionSchemeItemDO> getInspectionSchemeItemPage(InspectionSchemeItemPageReqVO pageReqVO);



}
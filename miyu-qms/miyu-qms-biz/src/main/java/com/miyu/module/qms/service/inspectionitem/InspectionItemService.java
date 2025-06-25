package com.miyu.module.qms.service.inspectionitem;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionitem.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 检测项目 Service 接口
 *
 * @author 芋道源码
 */
public interface InspectionItemService {

    /**
     * 创建检测项目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionItem(@Valid InspectionItemSaveReqVO createReqVO);

    /**
     * 更新检测项目
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionItem(@Valid InspectionItemSaveReqVO updateReqVO);

    /**
     * 删除检测项目
     *
     * @param id 编号
     */
    void deleteInspectionItem(String id);

    /**
     * 获得检测项目
     *
     * @param id 编号
     * @return 检测项目
     */
    InspectionItemDO getInspectionItem(String id);

    /**
     * 获得检测项目分页
     *
     * @param pageReqVO 分页查询
     * @return 检测项目分页
     */
    PageResult<InspectionItemDO> getInspectionItemPage(InspectionItemPageReqVO pageReqVO);


    /***
     * 根据分类查询检测项目
     * @param inspectionItemTypeId
     * @return
     */
    List<InspectionItemDO> getInspectionItemByTypeId(String inspectionItemTypeId);
}
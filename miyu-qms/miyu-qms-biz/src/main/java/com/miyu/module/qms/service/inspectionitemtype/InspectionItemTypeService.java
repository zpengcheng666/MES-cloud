package com.miyu.module.qms.service.inspectionitemtype;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 检测项目分类 Service 接口
 *
 * @author 芋道源码
 */
public interface InspectionItemTypeService {

    /**
     * 创建检测项目分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionItemType(@Valid InspectionItemTypeSaveReqVO createReqVO);

    /**
     * 更新检测项目分类
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionItemType(@Valid InspectionItemTypeSaveReqVO updateReqVO);

    /**
     * 删除检测项目分类
     *
     * @param id 编号
     */
    void deleteInspectionItemType(String id);

    /**
     * 获得检测项目分类
     *
     * @param id 编号
     * @return 检测项目分类
     */
    InspectionItemTypeDO getInspectionItemType(String id);

    /**
     * 获得检测项目分类列表
     *
     * @param listReqVO 查询条件
     * @return 检测项目分类列表
     */
    List<InspectionItemTypeDO> getInspectionItemTypeList(InspectionItemTypeListReqVO listReqVO);

}
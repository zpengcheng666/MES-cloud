package com.miyu.module.qms.service.inspectionitemconfig;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectionitemconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitemconfig.InspectionItemConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 检测项配置表（检测内容名称） Service 接口
 *
 * @author 芋道源码
 */
public interface InspectionItemConfigService {

    /**
     * 创建检测项配置表（检测内容名称）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionItemConfig(@Valid InspectionItemConfigSaveReqVO createReqVO);

    /**
     * 更新检测项配置表（检测内容名称）
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionItemConfig(@Valid InspectionItemConfigSaveReqVO updateReqVO);

    /**
     * 删除检测项配置表（检测内容名称）
     *
     * @param id 编号
     */
    void deleteInspectionItemConfig(String id);

    /**
     * 获得检测项配置表（检测内容名称）
     *
     * @param id 编号
     * @return 检测项配置表（检测内容名称）
     */
    InspectionItemConfigDO getInspectionItemConfig(String id);

    /**
     * 获得检测项配置表（检测内容名称）分页
     *
     * @param pageReqVO 分页查询
     * @return 检测项配置表（检测内容名称）分页
     */
    PageResult<InspectionItemConfigDO> getInspectionItemConfigPage(InspectionItemConfigPageReqVO pageReqVO);
    List<InspectionItemConfigDO> getInspectionItemConfigList();

}
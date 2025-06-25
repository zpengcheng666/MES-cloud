package com.miyu.module.qms.service.inspectiontool;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.inspectiontool.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 检测工具 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionToolService {

    /**
     * 创建检测工具
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionTool(@Valid InspectionToolSaveReqVO createReqVO);

    /**
     * 更新检测工具
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionTool(@Valid InspectionToolSaveReqVO updateReqVO);

    /**
     * 删除检测工具
     *
     * @param id 编号
     */
    void deleteInspectionTool(String id);

    /**
     * 获得检测工具
     *
     * @param id 编号
     * @return 检测工具
     */
    InspectionToolDO getInspectionTool(String id);

    /**
     * 获得检测工具分页
     *
     * @param pageReqVO 分页查询
     * @return 检测工具分页
     */
    PageResult<InspectionToolDO> getInspectionToolPage(InspectionToolPageReqVO pageReqVO);

    /***
     * 获取检测工具集合
     * @return
     */
    List<InspectionToolDO> getInspectionToolList();

    /**
     * 保存检测工具送检任务
     */
    void createInspectionToolVerificationRecord();
}
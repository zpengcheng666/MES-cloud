package com.miyu.module.qms.service.managementtree;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.managementtree.vo.*;
import com.miyu.module.qms.dal.dataobject.managementtree.ManagementTreeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 质量管理关联树 Service 接口
 *
 * @author Zhangyunfei
 */
public interface ManagementTreeService {

    /**
     * 创建质量管理关联树
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createManagementTree(@Valid ManagementTreeSaveReqVO createReqVO);

    /**
     * 更新质量管理关联树
     *
     * @param updateReqVO 更新信息
     */
    void updateManagementTree(@Valid ManagementTreeSaveReqVO updateReqVO);

    /**
     * 删除质量管理关联树
     *
     * @param id 编号
     */
    void deleteManagementTree(String id);

    /**
     * 获得质量管理关联树
     *
     * @param id 编号
     * @return 质量管理关联树
     */
    ManagementTreeDO getManagementTree(String id);

    /**
     * 获得质量管理关联树分页
     *
     * @param pageReqVO 分页查询
     * @return 质量管理关联树分页
     */
    PageResult<ManagementTreeDO> getManagementTreePage(ManagementTreePageReqVO pageReqVO);


    /**
     * 获取资料库结构树
     * @return
     */
    List<ManagementTreeDO> getManagementTreeList();
}
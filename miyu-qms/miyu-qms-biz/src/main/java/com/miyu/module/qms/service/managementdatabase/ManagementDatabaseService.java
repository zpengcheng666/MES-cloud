package com.miyu.module.qms.service.managementdatabase;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.managementdatabase.vo.*;
import com.miyu.module.qms.dal.dataobject.managementdatabase.ManagementDatabaseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.qms.dal.dataobject.managementdatabasefile.ManagementDatabaseFileDO;

/**
 * 质量管理资料库 Service 接口
 *
 * @author Zhangyunfei
 */
public interface ManagementDatabaseService {

    /**
     * 创建质量管理资料库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createManagementDatabase(@Valid ManagementDatabaseSaveReqVO createReqVO);

    /**
     * 更新质量管理资料库
     *
     * @param updateReqVO 更新信息
     */
    void updateManagementDatabase(@Valid ManagementDatabaseSaveReqVO updateReqVO);

    /**
     * 删除质量管理资料库
     *
     * @param id 编号
     */
    void deleteManagementDatabase(String id);

    /**
     * 获得质量管理资料库
     *
     * @param id 编号
     * @return 质量管理资料库
     */
    ManagementDatabaseDO getManagementDatabase(String id);

    /**
     * 获得质量管理资料库分页
     *
     * @param pageReqVO 分页查询
     * @return 质量管理资料库分页
     */
    PageResult<ManagementDatabaseDO> getManagementDatabasePage(ManagementDatabasePageReqVO pageReqVO);

    /**
     * 资料库主键查询附件集合
     * @param id
     * @return
     */
    List<ManagementDatabaseFileDO> getManagementDatabaseFileByDatabaseId(String id);

    /**
     * 提交审核
     * @param id
     * @param processKey
     * @param loginUserId
     */
    void submitManagementDatabase(String id, String processKey, Long loginUserId);

    /**
     * 审批更新状态
     * @param businessKey
     * @param status
     */
    void updateDatabaseAuditStatus(String businessKey, Integer status);
}
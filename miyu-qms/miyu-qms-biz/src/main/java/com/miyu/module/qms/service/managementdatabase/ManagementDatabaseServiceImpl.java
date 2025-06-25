package com.miyu.module.qms.service.managementdatabase;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.qms.dal.dataobject.managementdatabasefile.ManagementDatabaseFileDO;
import com.miyu.module.qms.dal.mysql.managementdatabasefile.ManagementDatabaseFileMapper;
import com.miyu.module.qms.enums.ManagementSystemStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.qms.controller.admin.managementdatabase.vo.*;
import com.miyu.module.qms.dal.dataobject.managementdatabase.ManagementDatabaseDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.managementdatabase.ManagementDatabaseMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 质量管理资料库 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class ManagementDatabaseServiceImpl implements ManagementDatabaseService {

    @Resource
    private ManagementDatabaseMapper managementDatabaseMapper;

    @Resource
    private ManagementDatabaseFileMapper managementDatabaseFileMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional
    public String createManagementDatabase(ManagementDatabaseSaveReqVO createReqVO) {
        // 插入质量管理质料库
        ManagementDatabaseDO managementDatabase = BeanUtils.toBean(createReqVO, ManagementDatabaseDO.class, o -> {
            o.setStatus(ManagementSystemStatusEnum.DRAFT.getStatus());
        });
        managementDatabaseMapper.insert(managementDatabase);

        createReqVO.setId(managementDatabase.getId());
        // 保存附件
        createManagementDatabaseFile(createReqVO);
        // 返回
        return managementDatabase.getId();
    }

    @Override
    @Transactional
    @LogRecord(type = QMS_DATABASE_TYPE, subType = QMS_UPDATE_DATABASE_SUB_TYPE, bizNo = "{{#database.id}}",
            success = QMS_UPDATE_DATABASE_SUCCESS)
    public void updateManagementDatabase(ManagementDatabaseSaveReqVO updateReqVO) {
        // 校验存在
        ManagementDatabaseDO database = validateManagementDatabaseExists(updateReqVO.getId());
        // 更新
        ManagementDatabaseDO updateObj = BeanUtils.toBean(updateReqVO, ManagementDatabaseDO.class);
        managementDatabaseMapper.updateById(updateObj);
        // 删除附件
        managementDatabaseFileMapper.delete(ManagementDatabaseFileDO::getDatabaseId, updateReqVO.getId());
        // 保存附件
        createManagementDatabaseFile(updateReqVO);

        // 记录操作日志上下文
        LogRecordContext.putVariable("database", database);
        LogRecordContext.putVariable("name", database.getType() == null ? "" : DictFrameworkUtils.getDictDataLabel("qms_management_database_type", database.getType().toString()));
    }

    @Override
    @Transactional
    public void deleteManagementDatabase(String id) {
        // 校验存在
        validateManagementDatabaseExists(id);
        // 删除
        managementDatabaseMapper.deleteById(id);
        // 删除附件
        managementDatabaseFileMapper.delete(ManagementDatabaseFileDO::getDatabaseId, id);
    }


    /**
     * 保存附件
     */
    private void createManagementDatabaseFile(ManagementDatabaseSaveReqVO createReqVO){
        // 批量保存附件
        List<ManagementDatabaseFileDO> fileList = new ArrayList<>();
        for(String url : createReqVO.getFileUrl()){
            ManagementDatabaseFileDO fileDO = new ManagementDatabaseFileDO();
            fileDO.setDatabaseId(createReqVO.getId());
            fileDO.setFileUrl(url);
            fileList.add(fileDO);
        }
        // 插入附件
        managementDatabaseFileMapper.insertBatch(fileList);
    }

    private ManagementDatabaseDO validateManagementDatabaseExists(String id) {
        ManagementDatabaseDO databaseDO = managementDatabaseMapper.selectById(id);
        if (databaseDO == null) {
            throw exception(MANAGEMENT_DATABASE_NOT_EXISTS);
        }

        return databaseDO;
    }

    @Override
    public ManagementDatabaseDO getManagementDatabase(String id) {
        return managementDatabaseMapper.selectById(id);
    }

    @Override
    public PageResult<ManagementDatabaseDO> getManagementDatabasePage(ManagementDatabasePageReqVO pageReqVO) {
        return managementDatabaseMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ManagementDatabaseFileDO> getManagementDatabaseFileByDatabaseId(String id) {
        return managementDatabaseFileMapper.selectList(ManagementDatabaseFileDO::getDatabaseId, id);
    }


    /**
     * 提交审批
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitManagementDatabase(String id, String processKey, Long userId) {
        // 1. 校验状态是否在审批
        ManagementDatabaseDO databaseDO = validateManagementDatabaseExists(id);
        if (ObjUtil.notEqual(databaseDO.getStatus(), ManagementSystemStatusEnum.DRAFT.getStatus())) {
            throw exception(MANAGEMENT_DATABASE_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(processKey).setBusinessKey(String.valueOf(id)).setVariables(new HashMap<>())).getCheckedData();

        // 3. 更新工作流编号
        managementDatabaseMapper.updateById(new ManagementDatabaseDO().setId(id).setProcessInstanceId(processInstanceId).setStatus(ManagementSystemStatusEnum.PROCESS.getStatus()));
    }

    /**
     * 更新审批状态
     * @param businessKey
     * @param status
     */
    @Override
    public void updateDatabaseAuditStatus(String businessKey, Integer status) {
        // 1 校验状态是否在审批
        ManagementDatabaseDO databaseDO = validateManagementDatabaseExists(businessKey);
        // 2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(databaseDO.getStatus(), ManagementSystemStatusEnum.PROCESS.getStatus())) {
            throw exception(MANAGEMENT_DATABASE_SUBMIT_FAIL_NOT_PROCESS);
        }

        ManagementDatabaseDO updateObj = new ManagementDatabaseDO().setId(businessKey).setStatus(status);
        // 更新付款审批结果
        managementDatabaseMapper.updateById(updateObj);
    }

}

package com.miyu.module.ppm.service.company;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import com.miyu.module.ppm.dal.dataobject.companydatabasefile.CompanyDatabaseFileDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.mysql.companycontact.CompanyContactMapper;
import com.miyu.module.ppm.dal.mysql.companydatabasefile.CompanyDatabaseFileMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.enums.common.CompanyAuditStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.company.vo.*;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.company.CompanyMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.system.enums.LogRecordConstants.*;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 企业基本信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private CompanyContactMapper companyContactMapper;

    @Resource
    private CompanyDatabaseFileMapper companyDatabaseFileMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    public String createCompany(CompanySaveReqVO createReqVO) {
        // 插入
        CompanyDO company = BeanUtils.toBean(createReqVO, CompanyDO.class).setStatus(CompanyAuditStatusEnum.DRAFT.getStatus());
        company.setCreationIp(getClientIP());
        companyMapper.insert(company);
        // 返回
        return company.getId();
    }

    @Override
    @LogRecord(type = COMPANY_TYPE, subType = COMPANY_UPDATE_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = COMPANY_UPDATE_SUCCESS)
    public void updateCompany(CompanySaveReqVO updateReqVO) {
        // 校验存在
        CompanyDO company = validateCompanyExists(updateReqVO.getId());
        updateReqVO.setUpdatedIp(getClientIP());
        // 只有草稿可以更新；
        if (!ObjectUtils.equalsAny(updateReqVO.getStatus(), CompanyAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(COMPANY_UPDATE_FAIL_NOT_DRAFT, "更新");
        }
        // 更新
        CompanyDO updateObj = BeanUtils.toBean(updateReqVO, CompanyDO.class);
        companyMapper.updateById(updateObj);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(company, CompanySaveReqVO.class));
        LogRecordContext.putVariable("company", company);
    }

    @Override
    @LogRecord(type = COMPANY_TYPE, subType = COMPANY_UPDATE_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = COMPANY_UPDATE_SUCCESS)
    public void updateCompanyAndSubmit(@Valid CompanySaveReqVO updateReqVO) {
        CompanyDO company = validateCompanyExists(updateReqVO.getId());
//        if (ObjUtil.notEqual(company.getStatus(), CompanyAuditStatusEnum.DRAFT.getStatus())) {
//            throw exception(COMPANY_SUBMIT_FAIL_NOT_DRAFT);
//        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(updateReqVO.getProcessKey()).setBusinessKey(String.valueOf(updateReqVO.getId())).setVariables(new HashMap<>())).getCheckedData();

        // 3. 更新工作流编号
        CompanyDO updateObj = BeanUtils.toBean(updateReqVO, CompanyDO.class);
        updateObj.setProcessInstanceId(processInstanceId)
                .setStatus(CompanyAuditStatusEnum.PROCESS.getStatus());
        companyMapper.updateById(updateObj);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(company, CompanySaveReqVO.class));
        LogRecordContext.putVariable("company", company);
    }

    @Override
    @Transactional
    public void deleteCompany(List<String> ids) {

        //  校验企业是否有联系人、有销售、采购等信息
        List<CompanyDO> companys = companyMapper.selectBatchIds(ids);

        companys.forEach(company -> {
            // 校验存在
            validateCompanyExists(company.getId());
            // 只有草稿可以删除；
            if (!ObjectUtils.equalsAny(company.getStatus(), CompanyAuditStatusEnum.DRAFT.getStatus())) {
                throw exception(CONTRACT_UPDATE_FAIL_NOT_DRAFT, "删除");
            }
            // 验证是否存在联系人
            List<CompanyContactDO> contactList = companyContactMapper.selectList(CompanyContactDO::getCompanyId, company.getId());
            if(contactList.size()>0){
                throw exception(COMPANY_FAIL_DELETE_WITH_CONTACT);
            }
            // 验证是否存在关联的采购订单
            List<ContractDO> contractlist = contractMapper.selectList(ContractDO::getDepartment, company.getId());
            if(contractlist.size()>0){
                throw exception(COMPANY_FAIL_DELETE_WITH_CONTRACT);
            }
            // TODO (验证销售订单)
            companyMapper.deleteById(company.getId());
        });
    }

    private CompanyDO validateCompanyExists(String id) {
        CompanyDO company = companyMapper.selectById(id);
        if (company == null) {
            throw exception(COMPANY_NOT_EXISTS);
        }
        return company;
    }


    @Override
    public CompanyDO getCompany(String id) {
        return companyMapper.selectById(id);
    }

    @Override
    public PageResult<CompanyDO> getCompanyPage(CompanyPageReqVO pageReqVO) {
        return companyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<Map<String, Object>> getCompanySimpleList() {
        List<CompanyDO> list = companyMapper.selectList();
        List<Map<String, Object>> result = list.stream().map(t -> {
            Map<String, Object> vo = new HashMap<>();
            vo.put("value", t.getId());
            vo.put("label", t.getName());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<CompanyDO> getCompanyListByType(Collection<String> types) {

        if(CollUtil.isEmpty(types)){
            return Collections.emptyList();
        }

        return companyMapper.selectListByType(types);
    }

    @Override
    public List<CompanyDO> getCompanyListByIds(Collection<String> ids) {
        if(CollUtil.isEmpty(ids)){
            return Collections.emptyList();
        }

        return companyMapper.selectListByIds(ids);
    }

    /**
     * 提交合同审批
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitCompany(String id, String processKey, Long userId) {
        // 1. 校验供应商是否在审批
        CompanyDO company = validateCompanyExists(id);
        if (ObjUtil.notEqual(company.getStatus(), CompanyAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(COMPANY_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(processKey).setBusinessKey(String.valueOf(id)).setVariables(new HashMap<>())).getCheckedData();

        // 3. 更新工作流编号
        companyMapper.updateById(new CompanyDO().setId(id).setProcessInstanceId(processInstanceId).setStatus(CompanyAuditStatusEnum.PROCESS.getStatus()));
    }

    /**
     * 更新审批状态
     * @param businessKey
     * @param status
     */
    @Override
    public void updateCompanyAuditStatus(String businessKey, Integer status) {
        // 1 校验付款是否存在
        CompanyDO company = validateCompanyExists(businessKey);
        // 2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(company.getStatus(), CompanyAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(COMPANY_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        CompanyDO updateObj = new CompanyDO().setId(businessKey).setStatus(status);
        // 更新付款审批结果
        companyMapper.updateById(updateObj);
    }

    /**
     * 创建并提交付款审批
     * @param createReqVO
     * @return
     */
    @Override
    @Transactional
    public void createAndSubmitCompany(CompanySaveReqVO createReqVO) {
        String id = createCompany(createReqVO);
        submitCompany(id, createReqVO.getProcessKey(), getLoginUserId());
    }

    /**
     * 外协供应商创建
     * @param createReqVO
     * @return
     */
    @Override
    @Transactional
    public String createCompanyCoord(CompanyCoordSaveReqVO createReqVO) {
        // 插入
        String id = createCompany(BeanUtils.toBean(createReqVO, CompanySaveReqVO.class));
        // 保存附件
        createReqVO.setId(id);
        createCompanyDatabaseFile(createReqVO);
        // 返回
        return id;
    }

    /**
     * 外协供应商修改
     * @param updateReqVO
     */
    @Override
    @Transactional
    public void updateCompanyCoord(CompanyCoordSaveReqVO updateReqVO) {
        // 更新供应商
        updateCompany(BeanUtils.toBean(updateReqVO, CompanySaveReqVO.class));
        // 删除附件
        companyDatabaseFileMapper.delete(CompanyDatabaseFileDO::getDatabaseId, updateReqVO.getId());
        // 保存附件
        createCompanyDatabaseFile(updateReqVO);
    }

    /**
     * 删除外协供应商
     * @param ids
     */
    @Override
    @Transactional
    public void deleteCompanyCoord(List<String> ids) {
        // 删除供应商
        deleteCompany(ids);
        // 删除附件
        companyDatabaseFileMapper.deleteBatchIds(ids);
    }

    /**
     * 保存附件
     */
    private void createCompanyDatabaseFile(CompanyCoordSaveReqVO createReqVO){
        // 批量保存附件
        List<CompanyDatabaseFileDO> fileList = new ArrayList<>();
        for(String url : createReqVO.getFileUrl()){
            CompanyDatabaseFileDO fileDO = new CompanyDatabaseFileDO();
            fileDO.setDatabaseId(createReqVO.getId());
            fileDO.setFileUrl(url);
            fileList.add(fileDO);
        }

        // 插入附件
        companyDatabaseFileMapper.insertBatch(fileList);
    }


    /**
     * 供应商主键获取附件集合
     */
    @Override
    public List<CompanyDatabaseFileDO> getDatabaseFileListByCompanyId(String companyId){

       return companyDatabaseFileMapper.selectList(CompanyDatabaseFileDO::getDatabaseId, companyId);
    }
}

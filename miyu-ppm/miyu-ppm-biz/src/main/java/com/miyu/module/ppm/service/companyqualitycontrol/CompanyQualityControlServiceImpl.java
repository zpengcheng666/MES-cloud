package com.miyu.module.ppm.service.companyqualitycontrol;

import com.miyu.module.ppm.controller.admin.companyfinance.vo.CompanyFinanceSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.mysql.company.CompanyMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.companyqualitycontrol.CompanyQualityControlMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 企业质量控制信息 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class CompanyQualityControlServiceImpl implements CompanyQualityControlService {

    @Resource
    private CompanyQualityControlMapper companyQualityControlMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Override
    public String createCompanyQualityControl(CompanyQualityControlSaveReqVO createReqVO) {
        // 插入
        CompanyQualityControlDO companyQualityControl = BeanUtils.toBean(createReqVO, CompanyQualityControlDO.class);
        companyQualityControl.setCreationIp(getClientIP());
        companyQualityControlMapper.insert(companyQualityControl);
        // 返回
        return companyQualityControl.getId();
    }

    @Override
    @LogRecord(type = COMPANY_QUANTITY_TYPE, subType = COMPANY_QUANTITY_UPDATE_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = COMPANY_QUANTITY_UPDATE_SUCCESS)
    public void updateCompanyQualityControl(CompanyQualityControlSaveReqVO updateReqVO) {
        // 校验存在
        CompanyQualityControlDO companyQualityControlDO =  validateCompanyQualityControlExists(updateReqVO.getId());
        updateReqVO.setUpdatedIp(getClientIP());
        // 更新
        CompanyQualityControlDO updateObj = BeanUtils.toBean(updateReqVO, CompanyQualityControlDO.class);
        companyQualityControlMapper.updateById(updateObj);


        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(companyQualityControlDO, CompanyQualityControlSaveReqVO.class));
        LogRecordContext.putVariable("companyQualityControl", companyQualityControlDO);
    }

    @Override
    public void deleteCompanyQualityControl(String id) {
        // 校验存在
        validateCompanyQualityControlExists(id);
        // 删除
        companyQualityControlMapper.deleteById(id);
    }

    private CompanyQualityControlDO validateCompanyQualityControlExists(String id) {
      CompanyQualityControlDO companyQualityControlDO =   companyQualityControlMapper.selectById(id);
        if (companyQualityControlDO == null) {
            throw exception(COMPANY_QUALITY_CONTROL_NOT_EXISTS);
        }
        CompanyDO companyDO =  companyMapper.selectById(companyQualityControlDO.getCompanyId());
        companyQualityControlDO.setCompanyName(companyDO.getName());
        return companyQualityControlDO;
    }

    @Override
    public CompanyQualityControlDO getCompanyQualityControl(String id) {
        return companyQualityControlMapper.selectById(id);
    }

    @Override
    public PageResult<CompanyQualityControlDO> getCompanyQualityControlPage(CompanyQualityControlPageReqVO pageReqVO) {
        return companyQualityControlMapper.selectPage(pageReqVO);
    }

    @Override
    public CompanyQualityControlDO getCompanyQualityControlByCompanyId(String companyId) {
        return companyQualityControlMapper.selectByCompanyId(companyId);
    }

}
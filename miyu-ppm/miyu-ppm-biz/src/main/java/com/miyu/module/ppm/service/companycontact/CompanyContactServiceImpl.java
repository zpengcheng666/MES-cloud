package com.miyu.module.ppm.service.companycontact;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.ppm.controller.admin.company.vo.CompanySaveReqVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.mysql.company.CompanyMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.companycontact.vo.*;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.companycontact.CompanyContactMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 企业联系人 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class CompanyContactServiceImpl implements CompanyContactService {

    @Resource
    private CompanyContactMapper companyContactMapper;
    @Resource
    private CompanyMapper companyMapper;

    @Override
    public String createCompanyContact(CompanyContactSaveReqVO createReqVO) {
        // 插入
        CompanyContactDO companyContact = BeanUtils.toBean(createReqVO, CompanyContactDO.class);
        companyContactMapper.insert(companyContact);
        // 返回
        return companyContact.getId();
    }

    @Override
    @LogRecord(type = COMPANY_CONTACT_TYPE, subType = COMPANY_CONTACT_UPDATE_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = COMPANY_CONTACT_UPDATE_SUCCESS)
    public void updateCompanyContact(CompanyContactSaveReqVO updateReqVO) {
        // 校验存在
        CompanyContactDO companyContactDO = validateCompanyContactExists(updateReqVO.getId());
        // 更新
        CompanyContactDO updateObj = BeanUtils.toBean(updateReqVO, CompanyContactDO.class);
        companyContactMapper.updateById(updateObj);


        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(companyContactDO, CompanyContactSaveReqVO.class));
        LogRecordContext.putVariable("companyContact", companyContactDO);
    }

    @Override
    public void deleteCompanyContact(String id) {
        // 校验存在
        validateCompanyContactExists(id);
        // 删除
        companyContactMapper.deleteById(id);
    }

    private CompanyContactDO validateCompanyContactExists(String id) {
        CompanyContactDO companyContactDO = companyContactMapper.selectById(id);
        CompanyDO companyDO =  companyMapper.selectById(companyContactDO.getCompanyId());
        if ( companyContactDO== null) {
            throw exception(COMPANY_CONTACT_NOT_EXISTS);
        }
        companyContactDO.setCompanyName(companyDO.getName());
        return  companyContactDO;
    }

    @Override
    public CompanyContactDO getCompanyContact(String id) {
        return companyContactMapper.selectById(id);
    }

    @Override
    public PageResult<CompanyContactDO> getCompanyContactPage(CompanyContactPageReqVO pageReqVO) {
        return companyContactMapper.selectPage(pageReqVO);
    }

    @Override
    public Map<String, Object> getCompanyContactSimpleList(String companyId) {
        List<CompanyContactDO> list = companyContactMapper.selectList(Wrappers.lambdaQuery(CompanyContactDO.class)
                    .eq(StringUtils.isNotBlank(companyId), CompanyContactDO::getCompanyId, companyId));


//        List<Map<String, Object>> nameList = list.stream().map(a -> {
//            Map<String, Object> dictMap = new HashMap<>();
//            dictMap.put("id", a.getId());
//            // 直属上级
//            dictMap.put("name", a.getName());
//            return dictMap;
//        }).collect(Collectors.toList());

        // 根据部门去重
        List<CompanyContactDO> deptList = list.stream().filter(o -> o != null && o.getDepart() != null).collect(
                Collectors.collectingAndThen(Collectors.toMap(CompanyContactDO::getDepart, o -> o, (k1, k2) -> k2), x -> new ArrayList<>(x.values())));

        // 根据职位去重
        List<CompanyContactDO> posList = list.stream().filter(o -> o != null && o.getPosition() != null).collect(
                Collectors.collectingAndThen(Collectors.toMap(CompanyContactDO::getPosition, o -> o, (k1, k2) -> k2), x -> new ArrayList<>(x.values())));


        Map<String, Object> resMap = new HashMap<>();
        resMap.put("deptList", deptList);
        resMap.put("posList", posList);
        resMap.put("contactList", list);
        return resMap;
    }
}
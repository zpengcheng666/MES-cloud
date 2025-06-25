package com.miyu.module.es.service.visit;

import com.miyu.module.es.dal.dataobject.visitlicense.VisitLicenseDO;
import com.miyu.module.es.dal.mysql.visitlicense.VisitLicenseMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.es.controller.admin.visit.vo.*;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.es.dal.mysql.visit.VisitMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.es.enums.ErrorCodeConstants.VISIT_NOT_EXISTS;


/**
 * 访客记录 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
@Transactional
public class VisitServiceImpl implements VisitService {

    @Resource
    private VisitMapper visitMapper;

    @Resource
    private VisitLicenseMapper visitLicenseMapper;

    private void validateVisitExists(String id) {
        if (visitMapper.selectById(id) == null) {
            throw exception(VISIT_NOT_EXISTS);
        }
    }

    @Override
    public VisitRespVO getVisit(String id) {
        VisitDO visitDO = visitMapper.selectByVisitRecordId(id);
        VisitRespVO visitRespVO = BeanUtils.toBean(visitDO, VisitRespVO.class);

        List<VisitLicenseDO> visitLicenseDO = visitLicenseMapper.selectByVisitRecordId(id);
        List<String> licenseNoList = visitLicenseDO.stream().map(VisitLicenseDO::getLicenseNo).collect(Collectors.toList());
        String licenseNo = String.join(",", licenseNoList);

        return visitRespVO.setLicenseNo(licenseNo);

    }

    @Override
    public PageResult<VisitDO> getVisitPage(VisitPageReqVO pageReqVO) {
        return visitMapper.selectPage(pageReqVO);
    }


}
package com.miyu.module.ppm.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.service.company.CompanyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class CompanyApiImpl implements CompanyApi {

    @Resource
    private CompanyService companyService;

    @Override
    public CommonResult<List<CompanyRespDTO>> getCompanyList(Collection<String> ids) {
        return success(BeanUtils.toBean(companyService.getCompanyListByIds(ids), CompanyRespDTO.class));
    }

    @Override
    public CommonResult<String> updateCompanyAuditStatus(String businessKey, Integer status) {
        companyService.updateCompanyAuditStatus(businessKey, status);
        return null;
    }
}

package com.miyu.module.ppm.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.companyProduct.CompanyProductApi;
import com.miyu.module.ppm.api.companyProduct.dto.CompanyProductDTO;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class CompanyProductApiImpl implements CompanyProductApi {

    @Resource
    private CompanyProductService companyProductService;


    @Override
    public CommonResult<List<CompanyProductDTO>> getCompanyProductList(String id) {
        return success(BeanUtils.toBean(companyProductService.getProductListByCompanyId(id),CompanyProductDTO.class));
    }
}

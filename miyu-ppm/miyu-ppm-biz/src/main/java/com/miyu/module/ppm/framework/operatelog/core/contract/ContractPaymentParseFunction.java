package com.miyu.module.ppm.framework.operatelog.core.contract;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.miyu.module.ppm.service.companycontact.CompanyContactService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 公司类型的 {@link IParseFunction} 实现类
 *
 * @author HUIHUI
 */
@Component
@Slf4j
public class ContractPaymentParseFunction implements IParseFunction {

    public static final String NAME = "getContractPayment";
    @Override
    public boolean executeBefore() {
        return true; // 先转换值后对比
    }

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }
        return JSON.toJSONString(value);
    }

}

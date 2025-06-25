package com.miyu.module.ppm.framework.operatelog.core.company;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;
import com.miyu.module.ppm.enums.DictTypeConstants;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 质量认证体系的 {@link IParseFunction} 实现类
 *
 * @author HUIHUI
 */
@Component
@Slf4j
public class CompanyQuantityOqcParseFunction implements IParseFunction {

    public static final String NAME = "getCompanyQuantityOqc";

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
        return DictFrameworkUtils.getDictDataLabel(DictTypeConstants.PD_COMPANY_QUALITY_CONTROL_OQC, value.toString());
    }

}

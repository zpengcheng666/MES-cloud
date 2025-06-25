package com.miyu.module.ppm.framework.operatelog.core.company;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.dict.core.DictFrameworkUtils;
import cn.iocoder.yudao.module.system.api.ip.AreaApi;
import com.miyu.module.ppm.enums.DictTypeConstants;
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
public class CompanyAreaParseFunction implements IParseFunction {

    public static final String NAME = "getCompanyArea";
    @Resource
    private AreaApi areaApi;

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
        return areaApi.getArea((int)value);
    }

}

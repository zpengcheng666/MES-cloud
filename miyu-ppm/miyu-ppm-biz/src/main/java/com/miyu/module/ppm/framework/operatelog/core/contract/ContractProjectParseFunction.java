package com.miyu.module.ppm.framework.operatelog.core.contract;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.service.company.CompanyService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 公司类型的 {@link IParseFunction} 实现类
 *
 * @author HUIHUI
 */
@Component
@Slf4j
public class ContractProjectParseFunction implements IParseFunction {

    public static final String NAME = "getContractProject";
    @Resource
    private PmsApi pmsApi;

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
        Map<String, PmsApprovalDto> map = pmsApi.getApprovalMap(Lists.newArrayList((String) value));

        return map.get((String) value).getProjectName();
    }

}

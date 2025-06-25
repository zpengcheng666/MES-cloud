package cn.iocoder.yudao.module.system.api.ip;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class AreaApiImpl implements AreaApi {
    @Override
    public CommonResult<String> format(Integer id) {
        return success(AreaUtils.format(id, "-"));
    }
}

package com.miyu.cloud.dms.api.maintainapplication;

import com.miyu.cloud.dms.dal.dataobject.maintainapplication.MaintainApplicationDO;
import com.miyu.cloud.dms.dal.mysql.maintainapplication.MaintainApplicationMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.MAINTAIN_APPLICATION_NOT_EXISTS;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class MaintainApplicationApiImpl implements MaintainApplicationApi {
    @Resource
    private MaintainApplicationMapper maintainApplicationMapper;

    @Override
    public void updateMaintenanceStatus(String id, Integer status) {
        validateMaintainApplicationExists(id);
        maintainApplicationMapper.updateById(new MaintainApplicationDO().setId(id).setStatus(status));
    }

    private void validateMaintainApplicationExists(String id) {
        if (maintainApplicationMapper.selectById(id) == null) {
            throw exception(MAINTAIN_APPLICATION_NOT_EXISTS);
        }
    }
}

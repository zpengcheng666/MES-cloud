package com.miyu.cloud.dms.api.ledger;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToUserDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerToUserMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class LedgerApiImpl implements LedgerApi {

    @Resource
    LedgerMapper ledgerMapper;

    @Resource
    LedgerToUserMapper ledgerToUserMapper;

    @Override
    public CommonResult<LedgerDataResDTO> getNameByDeviceId(String deviceId) {
         LedgerDataResDTO ledgerDataResDTO = BeanUtils.toBean(ledgerMapper.selectById(deviceId), LedgerDataResDTO.class);
         List<LedgerToUserDO> list = ledgerToUserMapper.getUsersByLedger(ledgerDataResDTO.getId());
         List<String> users = list.stream().map(LedgerToUserDO::getUser).collect(Collectors.toList());
         ledgerDataResDTO.setUsers(users);
         return success(ledgerDataResDTO);
    }

    @Override
    public CommonResult<List<LedgerDataResDTO>> getLedgerListByDeviceType(String deviceTypeId) {
        return success(BeanUtils.toBean(ledgerMapper.getLedgerListByDeviceType(deviceTypeId),LedgerDataResDTO.class));
    }


}

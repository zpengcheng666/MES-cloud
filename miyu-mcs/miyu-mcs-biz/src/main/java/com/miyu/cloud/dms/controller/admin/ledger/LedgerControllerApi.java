package com.miyu.cloud.dms.controller.admin.ledger;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerPageReqVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerRespVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台API - 设备台账")
@RestController
@RequestMapping("/dms")
@Validated
public class LedgerControllerApi {

    @Resource
    private LedgerService ledgerService;

    @PostMapping("/deviceData")
    @Operation(summary = "更新设备台账")
    public CommonResult<Boolean> updateLedgerList(@Valid @RequestBody LedgerSaveReqVO[] updateData) {
        for (LedgerSaveReqVO reqVO : updateData) {
            ledgerService.updateLedger(reqVO);
        }
        return success(true);
    }

    @GetMapping("/device/{id}")
    @Operation(summary = "获得设备台账")
    public CommonResult<LedgerRespVO> getLedger(@PathVariable String id) {
        LedgerDO ledger = ledgerService.getLedger(id);
        return success(BeanUtils.toBean(ledger, LedgerRespVO.class));
    }

    @GetMapping("/device")
    @Operation(summary = "获得设备台账")
    public CommonResult<LedgerRespVO> getLedger2(@RequestParam("code") String code) {
        LedgerPageReqVO query = new LedgerPageReqVO();
        query.setCode(code);
        LedgerDO ledger = ledgerService.getLedgerPage(query).getList().get(0);
        return success(BeanUtils.toBean(ledger, LedgerRespVO.class));
    }


    @PostMapping("/deviceStatus")
    @Operation(summary = "更新设备状态")
    public CommonResult<Boolean> updateLedgerStatus(@Valid @RequestBody DeviceStatus updateData) {

        LedgerSaveReqVO data = new LedgerSaveReqVO();

        data.setId(updateData.getDeviceNumber());
        data.setStatus(updateData.getDeviceStatus());
        data.setRunStatus(updateData.getRunStatus());
        data.setOnlineStatus(updateData.getOnlineStatus());

        ledgerService.updateLedger(data);
        return success(true);
    }
}

@Data
@ToString(callSuper = true)
class DeviceStatus {
    private String deviceNumber;
    private Integer deviceStatus;
    private Integer runStatus;
    private Integer onlineStatus;
}

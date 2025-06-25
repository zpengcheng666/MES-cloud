package com.miyu.cloud.mcs.controller.admin.test;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionapplication.DistributionApplicationMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordEventDTO;
import com.miyu.cloud.mcs.restServer.api.ManufacturingService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;

import static com.miyu.cloud.mcs.enums.DictConstants.MCS_DELIVERY_RECORD_STATUS_COMPLETED;

@Tag(name = "生产-测试")
@RestController
@RequestMapping("/mcs/test")
@Validated
public class TestController {

    @Resource
    private McsManufacturingControlApi manufacturingControlApi;
    @Resource
    private TechnologyRestService technologyRestService;
    @Resource
    private ManufacturingService manufacturingService;

    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private DistributionRecordMapper distributionRecordMapper;
    @Resource
    private DistributionApplicationMapper distributionApplicationMapper;
    @Resource
    private LedgerMapper ledgerMapper;

    //批次任务开工
    @PostMapping("/batchRecordStart")
    public CommonResult<?> batchRecordStart(@RequestBody McsBatchRecordEventDTO mcsBatchRecordEventDTO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        mcsBatchRecordEventDTO.setOperatorId(loginUserId+"");
        return manufacturingControlApi.batchRecordStart(mcsBatchRecordEventDTO);
    }

    @PostMapping("/batchRecordEnd")
    public CommonResult<?> batchRecordEnd(@RequestBody McsBatchRecordEventDTO mcsBatchRecordEventDTO) {

        mcsBatchRecordEventDTO.setOperatorId("1");
        return manufacturingControlApi.batchRecordEnd(mcsBatchRecordEventDTO);
    }

    @PostMapping("/materialReception")
    public CommonResult<?> materialReception(@RequestBody String distributionRecordId) {
        try {
            DistributionRecordDO distributionRecordDO = distributionRecordMapper.selectById(distributionRecordId);
            distributionRecordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_COMPLETED);
            manufacturingService.deliveryCompleted(Collections.singletonList(distributionRecordDO));
            return CommonResult.success("签收成功");
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }
}

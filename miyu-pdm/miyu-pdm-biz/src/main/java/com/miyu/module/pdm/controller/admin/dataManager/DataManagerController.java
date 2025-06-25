package com.miyu.module.pdm.controller.admin.dataManager;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomReqVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomRespVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsPageReqVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsRespVO;
import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;
import com.miyu.module.pdm.service.dataStatistics.DataStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 数据包管理")
@RestController
@RequestMapping("pdm/data-manager")
@Validated
public class DataManagerController {

    @Resource
    private DataStatisticsService dataStatisticsService;

    @GetMapping("list")
    @Operation(summary = "获得数据包列表", description = "数据包编号倒序取所有")
    public CommonResult<List<DataStatisticsRespVO>> getDataList(@Valid DataStatisticsPageReqVO reqVO) {
        List<DataStatisticsDO> list = dataStatisticsService.getDataStatisticsList(reqVO);
        return success(BeanUtils.toBean(list, DataStatisticsRespVO.class));
    }

    @GetMapping("/getDataInfo")
    @Operation(summary = "获取数据包详细信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DataStatisticsRespVO> getDataInfo(@RequestParam("id") String id) {
        DataStatisticsDO dataStatistics = dataStatisticsService.getDataStatistics(id);
        return success((BeanUtils.toBean(dataStatistics, DataStatisticsRespVO.class)));
    }

    @GetMapping("getPartListByReceiveInfoId")
    @Operation(summary = "获得当前数据包零件目录列表", description = "根据选中数据包id")
    public CommonResult<List<BomRespVO>> getPartListByReceiveInfoId(@Valid BomReqVO reqVO) {
        List<BomRespVO> list = dataStatisticsService.getPartListByReceiveInfoId(reqVO);
        return success(list);
    }
}

package com.miyu.module.pdm.controller.admin.dataStatistics;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsPageReqVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsRespVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductPageReqVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductRespVO;
import com.miyu.module.pdm.controller.admin.util.StringListUtils;
import com.miyu.module.pdm.convert.dataStatistics.DataStatisticsConvert;
import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;
import com.miyu.module.pdm.service.dataStatistics.DataStatisticsService;
import com.miyu.module.pdm.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "后台管理 - 设计数据包接受记录")
@RestController
@RequestMapping("pdm/dataStatistics")
@Validated
public class DataStatisticsController {
    @Resource
    private DataStatisticsService dataStatisticsService;
    @Resource
    private AdminUserApi userApi;

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除设计数据包记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pdm:dataStatistics:delete')")
    public CommonResult<Boolean> deleteDataStatistics(@RequestParam("id") String id) {
        dataStatisticsService.deleteDataStatistics(id);
        return success(true);
    }
    /**
     * 获取
     * @param id
     * @return
     */
    @GetMapping("/get")
    @Operation(summary = "获取设计数据包记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:dataStatistics:query')")
    public CommonResult<DataStatisticsRespVO> getDataStatistics(@RequestParam("id") String id) {
        DataStatisticsDO dataStatistics = dataStatisticsService.getDataStatistics(id);
        return success((BeanUtils.toBean(dataStatistics, DataStatisticsRespVO.class)));
    }
    /**
     * 分页
     * @param
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页设计数据包记录")
    @PreAuthorize("@ss.hasPermission('pdm:dataStatistics:query')")
    public CommonResult<PageResult<DataStatisticsRespVO>> getDataStatisticsPage(@Valid DataStatisticsPageReqVO pageReqVO){
        PageResult<DataStatisticsDO> dataStatistics = dataStatisticsService.getDataStatisticsPage(pageReqVO);
   //    return success(BeanUtils.toBean(dataStatistics, DataStatisticsRespVO.class));
        //创建者
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(dataStatistics.getList(), DataStatisticsDO::getCreator));
        Map<Long, AdminUserRespDTO> userMap = null;
        if(CollectionUtils.isNotEmpty(creatorIds)){
            creatorIds = creatorIds.stream().distinct().collect(Collectors.toList());
            // 拼接数据
            userMap = userApi.getUserMap(creatorIds);
        }
        // 转换响应对象
        List<DataStatisticsRespVO> dataStatisticsRespVOS = DataStatisticsConvert.INSTANCE.convertList(dataStatistics.getList(), userMap);
        for (DataStatisticsRespVO dataStatisticsRespVO : dataStatisticsRespVOS) {
            // 计算耗时
            getDatePoor(dataStatisticsRespVO);
            // 零件数量
            getPartCount(dataStatisticsRespVO);
        }

        return success(new PageResult<>(dataStatisticsRespVOS, dataStatistics.getTotal()));
  }

    @GetMapping("/export-excel")
    @Operation(summary = "导出数据包 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void export(HttpServletResponse response, @Validated DataStatisticsPageReqVO pageReqVO) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<DataStatisticsDO> dataStatistics = dataStatisticsService.getDataStatisticsPage(pageReqVO);
        //创建者
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(dataStatistics.getList(), DataStatisticsDO::getCreator));
        Map<Long, AdminUserRespDTO> userMap = null;
        if(CollectionUtils.isNotEmpty(creatorIds)){
            creatorIds = creatorIds.stream().distinct().collect(Collectors.toList());
            // 拼接数据
            userMap = userApi.getUserMap(creatorIds);
        }
        // 转换响应对象
        List<DataStatisticsRespVO> dataStatisticsRespVOS = DataStatisticsConvert.INSTANCE.convertList(dataStatistics.getList(), userMap);
        for (DataStatisticsRespVO dataStatisticsRespVO : dataStatisticsRespVOS) {
            // 计算耗时
            getDatePoor(dataStatisticsRespVO);
            // 零件数量
            getPartCount(dataStatisticsRespVO);
        }
        // 导出 Excel
        ExcelUtils.write(response, "数据包.xls", "数据", DataStatisticsRespVO.class,
                dataStatisticsRespVOS);
    }

    /**
     * 获取零件数量
     * @param dataStatisticsRespVO
     */
    public void getPartCount(DataStatisticsRespVO dataStatisticsRespVO) {
        DataStatisticsDO dataStatistics = dataStatisticsService.getDataStatistics(dataStatisticsRespVO.getId());
        dataStatisticsRespVO.setPartCount(String.valueOf(dataStatisticsService.countPart(dataStatistics.getId())));
    }

    /**
     * 获取耗时
     * @param dataStatisticsRespVO
     */
    public void getDatePoor(DataStatisticsRespVO dataStatisticsRespVO) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        //LocalDateTime转String
        String dateTimeStr = dataStatisticsRespVO.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String  dateStr = dataStatisticsRespVO.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(dateTimeStr);
            Date date1 = formatter.parse(dateStr);

            //计算时间差
            long diff = date.getTime()- date1.getTime();
            // 计算差多少天
            // long day = diff / nd;
            // 计算差多少小时du
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;
            // 计算差多少秒//输出结果
            long sec = diff % nd % nh % nm / ns;
            String useTime = hour + "时" + min + "分" + sec + "秒";
            dataStatisticsRespVO.setUseTime(useTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

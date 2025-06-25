package com.miyu.module.es.controller.admin.brake;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.es.api.brake.dto.BrakeData;
import com.miyu.cloud.es.api.brake.dto.BrakeRes;
import com.miyu.module.es.service.http.HttpReadUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.miyu.cloud.es.enums.ErrorCodeConstants.*;

import com.miyu.module.es.controller.admin.brake.vo.*;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import com.miyu.module.es.service.brake.BrakeService;

@Tag(name = "管理后台 - 旧厂车牌数据")
@RestController
@RequestMapping("/es/brake")
@Validated
public class BrakeController {

    @Resource
    private BrakeService brakeService;

    @PostMapping("/create")
    @Operation(summary = "创建旧厂车牌数据")
    @PreAuthorize("@ss.hasPermission('es:brake:create')")
    public CommonResult<String> createBrake(@Valid @RequestBody BrakeSaveReqVO createReqVO) {
        return success(brakeService.createBrake(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新旧厂车牌数据")
    @PreAuthorize("@ss.hasPermission('es:brake:update')")
    public CommonResult<Boolean> updateBrake(@Valid @RequestBody BrakeSaveReqVO updateReqVO) {
        brakeService.updateBrake(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除旧厂车牌数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('es:brake:delete')")
    public CommonResult<Boolean> deleteBrake(@RequestParam("id") String id) {
        brakeService.deleteBrake(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得旧厂车牌数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('es:brake:query')")
    public CommonResult<BrakeRespVO> getBrake(@RequestParam("id") String id) throws UnsupportedEncodingException {
//        String body= new JSONObject().toString();
        CloseableHttpClient client = HttpClients.createDefault();
        String tou = "?do=queryPassportInfo&page_size=" + 1 + "&page_num=" + 1 + "&car_plate_no=" + id;
        //正式用
        //HttpPost httpPost = new HttpPost("http://h2服务器ip/plugin/client.action"+tou);

        //旧厂测试用
        HttpPost httpPost = new HttpPost("http://192.168.2.136:443/rpc-api/es/brake/getBrakePage"+tou);

        httpPost.addHeader("Content-Type", "application/json");
//        httpPost.setEntity(new StringEntity(body, "utf-8"));
        String reultStr = HttpReadUtils.httpRead(client, httpPost);
        //返回结果转换为json对象
        JSONObject jsonObjectData = JSONObject.parseObject(reultStr);
        if(jsonObjectData!=null) {
            //返回值判断根据接口文档判断按个值是成功的标志
            if (jsonObjectData.getString("code").contains("0")) {
                String data = JSONObject.parseObject(jsonObjectData.getString("data")).getString("data").replaceAll("^\\[|\\]$", "");
                //需进行数据处理


                BrakeRespVO brakeRespVO = JSONObject.parseObject(data, BrakeRespVO.class);
                return success(brakeRespVO);
            } else {
                throw exception(DATA_NOT_EXISTS);
            }
        }else {return null;}
    }

    @GetMapping("/page")
    @Operation(summary = "获得旧厂车牌数据分页")
    @PreAuthorize("@ss.hasPermission('es:brake:query')")
    public CommonResult<BrakeData> getBrakePage(@Valid BrakePageReqVO pageReqVO) throws UnsupportedEncodingException {
//        String body= new JSONObject().toString();
        CloseableHttpClient client = HttpClients.createDefault();
        String tou = null;
        if(pageReqVO.getCarPlateNo() == null || pageReqVO.getCarPlateNo().equals("")) {
            tou = "?do=queryPassportInfo&page_size=" + pageReqVO.getPageSize() + "&page_num=" + pageReqVO.getPageNum();
        }else {
            tou = "?do=queryPassportInfo&page_size=" + pageReqVO.getPageSize() + "&page_num=" + pageReqVO.getPageNum()+ "&car_plate_no=" + pageReqVO.getCarPlateNo();
        }
        //正式用
        //HttpPost httpPost = new HttpPost("http://h2服务器ip/plugin/client.action"+tou);
        //旧厂测试用
        HttpPost httpPost = new HttpPost("http://192.168.2.136:443/rpc-api/es/brake/getBrakePage"+tou);
        httpPost.addHeader("Content-Type", "application/json");
//        httpPost.setEntity(new StringEntity(body, "utf-8"));
        String reultStr = HttpReadUtils.httpRead(client, httpPost);
        //返回结果转换为json对象
        JSONObject jsonObjectData = JSONObject.parseObject(reultStr);
        //返回值判断根据接口文档判断按个值是成功的标志
        if(jsonObjectData!=null) {
            if (jsonObjectData.getString("code").contains("0")) {
                String object = JSONObject.parseObject(jsonObjectData.getString("data")).toString();
                //需进行数据处理



                BrakeRes brakeRes = JSONObject.parseObject(object, BrakeRes.class);
                BrakeData brakeData = BeanUtils.toBean(brakeRes, BrakeData.class).setTotal(brakeRes.getTotalSize());
                return success(brakeData);
            } else {
                throw exception(OPEN_NOT_EXISTS);
            }
        }else {
            return success(null);
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出旧厂车牌数据 Excel")
    @PreAuthorize("@ss.hasPermission('es:brake:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBrakeExcel(@Valid BrakePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BrakeDO> list = brakeService.getBrakePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "旧厂车牌数据.xls", "数据", BrakeRespVO.class,
                        BeanUtils.toBean(list, BrakeRespVO.class));
    }

    @PutMapping("/syncBrake")
    @Operation(summary = "同步车牌信息")
    @PreAuthorize("@ss.hasPermission('es:brake:update')")
    public CommonResult<Boolean> syncBrake(@RequestParam("ids") List<String> ids) {
        brakeService.syncBrake(ids);
        return success(true);
    }
}
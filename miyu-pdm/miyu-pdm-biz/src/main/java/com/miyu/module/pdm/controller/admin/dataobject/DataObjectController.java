package com.miyu.module.pdm.controller.admin.dataobject;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import com.miyu.module.pdm.controller.admin.dataobject.vo.*;
import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;
import com.miyu.module.pdm.service.dataobject.DataObjectService;

@Tag(name = "管理后台 - 产品数据对象")
@RestController
@RequestMapping("/pdm/data-object")
@Validated
public class DataObjectController {

    @Resource
    private DataObjectService dataObjectService;

    @PostMapping("/create")
    @Operation(summary = "创建产品数据对象")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:create')")
    public CommonResult<Integer> createDataObject(@Valid @RequestBody DataObjectSaveReqVO createReqVO) {
        return success(dataObjectService.insertIndex(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品数据对象")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:update')")
    public CommonResult<Boolean> updateDataObject(@Valid @RequestBody DataObjectSaveReqVO updateReqVO) {
        dataObjectService.insertOrUpdate(updateReqVO);
        return success(true);
    }
    @RequestMapping("/updateIndex")
    @Operation(summary = "更新产品数据对象")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:update')")
    public CommonResult<Boolean>  updateIndex(@Valid @RequestBody DataObjectSaveReqVO updateReqVO) {
        dataObjectService.updateIndex(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品数据对象")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pdm:data-object:delete')")
    public int deleteDataObject(@Valid @RequestBody DataObjectPageReqVO pageReqVO) throws Exception {
        return dataObjectService.delIndex(pageReqVO);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品数据对象")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:query')")
    public CommonResult<DataObjectRespVO> getDataObject(@RequestParam("id") String id) {
        DataObjectDO dataObject = dataObjectService.getDataObject(id);
        return success(BeanUtils.toBean(dataObject, DataObjectRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品数据对象分页")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:query')")
    public CommonResult<PageResult<DataObjectRespVO>> getDataObjectPage(@Valid DataObjectPageReqVO pageReqVO) {
        PageResult<DataObjectDO> pageResult = dataObjectService.getDataObjectPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DataObjectRespVO.class));
    }
    /**
     * 获得客户化标识列表
     */

    @GetMapping("/getCustomizedIndicesByRootProductId")
    @Operation(summary = "获得客户化标识列表")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:query')")
    public CommonResult<List<DataObjectRespVO>> getCustomizedIndicesByRootProductId(@RequestParam("rootproductId") String rootproductId) {
        List<DataObjectRespVO> list = dataObjectService.getCustomizedIndicesByRootProductId(rootproductId);
        return success(BeanUtils.toBean(list, DataObjectRespVO.class));
    }

    /**
     * 实例化
     * @return
     */
    @RequestMapping("/createIndex")
    @Operation(summary = "实例化")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:query')")
    public CommonResult<Integer> createIndex(@Valid @RequestBody DataObjectSaveReqVO createReqVO) {
        return success(dataObjectService.createIndex(createReqVO));
    }

    /**
     * 查看详细
     * @param id
     * @return
     */
    @RequestMapping("/selectOneById")
    @Operation(summary = "查看详细")
    @PreAuthorize("@ss.hasPermission('pdm:data-object:query')")
    public CommonResult<List<Map<String, Object>>> selectOneById(@RequestParam("id") String id) {
        return success(dataObjectService.selectOneById(id));
    }
}
package com.miyu.module.pdm.controller.admin.instance;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.controller.admin.part.vo.PartInstanceListReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.PartInstanceRespVO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.service.part.PartInstanceService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.*;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - pdm__part_instance")
@RestController
@RequestMapping("/pdm/-part-instance")
@Validated
public class PartInstanceController {

    @Resource
    private PartInstanceService partInstanceService;



    @GetMapping("/get")
    @Operation(summary = "获得pdm__part_instance")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pdm:-part-instance:query')")
    public CommonResult<PartInstanceRespVO> getPartInstance(@RequestParam("id") String id) {
        PartInstanceDO PartInstance = partInstanceService.getPartInstance(id);
        return success(BeanUtils.toBean(PartInstance, PartInstanceRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得pdm__part_instance列表")
    @PreAuthorize("@ss.hasPermission('pdm:-part-instance:query')")
    public CommonResult<List<PartInstanceRespVO>> getPartInstanceList(@Valid PartInstanceListReqVO listReqVO) {
        List<PartInstanceDO> list = partInstanceService.getPartInstanceList(listReqVO);
        return success(BeanUtils.toBean(list, PartInstanceRespVO.class));
    }


}
package com.miyu.module.pdm.controller.admin.combination;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.combination.vo.CombinationListReqVO;
import com.miyu.module.pdm.controller.admin.combination.vo.CombinationRespVO;
import com.miyu.module.pdm.dal.dataobject.combination.CombinationDO;
import com.miyu.module.pdm.service.combination.CombinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - PDM 刀组-临时")
@RestController
@RequestMapping("/pdm/combination")
@Validated
public class CombinationController {

    @Resource
    private CombinationService combinationService;

    @GetMapping("/getCombinationList")
    @Operation(summary = "获得刀组列表")
    public CommonResult<List<CombinationRespVO>> getCombinationList(@Valid CombinationListReqVO listReqVO) {
        List<CombinationDO> list = combinationService.getCombinationList(listReqVO);
        return success(BeanUtils.toBean(list, CombinationRespVO.class));
    }

    @GetMapping("/getCombinationListByCombinationIds")
    @Operation(summary = "根据刀组id数组获得刀组列表")
    public CommonResult<List<CombinationRespVO>> getCombinationListByCombinationIds(@RequestParam("ids") List<String> combinationIds) {
        List<CombinationDO> list = combinationService.getCombinationListByCombinationIds(combinationIds);
        return success(BeanUtils.toBean(list, CombinationRespVO.class));
    }
}

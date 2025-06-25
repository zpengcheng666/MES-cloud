package com.miyu.module.pdm.controller.admin.part;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartRespVO;
import com.miyu.module.pdm.service.part.NewPartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 零部件管理-NEW")
@RestController
@RequestMapping("pdm/newpart")
@Validated
public class NewPartController {

    @Resource
    private NewPartService newPartService;

    @GetMapping("getNewPartBomTreeList")
    @Operation(summary = "获得当前零件结构树")
    public CommonResult<List<NewPartRespVO>> getNewPartBomTreeList(@Valid NewPartReqVO reqVO) {
        List<NewPartRespVO> list = newPartService.getTreeList(reqVO);
        return success(list);
    }

}

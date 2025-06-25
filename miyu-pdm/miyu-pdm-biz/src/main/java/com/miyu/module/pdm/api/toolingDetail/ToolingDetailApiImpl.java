package com.miyu.module.pdm.api.toolingDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.api.toolingDetail.dto.ProductDTO;
import com.miyu.module.pdm.api.toolingDetail.dto.ToolingProductDTO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailReqVO;
import com.miyu.module.pdm.controller.admin.toolingApply.vo.ToolingDetailTreeRespVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskReqVO;
import com.miyu.module.pdm.controller.admin.toolingTask.vo.ToolingTaskRespVO;
import com.miyu.module.pdm.service.toolingApply.ToolingDetailService;
import com.miyu.module.pdm.service.toolingTask.ToolingTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class ToolingDetailApiImpl implements ToolingDetailApi {

    @Resource
    private ToolingTaskService toolingTaskService;

    @Resource
    private ToolingDetailService toolingDetailService;

    @Override
    public CommonResult<String> updateToolingDetailStatus(String businessKey, Integer status) {
        toolingTaskService.updateApplyInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<List<ToolingProductDTO>> getToolingList() {
        ToolingTaskReqVO reqVO = new ToolingTaskReqVO();
        List<ToolingTaskRespVO> list = toolingTaskService.getToolingDetailList(reqVO);
        List<ToolingProductDTO> productList = BeanUtils.toBean(list, ToolingProductDTO.class);
        return CommonResult.success(productList);
    }

    @Override
    public CommonResult<List<ProductDTO>> getToolingPartInstanceByRootProductId(String rootProductId) {
        ToolingDetailReqVO toolingDetailReqVO = new ToolingDetailReqVO();
        toolingDetailReqVO.setRootProductId(rootProductId);
        List<ToolingDetailTreeRespVO> list = toolingDetailService.getTreeList(toolingDetailReqVO);
        List<ProductDTO> productDTOS = BeanUtils.toBean(list, ProductDTO.class);
        return success(productDTOS);
    }


}

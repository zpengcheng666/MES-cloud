package cn.iocoder.yudao.module.pms.api.pms;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.ProductStatusRespDTO;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 项目信息")
public interface PmsApi {

    String PREFIX = ApiConstants.PREFIX;

    /**
     * 获得项目列表(全部)
     * @return
     */
    @GetMapping(PREFIX + "/approval/getApprovalList")
    @Operation(summary = "获得项目列表")
    CommonResult<List<PmsApprovalDto>> getApprovalList(@RequestParam("projectCode")String projectCode);

    /**
     * 通过项目id获得单个项目计划
     * @param id
     * @return
     */
    @GetMapping(PREFIX + "/plan/getPmsPlanByProjectId")
    @Operation(summary = "获得项目计划")
    CommonResult<PmsPlanDTO> getPlanByProjectId(@RequestParam("id")String id);


    /**
     * 通过项目id集合获取项目列表
     * @param ids
     * @return
     */
    @GetMapping(PREFIX + "/approval/getApprovalListByIds")
    @Operation(summary = "获得项目列表")
    CommonResult<List<PmsApprovalDto>> getApprovalListByIds(@RequestParam("ids") Collection<String> ids);

    /**
     * 通过项目id集合获取项目列表
     * @param ids
     * @return
     */
    default Map<String, PmsApprovalDto> getApprovalMap(Collection<String> ids) {
        List<PmsApprovalDto> checkedData = getApprovalListByIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, PmsApprovalDto::getId);
    }


    @PostMapping(PREFIX + "/updateProjectStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateProcessStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);

    @GetMapping(PREFIX + "/approval/getProductStatusList")
    @Operation(summary = "根据项目id查询加工状态,每个订单的完成数量那种")
    CommonResult<List<ProductStatusRespDTO>> getProductStatusList(@RequestParam("ids") Collection<String> ids);

}

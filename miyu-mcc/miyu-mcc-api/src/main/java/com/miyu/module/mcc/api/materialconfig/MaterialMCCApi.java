package com.miyu.module.mcc.api.materialconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.mcc.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 创建物料类型")
public interface MaterialMCCApi {

    String PREFIX = ApiConstants.PREFIX + "/material_mcc";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建物料")
    CommonResult<String> createMaterialConfig(@Valid @RequestBody MaterialConfigReqDTO reqDTO);

    @GetMapping(PREFIX + "/list")
    @Operation(summary = "通过用户 ID 查询物料")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigList(@RequestParam("ids") Collection<String> ids);

    @PostMapping(PREFIX + "/getMaterialConfigListByTypeCode")
    @Operation(summary = "根据物料类别编码 查询物料")
    CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigListByTypeCode(@Valid @RequestBody MaterialConfigReqDTO reqDTO);

    default Map<String, MaterialConfigRespDTO> getMaterialConfigMap(Collection<String> ids) {
        List<MaterialConfigRespDTO> materialConfigList = getMaterialConfigList(ids).getCheckedData();
        return CollectionUtils.convertMap(materialConfigList, MaterialConfigRespDTO::getId);
    }


    @GetMapping(PREFIX + "/listByCode")
    @Operation(summary = "通过物料码查询物料")
    @Parameter(name = "codes", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigListByCode(@RequestParam("codes") Collection<String> codes);

    default Map<String, MaterialConfigRespDTO> getMaterialConfigCodeMap(Collection<String> codes) {
        List<MaterialConfigRespDTO> materialConfigList = getMaterialConfigListByCode(codes).getCheckedData();
        return CollectionUtils.convertMap(materialConfigList, MaterialConfigRespDTO::getMaterialNumber);
    }

    @GetMapping(PREFIX + "/listByTypeId")
    @Operation(summary = "通过物类别ID查询物料")
    @Parameter(name = "materialTypeId", description = "物类别ID", example = "1", required = true)
    CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigListByTypeId(@RequestParam("codes") String materialTypeId);

//    @PostMapping(PREFIX + "/send-single-member")
//    @Operation(summary = "发送单条邮件给 Member 用户")
//    CommonResult<Long> sendSingleMailToMember(@Valid @RequestBody MailSendSingleToUserReqDTO reqDTO);



    @PostMapping(PREFIX + "/updateAudit")
    @Operation(summary = "更新物料审批状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updateAudit(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);

}

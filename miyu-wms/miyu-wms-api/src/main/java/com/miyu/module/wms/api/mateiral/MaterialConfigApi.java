package com.miyu.module.wms.api.mateiral;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.api.mateiral.dto.MCCMaterialConfigReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigSaveReqDTO;
import com.miyu.module.wms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;



@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 创建物料类型")
@Validated
public interface MaterialConfigApi {

    String PREFIX = ApiConstants.PREFIX + "/material-config";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建物料")
    CommonResult<String> createMaterialConfig(@Valid @RequestBody MCCMaterialConfigReqDTO reqDTO);

    @PutMapping(PREFIX + "/update")
    @Operation(summary = "更新物料类型")
    CommonResult<Boolean> updateMaterialConfig(@Valid @RequestBody MCCMaterialConfigReqDTO reqVO);

    /**
     * 物料类型新增 审批使用
     * @param reqVO
     * @return
     */
    @PutMapping(PREFIX + "/otherUpdate")
    @Operation(summary = "更新物料类型其他属性")
    CommonResult<Boolean> otherUpdateMaterialConfig(@Valid @RequestBody MaterialConfigSaveReqDTO reqVO);

    @DeleteMapping(PREFIX + "/delete")
    @Operation(summary = "删除物料类型")
    @Parameter(name = "id", description = "编号", required = true)
    CommonResult<Boolean> deleteMaterialConfig(@RequestParam("id") String id);

    @GetMapping(PREFIX + "/list")
    @Operation(summary = "通过用户 ID 查询物料")
    @Parameter(name = "ids", description = "部门编号数组", example = "1,2", required = true)
    CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigList(@RequestParam("ids") Collection<String> ids);


    default Map<String, MaterialConfigRespDTO> getMaterialConfigMap(Collection<String> ids) {
        List<MaterialConfigRespDTO> materialConfigList = getMaterialConfigList(ids).getCheckedData();
        return CollectionUtils.convertMap(materialConfigList, MaterialConfigRespDTO::getId);
    }


//    @PostMapping(PREFIX + "/send-single-member")
//    @Operation(summary = "发送单条邮件给 Member 用户")
//    CommonResult<Long> sendSingleMailToMember(@Valid @RequestBody MailSendSingleToUserReqDTO reqDTO);

    /**
     * 通过物料类型id获取物料类型信息
     */
    @PostMapping(PREFIX + "/getMaterialConfigById")
    @Operation(summary = "通过物料类型id获取物料类型信息")
    CommonResult<MaterialConfigRespDTO> getMaterialConfigById(@RequestParam("id") String id);

}

package com.miyu.module.tms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.tms.api.dto.ToolConfigSaveReqDTO;
import com.miyu.module.tms.controller.admin.toolconfig.vo.ToolConfigSaveReqVO;
import com.miyu.module.tms.service.toolconfig.ToolConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


import java.util.Arrays;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class ToolConfigApiImpl implements ToolConfigApi {

    @Resource
    private ToolConfigService toolConfigService;

    @Resource
    private MaterialMCCApi materialMCCApi;


    @Override
    public CommonResult<Boolean> createToolConfig(ToolConfigSaveReqDTO reqDTO) {
        ToolConfigSaveReqVO bean = BeanUtils.toBean(reqDTO, ToolConfigSaveReqVO.class);
        // 物料类型ID获取mcc物料类型
        List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigList(Arrays.asList(reqDTO.getMaterialConfigId())).getCheckedData();
        bean.setToolName(reqDTO.getMaterialName());
        bean.setToolType(reqDTO.getToolType());
        bean.setMaterialTypeId(materialConfigList.get(0).getMaterialTypeId());
        bean.setMaterialTypeCode(materialConfigList.get(0).getMaterialTypeCode());
        bean.setMaterialTypeName(materialConfigList.get(0).getMaterialTypeName());
        toolConfigService.createToolConfig(bean);
        return success(true);
    }
}

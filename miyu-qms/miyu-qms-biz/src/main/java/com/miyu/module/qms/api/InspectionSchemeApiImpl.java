package com.miyu.module.qms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.api.dto.InspectionSchemeReqDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeRespDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeSaveReqDTO;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import com.miyu.module.qms.controller.admin.inspectionscheme.vo.InspectionSchemeReqVO;
import com.miyu.module.qms.controller.admin.inspectionscheme.vo.InspectionSchemeSaveReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import com.miyu.module.qms.service.inspectionscheme.InspectionSchemeService;
import com.miyu.module.qms.service.inspectionschemeitem.InspectionSchemeItemService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class InspectionSchemeApiImpl implements InspectionSchemeApi {

    @Resource
    private InspectionSchemeService inspectionSchemeService;

    @Resource
    private InspectionSchemeItemService inspectionSchemeItemService;

    @Override
    public CommonResult<List<InspectionSchemeRespDTO>> getInspectionScheme(InspectionSchemeReqDTO reqDTO) {
        return CommonResult.success(BeanUtils.toBean(inspectionSchemeService.getInspectionSchemeList4InspectionSheet(BeanUtils.toBean(reqDTO, InspectionSchemeReqVO.class)),InspectionSchemeRespDTO.class));
    }

    @Override
    public CommonResult<String> createInspectionScheme(InspectionSchemeSaveReqDTO reqDTO) {
        return CommonResult.success(inspectionSchemeService.createInspectionScheme(BeanUtils.toBean(reqDTO, InspectionSchemeSaveReqVO.class, vo -> {
            vo.setInspectionSchemeItems(BeanUtils.toBean(reqDTO.getItems(), InspectionSchemeItemDO.class));
        })));
    }

    @Override
    public CommonResult<InspectionSchemeRespDTO> getInspectionSchemeByProcessId(String technologyId, String processId) {
        List<InspectionSchemeDO> list = inspectionSchemeService.getInspectionSchemeByProcessId(technologyId, processId);
        if(list.size() > 0) {
            List<InspectionSchemeItemDO> schemeItemList = inspectionSchemeService.getInspectionSchemeItemListByInspectionSchemeId(list.get(0).getId());
            return CommonResult.success(BeanUtils.toBean(list.get(0), InspectionSchemeRespDTO.class, o -> {
                o.setItems(BeanUtils.toBean(schemeItemList, InspectionSchemeRespDTO.Item.class));
            }));
        }
        return CommonResult.success(new InspectionSchemeRespDTO());
    }

    @Override
    public CommonResult<Boolean> submitEffective(String technologyId, Integer isEffective) {
        inspectionSchemeService.submitEffectiveByTechnologyId(technologyId, isEffective);
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<Boolean> deleteInspectionSchemeItemById(String id) {
        inspectionSchemeItemService.deleteInspectionSchemeItem(id);
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<Boolean> deleteInspectionSchemeByTechnologyId(String technologyId) {
        inspectionSchemeService.deleteInspectionSchemeByTechnologyId(technologyId);
        return CommonResult.success(true);
    }
}

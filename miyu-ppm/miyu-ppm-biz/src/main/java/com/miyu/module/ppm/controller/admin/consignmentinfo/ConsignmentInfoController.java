package com.miyu.module.ppm.controller.admin.consignmentinfo;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.ConsignmentReturnDetailPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.convert.purchaseConsignment.PurchaseConsignmentConvert;
import com.miyu.module.ppm.convert.purchaseConsignmentDetail.PurchaseConsignmentDetailConvert;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.math.BigDecimal;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static com.miyu.module.ppm.enums.ApiConstants.IN_WARHOUSE;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.CONSIGNMENT_SIGN_ERROR;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.CONSIGNMENT_SIGN_MATERIAL_ERROR;

import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.service.consignmentinfo.ConsignmentInfoService;

@Tag(name = "管理后台 - 收货产品")
@RestController
@RequestMapping("/ppm/consignment-info")
@Validated
public class ConsignmentInfoController {

    @Resource
    private ConsignmentInfoService consignmentInfoService;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    @Resource
    private ContractService contractService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建收货产品")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:create')")
    public CommonResult<String> createConsignmentInfo(@Valid @RequestBody ConsignmentInfoSaveReqVO createReqVO) {
        return success(consignmentInfoService.createConsignmentInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:update')")
    public CommonResult<Boolean> updateConsignmentInfo(@Valid @RequestBody ConsignmentInfoSaveReqVO updateReqVO) {
        consignmentInfoService.updateConsignmentInfo(updateReqVO);
        return success(true);
    }


    @PutMapping("/sign")
    @Operation(summary = "签收数量")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:update')")
    public CommonResult<Boolean> signConsignmentInfo(@Valid @RequestBody ConsignmentInfoSaveReqVO updateReqVO) {
        updateReqVO.setLocationId(IN_WARHOUSE);
        consignmentInfoService.signInfo(updateReqVO);
        return success(true);
    }

    @PutMapping("/signMaterial")
    @Operation(summary = "签收物料")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:update')")
    public CommonResult<Boolean> signConsignmentDetail(@Valid @RequestBody ConsignmentInfoSaveReqVO updateReqVO) {
        if (org.springframework.util.CollectionUtils.isEmpty(updateReqVO.getIds())){
            throw exception(CONSIGNMENT_SIGN_MATERIAL_ERROR);
        }
        updateReqVO.setLocationId(IN_WARHOUSE);
        consignmentInfoService.signMaterial(updateReqVO);
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除收货产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:delete')")
    public CommonResult<Boolean> deleteConsignmentInfo(@RequestParam("id") String id) {
        consignmentInfoService.deleteConsignmentInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收货产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:query')")
    public CommonResult<ConsignmentInfoRespVO> getConsignmentInfo(@RequestParam("id") String id) {
        ConsignmentInfoDO consignmentInfo = consignmentInfoService.getConsignmentInfo(id);
        return success(BeanUtils.toBean(consignmentInfo, ConsignmentInfoRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收货产品分页")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:query')")
    public CommonResult<PageResult<ConsignmentInfoRespVO>> getConsignmentInfoPage(@Valid ConsignmentInfoPageReqVO pageReqVO) {
        PageResult<ConsignmentInfoDO> pageResult = consignmentInfoService.getConsignmentInfoPage(pageReqVO);
        if (org.springframework.util.CollectionUtils.isEmpty(pageResult.getList())) {
            return success(new PageResult<ConsignmentInfoRespVO>(null, pageResult.getTotal()));
        }
        //主单信息
        List<String> consignmentIds = pageResult.getList().stream().map(ConsignmentInfoDO::getConsignmentId).distinct().collect(Collectors.toList());

        List<ConsignmentDO> consignmentDOS = purchaseConsignmentService.getConsignmentDetailByIds(consignmentIds);

        Map<String, ConsignmentDO> consignmentDOMap = CollectionUtils.convertMap(consignmentDOS, ConsignmentDO::getId);

        //合同信息
        List<String> contractIds = pageResult.getList().stream().map(ConsignmentInfoDO::getContractId).distinct().collect(Collectors.toList());

        List<ContractDO> contractDOS = contractService.getContractListByIds(contractIds);
        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        //项目信息
        List<String> projectIds = pageResult.getList().stream().map(ConsignmentInfoDO::getProjectId).distinct().collect(Collectors.toList());
        Map<String, PmsApprovalDto> map = new HashMap<>();
        try {
            map = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {

        }


        //物料信息
        List<String> materialConfigIds = pageResult.getList().stream().map(ConsignmentInfoDO::getMaterialConfigId).distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> configRespDTOMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        List<ConsignmentInfoRespVO> respVOS = new ArrayList<>();

        for (ConsignmentInfoDO infoDO : pageResult.getList()) {
            ConsignmentInfoRespVO respVO = BeanUtils.toBean(infoDO, ConsignmentInfoRespVO.class);
            ConsignmentDO consignmentDO = consignmentDOMap.get(infoDO.getConsignmentId());
            ContractDO contractDO = contractDOMap.get(infoDO.getContractId());
            PmsApprovalDto pmsApprovalDto = map.get(infoDO.getProjectId());
            MaterialConfigRespDTO a = configRespDTOMap.get(infoDO.getMaterialConfigId());

            respVO.setNo(consignmentDO.getNo()).setName(consignmentDO.getName());
            respVO.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialConfigId(a.getId());

            if (contractDO != null) {
                respVO.setContractCode(contractDO.getNumber()).setContractName(contractDO.getName());
            }
            if (pmsApprovalDto != null) {
                respVO.setProjectName(pmsApprovalDto.getProjectName()).setProjectCode(pmsApprovalDto.getProjectCode());
            }
            respVOS.add(respVO);
        }

        return success(new PageResult<ConsignmentInfoRespVO>(respVOS, pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收货产品 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportConsignmentInfoExcel(@Valid ConsignmentInfoPageReqVO pageReqVO,
                                           HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConsignmentInfoDO> list = consignmentInfoService.getConsignmentInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收货产品.xls", "数据", ConsignmentInfoRespVO.class,
                BeanUtils.toBean(list, ConsignmentInfoRespVO.class));
    }


    @GetMapping("/detailPage")
    @Operation(summary = "获得收货产品分页")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-info:query')")
    public CommonResult<PageResult<PurchaseConsignmentDetailRespVO>> getConsignmentDetailPage(@Valid PurchaseConsignmentDetailPageReqVO pageReqVO) {

        PageResult<ConsignmentDetailDO> pageResult = purchaseConsignmentDetailService.getPurchaseConsignmentDetailPage(pageReqVO);

        List<ConsignmentDetailDO> consignmentDetailDO = pageResult.getList();

        //获取订单下每个产品的ID
        List<String> materialIds = convertList(consignmentDetailDO, ConsignmentDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);

        return success(new PageResult<>(PurchaseConsignmentDetailConvert.INSTANCE.convertList(pageResult.getList(), map),
                pageResult.getTotal()
        ));
    }

}
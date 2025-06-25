package com.miyu.module.ppm.controller.admin.dmcontract;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationReqListDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsOrderApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderListDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderRespDTO;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractPageReqVO;
import com.miyu.module.ppm.controller.admin.dmcontract.vo.ContractProjectPageReqVO;
import com.miyu.module.ppm.controller.admin.dmcontract.vo.ContractRespVO;
import com.miyu.module.ppm.controller.admin.dmcontract.vo.ProjectOrderDetailRespVO;
import com.miyu.module.ppm.controller.admin.dmcontract.vo.ProjectOrderRespVO;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import com.miyu.module.ppm.service.contractrefund.ContractRefundService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
import com.miyu.module.ppm.service.shippingreturn.ShippingReturnService;
import com.miyu.module.ppm.service.shippingreturndetail.ShippingReturnDetailService;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;

import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.MapUtils.findAndThen;

@Tag(name = "管理后台 - 购销合同")
@RestController
@RequestMapping("/ppm/dmcontract")
@Validated
public class DMContractController {


    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private ContractApi contractApi;
    @Resource
    private ShippingService shippingService;
    @Resource
    private ShippingReturnService shippingReturnService;
    @Resource
    private ContractRefundService contractRefundService;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private PmsOrderApi pmsOrderApi;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Resource
    private ShippingReturnDetailService shippingReturnDetailService;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;


    @GetMapping("/get-contract-detail-info-by-id")
    @Operation(summary = "获得购销合同")
    @Parameter(name = "id", description = "合同iD", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<ContractRespVO> getContractDetailInfoById(@RequestParam("id") String id) {
        // 合同信息
        CommonResult<ContractRespDTO> result = contractApi.getContractDetailInfoById(id);
        if (result.getCheckedData() == null) {
            return success(new ContractRespVO());
        }
        ContractRespDTO respDTO = result.getCheckedData();

        Map<String, CompanyRespDTO> companyRespDTOMap = companyApi.getCompanyMap(Lists.newArrayList(respDTO.getParty()));
        // 产品主键获取产品物料属性
        Map<String, MaterialConfigRespDTO> productMap = materialMCCApi.getMaterialConfigMap(
                convertSet(respDTO.getOrders(), obj -> obj.getMaterialId()));
        // 封装产品属性
        List<ContractRespVO.Product> productList = BeanUtils.toBean(respDTO.getOrders(), ContractRespVO.Product.class, vo -> {
            // 设置单位
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setMaterialUnit(product.getMaterialUnit()));
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setMaterialName(product.getMaterialName()));
        });

        List<Long> userIdList = new ArrayList<>();
        //合并用户集合
        if (respDTO.getSelfContact() != null) userIdList.add(NumberUtils.parseLong(respDTO.getSelfContact()));
        if (respDTO.getPurchaser() != null) userIdList.add(NumberUtils.parseLong(respDTO.getPurchaser()));

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);
        HashSet<Long> deptSet = new HashSet<>();
        if (respDTO.getDepartment() != null) deptSet.add(Long.parseLong(respDTO.getDepartment()));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(deptSet);


        //发货单
        List<ShippingDTO> shippingDTOS = shippingService.getShippingListByContractIds(Lists.newArrayList(respDTO.getId()));
        //退货单
        List<ShippingReturnDTO> shippingReturnDTOS = shippingReturnService.getShippingReturnListByContractIds(Lists.newArrayList(respDTO.getId()));

        List<ContractRefundDO> refundList = contractRefundService.getContractRefundByContractId(respDTO.getId());


        return success(BeanUtils.toBean(respDTO, ContractRespVO.class, vo -> {
            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());
            vo.setPartyName(companyRespDTOMap.get(vo.getParty()).getName());
            vo.setShippings(shippingDTOS);
            vo.setProducts(productList);
            vo.setPayments(respDTO.getPayments());
            vo.setShippingReturns(shippingReturnDTOS);
            vo.setContractStatus(respDTO.getStatus());
            vo.setContractRefunds(refundList);
        }));
    }


    @GetMapping("/getContractInfoByProjectId")
    @Operation(summary = "根据项目获得购销合同")
    @Parameter(name = "id", description = "合同iD", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ContractRespVO>> getContractInfoByProjectId(@RequestParam("id") String id) {
        // 合同信息
        CommonResult<List<ContractRespDTO>> result = contractApi.getContractListByProjectIds(Lists.newArrayList(id), 2);


        return success(BeanUtils.toBean(result.getCheckedData(), ContractRespVO.class));
    }


    @GetMapping("/getProductList")
    @Operation(summary = "获取项目")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<PmsApprovalDto>> getProductList(ContractProjectPageReqVO pageReqVO) {
        //获取项目列表
        CommonResult<List<PmsApprovalDto>> result = pmsApi.getApprovalList(pageReqVO.getProjectCode());

//        PageResult<PmsApprovalDto> pageResult = new PageResult();
//        if (!CollectionUtils.isEmpty(result.getCheckedData())){
//            List<PmsApprovalDto> list = result.getCheckedData();
//            int count = list.size(); // 总记录数
//            // 计算总页数
//            int pages = count % pageReqVO.getPageSize() == 0 ? count / pageReqVO.getPageSize() : count / pageReqVO.getPageSize() + 1;
//            // 起始位置
//            int start = pageReqVO.getPageNo() <= 0 ? 0 : (pageReqVO.getPageNo() > pages ? (pages - 1) * pageReqVO.getPageSize() : (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize());
//            // 终止位置
//            int end = pageReqVO.getPageNo() <= 0 ? (pageReqVO.getPageSize() <= count ? pageReqVO.getPageSize() : count) : (pageReqVO.getPageSize() * pageReqVO.getPageNo() <= count ? pageReqVO.getPageSize() * pageReqVO.getPageNo() : count);
//
//            pageResult.setTotal((long)count);
//            pageResult.setList(list.subList(start, end));
//        }

        return CommonResult.success(result.getCheckedData());
    }




    /***
     *
     * @param type  类型  1 查毛坯  2 查成品
     * @return
     */
    @GetMapping("/getProductOrderList")
    @Operation(summary = "获取项目订单")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ProjectOrderRespVO>> getProductOrderList(@RequestParam("type") Integer type,@RequestParam("projectId") String projectId) {

        //获取订单信息
        CommonResult<List<OrderListDTO>> result = pmsOrderApi.getOrderItemList(projectId);

        List<ProjectOrderRespVO> list = BeanUtils.toBean(result.getCheckedData(), ProjectOrderRespVO.class);
        Set<String> codes = convertSet(list, obj -> type.intValue()==1?obj.getMaterialNumber():obj.getPartNumber());
        Set<String> projectIds = convertSet(list, obj -> obj.getProjectId());
        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigCodeMap(codes);

        Map<String, List<ContractOrderDTO>> contractMap = contractApi.getContractOrderMapByProjectIds(projectIds, 2);
        for (ProjectOrderRespVO vo1 : list) {
            List<ContractOrderDTO> dtos = contractMap.get(vo1.getProjectId());
            Integer number = 0;
            if (!CollectionUtils.isEmpty(dtos)) {
                for (ContractOrderDTO dto : dtos) {
                    number = number + dto.getQuantity().intValue();
                }
            }
            MaterialConfigRespDTO respDTO = map.get(type.intValue()==1?vo1.getMaterialNumber():vo1.getPartNumber());
            vo1.setCanQuantity(vo1.getQuantity() - number);
            vo1.setMaterialUnit(respDTO !=null?respDTO.getMaterialUnit():null);
            vo1.setMaterialConfigId(respDTO !=null?respDTO.getId():null);
            vo1.setMaterialName(respDTO !=null?respDTO.getMaterialName():null);
        }

        return CommonResult.success(list);
    }


    /***
     * 销售发货用
     * @param id
     * @return
     */
    @GetMapping("/getProjectOrderByProjectId")
    @Operation(summary = "根据项目获取项目原始物料信息")
    @Parameter(name = "id", description = "项目iD", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ProjectOrderDetailRespVO>> getProjectOrderByProjectId(@RequestParam("id") String id) {
        // 查询项目订单详细
        CommonResult<List<OrderListDTO>> orderResult = pmsOrderApi.getOrderItemList(id);

        OrderMaterialRelationReqListDTO orderMaterialRelationReqListDTO = new OrderMaterialRelationReqListDTO();
        List<String> orderIds = orderResult.getCheckedData().stream().map(OrderListDTO::getId).collect(Collectors.toList());
        orderMaterialRelationReqListDTO.setOrderIds(orderIds);

        CommonResult<List<OrderMaterialRelationRespDTO>> result = pmsOrderMaterialRelationApi.getRelationByPlanOrOrderIds(orderMaterialRelationReqListDTO);
        List<ProjectOrderDetailRespVO> list = BeanUtils.toBean(result.getCheckedData(),ProjectOrderDetailRespVO.class);

        Set<String> barcode = convertSet(list, obj -> obj.getVariableCode());
        String code = list.get(0).getVariableCode();
        CommonResult<List<MaterialStockRespDTO>> commonResult = materialStockApi.getMaterialsByBarCodes(barcode);

        List<MaterialStockRespDTO> respDTOS = commonResult.getCheckedData();

        Set<String> configIds = convertSet(respDTOS, obj -> obj.getMaterialConfigId());


//
//        CommonResult<List<MaterialStockRespDTO>> stockResult = materialStockApi.getMaterialsByConfigIds(configIds);
//        //CommonResult<List<MaterialConfigRespDTO>> materialConfigMapp =  materialMCCApi.getMaterialConfigList(configIds);
        //TODO  根据条码查询物料信息
        Map<String, MaterialConfigRespDTO>  materialConfigMap = materialMCCApi.getMaterialConfigMap(configIds);

        //key  barcode  value 物料属性
        Map<String,MaterialConfigRespDTO> map  =new HashMap<>();
        Map<String,MaterialStockRespDTO> stockRespDTOMap  =new HashMap<>();

        for (MaterialStockRespDTO respDTO :respDTOS){
            MaterialConfigRespDTO configRespDTO = materialConfigMap.get(respDTO.getMaterialConfigId());
            map.put(respDTO.getBarCode(),configRespDTO);
            stockRespDTOMap.put(respDTO.getBarCode(),respDTO);
        }

        List<ShippingDetailDO> shippingDetailDOList = shippingDetailService.getDetailByProjectId(id,null,null);

        List<String> barCodes = CollectionUtils.isEmpty(shippingDetailDOList) ? new ArrayList<>() : shippingDetailDOList.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
        List<String> detailIds = CollectionUtils.isEmpty(shippingDetailDOList) ? new ArrayList<>() : shippingDetailDOList.stream().map(ShippingDetailDO::getId).collect(Collectors.toList());
        List<String> returnBarCodes = new ArrayList<>();
        Map<String, Integer> pushMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(barCodes)) {
            for (String element : barCodes) {
                pushMap.put(element, pushMap.getOrDefault(element, 0) + 1);
            }
        }

        Map<String, Integer> returnMap = new HashMap<>();

        List<ProjectOrderDetailRespVO> finalList = new ArrayList<>();
        for (ProjectOrderDetailRespVO vo : list) {
            if (!barCodes.contains(vo.getProductCode())) {//如果发货单里没有该条码 则可选择
                ProjectOrderDetailRespVO vo1 = BeanUtils.toBean(vo, ProjectOrderDetailRespVO.class);
                finalList.add(vo1);
            } else {//如果发货单有该条码 但是有退货单   并且出货的次数和退货的次数相等  则仍然可以选择
                if (returnBarCodes.contains(vo.getProductCode())) {
                    if (pushMap.get(vo.getProductCode()).equals(returnMap.get(vo.getProductCode()))) {
                        ProjectOrderDetailRespVO vo1 = BeanUtils.toBean(vo, ProjectOrderDetailRespVO.class);
                        finalList.add(vo1);
                    }
                }
            }
        }
        finalList.forEach(projectOrderDetailRespVO -> {
            MaterialConfigRespDTO a = map.get(projectOrderDetailRespVO.getVariableCode());
            projectOrderDetailRespVO.setMaterialConfigId(a.getId()).setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setQuantity(1);
            MaterialStockRespDTO respDTO = stockRespDTOMap.get(projectOrderDetailRespVO.getVariableCode());
            projectOrderDetailRespVO.setMaterialStockId(respDTO.getId());
            projectOrderDetailRespVO.setBatchNumber(respDTO.getBatchNumber());
        });

        return success(BeanUtils.toBean(finalList, ProjectOrderDetailRespVO.class));
    }


    /***
     * 获取物料信息
     * @param id
     * @return
     */
    @GetMapping("/getMaterialStockByProjectId")
    @Operation(summary = "根据项目获取项目原始物料信息")
    @Parameter(name = "id", description = "项目iD", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ProjectOrderDetailRespVO>> getMaterialStockByProjectId(@RequestParam("id") String id) {
        // 查询项目订单详细
        CommonResult<List<OrderListDTO>> orderResult = pmsOrderApi.getOrderItemList(id);


        //查看图号（物料类型码）
        Set<String> typeCodes = convertSet(orderResult.getCheckedData(), obj -> obj.getPartNumber());

        //根据物料类型码查询物料类型
        CommonResult<List<MaterialConfigRespDTO>> commonResult = materialMCCApi.getMaterialConfigListByCode(typeCodes);

        Map<String, MaterialConfigRespDTO>  configRespDTOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(commonResult.getCheckedData(), MaterialConfigRespDTO::getId);
        Set<String> configIds = convertSet(commonResult.getCheckedData(), obj -> obj.getId());

        //根据物料类型ID集合查询物料库存
        CommonResult<List<MaterialStockRespDTO>> stockResult = materialStockApi.getMaterialsByConfigIds(configIds);

        Set<String> codes = convertSet(stockResult.getCheckedData(), obj -> obj.getBarCode());
        //根据物料条码 查看归属项目
        CommonResult<List<OrderMaterialRelationRespDTO>> listCommonResult = pmsOrderMaterialRelationApi.selectByMaterialCodes(codes);
        Map<String, OrderMaterialRelationRespDTO>  relationRespDTOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(listCommonResult.getCheckedData(), OrderMaterialRelationRespDTO::getVariableCode);

        List<ProjectOrderDetailRespVO> respVOS = new ArrayList<>();
        stockResult.getCheckedData().forEach(stockRespDTO -> {
            MaterialConfigRespDTO a = configRespDTOMap.get(stockRespDTO.getMaterialConfigId());
            OrderMaterialRelationRespDTO respDTO = relationRespDTOMap.get(stockRespDTO.getBarCode());
            ProjectOrderDetailRespVO respVO = new ProjectOrderDetailRespVO();

            if (respDTO !=null){
                respVO = BeanUtils.toBean(respDTO,ProjectOrderDetailRespVO.class);
            }
            respVO.setVariableCode(stockRespDTO.getBarCode());
            respVO.setMaterialConfigId(a.getId()).setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setQuantity(1);
            respVO.setMaterialStockId(stockRespDTO.getId());
            respVO.setId(stockRespDTO.getId());
            respVO.setQuantity(stockRespDTO.getTotality());
            respVO.setBatchNumber(stockRespDTO.getBatchNumber());
            respVOS.add(respVO);
        });

        return success(respVOS);
    }

}
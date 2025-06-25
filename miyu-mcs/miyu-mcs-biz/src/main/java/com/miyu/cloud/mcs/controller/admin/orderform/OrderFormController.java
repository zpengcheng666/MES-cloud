package com.miyu.cloud.mcs.controller.admin.orderform;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepDetailRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

import com.miyu.cloud.mcs.controller.admin.orderform.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.service.orderform.OrderFormService;

@Tag(name = "管理后台 - 生产订单")
@RestController
@RequestMapping("/mcs/order-form")
@Validated
public class OrderFormController {

    @Resource
    private OrderFormService orderFormService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private TechnologyRestService technologyRestService;

    @PostMapping("/create")
    @Operation(summary = "创建生产订单")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:create')")
    public CommonResult<String> createOrderForm(@Valid @RequestBody OrderFormSaveReqVO createReqVO) {
        String orderForm = orderFormService.createOrderFormIntegral(createReqVO);
        return success(orderForm);
    }

    @PutMapping("/update")
    @Operation(summary = "更新生产订单")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:update')")
    public CommonResult<Boolean> updateOrderForm(@Valid @RequestBody OrderFormSaveReqVO updateReqVO) {
        orderFormService.updateOrderForm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除生产订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:order-form:delete')")
    public CommonResult<Boolean> deleteOrderForm(@RequestParam("id") String id) {
        orderFormService.deleteOrderForm(id);
        return success(true);
    }

    @PostMapping("/orderSubmit")
    @Operation(summary = "订单提交")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:update')")
    public CommonResult<String> orderSubmit(@RequestParam("id") String id) {
        try {
            orderFormService.orderSubmit(id);
            return success("校验成功, 已提交");
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @GetMapping("/get")
    @Operation(summary = "获得生产订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:query')")
    public CommonResult<OrderFormRespVO> getOrderForm(@RequestParam("id") String id) {
        OrderFormDO orderForm = orderFormService.getOrderForm(id);
        return success(BeanUtils.toBean(orderForm, OrderFormRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "生产订单集合")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:query')")
    public CommonResult<List<OrderFormRespVO>> list(OrderFormDO orderFormDO) {
        QueryWrapper<OrderFormDO> queryWrapper = new QueryWrapper<>(orderFormDO);
        List<OrderFormDO> orderFormList = orderFormService.list(queryWrapper);
        return success(BeanUtils.toBean(orderFormList, OrderFormRespVO.class));
    }

    @GetMapping("/listByIds")
    @Operation(summary = "生产订单集合")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:query')")
    public CommonResult<List<OrderFormRespVO>> list(@RequestParam("orderIds") String orderIds) {
        if (StringUtils.isBlank(orderIds)) return success(new ArrayList<>());
        List<String> list = Arrays.asList(orderIds.split(","));
        QueryWrapper<OrderFormDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", list);
        List<OrderFormDO> orderFormList = orderFormService.list(queryWrapper);
        Set<Long> userIds = orderFormList.stream().filter(item -> item.getResponsiblePerson() != null).map(item -> Long.parseLong(item.getResponsiblePerson())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));
        List<OrderFormRespVO> data = BeanUtils.toBean(orderFormList, OrderFormRespVO.class);
        for (OrderFormRespVO orderForm : data) {
            orderForm.setResponsiblePersonName(userMap.get(orderForm.getResponsiblePerson()));
        }
        return success(data);
    }

    @GetMapping("/list1")
    @Operation(summary = "生产订单集合")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:query')")
    public CommonResult<List<OrderFormRespVO>> list1(OrderFormSelectListRespVO listRespVO) {
        List<OrderFormDO> orderFormList = orderFormService.getOrderFormSelectList(listRespVO);
        return success(BeanUtils.toBean(orderFormList, OrderFormRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得生产订单分页")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:query')")
    public CommonResult<PageResult<OrderFormRespVO>> getOrderFormPage(@Valid OrderFormPageReqVO pageReqVO) {
        PageResult<OrderFormDO> pageResult = orderFormService.getOrderFormPage(pageReqVO);
        Set<Long> userIds = pageResult.getList().stream().filter(item -> item.getResponsiblePerson() != null).map(item -> Long.parseLong(item.getResponsiblePerson())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));
        PageResult<OrderFormRespVO> data = BeanUtils.toBean(pageResult, OrderFormRespVO.class);
        for (OrderFormRespVO orderForm : data.getList()) {
            orderForm.setResponsiblePersonName(userMap.get(orderForm.getResponsiblePerson()));
        }
        return success(data);
    }

    @GetMapping("/getListByProjectPlanId")
    @Operation(summary = "获得生产订单分页")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:query')")
    public CommonResult<List<OrderFormRespVO>> getOrderFormPage(@RequestParam("orderIds") String orderIds) {
        if (StringUtils.isBlank(orderIds)) return success(new ArrayList<>());
        List<String> list = Arrays.asList(orderIds.split(","));
        LambdaQueryWrapper<OrderFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.notIn(OrderFormDO::getStatus, MCS_ORDER_STATUS_COMPLETED, MCS_ORDER_STATUS_RESCINDED);
        queryWrapper.in(OrderFormDO::getProjectPlanId, list);
        List<OrderFormDO> orderList = orderFormService.list(queryWrapper);
        Set<Long> userIds = orderList.stream().filter(item -> item.getResponsiblePerson() != null).map(item -> Long.parseLong(item.getResponsiblePerson())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));
        List<OrderFormRespVO> data = BeanUtils.toBean(orderList, OrderFormRespVO.class);
        for (OrderFormRespVO orderForm : data) {
            orderForm.setResponsiblePersonName(userMap.get(orderForm.getResponsiblePerson()));
        }
        return success(data);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出生产订单 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderFormExcel(@Valid OrderFormPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrderFormDO> list = orderFormService.getOrderFormPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "生产订单.xls", "数据", OrderFormRespVO.class,
                        BeanUtils.toBean(list, OrderFormRespVO.class));
    }

    @PostMapping("/orderCancel")
    @Operation(summary = "订单作废")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:delete')")
    public CommonResult<String> orderCancel(@RequestParam("id") String id) {
        try {
            orderFormService.orderCancel(id);
            return success("订单已作废");
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @PostMapping("/orderIssued")
    @Operation(summary = "订单下发")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:update')")
    public CommonResult<String> orderIssued(@RequestParam("id") String id) {
        try {
            orderFormService.orderIssued(id);
            return success("订单已下发");
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @PostMapping("/generateDemandByOrderIds")
    @Operation(summary = "批量生成订单需求")
    @PreAuthorize("@ss.hasPermission('mcs:order-form:update')")
    public CommonResult<String> generateDemandByOrderIds(@RequestBody List<String> orderIdList) {
        try {
            orderFormService.generateDemandByOrderIds(orderIdList);
            return success("操作成功");
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @GetMapping("/getProcessByOrderId")
    @Operation(summary = "根据订单id,获取工序集合")
    public CommonResult<?> getProcessByOrderId(@RequestParam("id") String id) {
        try {
            OrderFormDO orderForm = orderFormService.getOrderForm(id);
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(id, orderForm.getTechnologyId());
            List<ProcedureRespDTO> procedureList = technology.getProcedureList();
            List<ProcedureRespDTO> collect = procedureList.stream().filter(item -> !ProcedureRespDTO.isIgnoreProcedure(item)).collect(Collectors.toList());
            procedureList.forEach(process -> process.setResourceList(process.getResourceList().stream().filter(item -> item.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE).collect(Collectors.toList())));
            return success(collect);
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @GetMapping("/getLedgersByOrderAndProcess")
    @Operation(summary = "根据工艺获取可用设备")
    public CommonResult<?> getProcessByOrderId(@RequestParam("orderId") String orderId, @RequestParam("processId") String processId) {
        try {
            OrderFormDO orderForm = orderFormService.getOrderForm(orderId);
            ProcedureRespDTO process = technologyRestService.getProcessCache(orderId, orderForm.getTechnologyId(), processId);
            Set<String> unitTypeSet = process.getResourceList().stream().filter(item -> item.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE).map(ProcedureDetailRespDTO::getResourcesTypeId).collect(Collectors.toSet());
            Set<String> typeSet = process.getStepList().stream().flatMap(item -> item.getResourceList().stream()).filter(item -> item.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE).map(StepDetailRespDTO::getResourcesTypeId).collect(Collectors.toSet());
            if (unitTypeSet.size() == 0) return success(new ArrayList<>());
            List<LedgerDO> deviceList = ledgerService.getByLineTypeAndDeviceType(unitTypeSet, typeSet);
            return success(deviceList);
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @GetMapping("/getResourceDemandByOrderId")
    @Operation(summary = "根据订单id,获取资源需求集合")
    public CommonResult<?> getResourceDemandByOrderId(@RequestParam("orderIds") String orderIds) {
        try {
            return success(orderFormService.getResourceDemandByOrderId(Arrays.asList(orderIds.split(","))));
        } catch (ServiceException e) {
            return error(e);
        }
    }
}

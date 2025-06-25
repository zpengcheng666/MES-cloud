package cn.iocoder.yudao.module.pms.controller.admin.order;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCancelReqDto;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsOrderApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderRespDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderSaveReqDTO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderListRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderService;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
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
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

/**
 * TODO 先排除一切合同
 */
@Tag(name = "管理后台 - 项目订单")
@RestController
@RequestMapping("/pms/order")
@Validated
public class PmsOrderController {

    @Resource
    private PmsOrderService orderService;

    @Resource
    private ContractApi contractApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private PmsOrderApi pmsOrderApi;

    @PostMapping("/create")
    @Operation(summary = "创建项目订单")
    @PreAuthorize("@ss.hasPermission('pms:order:create')")
    public CommonResult<String> createOrder(@Valid @RequestBody PmsOrderSaveReqVO createReqVO) {
        return success(orderService.createOrder(createReqVO));
    }
    @PostMapping("/createOutOrder")
    @Operation(summary = "创建外部项目订单")
    public CommonResult<String> createOutOrder(@Valid @RequestBody PmsOrderSaveReqVO createReqVO) {
        //给物料牌号就是带料1,没给就是2
        if(ObjectUtil.isNotEmpty(createReqVO.getMaterialNumber())){
            createReqVO.setProcessType(1);
        }else {
            createReqVO.setProcessType(2);
        }
        return success(orderService.createOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新项目订单")
    @PreAuthorize("@ss.hasPermission('pms:order:update')")
    public CommonResult<Boolean> updateOrder(@Valid @RequestBody PmsOrderSaveReqVO updateReqVO) {
        orderService.updateOrder(updateReqVO);
        return success(true);
    }

    /**
     * 目前只允许改变物料牌号,如果他没有告诉我的话
     * @param updateReqVO
     * @return
     */
    @PutMapping("/updateOutOrder")
    @Operation(summary = "更新项目订单")
    @PreAuthorize("@ss.hasPermission('pms:order:update')")
    public CommonResult<Boolean> updateOutOrder(@Valid @RequestBody PmsOrderSaveReqVO updateReqVO) {
        orderService.updateOutOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除项目订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:order:delete')")
    public CommonResult<Boolean> deleteOrder(@RequestParam("id") String id) {
        orderService.deleteOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得项目订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
    public CommonResult<PmsOrderRespVO> getOrder(@RequestParam("id") String id) {
        PmsOrderDO order = orderService.getOrder(id);
        return success(BeanUtils.toBean(order, PmsOrderRespVO.class));
    }

    /**
     * 通过订单id
     * 和上面那个一样,不过要检测物料存不存在
     */
    @GetMapping("/getById")
    @Operation(summary = "获得项目订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
    public CommonResult<List<PmsOrderRespVO>> getOrderById(@RequestParam("id") String id) {
        //配合前端的列表，还是选择用list
        List<PmsOrderRespVO> list = new ArrayList<>();
        PmsOrderDO order = orderService.getOrder(id);
        List<String> ids = new ArrayList<>();
        ids.add(order.getMaterialNumber());
        ids.add(order.getPartNumber());

        PmsOrderRespVO pmsOrderRespVO = BeanUtils.toBean(order, PmsOrderRespVO.class);
        MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
        try {


            //查物料
//            Map<String, MaterialConfigRespDTO> materialConfigMap = materialMCCApi.getMaterialConfigMap(ids);
            if(ObjectUtil.isNotEmpty(order.getMaterialNumber())){
                dto.setMaterialNumber(order.getMaterialNumber());
                List<MaterialConfigRespDTO> list1 = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
                if(list1.size()>0){
                    pmsOrderRespVO.setMaterialStatus(1);
                }
            }
            if(ObjectUtil.isNotEmpty(order.getPartNumber())){
                dto.setMaterialNumber(order.getPartNumber());
                List<MaterialConfigRespDTO> list2 = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
                if(list2.size()>0){
                    pmsOrderRespVO.setProductStatus(1);
                }
            }
//            if (materialConfigMap!=null&&materialConfigMap.containsKey(order.getMaterialNumber())){
//                pmsOrderRespVO.setMaterialStatus(1);
//            }
//
//            if (materialConfigMap!=null&&materialConfigMap.containsKey(order.getPartNumber())){
//                pmsOrderRespVO.setProductStatus(1);
//            }
            list.add(pmsOrderRespVO);
            return success(list);
        }catch (Exception e){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.Handle_ORDER_MCC_NOT_EXISTS);
//            e.printStackTrace();
        }
//        throw ServiceExceptionUtil.exception(ErrorCodeConstants.Handle_ORDER_MCC_NOT_EXISTS);
        //改从前端判断了
//        PmsOrderRespVO pmsOrderRespVO = BeanUtils.toBean(order, PmsOrderRespVO.class);
////        list.add(pmsOrderRespVO);
////        return success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "获得项目订单分页")
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
    public CommonResult<PageResult<PmsOrderRespVO>> getOrderPage(@Valid PmsOrderPageReqVO pageReqVO) {
        PageResult<PmsOrderDO> pageResult = orderService.getOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PmsOrderRespVO.class));
    }

    @GetMapping("/all")
    @Operation(summary = "获得所有项目订单")
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
    public CommonResult<List<PmsOrderRespVO>> getListAll() {
        List<PmsOrderDO> listAll = orderService.getListAll();
        return success(BeanUtils.toBean(listAll, PmsOrderRespVO.class));
    }



    @GetMapping("/export-excel")
    @Operation(summary = "导出项目订单 Excel")
    @PreAuthorize("@ss.hasPermission('pms:order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderExcel(@Valid PmsOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PmsOrderDO> list = orderService.getOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "项目订单.xls", "数据", PmsOrderRespVO.class,
                        BeanUtils.toBean(list, PmsOrderRespVO.class));
    }
    @GetMapping("/listMaterialByTypeCode")
    @Operation(summary = "根据物料类型查询物料")
    public CommonResult<List<MaterialConfigRespDTO>> getAllMaterialConfig(@RequestParam("typeCode") String typeCode){
        //如果传空串"",就是查全部了
        MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
        dto.setMaterialTypeCode(typeCode);
        List<MaterialConfigRespDTO> list = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
        return success(list);

    }

    @GetMapping("/getOrderByProjectId")
    @Operation(summary = "根据物料类型查询物料")
    public CommonResult<List<PmsOrderRespVO>> getOrderByProjectId(@RequestParam("projectId") String projectId){
        List<PmsOrderDO> orderList = orderService.getOrderByProject(projectId);
        return success(BeanUtils.toBean(orderList,PmsOrderRespVO.class));
    }

//    @PostMapping("/testCreateOutOrderAPI")
//    @Operation(summary = "外部订单创建测试")
//    public CommonResult<String> testCreateOutOrderAPI(@RequestBody PmsOrderSaveReqDTO dto){
//        String checkedData = pmsOrderApi.createOrder(dto).getCheckedData();
//        return success("checkedData");
//
//    }

    /**
     * 解除项目与订单之间的关联
     * @param id
     * @return
     */
    @PostMapping("/unbind")
    @Operation(summary = "项目解绑")
    public CommonResult<Boolean> unbind(@RequestParam("id") String id){
        orderService.unbind(id);
        return success(true);
    }

    // ==================== 子表（项目订单表子） ====================

    @GetMapping("/order-list/page")
    @Operation(summary = "获得项目订单表子分页")
    @Parameter(name = "projectOrderId", description = "项目订单id")
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
    public CommonResult<PageResult<OrderListDO>> getOrderListPage(PageParam pageReqVO,
                                                                                        @RequestParam("projectOrderId") String projectOrderId) {
        return success(orderService.getOrderListPage(pageReqVO, projectOrderId));
    }

    @PostMapping("/order-list/create")
    @Operation(summary = "创建项目订单表子")
    @PreAuthorize("@ss.hasPermission('pms:order:create')")
    public CommonResult<String> createOrderList(@Valid @RequestBody OrderListDO orderList) {
        return success(orderService.createOrderList(orderList));
    }

    @PutMapping("/order-list/update")
    @Operation(summary = "更新项目订单表子")
    @PreAuthorize("@ss.hasPermission('pms:order:update')")
    public CommonResult<Boolean> updateOrderList(@Valid @RequestBody OrderListDO orderList) {
        orderService.updateOrderList(orderList);
        return success(true);
    }

    @DeleteMapping("/order-list/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除项目订单表子")
    @PreAuthorize("@ss.hasPermission('pms:order:delete')")
    public CommonResult<Boolean> deleteOrderList(@RequestParam("id") String id) {
        orderService.deleteOrderList(id);
        return success(true);
    }

	@GetMapping("/order-list/get")
	@Operation(summary = "获得项目订单表子")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
	public CommonResult<OrderListDO> getOrderList(@RequestParam("id") String id) {
	    return success(orderService.getOrderList(id));
	}

    @GetMapping("/order-list/list-by-project-order-id")
    @Operation(summary = "获得项目订单表子列表")
    @Parameter(name = "projectOrderId", description = "项目订单id")
    @PreAuthorize("@ss.hasPermission('pms:order:query')")
    public CommonResult<List<OrderListRespVO>> getOrderListByProjectOrderId(@RequestParam("projectOrderId") String projectOrderId) {
        List<OrderListDO> list = orderService.getOrderListListByProjectOrderId(projectOrderId);
        return success(BeanUtils.toBean(list, OrderListRespVO.class));
    }

}

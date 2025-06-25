package cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationSaveReqDTO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.service.orderMaterialRelation.OrderMaterialRelationService;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
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

@Tag(name = "管理后台 - 订单物料关系表")
@RestController
@RequestMapping("/pms/order-material-relation")
@Validated
public class OrderMaterialRelationController {

    @Resource
    private OrderMaterialRelationService orderMaterialRelationService;

    @Resource
    private PmsPlanService pmsPlanService;

//    @Resource
//    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;

    @PostMapping("/create")
    @Operation(summary = "创建订单物料关系表")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:create')")
    public CommonResult<String> createOrderMaterialRelation(@Valid @RequestBody OrderMaterialRelationSaveReqVO createReqVO) {
        return success(orderMaterialRelationService.createOrderMaterialRelation(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单物料关系表")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:update')")
    public CommonResult<Boolean> updateOrderMaterialRelation(@Valid @RequestBody OrderMaterialRelationSaveReqVO updateReqVO) {
        orderMaterialRelationService.updateOrderMaterialRelation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单物料关系表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:delete')")
    public CommonResult<Boolean> deleteOrderMaterialRelation(@RequestParam("id") String id) {
        orderMaterialRelationService.deleteOrderMaterialRelation(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单物料关系表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:query')")
    public CommonResult<OrderMaterialRelationRespVO> getOrderMaterialRelation(@RequestParam("id") String id) {
        OrderMaterialRelationDO orderMaterialRelation = orderMaterialRelationService.getOrderMaterialRelation(id);
        return success(BeanUtils.toBean(orderMaterialRelation, OrderMaterialRelationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单物料关系表分页")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:query')")
    public CommonResult<PageResult<OrderMaterialRelationRespVO>> getOrderMaterialRelationPage(@Valid OrderMaterialRelationPageReqVO pageReqVO) {
        PageResult<OrderMaterialRelationDO> pageResult = orderMaterialRelationService.getOrderMaterialRelationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OrderMaterialRelationRespVO.class));
    }

    @GetMapping("/getOrderMaterialRelationWith")
    @Operation(summary = "条件查询")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:query')")
    public CommonResult<List<OrderMaterialRelationRespVO>> getOrderMaterialRelationWith(OrderMaterialRelationSaveReqVO req) {
        List<OrderMaterialRelationDO> orderMaterialRelationWith = orderMaterialRelationService.getOrderMaterialRelationWith(req);
        return success(BeanUtils.toBean(orderMaterialRelationWith,OrderMaterialRelationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单物料关系表 Excel")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderMaterialRelationExcel(@Valid OrderMaterialRelationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrderMaterialRelationDO> list = orderMaterialRelationService.getOrderMaterialRelationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单物料关系表.xls", "数据", OrderMaterialRelationRespVO.class,
                        BeanUtils.toBean(list, OrderMaterialRelationRespVO.class));
    }

    @GetMapping("/getRelationListByOrderId")
    @Operation(summary = "根据项目订单,获得订单物料关系表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:query')")
    public CommonResult<List<OrderMaterialRelationRespVO>> getRelationListByOrderId(@RequestParam("id") String id) {
        OrderMaterialRelationDO orderMaterialRelation = orderMaterialRelationService.getOrderMaterialRelation(id);
        List<OrderMaterialRelationDO> list = orderMaterialRelationService.getRelationListByOrderId(id);
        return success(BeanUtils.toBean(list, OrderMaterialRelationRespVO.class));
    }

//    /**
//     * 获取计划类型剩余的可分配数量
//     * 应该也用不到了
//     * @param projectPlanId
//     * @param planType
//     * @return
//     */
//    @GetMapping("/getSelectedAmount")
//    public CommonResult<Integer> getSelectedAmount(@RequestParam("projectPlanId")String projectPlanId,@RequestParam("planType")Integer planType){
//        PmsPlanDO plan = pmsPlanService.getPlan(projectPlanId);
//        List<OrderMaterialRelationDO> relationList = orderMaterialRelationService.getRelationListByOrderId(plan.getProjectOrderId());
//        int amount = 0;
//        //普通加工
//        if(planType==1){
//            int total = plan.getQuantity() - plan.getOutSourceAmount() - plan.getStepOutSourceAmount();
//            List<OrderMaterialRelationDO> collect = relationList.stream().filter((item)-> ObjectUtil.isNotEmpty(item.getPlanType())).filter((item) -> item.getPlanType() == 1).collect(Collectors.toList());
//            amount = total - collect.size();
//
//        }
//        //外协
//        if(planType==2){
//            int total = plan.getOutSourceAmount();
//            List<OrderMaterialRelationDO> collect = relationList.stream().filter((item)-> ObjectUtil.isNotEmpty(item.getPlanType())).filter((item) -> item.getPlanType() == 2).collect(Collectors.toList());
//            amount = total - collect.size();
//        }
//        //不是外协,是外协不带料
//        if(planType==3){
//            int total = plan.getStepOutSourceAmount();
//            List<OrderMaterialRelationDO> collect = relationList.stream().filter((item)-> ObjectUtil.isNotEmpty(item.getPlanType())).filter((item) -> item.getPlanType() == 3).collect(Collectors.toList());
//            amount = total - collect.size();
//        }
//        return success(amount);
//    }

    /**
     * 备料更新,选择空关系,填写物料码,prepare改为1
     * @param createReqVO
     * @return
     */
    @PostMapping("/prepareUpdate")
    @Operation(summary = "备料,订单物料关系更新")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:create')")
    public CommonResult<Boolean> prepareUpdate(@Valid @RequestBody OrderMaterialRelationSaveReqVO createReqVO) {
        orderMaterialRelationService.prepareUpdate(createReqVO);
        return success(true);
    }

    @GetMapping("selectCompleteMaterialCodeByRelationId")
    @Operation(summary = "根据关系查询可用的物码,整单外协用")
    public CommonResult<List<MaterialStockRespDTO>> selectCompleteMaterialCodeByRelationId(@RequestParam("id") String id){
        List<MaterialStockRespDTO> list = orderMaterialRelationService.selectCompleteMaterialCodeByRelationId(id);
        return success(list);
    }

    /**
     * 外协完成的替换策略
     * 有码的还是查码
     * 没码的不是查库存，而是通过主计划查收货单
     * @param id
     * @return
     */
    @GetMapping("selectCompleteMaterialCodeByRelationId2")
    @Operation(summary = "根据关系查询可用的物码,整单外协用")
    public CommonResult<List<MaterialStockRespDTO>> selectCompleteMaterialCodeByRelationId2(@RequestParam("id") String id,@RequestParam("planId") String planId){
        List<MaterialStockRespDTO> list = orderMaterialRelationService.selectCompleteMaterialCodeByRelationId2(id,planId);
        return success(list);
    }

    @GetMapping("selectStorageMaterialCodeByRelationId")
    @Operation(summary = "根据关系查询可用的物码,工序外协用")
    public CommonResult<List<MaterialStockRespDTO>> selectStorageMaterialCodeByRelationId(@RequestParam("id") String id){
        List<MaterialStockRespDTO> list = orderMaterialRelationService.selectStorageMaterialCodeByRelationId(id);
        return success(list);
    }

    /**
     * 整单外协完成
     * @param req
     * @return
     */
    @PostMapping("outsourceComplete")
    @Operation(summary = "整单外协完成,修改物料状态")
    public CommonResult<Boolean> outsourceComplete(@RequestBody OrderMaterialRelationSaveReqVO req ){
        orderMaterialRelationService.outsourceComplete(req);
        return success(true);
    }
    /**
     * 外协入库，工序外协用
     * 重新选择外协后的码,更新物料状态，通知mcs继续生产
     * @param req
     * @return
     */
    @PostMapping("outsourceInStorage")
    @Operation(summary = "外协入库,修改物料状态")
    public CommonResult<Boolean> outsourceInStorage(@RequestBody OrderMaterialRelationSaveReqVO req ){
        orderMaterialRelationService.outsourceInStorage(req);
        return success(true);
    }

    @PostMapping("/addRelation")
    @Operation(summary = "创建订单物料关系,原物料废弃时使用，额外添加一个关系")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:create')")
    public CommonResult<String> addRelation(@Valid @RequestBody OrderMaterialRelationSaveReqVO createReqVO) {
        return success(orderMaterialRelationService.addRelation(createReqVO));
    }

    @DeleteMapping("/releaseCode")
    @Operation(summary = "释放关系码")
    @PreAuthorize("@ss.hasPermission('pms:order-material-relation:delete')")
    public CommonResult<String> releaseCode(@RequestParam("id") String id) {
        String code = orderMaterialRelationService.releaseCode(id);
        return success(code);
    }

//    @GetMapping("test")
//    public void test(@RequestParam("codes") List<String> codes){
//        List<OrderMaterialRelationRespDTO> checkedData = pmsOrderMaterialRelationApi.selectByMaterialCodes(codes).getCheckedData();
//        String checkedData1 = pmsOrderMaterialRelationApi.resetRelations(codes).getCheckedData();
//        System.out.println(checkedData);
//    }

}

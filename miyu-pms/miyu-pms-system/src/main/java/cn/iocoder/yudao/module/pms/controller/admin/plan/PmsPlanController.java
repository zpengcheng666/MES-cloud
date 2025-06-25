package cn.iocoder.yudao.module.pms.controller.admin.plan;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.plan.PlanApi;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.*;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import cn.iocoder.yudao.module.pms.service.orderMaterialRelation.OrderMaterialRelationService;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.pdm.api.projectPlan.PdmProjectPlanApi;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomReqDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ReturnMaterialDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.time.LocalDateTime;
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

@Tag(name = "管理后台 - 项目计划")
@RestController
@RequestMapping("/pms/plan")
@Validated
public class PmsPlanController {

    @Resource
    private PmsPlanService planService;

    @Resource
    private PdmProjectPlanApi projectPlanApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private OrderMaterialRelationService relationService;

    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;

    @Resource
    private ConsignmentReturnApi consignmentReturnApi;

//    @Resource
//    private PlanApi planApi;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;

    @PostMapping("/create")
    @Operation(summary = "创建项目计划")
    @PreAuthorize("@ss.hasPermission('pms:plan:create')")
    public CommonResult<String> createPlan(@Valid @RequestBody PmsPlanSaveReqVO createReqVO) {
        return success(planService.createPlan(createReqVO));
    }

    @PostMapping("/createBpm")
    @Operation(summary = "创建项目计划")
    @PreAuthorize("@ss.hasPermission('pms:plan:create')")
    public CommonResult<String> createPlanBpm(@Valid @RequestBody PmsPlanSaveReqVO createReqVO) {
        return success(planService.createPlanBpm(createReqVO));
    }
    @PostMapping("/createBpm2")
    @Operation(summary = "创建项目计划")
    @PreAuthorize("@ss.hasPermission('pms:plan:create')")
    public CommonResult<String> createPlanBpm2(@Valid @RequestBody PmsPlanSaveReqVO createReqVO) {
        return success(planService.createPlanBpm2(createReqVO.getId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新项目计划")
    @PreAuthorize("@ss.hasPermission('pms:plan:update')")
    public CommonResult<Boolean> updatePlan(@Valid @RequestBody PmsPlanSaveReqVO updateReqVO) {
        if(ObjectUtil.isNull(updateReqVO.getId())){
            planService.createPlan(updateReqVO);
        }else {
            planService.updatePlan(updateReqVO);
        }
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除项目计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:plan:delete')")
    public CommonResult<Boolean> deletePlan(@RequestParam("id") String id) {
        planService.deletePlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得项目计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<PmsPlanRespVO> getPlan(@RequestParam("id") String id) {
        PmsPlanDO plan = planService.getPlan(id);
        return success(BeanUtils.toBean(plan, PmsPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得项目计划分页")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<PageResult<PmsPlanRespVO>> getPlanPage(@Valid PmsPlanPageReqVO pageReqVO) {
        PageResult<PmsPlanDO> pageResult = planService.getPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PmsPlanRespVO.class));
    }
    @GetMapping("/listWith")
    @Operation(summary = "项目计划查询")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<List<PmsPlanRespVO>> listWith(PmsPlanSaveReqVO pageReqVO) {
        List<PmsPlanDO> pmsPlanDOS = planService.selectListWith(pageReqVO);
        return success(BeanUtils.toBean(pmsPlanDOS, PmsPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出项目计划 Excel")
    @PreAuthorize("@ss.hasPermission('pms:plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPlanExcel(@Valid PmsPlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PmsPlanDO> list = planService.getPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "项目计划.xls", "数据", PmsPlanRespVO.class,
                        BeanUtils.toBean(list, PmsPlanRespVO.class));
    }

    @GetMapping("/getProjPartBomPlanList")
    @Operation(summary = "获取工艺方案列表(一整个项目下的所有节点,然后筛选)")
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomPlanList(@RequestParam("projectCode") String projectCode,@RequestParam(name="partNumber",defaultValue = "") String partNumber,@RequestParam("viewSelf") boolean viewSelf){
        //目标数据类型type=2,再加上图号可过滤出来,在前端过滤也行,还要过滤status为5的(编制完成)
        ProjPartBomReqDTO dto = new ProjPartBomReqDTO();
        dto.setProjectCode(projectCode);
        dto.setPartNumber(partNumber);
        List<ProjPartBomTreeRespDTO> checkedData = projectPlanApi.getProjPartBomPlanList(dto).getCheckedData();
        List<ProjPartBomTreeRespDTO> collect = new ArrayList<>();
        if("".equals(partNumber)){
            //没传图号
            collect = checkedData.stream().filter(item -> item.getType() == 2).filter(item->"5".equals(item.getStatus())).collect(Collectors.toList());
        }else {
            collect = checkedData.stream().filter(item -> item.getType() == 2).filter(item->"5".equals(item.getStatus())).filter(item -> item.getPartNumber().equals(partNumber)).collect(Collectors.toList());
        }
        return success(collect);
    }

    @GetMapping("/getPlanByProjectId")
    @Operation(summary = "通过项目id获得主计划")
    @Parameter(name = "projectId", description = "项目id")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<PmsPlanRespVO> getPlanByProjectId(@RequestParam("projectId") String projectId) {
        PmsPlanDO pmsPlanDO = planService.getByProjectId(projectId);
        return success(BeanUtils.toBean(pmsPlanDO,PmsPlanRespVO.class));
    }

    /**
     * 备料,选取物料
     * 通过物料牌号查询可用的料
     * @param materialNumber
     */
    @GetMapping("/getPrepareMaterial")
    @Operation(summary = "查询备料时可选择的料")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<List<MaterialStockRespDTO>> getPrepareMaterial(@RequestParam("materialNumber") String materialNumber){
        //1.根据物料牌号查询物料类型
        MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
        List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto.setMaterialNumber(materialNumber)).getCheckedData();
        if(!(materialConfigList.size()>0)){
            List<MaterialStockRespDTO> list = new ArrayList<>();
            return success(list);
        }
        //2.根据物料类型查询库存
        MaterialConfigRespDTO materialConfigRespDTO = materialConfigList.get(0);
        List<String> ids = new ArrayList<>();
        ids.add(materialConfigRespDTO.getId());
        List<MaterialStockRespDTO> materialStockRespListTemp = materialStockApi.getOutOrderMaterialsByConfigIds(ids).getCheckedData();
        List<MaterialStockRespDTO> materialStockRespList = materialStockRespListTemp.stream().filter((item) -> {return ObjectUtil.isNotNull(item.getMaterialStatus())&&item.getMaterialStatus() == 2;}).collect(Collectors.toList());
        //3.根据上面的barCode查关系
        List<String> barCodeList = materialStockRespList.stream().map(MaterialStockRespDTO::getBarCode).collect(Collectors.toList());
        List<OrderMaterialRelationDO> relationByBarCodeList = relationService.getRelationByBarCode(barCodeList);
        //这是存在的materialCode
        List<String> materialCodeList = relationByBarCodeList.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.toList());
        //3.1新增退货单排除，能从退货单中查到,说明也不能用
        List<ReturnMaterialDTO> ReturnMaterialList = consignmentReturnApi.getReturnByCodes(barCodeList).getCheckedData();
        List<String> returnCodeList = ReturnMaterialList.stream().filter(item -> ObjectUtil.isNotNull(item.getBarCode())).map(ReturnMaterialDTO::getBarCode).collect(Collectors.toList());
        materialCodeList.addAll(returnCodeList);
        //4.能查到的说明被选了，要排除这些料
        List<MaterialStockRespDTO> list = materialStockRespList.stream().filter((item) -> {
            return !(materialCodeList.contains(item.getBarCode()));
        }).collect(Collectors.toList());
        return success(list);
    }

    /**
     * 选取工艺方案
     * 主要目的是决定关系表的物料状态
     * @param createReqVO
     * @return
     */
    @PostMapping("/selectProcessScheme")
    @Operation(summary = "选取工艺方案")
    @PreAuthorize("@ss.hasPermission('pms:plan:create')")
    public CommonResult<String> selectProcessScheme(@RequestBody PmsPlanSaveReqVO createReqVO) {
        planService.selectProcessScheme(createReqVO);
        return success("ok");
    }

    /**
     * 工艺方案展示
     * 首页选择工艺方案用，可选但是没选的展示出来(不可选的也展示出来)
     * @return
     */
    @GetMapping("/showProcessScheme")
    @Operation(summary = "工艺方案展示")
    @PreAuthorize("@ss.hasPermission('pms:plan:create')")
    public CommonResult<List<PmsPlanRespVO>> showProcessScheme() {
        List<PmsPlanDO> pmsPlanDOS = planService.showProcessScheme();
        return success(BeanUtils.toBean(pmsPlanDOS,PmsPlanRespVO.class));
    }

    @PostMapping("/batchSelect")
    @Operation(summary = "批量查询")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<PmsPlanHandleVO> batchSelect(@RequestBody List<PmsPlanHandleReqVO> req) {
        PmsPlanHandleVO handleVO = planService.batchSelect(req);
        return success(handleVO);
    }

    /** 不用了 */
//    @PostMapping("/batchPurchaseAndOrder")
//    @Operation(summary = "批量采购和下单")
//    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
//    public CommonResult<String> batchPurchaseAndOrder(@RequestBody List<PmsPlanHandleReqVO> req) {
//        String s = planService.batchPurchaseAndOrder(req);
//        return success("ok");
//    }

    @PostMapping("/batchPurchase")
    @Operation(summary = "批量采购")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<String> batchPurchase(@RequestBody List<PmsPlanHandleReqVO> req) {
        //String s = planService.batchPurchase(req);
        String s = planService.batchPurchase2(req);
        return success("ok");
    }

    @GetMapping("/pagePPO")
    @Operation(summary = "批量查询")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<PageResult<OrderWithPlan>> getPagePPO(@Valid PmsPlanPageReqVO page) {
        PageResult<OrderWithPlan> pageResult = planService.getPPOPage(page);
        return success(pageResult);
    }

    @GetMapping("/selectPlanToDo")
    @Operation(summary = "查询未编辑的计划,也就是还没创建只有订单的计划")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<List<OrderWithPlan>> selectPlanToDo(){
        List<OrderWithPlan> planToDo = planService.getPlanToDo();
        return success(planToDo);
    }


    // ==================== 子表（项目计划子表，产品计划完善） ====================

    @GetMapping("/plan-item/list-by-project-plan-id")
    @Operation(summary = "获得项目计划子表，产品计划完善列表")
    @Parameter(name = "projectPlanId", description = "项目计划id")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<List<PlanItemDO>> getPlanItemListByProjectPlanId(@RequestParam("projectPlanId") String projectPlanId) {
        List<PlanItemDO> list = planService.getPlanItemListByProjectPlanId(projectPlanId);
        return success(list);
    }

    @PostMapping("/plan-item/create-plan-item")
    @Operation(summary = "创建子计划")
    public CommonResult<String> createPlanItem(@Valid @RequestBody PmsPlanItemReqVO req) throws InterruptedException {
        return success(planService.createPlanItem(req));
    }
    @GetMapping("/plan-item/create-plan-item-byids")
    @Operation(summary = "创建子计划,只有生产,没有整单外协")
    public CommonResult<String> createPlanItemByIds (@RequestParam("ids") List<String> ids) throws Exception{
        planService.createPlanItemByIds(ids);
        return success("ok");
    }
    @DeleteMapping("/plan-item/delete-plan-item")
    @Operation(summary = "删除子计划")
    public CommonResult<Boolean> deletePlanItem(@RequestParam("id") String id){
        planService.deletePlanItem(id);
        return success(true);
    }

    /**
     * 生成物料采购计划(是毛坯，不是外协)
     */
    @PostMapping("/plan-item/creatMaterialPurchsePlan")
    @Operation(summary = "创建物料采购计划")
    public CommonResult<Boolean> creatMaterialPurchsePlan(@RequestBody MaterialPurchsePlanReqVO req){
        planService.createMaterialPurchasePlan(req);
        return success(true);
    }

    /**
     * 工序外协用,和上面的物料采购计划很像，但是planType是2
     */
    @PostMapping("/plan-item/creatStepOutsourcePurchsePlan")
    @Operation(summary = "创建物料采购计划")
    public CommonResult<Boolean> creatStepOutsourcePurchsePlan(@RequestBody StepOutsourcePurchseReqVO req){
        if(ObjectUtil.isNull(req.getMaterialCodeList())||req.getMaterialCodeList().size()==0){
            return success(true);
        }
        planService.creatStepOutsourcePurchsePlan(req);
        return success(true);
    }

    /**
     * 获取工序列表
     */
    @GetMapping("/plan-item/getProcedureListByProcessVersionId")
    @Operation(summary = "获得工序列表")
    @Parameter(name = "processVersionId", description = "工艺版本id")
    public CommonResult<List<ProcedureRespDTO>> getProcedureListByProcessVersionId(@RequestParam("processVersionId") String processVersionId){
        List<ProcedureRespDTO> list = processPlanDetailApi.getProcedureListByProcessVersionId(processVersionId).getCheckedData();
        return success(list);
    }


    // ==================== 子表（项目计划子表，物料采购计划中的设备采购） ====================

    @GetMapping("/plan-device/list-by-project-plan-id")
    @Operation(summary = "获得项目计划子表，物料采购计划中的设备采购列表")
    @Parameter(name = "projectPlanId", description = "项目计划id")
    @PreAuthorize("@ss.hasPermission('pms:plan:query')")
    public CommonResult<List<PlanDeviceDO>> getPlanDeviceListByProjectPlanId(@RequestParam("projectPlanId") String projectPlanId) {
        return success(planService.getPlanDeviceListByProjectPlanId(projectPlanId));
    }


    @GetMapping("test")
    public void test(){
//        List<String> ids = new ArrayList<>();
//        ids.add("1");
//        ids.add("2");
//        String checkedData = planApi.closePlanItem(ids).getCheckedData();
        List<String> list = new ArrayList<>();
        list.add("Test");
        pmsOrderMaterialRelationApi.orderMaterialFill("OR20241225010",list);
    }

}

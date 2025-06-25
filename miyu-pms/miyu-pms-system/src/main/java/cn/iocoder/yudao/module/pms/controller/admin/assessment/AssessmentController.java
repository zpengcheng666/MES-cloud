package cn.iocoder.yudao.module.pms.controller.admin.assessment;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentSaveReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.TechnologyAssessmentRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDeviceRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderListRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentReplenishDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice.AssessmentDeviceDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.service.assessment.AssessmentService;
import cn.iocoder.yudao.module.pms.service.assessmentdevice.AssessmentDeviceService;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.pdm.api.projectAssessment.TechnologyAssessmentApi;
import com.miyu.module.pdm.api.projectAssessment.dto.*;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;


@Tag(name = "管理后台 - 项目评审")
@RestController
@RequestMapping("/pms/assessment")
@Validated
public class AssessmentController {

    @Resource
    private AssessmentService assessmentService;
    @Resource
    private PmsApprovalService approvalService;

    @Resource
    private PmsOrderMapper orderMapper;
    @Resource
    private ContractApi contractApi;

    @Resource
    private TechnologyAssessmentApi technologyAssessmentApi;

    @Resource
    private AssessmentDeviceService assessmentDeviceService;

    @Resource
    private PmsOrderService pmsOrderService;

    @Resource
    private AdminUserApi userApi;

    @PostMapping("/create")
    @Operation(summary = "创建项目评审")
    @PreAuthorize("@ss.hasPermission('pms:assessment:create')")
    public CommonResult<String> createAssessment(@Valid @RequestBody AssessmentSaveReqVO createReqVO) {
        return success(assessmentService.createAssessment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新项目评审")
    @PreAuthorize("@ss.hasPermission('pms:assessment:update')")
    public CommonResult<Boolean> updateAssessment(@Valid @RequestBody AssessmentSaveReqVO updateReqVO) {
        assessmentService.updateAssessment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除项目评审")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:assessment:delete')")
    public CommonResult<Boolean> deleteAssessment(@RequestParam("id") String id) {
        assessmentService.deleteAssessment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得项目评审")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:assessment:query')")
    public CommonResult<AssessmentRespVO> getAssessment(@RequestParam("id") String id) {
        AssessmentDO assessment = assessmentService.getAssessment(id);
        PmsApprovalDO approval = approvalService.getApproval(assessment.getProjectId());
        return success(BeanUtils.toBean(assessment, AssessmentRespVO.class,vo->{
            vo.setBudget(approval.getBudget());
        }));
    }

//    @GetMapping("/getContractOrder")
//    @Operation(summary = "获得项目合同订单")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    public CommonResult<List<ContractOrderRespDTO>> getContractOrder(@RequestParam("id") String id) {
//        AssessmentDO assessment = assessmentService.getAssessment(id);
//        List<PmsOrderDO> orderList = orderMapper.selectList("project_id", assessment.getProjectId());
//        List<ContractOrderRespDTO> orderLists = new ArrayList<>();
//        for (PmsOrderDO pmsOrderDO : orderList) {
//            if(ObjectUtil.isNotNull(pmsOrderDO.getContractId())){
//                List<ContractOrderRespDTO> checkedData = contractApi.getOrderList(pmsOrderDO.getContractId()).getCheckedData();
//                orderLists.addAll(checkedData);
//            }
//        }
//        return success(orderLists);
//    }
    //原方法，下面那个是改造后的
//    @GetMapping("/getOrderByAssessmentId")
//    @Operation(summary = "获得项目合同订单")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    public CommonResult<List<OrderListRespVO>> getOrderByAssessmentId(@RequestParam("id") String id) {
//        AssessmentDO assessment = assessmentService.getAssessment(id);
//        List<PmsOrderDO> orderList = pmsOrderService.getListByProjectId(assessment.getProjectId());
//        List<OrderListDO> orderLists = new ArrayList<>();
//        for (PmsOrderDO pmsOrderDO : orderList) {
//            orderLists.addAll(pmsOrderDO.getOrderItemList());
//        }
//        return success(BeanUtils.toBean(orderLists, OrderListRespVO.class));
//    }

    @GetMapping("/getOrderByAssessmentId")
    @Operation(summary = "获得项目订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<List<PmsOrderRespVO>> getOrderByAssessmentId(@RequestParam("id") String id) {
        AssessmentDO assessment = assessmentService.getAssessment(id);
        //虽然实际上只有一个，但还是用list
        List<PmsOrderDO> orderList = pmsOrderService.getListByProjectId(assessment.getProjectId());
        return success(BeanUtils.toBean(orderList, PmsOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得项目评审分页")
    @PreAuthorize("@ss.hasPermission('pms:assessment:query')")
    public CommonResult<PageResult<AssessmentRespVO>> getAssessmentPage(@Valid AssessmentPageReqVO pageReqVO) {
        PageResult<AssessmentDO> pageResult = assessmentService.getAssessmentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AssessmentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出项目评审 Excel")
    @PreAuthorize("@ss.hasPermission('pms:assessment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAssessmentExcel(@Valid AssessmentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AssessmentDO> list = assessmentService.getAssessmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "项目评审.xls", "数据", AssessmentRespVO.class,
                        BeanUtils.toBean(list, AssessmentRespVO.class));
    }

    @GetMapping("/getTechnologyAssessment")
    @Operation(summary = "获得项目评审相关数据")
    @Parameter(name = "projectCode", description = "项目编号", required = true, example = "1024")
    public CommonResult<TechnologyAssessmentRespDTO> getTechnologyAssessment(@RequestParam("projectCode") String projectCode) {
        TechnologyAssessmentRespDTO technologyAssessment = technologyAssessmentApi.getTechnologyAssement(projectCode).getCheckedData();
        if(ObjectUtil.isNull(technologyAssessment.getDeviceList())){
            technologyAssessment.setDeviceList(new ArrayList<DeviceRespDTO>());
        }
        if(ObjectUtil.isNull(technologyAssessment.getCombinationList())){
            technologyAssessment.setCombinationList(new ArrayList<CombinationRespDTO>());
        }
        if(ObjectUtil.isNull(technologyAssessment.getMaterialList())){
            technologyAssessment.setMaterialList(new ArrayList<MaterialRespDTO>());
        }
        if(ObjectUtil.isNull(technologyAssessment.getMeasureList())){
            technologyAssessment.setMeasureList(new ArrayList<MeasureRespDTO>());
        }
        return success(technologyAssessment);
    }

    @PostMapping("/createTechnologyAssessment")
    @Operation(summary = "创建项目评审相关数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> createTechnologyAssessment(@RequestBody TechnologyAssessmentRespVO technology) {
        boolean technologyAssessment = assessmentDeviceService.createTechnologyAssessment(technology);
        return success(technologyAssessment);
    }

    @PostMapping("/updateAuditor")
    @Operation(summary = "更新评审相关审批人")
    public CommonResult<Boolean> updateAuditor(@RequestBody Map<String,String> data) {
        assessmentService.updateAuditor(data);
        return success(true);
    }

    // ==================== 子表（评审子表，评审补充） ====================

    @GetMapping("/assessment-replenish/list-by-assessment-id")
    @Operation(summary = "获得评审子表，评审补充列表")
    @Parameter(name = "assessmentId", description = "评审表id")
    @PreAuthorize("@ss.hasPermission('pms:assessment:query')")
    public CommonResult<List<AssessmentReplenishDO>> getAssessmentReplenishListByAssessmentId(@RequestParam("assessmentId") String assessmentId) {
        List<AssessmentReplenishDO> assessmentReplenishList = assessmentService.getAssessmentReplenishListByAssessmentId(assessmentId);
        List<Long> collect = assessmentReplenishList.stream().map(item -> Long.valueOf(item.getAuditor())).collect(Collectors.toList());
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(collect);
        for (AssessmentReplenishDO assessmentReplenishDO : assessmentReplenishList) {
            assessmentReplenishDO.setUsername(userMap.get(Long.valueOf(assessmentReplenishDO.getAuditor())).getNickname());
        }
        return success(assessmentReplenishList);
    }

    @GetMapping("/assessment-device/list-by-assessment-id-map")
    @Operation(summary = "获得评审子表，评审设备列表")
    @Parameter(name = "assessmentId", description = "评审表id")
    @PreAuthorize("@ss.hasPermission('pms:assessment:query')")
    public CommonResult<Map<String, AssessmentDeviceRespVO>> getAssessmentDeviceListByAssessmentIdMap(@RequestParam("assessmentId") String assessmentId) {
        List<AssessmentDeviceDO> assessmentDeviceByAssessmentId = assessmentDeviceService.getAssessmentDeviceByAssessmentId(assessmentId);
        List<AssessmentDeviceRespVO> assessmentDeviceRespVOList = BeanUtils.toBean(assessmentDeviceByAssessmentId, AssessmentDeviceRespVO.class);
        Map<String, AssessmentDeviceRespVO> stringAssessmentDeviceRespVOMap = CollectionUtils.convertMap(assessmentDeviceRespVOList,(item)->{
            return item.getPartNumber()+item.getResourcesType()+item.getResourcesTypeId();
        });
        return success(stringAssessmentDeviceRespVOMap);
    }

}

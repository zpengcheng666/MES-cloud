package cn.iocoder.yudao.module.pms.api.orderMaterial;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.*;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderListDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderSaveReqDTO;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 订单物料关系表")
public interface PmsOrderMaterialRelationApi {

    String PREFIX = ApiConstants.PREFIX + "/orderMaterial";


    /**
     * 创建订单物料关系表,入库时创建
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/createOrderMaterial")
    @Operation(summary = "创建订单物料关系")
    CommonResult<String>  createOrderMaterial(@Valid @RequestBody OrderMaterialRelationSaveReqDTO req);

    /**
     * 创建订单物料关系表
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/createOrderMaterialBatch")
    @Operation(summary = "创建订单物料关系")
    CommonResult<String>  createOrderMaterialBatch(@Valid @RequestBody List<OrderMaterialRelationSaveReqDTO> req);

    /**
     * 更新变码或成品码,工序,物料状态(生产用)
     * 查询参数订单编码orderNumber和初始条码materialCode必填
     * 需要的参数子计划planItemId,中间码variableCode,更新后的中间码updateCode,更新后的成品码productCode,工序,物料转态
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/orderMaterialUpdate")
    @Operation(summary = "更新订单物料关系,通过子计划id和变码查询后更改")
    CommonResult<String>  orderMaterialUpdate(@Valid @RequestBody OrderMaterialRelationUpdateDTO req);

    @PostMapping(PREFIX + "/orderMaterialSlice")
    @Operation(summary = "物料切割成多个,生成的新物料请求这个")
    CommonResult<String>  orderMaterialSlice(@Valid @RequestBody OrderMaterialRelationUpdateDTO req);

    /**
     * 查询订单物料关系
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/getRelationByPlanOrOrder")
    @Operation(summary = "根据子计划id或订单id查关系集合")
    CommonResult<List<OrderMaterialRelationRespDTO>>  getRelationByPlanOrOrder(@RequestBody OrderMaterialRelationUpdateDTO req);

    /**
     * 通过集合查询订单物料关系
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/getRelationByPlanOrOrderIds")
    @Operation(summary = "通过集合查询订单物料关系")
    CommonResult<List<OrderMaterialRelationRespDTO>>  getRelationByPlanOrOrderIds(@RequestBody OrderMaterialRelationReqListDTO req);

    /**
     * 关系初始化,用于刚入库时更新空关系使用
     * 需要的参数,物料码materialCode,中间码variableCode,订单id orderId
     * @param req
     * @return
     */
    @PostMapping(PREFIX + "/orderMaterialInit")
    @Operation(summary = "更新订单物料关系,通过子计划id和变码查询后更改")
    CommonResult<String>  orderMaterialInit(@RequestBody OrderMaterialRelationUpdateDTO req);

    /**
     * 关系初始化,用于刚入库时更新空关系使用
     * 需要的参数,物料码materialCode,中间码variableCode,订单id orderId
     * @param list
     * @return
     */
    @PostMapping(PREFIX + "/orderMaterialInitBatch")
    @Operation(summary = "更新订单物料关系,通过子计划id和变码查询后更改")
    CommonResult<String>  orderMaterialInitBatch(@RequestBody List<OrderMaterialRelationUpdateDTO> list);

    /**
     * 没人用
     * 外协入库时更新
     * 查找物料状态为4外协中的物料,不需要传状态
     * (物料状态1,待分配,2，加工中3,未入库，4，外协中，5，加工完成,6,待外协)
     * @param list
     * @return
     */
    @PostMapping(PREFIX + "/outSourceUpdate")
    @Operation(summary = "更新订单物料关系,通过子计划id和变码查询后更改")
    CommonResult<String>  outSourceUpdate(@RequestBody List<OrderMaterialRelationUpdateDTO> list);

    /**
     * 外协选料
     * 采购审批通过,通知子计划下哪些物料被选中
     *
     * @param dto
     * @return
     */
    @PostMapping(PREFIX + "/outSourceSelectMaterial")
    @Operation(summary = "外协选料")
    CommonResult<String>  outSourceSelectMaterial(@Valid @RequestBody OutsourcingMaterialSelectDTO dto);

    /**
     * 通过物码集合查询关系
     * materialCode
     * @param codes
     * @return
     */
    @GetMapping(PREFIX + "/selectByMaterialCodes")
    @Operation(summary = "订单物料关系查询")
    CommonResult<List<OrderMaterialRelationRespDTO>>  selectByMaterialCodes(@RequestParam("codes")Collection<String> codes);

    /**
     * 通过物码集合重置关系
     * materialCode
     * @param codes
     * @return
     */
    @PostMapping(PREFIX + "/resetRelationByMaterialCodes")
    @Operation(summary = "订单物料关系重置")
    CommonResult<String>  resetRelations(@RequestParam("codes")Collection<String> codes);

    /**
     * 通过物码集合报废物料
     * materialCode
     * @param codes
     * @return
     */
    @GetMapping(PREFIX + "/scrapMaterialCodes")
    @Operation(summary = "报废物料")
    CommonResult<String> scrapMaterialCodes(@RequestParam("codes")Collection<String> codes);

    /**
     * 选好料后通知项目选了哪些料
     * @param orderNumber       订单编号
     * @param materialCodeList  物料码
     * @return
     */
    @PostMapping(PREFIX + "/orderMaterialFill")
    @Operation(summary = "更新订单物料关系")
    CommonResult<String>  orderMaterialFill(@RequestParam("orderNumber") String orderNumber,@RequestParam("materialCodeList") List<String> materialCodeList);
}

package com.miyu.module.wms.api.mateiral;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.mateiral.dto.*;
import com.miyu.module.wms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "RPC 服务 - 物料库存模块")
public interface MaterialStockApi {

    String PREFIX = ApiConstants.PREFIX + "/material-stock";


    /**
     * 出库订单创建时，获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping(PREFIX + "/outOrder/getMaterialsByConfigIds")
    @Operation(summary = "根据物料类型ids获取可出库的物料库存列表")
    CommonResult<List<MaterialStockRespDTO>> getOutOrderMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds);


    /**
     * 库存移交订单创建时，获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping(PREFIX + "/moveOrder/getMaterialsByConfigIds")
    @Operation(summary = "根据物料类型ids获取可移交的物料库存列表")
    CommonResult<List<MaterialStockRespDTO>> getMoveOrderMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds);

    /**
     * 库存移交订单创建时，获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping(PREFIX + "/InOrder/getMaterialsByConfigIds")
    @Operation(summary = "根据物料类型ids获取可入库的物料库存列表")
    CommonResult<List<MaterialStockRespDTO>> getInOrderMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds);

    /**
     * 获取物料库存
     * @return
     */
    @GetMapping(PREFIX + "/list/getMaterialsByConfigIds")
    @Operation(summary = "获取指定物料库存")
    CommonResult<List<MaterialStockRespDTO>> getMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds);

    /**
     * 获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping(PREFIX + "/list/getMaterialsAndLocationInfoByConfigIds")
    @Operation(summary = "根据物料类型ids获取可入库的物料库存列表包括所在库位类型信息")
    CommonResult<List<MaterialStockLocationTypeDTO>> getMaterialsAndLocationInfoByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds);




    @GetMapping(PREFIX + "/list/getMaterialsByBarCode")
    @Operation(summary = "根据物料编码获取物料库存")
    @Parameter(name = "barCode", description = "物料编码", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsByBarCode(@RequestParam("barCode") String barCode);

    @GetMapping(PREFIX + "/list/getMaterialAtLocationByBarCode")
    @Operation(summary = "根据物料编码获取物料库存和其所在库位")
    @Parameter(name = "barCode", description = "物料编码", required = true)
    CommonResult<MaterialStockRespDTO> getMaterialAtLocationByBarCode(@RequestParam("barCode") String barCode);

    @GetMapping(PREFIX + "/list/getMaterialAtLocationByBarCodes")
    @Operation(summary = "根据物料编码获取物料库存和其所在库位")
    @Parameter(name = "barCodes", description = "物料编码", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialAtLocationByBarCodes(@RequestParam("barCodes") Collection<String> barCodes);


    /////
    @GetMapping(PREFIX + "/list/getMaterialsByBarCodes")
    @Operation(summary = "根据物料编码获取物料库存")
    @Parameter(name = "barCode", description = "物料编码", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsByBarCodes(@RequestParam("barCodes") Collection<String> barCodes);


    @GetMapping(PREFIX + "/getById")
    @Operation(summary = "根据物料编码获取物料库存")
    @Parameter(name = "id", description = "物料库存id", required = true)
    CommonResult<MaterialStockRespDTO> getById(@RequestParam("id") String id);


    @GetMapping(PREFIX + "/list/getMaterialsByLocationId")
    @Operation(summary = "根据库位id获取库位上的物料库存列表")
    @Parameter(name = "locationId", description = "库位id", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsByLocationId(@RequestParam("locationId") String locationId);

    @GetMapping(PREFIX + "/list/getMaterialsAndLocationByIds")
    @Operation(summary = "根据物料ids获取物料库存列表-包含储位、库存信息")
    @Parameter(name = "ids", description = "物料ids", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsAndLocationByIds(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/list/getMaterialsAndConfigByIds")
    @Operation(summary = "根据物料ids获取物料库存列表-包含物料类型信息")
    @Parameter(name = "ids", description = "物料ids", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsAndConfigByIds(@RequestParam("ids") Collection<String> ids);


    @GetMapping(PREFIX + "/list/getMaterialsByIds")
    @Operation(summary = "根据物料ids获取物料库存列表")
    @Parameter(name = "ids", description = "物料ids", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsByIds(@RequestParam("ids") Collection<String> ids);


    @GetMapping(PREFIX + "/getMaterialGZByBarCode")
    @Operation(summary = "根据物料条码查询物料所在工装库存信息")
    @Parameter(name = "barCode", description = "物料条码", required = true)
    CommonResult<MaterialStockRespDTO> getMaterialGZByBarCode(@RequestParam("barCode") String barCode);


    /**
     * 物料拣选  不校验出入库任务单
     * @param materialStockId 被拣选的物料库存id
     * @param pickQuantity 拣选数量
     * @param locationId 拣选到的库位id
     * @param storageId 拣选到的储位id
     * @return  返回拣选后新生成的物料库存信息（如果拣选数量与库存一致，将会返回旧的物料信息）
     */
    @PostMapping(PREFIX + "/materialPicking")
    @Operation(summary = "物料拣选--不校验出入库任务单")
    CommonResult<MaterialStockRespDTO> materialPicking(@RequestParam("materialStockId") String materialStockId,@RequestParam("pickQuantity") int pickQuantity, @RequestParam("locationId") String locationId, @RequestParam("storageId") String storageId);

    /**
     * 物料拣选  校验出入库任务单   拣选数量使用出入单填写的数量
     * @param materialStockId 被拣选的物料库存id
     * @param locationId 拣选到的库位id
     * @param storageId 拣选到的储位id
     * @return  返回拣选后新生成的物料库存信息（如果拣选数量与库存一致，将会返回旧的物料信息）
     */
    @PostMapping(PREFIX + "/verifyMaterialPicking")
    @Operation(summary = "物料拣选--校验出入库任务单")
    CommonResult<MaterialStockRespDTO> verifyMaterialPicking(@RequestParam("orderId") String orderId, @RequestParam("materialStockId") String materialStockId, @RequestParam("pickQuantity") String locationId, @RequestParam("locationId") String storageId);


    /**
     * 根据物料id 和 物料类型id 更新物料库存的物料类型id----生产加工A→B
     * @param materialStockId
     * @param materialConfigId
     * @return
     */
    @PostMapping(PREFIX + "/updateMaterialStockConfig")
    @Operation(summary = "更新物料库存的物料类型")
    CommonResult<Boolean> updateMaterialStockConfig(@RequestParam("materialStockId") String materialStockId,@RequestParam("materialConfigId") String materialConfigId);


    /**
     * 物料收货
     * @param receiveMaterialReqDTOList
     * @return
     */
    @PostMapping(PREFIX + "/receiveMaterial")
    @Operation(summary = "物料收货")
    CommonResult<List<MaterialStockRespDTO>> receiveMaterial(@Valid @RequestBody List<ReceiveMaterialReqDTO> receiveMaterialReqDTOList);


    // 物料发货
    @PostMapping(PREFIX + "/sendMaterial")
    @Operation(summary = "物料发货")
    CommonResult<Boolean> sendMaterial(@RequestParam("barCodes") Collection<String> barCodes);

    // 物料质检状态更新
    @PostMapping(PREFIX + "/updateQualityCheckStatus")
    @Operation(summary = "物料质检状态更新")
    CommonResult<Boolean> updateQualityCheckStatus(@Valid @RequestBody List<MaterialQualityCheckStatus> materialQualityCheckStatusList);


    /**
     * 根据物料集合 获取 所在库区和仓库信息
     */
    @PostMapping(PREFIX + "/getMaterialStockLocationTypeByMaterialStockList")
    @Operation(summary = "根据物料集合 获取 所在库区和仓库信息")
    CommonResult<List<MaterialStockLocationTypeDTO>> getMaterialStockLocationTypeByMaterialStockList(@RequestBody List<MaterialStockRespDTO> materialStockList);


    /**
     * 生成或分解成品刀
     */
    @PostMapping(PREFIX + "/generateOrDisassembleProductTool")
    @Operation(summary = "管理刀具")
    CommonResult<String> generateOrDisassembleProductTool(@RequestBody ProductToolReqDTO productToolReqDTO);
    /**
     * 拆卸成品刀
     * 装配或恢复原料刀
     * 带存储位置字段 为 恢复原料刀
     * 不带存储位置字段 为 装配或拆卸成品刀
     */
    @PostMapping(PREFIX + "/assembleOrRecoveryMaterial")
    @Operation(summary = "拆卸成品刀、装配或恢复原料刀")
    CommonResult<Boolean> assembleOrRecoveryMaterial(@RequestBody List<AssembleToolReqDTO> assembleToolReqDTOList);
}

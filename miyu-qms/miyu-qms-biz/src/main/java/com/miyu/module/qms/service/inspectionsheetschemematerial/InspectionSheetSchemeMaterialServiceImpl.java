package com.miyu.module.qms.service.inspectionsheetschemematerial;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionMaterialUpdateReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.mysql.inspectionsheetscheme.InspectionSheetSchemeMapper;
import com.miyu.module.qms.enums.InspectionSheetSchemeMaterialStatusEnum;
import com.miyu.module.qms.enums.InspectionSheetSchemeStatusEnum;
import com.miyu.module.qms.service.inspectionsheet.InspectionSheetService;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.WarehouseApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.mateiral.dto.WarehouseRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheetschemematerial.InspectionSheetSchemeMaterialMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.wms.enums.DictConstants.WMS_ORDER_DETAIL_STATUS_5;

/**
 * 检验单产品 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetSchemeMaterialServiceImpl implements InspectionSheetSchemeMaterialService {

    @Resource
    private InspectionSheetSchemeMaterialMapper inspectionSheetSchemeMaterialMapper;

    @Resource
    private InspectionSheetSchemeMapper inspectionSheetSchemeMapper;

    @Resource
    private InspectionSheetService inspectionSheetService;

    @Resource
    private OrderApi orderApi;

    @Resource
    private WarehouseApi warehouseApi;

    @Resource
    private MaterialStockApi materialStockApi;

    @Override
    public String createInspectionSheetSchemeMaterial(InspectionSheetSchemeMaterialSaveReqVO createReqVO) {
        // 插入
        InspectionSheetSchemeMaterialDO inspectionSheetSchemeMaterial = BeanUtils.toBean(createReqVO, InspectionSheetSchemeMaterialDO.class);
        inspectionSheetSchemeMaterialMapper.insert(inspectionSheetSchemeMaterial);
        // 返回
        return inspectionSheetSchemeMaterial.getId();
    }

    @Override
    public void updateInspectionSheetSchemeMaterial(InspectionSheetSchemeMaterialSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetSchemeMaterialExists(updateReqVO.getId());
        // 更新
        InspectionSheetSchemeMaterialDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetSchemeMaterialDO.class);
        inspectionSheetSchemeMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionSheetSchemeMaterial(String id) {
        // 校验存在
        validateInspectionSheetSchemeMaterialExists(id);
        // 删除
        inspectionSheetSchemeMaterialMapper.deleteById(id);
    }

    private void validateInspectionSheetSchemeMaterialExists(String id) {
        if (inspectionSheetSchemeMaterialMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_SCHEME_MATERIAL_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSheetSchemeMaterialDO getInspectionSheetSchemeMaterial(String id) {
        return inspectionSheetSchemeMaterialMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionSheetSchemeMaterialDO> getInspectionSheetSchemeMaterialPage(InspectionSheetSchemeMaterialPageReqVO pageReqVO) {
        return inspectionSheetSchemeMaterialMapper.selectPage(pageReqVO);
    }


    /**
     * 更新产品检验结果
     * @param updateReqVO
     */
    @Override
    public void updateInspectionMaterialResult(InspectionMaterialUpdateReqVO updateReqVO) {
        // 主键获取检验单任务
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(updateReqVO.getSheetSchemeId());
        // 检验任务状态不是待检验
        if(!ObjectUtils.equalsAny(sheetScheme.getStatus(), InspectionSheetSchemeStatusEnum.TOINSPECT.getStatus(), InspectionSheetSchemeStatusEnum.INSPECTING.getStatus())){
            throw exception(INSPECTION_SHEET_SCHEME_STATUS_ERROR);
        }

        // 更新产品检验结果
        InspectionSheetSchemeMaterialDO material = new InspectionSheetSchemeMaterialDO();
        material.setId(updateReqVO.getSheetSchemeMaterialId());
        material.setContent(updateReqVO.getContent());
        material.setInspectionResult(updateReqVO.getInspectionResult());
        inspectionSheetSchemeMaterialMapper.updateById(material);
        // 获取当前检验任务下的所有检验单产品
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updateReqVO.getSheetSchemeId());
        // 过滤未检测的记录
        List<InspectionSheetSchemeMaterialDO> toInspectList = materialList.stream().filter(s -> s.getInspectionResult() == null).collect(Collectors.toList());
        // 当前检测任务全部都检测
        // 更新结束时间并完成当前产品检测任务
        if(toInspectList.size() == 0){
            InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
            updSheetScheme.setId(updateReqVO.getSheetSchemeId());
            // 结束时间
            updSheetScheme.setEndTime(LocalDateTime.now());
            updSheetScheme.setStatus(InspectionSheetSchemeStatusEnum.INSPECTED.getStatus());
            inspectionSheetSchemeMapper.updateById(updSheetScheme);
        }
    }

    /**
     * 检验任务ID获取不合格品集合
     * @param id
     * @return
     */
    @Override
    public List<InspectionSheetSchemeMaterialDO> getUnqualifiedMaterialListBySchemeId(String id) {

        InspectionSheetSchemeDO schemeDO = inspectionSheetSchemeMapper.selectById(id);
        // 非已完成
        if(schemeDO.getStatus() != InspectionSheetSchemeStatusEnum.INSPECTED.getStatus()){
            return new ArrayList<>();
        }

        return inspectionSheetSchemeMaterialMapper.getUnqualifiedMaterialListBySchemeId(id);
    }

    @Override
    public List<InspectionSheetSchemeMaterialDO> getUnqualifiedMaterialDefectiveListBySchemeId(String id) {
        return inspectionSheetSchemeMaterialMapper.selectUnqualifiedMaterialDefectiveListBySchemeId(id);
    }

//    /**
//     * 批量更新不合格品处理方式
//     * @param reqDTOList
//     */
//    @Override
//    public void updateUnqualifiedMaterialHandleMethodBatch(List<UnqualifiedMaterialReqDTO> reqDTOList) {
//        inspectionSheetSchemeMaterialMapper.updateBatch(BeanUtils.toBean(reqDTOList, InspectionSheetSchemeMaterialDO.class));
//    }

    @Override
    public List<InspectionSheetSchemeMaterialDO> getMaterialsByAnalysis(AnalysisReqVO vo) {
        return inspectionSheetSchemeMaterialMapper.getMaterialsByAnalysis(vo);
    }

    /**
     * 检验任务呼叫物料列表
     * 过滤已经呼叫过的物料
     * @param pageReqVO
     * @return
     */
    @Override
    public PageResult<InspectionSheetSchemeMaterialDO> getInspectionSheetSchemeMaterialTaskPage(InspectionSheetSchemeMaterialPageReqVO pageReqVO) {
        // 检验任务ID获取检验单
        InspectionSheetDO sheet = inspectionSheetService.getInspectionSheetBySchemeId(pageReqVO.getInspectionSheetSchemeId());
        // 获取检验任务产品
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, pageReqVO.getInspectionSheetSchemeId());

        List<OrderReqDTO> list = CollectionUtils.convertList(materialList, o -> {
            OrderReqDTO dto = new OrderReqDTO();
            dto.setOrderNumber(sheet.getSheetNo());
            dto.setOrderType(DictConstants.WMS_ORDER_TYPE_CHECK_OUT);
            return dto;
        });
        // 根据单号和类型查询
        List<OrderReqDTO> orderList = orderApi.orderList(list).getCheckedData();
        // 过滤已关闭的出库单
        orderList = orderList.stream().filter(o -> o.getOrderStatus() != WMS_ORDER_DETAIL_STATUS_5).collect(Collectors.toList());
        // 已经有出库单的物料
        List<String> orderBarCodeList = orderList.stream().map(OrderReqDTO::getRealBarCode).collect(Collectors.toList());
        // 有出库单的检验单产品 id集合
        List<String> ids = materialList.stream().filter(o -> orderBarCodeList.contains(o.getBarCode())).map(item -> item.getId()).collect(Collectors.toList());
        // 过滤掉已检验完成的产品
        List<String> finishIds = materialList.stream().filter(o -> o.getStatus() != null && o.getStatus() == InspectionSheetSchemeMaterialStatusEnum.FINISH.getStatus()).map(item -> item.getId()).collect(Collectors.toList());
        // 已出库 + 已完成
        ids.addAll(finishIds);

        // 排除已在当前仓库的id
        // 仓库编码获取仓库信息
        List<WarehouseRespDTO> warehouseList = warehouseApi.getWarehouseByCode(pageReqVO.getWarehouseCode()).getCheckedData();
        if(!warehouseList.isEmpty()){
            // barcode集合
            Set<String> barCodes = convertSet(materialList, obj -> obj.getBarCode());
            if(!barCodes.isEmpty()){
                // 物料条码查询库存
                List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCodes(barCodes).getCheckedData();
                // 根据物料集合 获取 所在库区和仓库信息
                List<MaterialStockLocationTypeDTO> materialStockLocationTypeList = materialStockApi.getMaterialStockLocationTypeByMaterialStockList(materialStockList).getCheckedData();
                // 库位对应仓库map
                Map<String, String> warehouseMap = materialStockLocationTypeList.stream().collect(Collectors.toMap(obj -> obj.getId(), obj -> obj.getWarehouseId()));
                // 在当前仓库的物料库存id集合
                List<String> stockBarCodeList = materialStockList.stream().filter(a -> warehouseMap.get(a.getId()).equals(warehouseList.get(0).getId())).map(b -> b.getBarCode()).collect(Collectors.toList());
                // 在当前仓库的检验单产品id集合
                ids.addAll(materialList.stream().filter(o -> o.getBarCode() != null && stockBarCodeList.contains(o.getBarCode())).map(item -> item.getId()).collect(Collectors.toList()));
            }
     }

        // 判断已经存在
        pageReqVO.setExcludeIds(ids.stream().distinct().collect(Collectors.toList()));

        return inspectionSheetSchemeMaterialMapper.selectPage(pageReqVO);
    }

    /**
     * 检验任务Id获取检验产品集合
     * @param schemeId
     * @return
     */
    @Override
    public List<InspectionSheetSchemeMaterialDO> getInspectionSheetSchemeMaterialListBySchemeId(String schemeId) {
        return inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, schemeId);
    }
}

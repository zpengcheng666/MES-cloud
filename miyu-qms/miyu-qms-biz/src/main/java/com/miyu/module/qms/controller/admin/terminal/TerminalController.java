package com.miyu.module.qms.controller.admin.terminal;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.controller.admin.terminal.vo.OrderReqVO;
import com.miyu.module.qms.dal.mysql.inspectionsheet.InspectionSheetMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetschemematerial.InspectionSheetSchemeMaterialMapper;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.INSPECTION_INOUTBOUND_ORDER_ERROR;
import static com.miyu.module.wms.enums.DictConstants.*;

@Tag(name = "管理后台 - 检验终端操作")
@RestController
@RequestMapping("/qms/terminal")
@Validated
public class TerminalController {

    @Resource
    private InspectionSheetSchemeMaterialMapper inspectionSheetSchemeMaterialMapper;

    @Resource
    private InspectionSheetMapper inspectionSheetMapper;

    @Resource
    private OrderApi orderApi;

    @Resource
    private EncodingService encodingService;


    //入库
    @PostMapping("/putInStorage")
    public CommonResult<Boolean> putInStorage(@RequestBody OrderReqVO orderReq) {

        // 判断是否已生成入库单
        List<OrderReqDTO> orderList = orderApi.getOrderListByChooseBarCode(orderReq.getBarCode(), null).getData();
        // 存在入库单
        if(orderList.size() > 0){
            throw exception(INSPECTION_INOUTBOUND_ORDER_ERROR);
        }

        ArrayList<OrderReqDTO> orderReqDTOS = new ArrayList<>();
        OrderReqDTO orderReqDTO = new OrderReqDTO();
        orderReqDTOS.add(orderReqDTO);
        orderReqDTO.setOrderType(WMS_ORDER_TYPE_CHECK_IN);
        orderReqDTO.setOrderNumber(encodingService.getDistributionCode());
        orderReqDTO.setBatchNumber(orderReq.getBatchNumber());
        orderReqDTO.setMaterialConfigId(orderReq.getMaterialConfigId());
        orderReqDTO.setQuantity(orderReq.getTotality());
        orderReqDTO.setChooseStockId(orderReq.getMaterialUid());
        orderReqDTO.setOrderStatus(WMS_ORDER_DETAIL_STATUS_1);

        CommonResult<List<String>> listCommonResult = orderApi.orderDistribute(orderReqDTOS);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());

        return CommonResult.success(true);
    }
}


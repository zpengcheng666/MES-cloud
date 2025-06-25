package cn.iocoder.yudao.module.pms.api;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.api.pms.PmsOrderApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderFillContractDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderListDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderRespDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderSaveReqDTO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
public class PmsOrderApiImpl implements PmsOrderApi {
    @Resource
    private PmsOrderMapper orderMapper;

    @Resource
    private PmsOrderService pmsOrderService;

//    @Override
//    public void fillContract(OrderFillContractDTO order) {
//        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectListByProjectId(order.getProjectId());
//        for (PmsOrderDO pmsOrderDO : pmsOrderDOS) {
//            orderMapper.updateById(pmsOrderDO.setContractId(order.getContractId()));
//        }
//    }

    @Override
    public CommonResult<List<OrderListDTO>> getOrderItemList(String projectId) {
        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectListByProjectId(projectId);
        return success(BeanUtils.toBean(pmsOrderDOS,OrderListDTO.class));
    }

    @Override
    public CommonResult<String> createOrder(@Valid PmsOrderSaveReqDTO req) {
        PmsOrderSaveReqVO pmsOrderSaveReqVO = BeanUtils.toBean(req, PmsOrderSaveReqVO.class);
        //给物料牌号就是带料1,没给就是2
        if(ObjectUtil.isNotEmpty(pmsOrderSaveReqVO.getMaterialNumber())){
            pmsOrderSaveReqVO.setProcessType(1);
        }else {
            pmsOrderSaveReqVO.setProcessType(2);
        }
        PmsOrderDO orderDO = BeanUtils.toBean(pmsOrderSaveReqVO, PmsOrderDO.class);
        //默认值都为0,不写也行
        orderDO.setOrderStatus(0).setOrderType(0);
        orderMapper.insert(orderDO);
        return success("ok");
    }

    @Override
    public CommonResult<List<PmsOrderRespDTO>> listByOrderStatus(Collection<Integer> orderStatusList) {
        if(orderStatusList.size()<=0){
            List<PmsOrderRespDTO> list = new ArrayList<>();
            return success(list);
        }
        List<PmsOrderDO> list = orderMapper.selectListWithProjectInfo(orderStatusList);
//        LambdaQueryWrapperX<PmsOrderDO> wrapperX = new LambdaQueryWrapperX<>();
//        wrapperX.in(PmsOrderDO::getOrderStatus,orderStatusList);
//        List<PmsOrderDO> list = orderMapper.selectList(wrapperX);
        return success(BeanUtils.toBean(list,PmsOrderRespDTO.class));
    }
}

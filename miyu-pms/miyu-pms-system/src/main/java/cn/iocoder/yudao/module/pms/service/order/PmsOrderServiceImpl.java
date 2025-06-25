package cn.iocoder.yudao.module.pms.service.order;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.order.OrderListMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;



/**
 * 项目订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PmsOrderServiceImpl implements PmsOrderService {

    @Resource
    private PmsOrderMapper orderMapper;
    @Resource
    private OrderListMapper orderListMapper;
    @Resource
    private PmsApprovalMapper approvalMapper;
    @Resource
    private OrderMaterialRelationMapper relationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(PmsOrderSaveReqVO createReqVO) {
        // 插入
        PmsOrderDO order = BeanUtils.toBean(createReqVO, PmsOrderDO.class).setOrderType(1);
        orderMapper.insert(order);
        //createRelation(createReqVO.getProjectId(),order.getId(),order.getQuantity());
        // 返回
        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(PmsOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderExists(updateReqVO.getId());
        // 更新
        PmsOrderDO updateObj = BeanUtils.toBean(updateReqVO, PmsOrderDO.class);
        orderMapper.updateById(updateObj);
        if(ObjectUtil.isNotNull(updateReqVO.getProjectId())){
            List<OrderMaterialRelationDO> relationDOList = relationMapper.selectList(OrderMaterialRelationDO::getOrderId, updateReqVO.getId());
            for (OrderMaterialRelationDO relationDO : relationDOList) {
                relationDO.setProjectId(updateReqVO.getProjectId());
            }
            if(relationDOList.size()>0){
                relationMapper.updateBatch(relationDOList);
            }

        }
    }

    /**
     * 这个接口的主要目的是给外部订单选物料
     * 原接口无法更新无法置null,因为会被过滤掉
     * @param updateReqVO
     */
    @Override
    public void updateOutOrder(PmsOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderExists(updateReqVO.getId());
        PmsOrderDO pmsOrderDO = new PmsOrderDO().setId(updateReqVO.getId()).setMaterialNumber(updateReqVO.getMaterialNumber());
        if(updateReqVO.getMaterialNumber()==null){
            LambdaUpdateWrapper<PmsOrderDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(PmsOrderDO::getMaterialNumber,null);
            updateWrapper.ge(PmsOrderDO::getId,pmsOrderDO.getId());
            orderMapper.update(pmsOrderDO,updateWrapper);
        }else {
            orderMapper.updateById(pmsOrderDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(String id) {
        // 校验存在
        validateOrderExists(id);
        // 删除
        orderMapper.deleteById(id);

        // 删除关系
        //relationMapper.delete(OrderMaterialRelationDO::getOrderId,id);
    }

    private void validateOrderExists(String id) {
        if (orderMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ORDER_NOT_EXISTS);
        }
    }

    @Override
    public PmsOrderDO getOrder(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    public PageResult<PmsOrderDO> getOrderPage(PmsOrderPageReqVO pageReqVO) {
        return orderMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PmsOrderDO> getListAll() {
        return orderMapper.selectAll();
    }

    @Override
    public List<PmsOrderDO> getListByProjectId(String projectId) {
        return orderMapper.selectListByProjectId(projectId);
    }

    @Override
    public List<PmsOrderDO> getOrderByProject(String projectId) {
        return orderMapper.selectList(PmsOrderDO::getProjectId,projectId);
    }

    /**
     * 解除项目与订单之间的绑定
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbind(String id) {
        PmsOrderDO orderDO = orderMapper.selectById(id);
        if(ObjectUtil.isNotNull(orderDO.getProjectId())){
            PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(orderDO.getProjectId());
            if (ObjectUtil.isNotNull(pmsApprovalDO.getStatus())){
                //项目已开始审批，无法解绑
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.APPROVAL_START_ASSESSMENT);
            }
        }
        //解绑项目
        LambdaUpdateWrapper<PmsOrderDO> orderUpdate = new LambdaUpdateWrapper<>();
        orderUpdate.set(PmsOrderDO::getProjectId,null);
        orderUpdate.set(PmsOrderDO::getProjectName,null);
        orderUpdate.set(PmsOrderDO::getProjectCode,null);
        orderUpdate.set(PmsOrderDO::getOutsource,null);
        orderUpdate.set(PmsOrderDO::getOutsourcePrepareMaterial,null);
        orderUpdate.eq(PmsOrderDO::getId,id);
        orderMapper.update(orderUpdate);

        //解绑物料关系
        LambdaUpdateWrapper<OrderMaterialRelationDO> relationUpdate = new LambdaUpdateWrapper<>();
        relationUpdate.set(OrderMaterialRelationDO::getProjectId,null);
        relationUpdate.eq(OrderMaterialRelationDO::getOrderId,id);
        relationMapper.update(relationUpdate);

    }

    /**
     * 创建空的订单物料关系
     */
    public void createRelation(String projectId,String orderId,Integer quantity){
        //创建空关系
        List<OrderMaterialRelationDO> relationDOList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            relationDOList.add(new OrderMaterialRelationDO().setOrderId(orderId).setMaterialStatus(3).setProjectId(projectId));
        }
        relationMapper.insertBatch(relationDOList);

    }

    // ==================== 子表（项目订单表子） ====================

    @Override
    public PageResult<OrderListDO> getOrderListPage(PageParam pageReqVO, String projectOrderId) {
        return orderListMapper.selectPage(pageReqVO, projectOrderId);
    }

//    private void createOrderListList(String projectOrderId, List<OrderListDO> list) {
//        list.forEach(o -> o.setProjectOrderId(projectOrderId));
//        orderListMapper.insertBatch(list);
//    }

//    private void updateOrderListList(String projectOrderId, List<OrderListDO> list) {
//        deleteOrderListByProjectOrderId(projectOrderId);
//        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
//        createOrderListList(projectOrderId, list);
//    }

    @Override
    public String createOrderList(OrderListDO orderList) {
        orderListMapper.insert(orderList);
        return orderList.getId();
    }

    @Override
    public void updateOrderList(OrderListDO orderList) {
        // 校验存在
        validateOrderListExists(orderList.getId());
        // 更新
        orderListMapper.updateById(orderList);
    }

    @Override
    public void deleteOrderList(String id) {
        // 校验存在
        validateOrderListExists(id);
        // 删除
        orderListMapper.deleteById(id);
    }

    @Override
    public OrderListDO getOrderList(String id) {
        return orderListMapper.selectById(id);
    }

    private void validateOrderListExists(String id) {
        if (orderListMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ORDER_LIST_NOT_EXISTS);
        }
    }

    private void deleteOrderListByProjectOrderId(String projectOrderId) {
        orderListMapper.deleteByProjectOrderId(projectOrderId);
    }

    @Override
    public List<OrderListDO> getOrderListListByProjectOrderId(String projectOrderId) {
        return orderListMapper.selectListByProjectOrderId(projectOrderId);
    }

}

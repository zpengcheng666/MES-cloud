package com.miyu.module.ppm.service.consignmentrefund;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.mysql.consignmentreturndetail.ConsignmentReturnDetailMapper;
import com.miyu.module.ppm.enums.consignmentrefund.ConsignmentRefundEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.PpmAuditStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import com.miyu.module.ppm.controller.admin.consignmentrefund.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentrefund.ConsignmentRefundDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.consignmentrefund.ConsignmentRefundMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.PM_REFUND_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 采购退款单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ConsignmentRefundServiceImpl implements ConsignmentRefundService {

    @Resource
    private ConsignmentRefundMapper consignmentRefundMapper;

    @Resource
    private ConsignmentReturnDetailMapper consignmentReturnDetailService;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    public String createConsignmentRefund(ConsignmentRefundSaveReqVO createReqVO) {
        //校验金额
        validateConsignmentRefundPrice(createReqVO);
        // 插入
        ConsignmentRefundDO consignmentRefund = BeanUtils.toBean(createReqVO, ConsignmentRefundDO.class);
        consignmentRefundMapper.insert(consignmentRefund);
        // 返回
        return consignmentRefund.getId();
    }

    @Override
    public String createConsignmentRefundAndSubmit(ConsignmentRefundSaveReqVO createReqVO) {
        //校验金额
        validateConsignmentRefundPrice(createReqVO);
        // 插入
        ConsignmentRefundDO consignmentRefund = BeanUtils.toBean(createReqVO, ConsignmentRefundDO.class);
        consignmentRefundMapper.insert(consignmentRefund);

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PM_REFUND_PROCESS_KEY).setBusinessKey(String.valueOf(consignmentRefund.getId())).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号

        consignmentRefund.setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus()).setRefundStatus(ConsignmentRefundEnum.PROCESS.getStatus());

        consignmentRefundMapper.updateById(consignmentRefund);

        // 返回
        return consignmentRefund.getId();
    }

    @Override
    public void updateConsignmentRefund(ConsignmentRefundSaveReqVO updateReqVO) {
        //校验金额
        validateConsignmentRefundPrice(updateReqVO);
        // 校验存在
        validateConsignmentRefundExists(updateReqVO.getId());
        // 更新
        ConsignmentRefundDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentRefundDO.class);
        consignmentRefundMapper.updateById(updateObj);
    }

    @Override
    public void updateConsignmentRefundAndSubmit(ConsignmentRefundSaveReqVO updateReqVO) {
        //校验金额
        validateConsignmentRefundPrice(updateReqVO);
        // 校验存在
        validateConsignmentRefundExists(updateReqVO.getId());
        // 更新
        ConsignmentRefundDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentRefundDO.class);

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PM_REFUND_PROCESS_KEY).setBusinessKey(String.valueOf(updateObj.getId())).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号

        updateObj.setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus()).setRefundStatus(ConsignmentRefundEnum.PROCESS.getStatus());

        consignmentRefundMapper.updateById(updateObj);



    }


    /**
     * 提交退款审批
     * (同时更新退款状态为审批中)
     * @param id
     * @param userId
     */
    @Override
    public void submitConsignmentRefund(String id, Long userId) {
        // 1. 校验发货单是否在审批
        ConsignmentRefundDO consignmentRefundDO = validateConsignmentRefundSubmit(id);
        if (!(ObjUtil.equals(consignmentRefundDO.getStatus(), PpmAuditStatusEnum.DRAFT.getStatus()) || ObjUtil.equals(consignmentRefundDO.getStatus(), PpmAuditStatusEnum.REJECT.getStatus()))) {
            throw exception(CONSIGENMENT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(PM_REFUND_PROCESS_KEY).setBusinessKey(String.valueOf(id)).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号
        consignmentRefundMapper.updateById(new ConsignmentRefundDO().setId(id).setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus()).setRefundStatus(ConsignmentRefundEnum.PROCESS.getStatus()));

        // 4. 记录日志
        LogRecordContext.putVariable("no", consignmentRefundDO.getNo());
    }


    /**
     * 退款完成确认
     * @param id
     * @param refundStatus
     */
    @Override
    public void updateConsignmentRefundStatus(String id, Integer refundStatus) {
        //校验存在
        validateConsignmentRefundExists(id);
        //更新状态
        consignmentRefundMapper.updateById(new ConsignmentRefundDO().setId(id).setRefundStatus(refundStatus));
    }


    @Override
    public void deleteConsignmentRefund(String id) {
        // 校验存在
        validateConsignmentRefundExists(id);
        // 删除
        consignmentRefundMapper.deleteById(id);
    }

    /**
     * 校验是否存在
     * @param id
     */
    private void validateConsignmentRefundExists(String id) {
        if (consignmentRefundMapper.selectById(id) == null) {
            throw exception(CONSIGNMENT_REFUND_NOT_EXISTS);
        }
    }

    @Override
    public ConsignmentRefundDO getConsignmentRefund(String id) {
        return consignmentRefundMapper.selectById(id);
    }

    @Override
    public PageResult<ConsignmentRefundDO> getConsignmentRefundPage(ConsignmentRefundPageReqVO pageReqVO) {
        return consignmentRefundMapper.selectPage(pageReqVO);
    }

    /**
     * 合同主键获取退款集合
     * @param id
     * @return
     */
    @Override
    public List<ConsignmentRefundDO> getConsignmentRefundListByContractId(String id) {

        return consignmentRefundMapper.selectList(ConsignmentRefundDO::getContractId, id);
    }

    /**
     * 校验金额
     */
    private void validateConsignmentRefundPrice(ConsignmentRefundSaveReqVO consignmentRefundSaveReqVO) {
        //查询采购退货单明细
        List<ConsignmentReturnDetailDO> consignmentReturnDetailDOS = consignmentReturnDetailService.getConsignmentReturnDetailsByConsignmentReturnId(consignmentRefundSaveReqVO.getConsignmentReturnId(), consignmentRefundSaveReqVO.getContractId());
        //计算可退款总金额
        final BigDecimal[] price = {BigDecimal.ZERO};
        consignmentReturnDetailDOS.forEach(i -> {price[0] = price[0].add(i.getTaxPrice().multiply(i.getConsignedAmount()));});
        //查询退款单下存在的退款金额
        BigDecimal refundPrice = BigDecimal.ZERO;
        if(consignmentRefundSaveReqVO.getId()==null){refundPrice = consignmentRefundMapper.queryRefundPriceByReturnId(consignmentRefundSaveReqVO.getConsignmentReturnId());}
        else {refundPrice = consignmentRefundMapper.queryRefundPriceByReturnId(consignmentRefundSaveReqVO.getConsignmentReturnId(),consignmentRefundSaveReqVO.getId());}
        //计算退款单剩余最大可退款金额
        BigDecimal maxPrice = price[0].subtract(refundPrice);
        //校验金额
        int result = maxPrice.compareTo(consignmentRefundSaveReqVO.getRefundPrice());
        if(result < 0) {throw exception(CONSIGNMENT_REFUND_NOT_PRICE);}
    }

    /**
     * 校验是否存在退款单审批
     */
    private ConsignmentRefundDO validateConsignmentRefundSubmit(String id) {
        ConsignmentRefundDO consignmentRefundDO = consignmentRefundMapper.selectById(id);
        if(consignmentRefundDO == null) {throw exception(CONSIGNMENT_REFUND_NOT_EXISTS);}
        return consignmentRefundDO;
    }
}
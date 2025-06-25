package com.miyu.module.ppm.service.consignmentreturndetail;

import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.ConsignmentReturnRespVO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.mysql.consignmentrefund.ConsignmentRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.consignmentreturndetail.ConsignmentReturnDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 销售退货单详情 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ConsignmentReturnDetailServiceImpl implements ConsignmentReturnDetailService {

    @Resource
    private ConsignmentReturnDetailMapper consignmentReturnDetailMapper;
    @Autowired
    private ConsignmentRefundMapper consignmentRefundMapper;

    @Override
    public String createConsignmentReturnDetail(ConsignmentReturnDetailSaveReqVO createReqVO) {
        // 插入
        ConsignmentReturnDetailDO consignmentReturnDetail = BeanUtils.toBean(createReqVO, ConsignmentReturnDetailDO.class);
        consignmentReturnDetailMapper.insert(consignmentReturnDetail);
        // 返回
        return consignmentReturnDetail.getId();
    }

    @Override
    public void updateConsignmentReturnDetail(ConsignmentReturnDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateConsignmentReturnDetailExists(updateReqVO.getId());
        // 更新
        ConsignmentReturnDetailDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentReturnDetailDO.class);
        consignmentReturnDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteConsignmentReturnDetail(String id) {
        // 校验存在
        validateConsignmentReturnDetailExists(id);
        // 删除
        consignmentReturnDetailMapper.deleteById(id);
    }

    private void validateConsignmentReturnDetailExists(String id) {
        if (consignmentReturnDetailMapper.selectById(id) == null) {
            throw exception(CONSIGNMENT_RETURN_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ConsignmentReturnDetailDO getConsignmentReturnDetail(String id) {
        return consignmentReturnDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ConsignmentReturnDetailDO> getConsignmentReturnDetailPage(ConsignmentReturnDetailPageReqVO pageReqVO) {
        return consignmentReturnDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ConsignmentReturnDetailDO> getConsignmentReturnDetails(List<String> ids , String contractId) {
        List<ConsignmentReturnDetailDO> list = consignmentReturnDetailMapper.getConsignmentReturnDetails(ids,contractId);
        list.forEach(i ->{i.setPrice(i.getTaxPrice().multiply(i.getConsignedAmount()));});
        return list;
    }

    /**
     * 退货单金额计算
     */
    @Override
    public List<ConsignmentReturnRespVO> queryConsignmentReturnPrice(List<ShippingDO> consignmentReturnDOS, List<ShippingDetailDO> consignmentReturnDetailDOS) {

        List<ConsignmentReturnRespVO> list = BeanUtils.toBean(consignmentReturnDOS,ConsignmentReturnRespVO.class);
//        list.forEach(i->{
//            //计算退款单总金额
//            consignmentReturnDetailDOS.forEach(l->{
//                if(i.getId().equals(l.getConsignmentId()) && !(i.getPrice() ==null)){i.setPrice(i.getPrice().add(l.getPrice()));}
//                else {i.setPrice(l.getPrice());}
//            });
//            //查询退款单下存在的退款金额
//            BigDecimal refundPrice = consignmentRefundMapper.queryRefundPriceByReturnId(i.getId());
//            //校验退款单剩余最大可退款金额
//            i.setPrice(i.getPrice().subtract(refundPrice));
//        });

        return list;
    }


}
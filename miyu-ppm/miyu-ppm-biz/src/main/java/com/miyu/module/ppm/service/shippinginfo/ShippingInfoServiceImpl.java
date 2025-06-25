package com.miyu.module.ppm.service.shippinginfo;

import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import com.miyu.module.ppm.controller.admin.shippinginfo.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shippinginfo.ShippingInfoMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 销售发货产品 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ShippingInfoServiceImpl implements ShippingInfoService {

    @Resource
    private ShippingInfoMapper shippingInfoMapper;

    @Override
    public String createShippingInfo(ShippingInfoSaveReqVO createReqVO) {
        // 插入
        ShippingInfoDO shippingInfo = BeanUtils.toBean(createReqVO, ShippingInfoDO.class);
        shippingInfoMapper.insert(shippingInfo);
        // 返回
        return shippingInfo.getId();
    }

    @Override
    public void updateShippingInfo(ShippingInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateShippingInfoExists(updateReqVO.getId());
        // 更新
        ShippingInfoDO updateObj = BeanUtils.toBean(updateReqVO, ShippingInfoDO.class);
        shippingInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteShippingInfo(String id) {
        // 校验存在
        validateShippingInfoExists(id);
        // 删除
        shippingInfoMapper.deleteById(id);
    }

    private void validateShippingInfoExists(String id) {
        if (shippingInfoMapper.selectById(id) == null) {
            throw exception(SHIPPING_INFO_NOT_EXISTS);
        }
    }

    @Override
    public ShippingInfoDO getShippingInfo(String id) {
        return shippingInfoMapper.selectById(id);
    }

    @Override
    public PageResult<ShippingInfoDO> getShippingInfoPage(ShippingInfoPageReqVO pageReqVO) {
        return shippingInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShippingInfoDO> getShippingInfoByShippingId(String shippingId) {
        return shippingInfoMapper.selectList(ShippingInfoDO::getShippingId,shippingId);
    }

    @Override
    public List<ShippingInfoDO> getShippingInfoByContractId(String contractId) {
        MPJLambdaWrapperX<ShippingInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(ShippingInfoDO::getContractId, contractId)
                .ne(ShippingInfoDO::getShippingStatus, ShippingStatusEnum.CANCEL.getStatus());

        return shippingInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingInfoDO> getShippingInfoByContractIds(List<String> contractId) {
        MPJLambdaWrapperX<ShippingInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ShippingInfoDO::getContractId, contractId)
                .eq(ShippingInfoDO::getShippingStatus, ShippingStatusEnum.FINISH.getStatus());

        return shippingInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentCompanyNumberRespVO> getConsignmentCompanyReturnNumber( LocalDateTime[] createTimeRange) {
        return shippingInfoMapper.getCompanyConsignmentReturnNumber(createTimeRange[0],createTimeRange[1]);
    }

    @Override
    public List<ShippingInfoDO> getShippingInfoByConsignmentIds(List<String> consignmentIds) {
        MPJLambdaWrapperX<ShippingInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inIfPresent(ShippingInfoDO::getConsignmentId, consignmentIds)
                .ne(ShippingInfoDO::getShippingStatus, ShippingStatusEnum.CANCEL.getStatus());
        return shippingInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingInfoDO> getShippingInfoByOrderIds(List<String> orderIds) {
        MPJLambdaWrapperX<ShippingInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inIfPresent(ShippingInfoDO::getOrderId,orderIds)
                .eqIfPresent(ShippingInfoDO::getShippingStatus, ShippingStatusEnum.FINISH.getStatus());
        return shippingInfoMapper.selectList(wrapperX);
    }

}
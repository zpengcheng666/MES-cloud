package com.miyu.module.ppm.service.shippinginstoragedetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shippinginstoragedetail.ShippingInstorageDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 销售订单入库明细 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ShippingInstorageDetailServiceImpl implements ShippingInstorageDetailService {

    @Resource
    private ShippingInstorageDetailMapper shippingInstorageDetailMapper;

    @Override
    public String createShippingInstorageDetail(ShippingInstorageDetailSaveReqVO createReqVO) {
        // 插入
        ShippingInstorageDetailDO shippingInstorageDetail = BeanUtils.toBean(createReqVO, ShippingInstorageDetailDO.class);
        shippingInstorageDetailMapper.insert(shippingInstorageDetail);
        // 返回
        return shippingInstorageDetail.getId();
    }

    @Override
    public void updateShippingInstorageDetail(ShippingInstorageDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateShippingInstorageDetailExists(updateReqVO.getId());
        // 更新
        ShippingInstorageDetailDO updateObj = BeanUtils.toBean(updateReqVO, ShippingInstorageDetailDO.class);
        shippingInstorageDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteShippingInstorageDetail(String id) {
        // 校验存在
        validateShippingInstorageDetailExists(id);
        // 删除
        shippingInstorageDetailMapper.deleteById(id);
    }

    private void validateShippingInstorageDetailExists(String id) {
        if (shippingInstorageDetailMapper.selectById(id) == null) {
            throw exception(SHIPPING_INSTORAGE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ShippingInstorageDetailDO getShippingInstorageDetail(String id) {
        return shippingInstorageDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ShippingInstorageDetailDO> getShippingInstorageDetailPage(ShippingInstorageDetailPageReqVO pageReqVO) {
        return shippingInstorageDetailMapper.selectPage(pageReqVO);
    }

}
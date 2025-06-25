package com.miyu.module.ppm.service.shippingreturndetail;

import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailRetraceDTO;
import com.miyu.module.ppm.convert.shippingdetail.ShippingDetailConvert;
import com.miyu.module.ppm.convert.shippingreturndetail.ShippingReturnDetailConvert;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.shippingreturndetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shippingreturndetail.ShippingReturnDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 销售退货单详情 Service 实现类
 *
 * @author miyudmA
 */
@Service
@Validated
public class ShippingReturnDetailServiceImpl implements ShippingReturnDetailService {

    @Resource
    private ShippingReturnDetailMapper shippingReturnDetailMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractApi contractApi;

    @Override
    public String createShippingReturnDetail(ShippingReturnDetailSaveReqVO createReqVO) {
        // 插入
        ShippingReturnDetailDO shippingReturnDetail = BeanUtils.toBean(createReqVO, ShippingReturnDetailDO.class);
        shippingReturnDetailMapper.insert(shippingReturnDetail);
        // 返回
        return shippingReturnDetail.getId();
    }

    @Override
    public void updateShippingReturnDetail(ShippingReturnDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateShippingReturnDetailExists(updateReqVO.getId());
        // 更新
        ShippingReturnDetailDO updateObj = BeanUtils.toBean(updateReqVO, ShippingReturnDetailDO.class);
        shippingReturnDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteShippingReturnDetail(String id) {
        // 校验存在
        validateShippingReturnDetailExists(id);
        // 删除
        shippingReturnDetailMapper.deleteById(id);
    }

    private void validateShippingReturnDetailExists(String id) {
        if (shippingReturnDetailMapper.selectById(id) == null) {
            throw exception(SHIPPING_RETURN_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ShippingReturnDetailDO getShippingReturnDetail(String id) {
        return shippingReturnDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ShippingReturnDetailDO> getShippingReturnDetailPage(ShippingReturnDetailPageReqVO pageReqVO) {
        return shippingReturnDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShippingReturnDetailDO> getShippingReturnDetails(String contractId, List<Integer> shippingReturnStatus) {
        return shippingReturnDetailMapper.getShippingReturnDetails(contractId,shippingReturnStatus);
    }

    @Override
    public List<ShippingReturnDetailDO> getShippingReturnDetails(List<String> shippingReturnIds) {
        return shippingReturnDetailMapper.selectList(ShippingReturnDetailDO::getShippingReturnId, shippingReturnIds);
    }

    @Override
    public List<ShippingReturnDetailRetraceDTO> getShippingReturnListByBarcode(String barCode) {
        List<ShippingReturnDetailRetraceDTO> shippingReturnDetailRetraceDTOS = new ArrayList<>();
        List<ShippingReturnDetailDO> shippingDetailDOS = shippingReturnDetailMapper.selectListByBarCode(barCode);
        if (!CollectionUtils.isEmpty(shippingDetailDOS)){

            List<Long> userIdList = new ArrayList<>();
            //主管
            List<Long> outboundIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingReturnDetailDO::getInboundBy));
            List<Long> singedIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingReturnDetailDO::getSignedBy));
            //创建人
            List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingReturnDetailDO::getCreator));
            //更新人
            List<Long> updaterIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingReturnDetailDO::getUpdater));

            //合并用户集合
            if (outboundIds != null) userIdList.addAll(outboundIds);
            if (creatorIds != null) userIdList.addAll(creatorIds);
            if (updaterIds != null) userIdList.addAll(updaterIds);
            if (singedIds != null) userIdList.addAll(singedIds);

            userIdList = userIdList.stream().distinct().collect(Collectors.toList());

            // 拼接数据
            Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);


            //合同集合
            List<ContractRespDTO> contractRespDTOS = contractApi.getContractList(Lists.newArrayList(shippingDetailDOS.get(0).getContractId())).getCheckedData();

            Map<String, ContractRespDTO> contractMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(contractRespDTOS, ContractRespDTO::getId);

            //合同供应商信息
            List<String> companyIds = convertList(contractRespDTOS, ContractRespDTO::getParty);
            companyIds = companyIds.stream().distinct().collect(Collectors.toList());
            Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(companyIds);


            shippingReturnDetailRetraceDTOS = ShippingReturnDetailConvert.INSTANCE.convertList1(shippingDetailDOS,userMap,contractMap,companyMap);

        }



        return shippingReturnDetailRetraceDTOS;
    }

    @Override
    public List<ShippingReturnDetailDO> getShippingReturnDetailListByShippingDetailIds(List<String> detailIds) {
        return shippingReturnDetailMapper.selectList(ShippingReturnDetailDO::getShippingDetailId, detailIds);
    }

}
package com.miyu.module.ppm.service.contractconsignmentdetail;

import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contractconsignmentdetail.ContractConsignmentDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 外协发货单详情 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ContractConsignmentDetailServiceImpl implements ContractConsignmentDetailService {

    @Resource
    private ContractConsignmentDetailMapper contractConsignmentDetailMapper;

    @Override
    public String createContractConsignmentDetail(ContractConsignmentDetailSaveReqVO createReqVO) {
        // 插入
        ContractConsignmentDetailDO contractConsignmentDetail = BeanUtils.toBean(createReqVO, ContractConsignmentDetailDO.class);
        contractConsignmentDetailMapper.insert(contractConsignmentDetail);
        // 返回
        return contractConsignmentDetail.getId();
    }

    @Override
    public void updateContractConsignmentDetail(ContractConsignmentDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateContractConsignmentDetailExists(updateReqVO.getId());
        // 更新
        ContractConsignmentDetailDO updateObj = BeanUtils.toBean(updateReqVO, ContractConsignmentDetailDO.class);
        contractConsignmentDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteContractConsignmentDetail(String id) {
        // 校验存在
        validateContractConsignmentDetailExists(id);
        // 删除
        contractConsignmentDetailMapper.deleteById(id);
    }

    private void validateContractConsignmentDetailExists(String id) {
        if (contractConsignmentDetailMapper.selectById(id) == null) {
            throw exception(CONTRACT_CONSIGNMENT_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ContractConsignmentDetailDO getContractConsignmentDetail(String id) {
        return contractConsignmentDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ContractConsignmentDetailDO> getContractConsignmentDetailPage(ContractConsignmentDetailPageReqVO pageReqVO) {
        return contractConsignmentDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ContractConsignmentDetailDO> getContractConsignmentDetails(Collection<String> projectIds) {
        MPJLambdaWrapperX<ContractConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        return contractConsignmentDetailMapper.selectList(wrapperX.inIfPresent(ContractConsignmentDetailDO::getProjectId,projectIds));
    }

    @Override
    public List<ContractConsignmentDetailDO> getContractConsignmentDetailsByContractId(String contractId) {
        return contractConsignmentDetailMapper.getContractConsignmentDetailsByContractId(contractId);
    }

}
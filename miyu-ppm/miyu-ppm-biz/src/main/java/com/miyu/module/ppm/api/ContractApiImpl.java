package com.miyu.module.ppm.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.api.contract.dto.*;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractRespVO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import com.miyu.module.ppm.service.contract.ContractOrderService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.contractinvoice.ContractInvoiceService;
import com.miyu.module.ppm.service.contractpayment.ContractPaymentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.MapUtils.findAndThen;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class ContractApiImpl implements ContractApi {

    @Resource
    private ContractService contractService;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private MaterialMCCApi materialMCCApi;


    @Resource
    private ContractPaymentService contractPaymentService;

    @Resource
    private ContractInvoiceService contractInvoiceService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;
    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Override
    public CommonResult<List<ContractRespDTO>> getContractList(Collection<String> ids) {
        return success(BeanUtils.toBean(contractService.getContractListByIds(ids), ContractRespDTO.class));
    }

    @Override
    public CommonResult<List<ContractOrderRespDTO>> getOrderList(String id) {
        return success(BeanUtils.toBean(contractService.getContractOrderListByContractId(id), ContractOrderRespDTO.class));
    }

    @Override
    public CommonResult<String> updateContractAuditStatus(String bussinessKey, Integer status) {
        contractService.updateContractAuditStatus(bussinessKey, status);
        return null;
    }

    @Override
    public CommonResult<ContractRespDTO> getContractDetailInfoById(String id) {
        // 合同信息
        ContractDO contractDO = contractService.getContractById(id);
        if (contractDO == null) {
            return success(new ContractRespDTO());
        }
        // 获取合同关联订单集合
        List<ContractOrderDO> orderList = contractService.getContractOrderListByContractId(contractDO.getId());
        // 获取付款计划集合
        List<ContractPaymentSchemeDO> schemeList = contractService.getContractPaymentSchemeListByContractId(contractDO.getId());

        List<ContractPaymentDTO> paymentList = contractPaymentService.getContractPaymentByContractId(contractDO.getId());
        // 发票集合
        List<ContractInvoiceDO> invoiceList = contractInvoiceService.getContractInvoiceByContractId(contractDO.getId());
        // 产品主键获取产品物料属性
        Map<String, MaterialConfigRespDTO> productMap = materialMCCApi.getMaterialConfigMap(
                convertSet(orderList, obj -> obj.getMaterialId()));
        // 封装产品属性
        List<ContractRespVO.Product> productList = BeanUtils.toBean(orderList, ContractRespVO.Product.class, vo -> {
            // 设置单位
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setMaterialUnit(product.getMaterialUnit()));
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setProductName(product.getMaterialName()));
        });

        List<Long> userIdList = new ArrayList<>();
        //合并用户集合
        if (contractDO.getSelfContact() != null)userIdList.add(NumberUtils.parseLong(contractDO.getSelfContact()));
        if (contractDO.getPurchaser() != null)userIdList.add(NumberUtils.parseLong(contractDO.getPurchaser()));

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);
        HashSet<Long> deptSet= new HashSet<>();
        if (contractDO.getDepartment() != null)deptSet.add(Long.parseLong(contractDO.getDepartment()));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(deptSet);

        return success(BeanUtils.toBean(contractDO, ContractRespDTO.class, vo -> {
            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());
            vo.setOrders(BeanUtils.toBean(productList, ContractOrderDTO.class));
            vo.setPaymentSchemes(BeanUtils.toBean(schemeList, ContractPaymentSchemeDTO.class));
            vo.setPayments(BeanUtils.toBean(paymentList, ContractPaymentDTO.class));
            vo.setInvoices(BeanUtils.toBean(invoiceList, ContractInvoiceDTO.class));
        }));

    }

    @Override
    public CommonResult<List<ContractRespDTO>> getContractListAll(){
        List<ContractDO> contractDOS = contractMapper.selectList();
        return success(BeanUtils.toBean(contractDOS,ContractRespDTO.class));
    }

    /**
     * 项目ID集合查询合同集合
     * @param ids
     * @return
     */
    @Override
    public CommonResult<List<ContractRespDTO>> getContractListByProjectIds(Collection<String> ids,Integer type) {
        MPJLambdaWrapperX<ContractDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.inIfPresent(ContractDO::getProjectId,ids).eqIfPresent(ContractDO::getType,type)
        .eqIfPresent(ContractDO::getStatus,2);
        List<ContractDO> contractDOS = contractMapper.selectList(wrapper);
        return success(BeanUtils.toBean(contractDOS,ContractRespDTO.class));
    }

    @Override
    public CommonResult<List<ContractOrderDTO>> getContractOrderListByProjectIds(Collection<String> ids,Integer type) {

        List<ContractOrderDO> contractOrderDOS = contractOrderMapper.getContractOrderListByProjectIds(ids,type);
        return success(BeanUtils.toBean(contractOrderDOS,ContractOrderDTO.class));
    }
}

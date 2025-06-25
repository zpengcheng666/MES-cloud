package com.miyu.module.ppm.dal.mysql.contract;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.enums.common.ContractAuditStatusEnum;
import com.miyu.module.ppm.enums.common.ContractStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contract.vo.*;

/**
 * 购销合同 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractMapper extends BaseMapperX<ContractDO> {

    default PageResult<ContractDO> selectPage(ContractPageReqVO reqVO) {

        MPJLambdaWrapperX<ContractDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, ContractDO::getParty)
                .leftJoin(CompanyContactDO.class, CompanyContactDO::getId, ContractDO::getContact)
                .selectAs(CompanyDO::getName, ContractDO::getPartyName)
                .selectAs(CompanyContactDO::getName, ContractDO::getContactName)
                .selectAll(ContractDO.class)
                .like(reqVO.getParty() != null, CompanyDO::getName, reqVO.getParty());

        return selectPage(reqVO, wrapper
                .eqIfPresent(ContractDO::getType, reqVO.getType())
                .eqIfPresent(ContractDO::getNumber, reqVO.getNumber())
                .likeIfPresent(ContractDO::getName, reqVO.getName())
                // .eqIfPresent(ContractDO::getParty, reqVO.getParty())
                .eqIfPresent(ContractDO::getContact, reqVO.getContact())
                .betweenIfPresent(ContractDO::getSigningDate, reqVO.getSigningDate())
                .eqIfPresent(ContractDO::getSigningAddress, reqVO.getSigningAddress())
                .eqIfPresent(ContractDO::getDepartment, reqVO.getDepartment())
                .eqIfPresent(ContractDO::getSelfContact, reqVO.getSelfContact())
                .eqIfPresent(ContractDO::getVat, reqVO.getVat())
                .eqIfPresent(ContractDO::getCurrency, reqVO.getCurrency())
                .eqIfPresent(ContractDO::getDelivery, reqVO.getDelivery())
                .eqIfPresent(ContractDO::getContractStatus, reqVO.getContractStatus())
                .eqIfPresent(ContractDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ContractDO::getPurchaser, reqVO.getPurchaser())
                .eqIfPresent(ContractDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ContractDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ContractDO::getCreationIp, reqVO.getCreationIp())
                .eqIfPresent(ContractDO::getUpdatedIp, reqVO.getUpdatedIp())
                .eqIfPresent(ContractDO::getContractType, reqVO.getContractType())
                .inIfPresent(ContractDO::getContractType, StringUtils.isBlank(reqVO.getContractTypes())?null:reqVO.getContractTypes().split(","))
                .orderByDesc(ContractDO::getId));
    }

    default List<ContractDO> selectByNo(String no) {
        MPJLambdaWrapperX<ContractDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.notIn(ContractDO::getContractStatus, Arrays.asList(ContractStatusEnum.TERMINATE.getStatus(),ContractStatusEnum.INVALID.getStatus()))
                .notIn(ContractDO::getStatus, Arrays.asList(ContractAuditStatusEnum.REJECT.getStatus(),ContractAuditStatusEnum.CANCEL.getStatus()))
                .eq(ContractDO::getNumber, no);
        return selectList(wrapper);
    }

    default List<ContractDO> selectListByType(Collection<String> types) {
        return selectList(ContractDO::getType, types);
    }

    default List<ContractDO> selectListByIds(Collection<String> ids) {
        return selectList(ContractDO::getId, ids);

    }

    default ContractDO getContractById(String id) {
        MPJLambdaWrapperX<ContractDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, ContractDO::getParty)
                .leftJoin(CompanyContactDO.class, CompanyContactDO::getId, ContractDO::getContact)
                .selectAs(CompanyDO::getName, ContractDO::getPartyName)
                .selectAs(CompanyContactDO::getName, ContractDO::getContactName)
                .selectAll(ContractDO.class);

        return selectOne(wrapper.eq(ContractDO::getId, id));
    }

    default ContractDO selectListByNumber(ContractReqVO reqVO) {
        MPJLambdaWrapperX<ContractDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(CompanyDO.class, CompanyDO::getId, ContractDO::getParty)
                .leftJoin(CompanyContactDO.class, CompanyContactDO::getId, ContractDO::getContact)
                .selectAs(CompanyDO::getName, ContractDO::getPartyName)
                .selectAs(CompanyContactDO::getName, ContractDO::getContactName)
                .selectAll(ContractDO.class);

        return selectOne(wrapper
                .eq(ContractDO::getType, reqVO.getType())
                .eq(ContractDO::getNumber, reqVO.getNumber()));
    }
}
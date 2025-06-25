package com.miyu.module.es.dal.mysql.brake;

import java.time.LocalDate;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import org.apache.ibatis.annotations.*;
import com.miyu.module.es.controller.admin.brake.vo.*;

import javax.annotation.security.PermitAll;

/**
 * 旧厂车牌数据 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface BrakeMapper extends BaseMapperX<BrakeDO> {

    default PageResult<BrakeDO> selectPage(BrakePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BrakeDO>()
                .eqIfPresent(BrakeDO::getRegisterPlate, reqVO.getRegisterPlate())
                .eqIfPresent(BrakeDO::getNumberTimes, reqVO.getNumberTimes())
                .eqIfPresent(BrakeDO::getPhoneNumber, reqVO.getPhoneNumber())
                .eqIfPresent(BrakeDO::getBalance, reqVO.getBalance())
                .eqIfPresent(BrakeDO::getIdentiType, reqVO.getIdentiType())
                .eqIfPresent(BrakeDO::getClientType, reqVO.getClientType())
                .likeIfPresent(BrakeDO::getPassTypeName, reqVO.getPassTypeName())
                .eqIfPresent(BrakeDO::getIdentiNumber, reqVO.getIdentiNumber())
                .eqIfPresent(BrakeDO::getClientNo, reqVO.getClientNo())
                .eqIfPresent(BrakeDO::getEmail, reqVO.getEmail())
                .eqIfPresent(BrakeDO::getDeadline, reqVO.getDeadline())
                .eqIfPresent(BrakeDO::getParkingSpaceType, reqVO.getParkingSpaceType())
                .likeIfPresent(BrakeDO::getCarTypeName, reqVO.getCarTypeName())
                .likeIfPresent(BrakeDO::getClientName, reqVO.getClientName())
                .eqIfPresent(BrakeDO::getAddress, reqVO.getAddress())
                .eqIfPresent(BrakeDO::getSex, reqVO.getSex())
                .betweenIfPresent(BrakeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BrakeDO::getId));
    }

    @TenantIgnore
    @Select("SELECT COUNT(*) FROM `es_brake`")
    Integer queryCountBrake();

    @TenantIgnore
    List<BrakeDO> queryBrakeList(@Param("carPlateNo") String carPlateNo,
                                 @Param("pageSize") Integer pageSize,
                                 @Param("num") Integer num);

    @TenantIgnore
    @Select("SELECT * FROM `es_brake` where register_plate = #{carPlateNo} ")
    BrakeDO selectByCarPlateNo(@Param("carPlateNo") String carPlateNo);

    @TenantIgnore
    @Update("update `es_brake` SET updater = #{updater},deleted = 1 where register_plate = #{carPlateNo}")
    void deleteByCarPlateNo(@Param("updater") String updater,@Param("registerPlate") String registerPlate);

    @TenantIgnore
    @Update("UPDATE `es_brake` SET id = #{id},register_plate = #{registerPlate},pass_type_name=#{passTypeName},deadline=#{deadline},address=#{address},client_no=#{clientNo} WHERE register_plate = #{carPlateNo} ")
    void updateByCarPlateNo(@Param("id") String id,
                            @Param("registerPlate") String registerPlate,
                            @Param("passTypeName") String passTypeName,
                            @Param("deadline") LocalDate deadline,
                            @Param("address") String address,
                            @Param("clientNo") String clientNo,
                            @Param("carPlateNo") String carPlateNo
    );

    @TenantIgnore
    @Update("UPDATE `es_brake` SET phone_number=#{phoneNumber}, balance=#{balance}, pass_type_name=#{passTypeName},client_no=#{clientNo}, deadline=#{deadline} , car_type_name=#{carTypeName}, client_name=#{clientName}, address=#{address} WHERE register_plate = #{registerPlate}")
    void updateNByCarPlateNo(@Param("phoneNumber") String phoneNumber,
                             @Param("balance") String balance,
                             @Param("passTypeName") String passTypeName,
                             @Param("clientNo") String clientNo,
                             @Param("deadline") String deadline,
                             @Param("carTypeName") String carTypeName,
                             @Param("clientName") String clientName,
                             @Param("address") String address,
                             @Param("registerPlate") String registerPlate
    );

}
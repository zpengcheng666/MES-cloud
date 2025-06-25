package com.miyu.module.es.api.brake;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.miyu.cloud.es.api.brake.BrakeApi;
import com.miyu.cloud.es.api.brake.dto.BrakeDTO;
import com.miyu.cloud.es.api.brake.dto.BrakeData;
import com.miyu.cloud.es.api.brake.dto.BrakeRes;
import com.miyu.cloud.es.api.brake.modify.ModifyRe;
import com.miyu.cloud.es.api.brake.modify.ModifyReData;
import com.miyu.cloud.es.api.brake.modify.ModifyReq;
import com.miyu.cloud.es.api.brake.modify.ModifyReqData;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNDTO;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNData;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNDataVO;
import com.miyu.module.es.controller.admin.brakeN.vo.QueryCondition;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import com.miyu.module.es.dal.dataobject.monthlyCar.MonthlyCarData;
import com.miyu.module.es.dal.dataobject.monthlyCar.MonthlyData;
import com.miyu.module.es.dal.mysql.brake.BrakeMapper;
import com.miyu.module.es.service.brake.BrakeService;
import com.miyu.module.es.service.brake.BrakeServiceImpl;
import com.miyu.module.es.service.brakeN.BrakeNServiceImpl;
import com.miyu.module.es.service.brakeSync.BrakeSyncService;
import com.miyu.module.es.service.hmac.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class BrakeApiImpl implements BrakeApi {

    @Resource
    private BrakeService brakeService;

    @Resource
    BrakeSyncService brakeSyncService;
    @Autowired
    private BrakeMapper brakeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<BrakeRes> getBrakePage(String d,
                                                String carPlateNo,
                                                Integer pageSize,
                                                Integer pageNum) {
        List<BrakeDTO> dataList = BeanUtils.toBean(brakeService.queryBrakeList(d, carPlateNo, pageSize, pageNum),BrakeDTO.class);
        Integer totalSize = brakeService.queryCountBrake();
        BrakeRes testData = new BrakeRes().setData(dataList).setResultCode(200).setMessage("成功").setPageSize(pageSize).setPageNum(pageNum).setTotalSize(totalSize);
        return success(testData);
    }

    /**
     * 旧厂月卡注册上报(增删改)
     * @param modifyReq
     * @return
     */
    @Override
    @TenantIgnore
    public ModifyRe getBrakeModify(ModifyReq modifyReq) {
        if(modifyReq.getCommand().equals("HM_PASSPORT_REGISTER") && modifyReq.getParkingId().equals("parking_id")){

            //判断开启逻辑
            Integer sync = brakeSyncService.getBrakeSync("1").getSync();
            if(sync == 3 || sync == 4) { //开启旧厂同步

                ModifyReqData modifyReqData = modifyReq.getContent();
                //校验是否存在相关数据
                BrakeDO brakeDO = brakeMapper.selectByCarPlateNo(modifyReqData.getCarPlateNo());

                //新增
                if (modifyReqData.getOpType().equals("U") && brakeDO == null) {
                    BrakeDO brakeDOT = new BrakeDO()
                            .setId(modifyReqData.getClientNo())//记录编号
                            .setRegisterPlate(modifyReqData.getCarPlateNo())//车牌号码
                            .setPassTypeName(modifyReqData.getPassportTypeName())//通行证类型
                            .setDeadline(modifyReqData.getDeadline())//过期时间
                            .setAddress(modifyReqData.getClientName())//车主名称
                            .setClientNo(modifyReqData.getClientNo());//客户编号
                    brakeMapper.insert(brakeDOT);
                    return new ModifyRe().setContent(new ModifyReData().setMessage("成功").setResultCode("200"));
                }

                //修改
                else if(modifyReqData.getOpType().equals("U") && brakeDO != null){
                    brakeMapper.updateByCarPlateNo(
                            modifyReqData.getClientNo(),
                            modifyReqData.getCarPlateNo(),
                            modifyReqData.getPassportTypeName(),
                            LocalDate.parse(modifyReqData.getDeadline()),
                            modifyReqData.getClientName(),
                            modifyReqData.getClientNo(),
                            modifyReqData.getCarPlateNo()
                            );
                }

                //删除逻辑
                else if (modifyReqData.getOpType().equals("D") && brakeDO != null) {
                    brakeDO.setDeleted(true);
                    brakeMapper.deleteByCarPlateNo("数据同步",brakeDO.getRegisterPlate());
                    return new ModifyRe().setContent(new ModifyReData().setMessage("成功").setResultCode("200"));
                }
            }
            else {
                return new ModifyRe().setContent(new ModifyReData().setMessage("未开启旧厂同步").setResultCode("200"));
            }
        }else {
            return new ModifyRe().setContent(new ModifyReData().setMessage("命令标识错误").setResultCode("400"));
        }

        return new ModifyRe().setContent(new ModifyReData().setMessage("当前系统无符合数据").setResultCode("200"));
    }



}

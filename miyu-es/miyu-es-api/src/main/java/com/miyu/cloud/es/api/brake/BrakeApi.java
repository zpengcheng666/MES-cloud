package com.miyu.cloud.es.api.brake;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.es.api.brake.dto.BrakeData;
import com.miyu.cloud.es.api.brake.dto.BrakeRes;
import com.miyu.cloud.es.api.brake.modify.ModifyRe;
import com.miyu.cloud.es.api.brake.modify.ModifyReq;
import com.miyu.cloud.es.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME , url = "http://192.168.2.136:443")
//@FeignClient(name = ApiConstants.NAME , url = "http://dingding.miyutech.cn")
public interface BrakeApi {

    String PREFIX = ApiConstants.PREFIX + "/brake/";

    /**
     * 模拟h2系统列表查询
     * @param d
     * @param carPlateNo
     * @param pageSize
     * @param pageNum
     * @return
     */
    @PermitAll
    @PostMapping(PREFIX + "getBrakePage")
    CommonResult<BrakeRes> getBrakePage(@RequestParam("do") String d,
                                        @RequestParam( value="car_plate_no" ,required = false) String carPlateNo,
                                        @RequestParam("page_size") Integer pageSize,
                                        @RequestParam("page_num") Integer pageNum);

    /**
     * 监听旧厂月卡(增删改)
     */
    @PermitAll
    @PostMapping(PREFIX + "getBrakeModify")
    ModifyRe getBrakeModify(@Valid @RequestBody ModifyReq modifyReq);


}

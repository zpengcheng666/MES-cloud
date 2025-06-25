package com.miyu.module.es.service.brakeN;

import com.miyu.cloud.es.api.brakeN.dto.BrakeNData;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNRest;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNDataVO;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNVO;

import javax.validation.Valid;

public interface BrakeNService {

    /**
     * 获得新厂车牌数据分页
     */
    BrakeNData getBrakeNPage(@Valid BrakeNVO brakeNVO);

    /**
     * 获得新厂车牌数据
     */
    BrakeNData getBrakeN(String id);
}

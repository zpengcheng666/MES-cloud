package com.miyu.cloud.es.api.brakeN;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.es.api.brake.dto.BrakeRes;
import com.miyu.cloud.es.api.brakeN.issue.IssueRe;
import com.miyu.cloud.es.api.brakeN.issue.IssueReq;
import com.miyu.cloud.es.api.brakeN.issue.LogOffReq;
import com.miyu.cloud.es.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@FeignClient(name = ApiConstants.NAME , url = "http://192.168.2.136:443")
//@FeignClient(name = ApiConstants.NAME , url = "http://dingding.miyutech.cn")
public interface BrakeNApi {

    String PREFIX = ApiConstants.PREFIX + "/brakeN/";

    /**
     * 监听新厂数据新增/编辑
     * @return
     */
    @PermitAll
    @PostMapping(PREFIX + "getBrakeNModify")
    IssueRe getBrakeNModify(@Valid @RequestBody String issueReqJson) throws Exception;

    /**
     * 监听新厂数据删除
     */
    @PermitAll
    @PostMapping(PREFIX + "getBrakeNDelete")
    IssueRe getBrakeNDelete(@Valid @RequestBody String logOffReqJson);
}

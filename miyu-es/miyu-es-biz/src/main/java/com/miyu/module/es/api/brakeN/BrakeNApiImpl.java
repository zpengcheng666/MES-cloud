package com.miyu.module.es.api.brakeN;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.es.api.brake.dto.BrakeRes;
import com.miyu.cloud.es.api.brakeN.BrakeNApi;
import com.miyu.cloud.es.api.brakeN.issue.*;
import com.miyu.module.es.controller.admin.brake.vo.BrakeRespVO;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncRespVO;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import com.miyu.module.es.dal.dataobject.brakeSync.BrakeSyncDO;
import com.miyu.module.es.dal.mysql.brake.BrakeMapper;
import com.miyu.module.es.service.brakeSync.BrakeSyncService;
import com.miyu.module.es.service.hmac.HmacUtils;
import com.miyu.module.es.service.http.HttpReadUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.cloud.es.enums.ErrorCodeConstants.OPEN_NOT_EXISTS;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class BrakeNApiImpl implements BrakeNApi {

    @Resource
    BrakeSyncService brakeSyncService;

    @Resource
    BrakeMapper brakeMapper;

    @Resource
    HmacUtils hmacUtils;

    /**
     * 监听新厂数据新增/编辑
     * @param issueReqJson
     * @return
     */
    @Override
    @TenantIgnore
    public IssueRe getBrakeNModify(String issueReqJson) throws Exception {
        IssueReq issueReq = JSONObject.parseObject(issueReqJson, IssueReq.class);
        if(issueReq.getParkKey().equals("1731331709324685") && issueReq.getAppId().equals("ncegrzby")) {
            //判断开启逻辑
            Integer sync = brakeSyncService.getBrakeSync("1").getSync();
            IssueData issueData = issueReq.getData();
            BrakeDO isBrake = brakeMapper.selectByCarPlateNo(issueData.getLicensePlateNumber());
            if (sync == 2 || sync == 4) {  //开启新厂同步
                BrakeDO brakeDO = new BrakeDO()
                            .setId(issueData.getOwnerNo())
                            .setRegisterPlate(issueData.getLicensePlateNumber())
                            .setPhoneNumber(hmacUtils.aesDecrypt(issueData.getPhoneNumber()))
                            .setBalance(issueData.getStoredVehicleBalance())
                            .setClientNo(issueData.getOwnerNo())
                            .setDeadline(issueData.getEndTime().substring(0,10))
                            .setCarTypeName(issueData.getVehicleTypeName())
                            .setClientName(issueData.getOwnerName());
                if(isBrake == null){
                    brakeMapper.insert(brakeDO);
                    return new IssueRe().setResultCode(200).setResultMsg("同步成功");
                }else {
                    brakeMapper.updateNByCarPlateNo(
                                hmacUtils.aesDecrypt(issueData.getPhoneNumber()),
                                String.valueOf(issueData.getStoredVehicleBalance()),
                                issueData.getParkingCardTypeName(),
                                issueData.getOwnerNo(),
                                issueData.getEndTime().substring(0,10),
                                issueData.getVehicleTypeName(),
                                issueData.getOwnerName(),
                                issueData.getResidentialAddress(),
                                issueData.getLicensePlateNumber()
                            );
                    return new IssueRe().setResultCode(200).setResultMsg("同步成功");
                }

//                CloseableHttpClient client = HttpClients.createDefault();
//                //旧厂测试用
//                HttpPost http = new HttpPost("http://192.168.2.117:443/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + issueReq.getData().getLicensePlateNumber());
//                //HttpPost http = new HttpPost("http://dingding.miyutech.cn/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + issueReq.getData().getLicensePlateNumber());
//                http.addHeader("Content-Type", "application/json");
//                String reult = HttpReadUtils.httpRead(client, http);
//                JSONObject json = JSONObject.parseObject(reult);
//
//                if (JSONObject.parseObject(json.getString("data")).getString("data").replaceAll("^\\[|\\]$", "").isEmpty()) { //不存在该车牌 新增
//                    String in = "?do=registClientPlate&register_plate=" + issueReq.getData().getLicensePlateNumber()
//                            + "&pass_type_name=" + "固定车"
//                            + "&parking_space_type=" + "专用车位"
//                            + "&car_type_name=" + "小车"
//                            + "&deadline=" + issueReq.getData().getEndTime()
//                            + "&client_no=" + issueReq.getData().getOwnerNo();
//                    return this.modify(in);
//                } else { //存在该车牌 更新
//                    String up = "?do=registClientPlate&client_no=" + issueReq.getData().getOwnerNo()
//                            + "&client_name=" + issueReq.getData().getOwnerName()
//                            + "&address=" + issueReq.getData().getResidentialAddress()
//                            + "&phone_number=" + issueReq.getData().getPhoneNumber();
//                    return this.modify(up);
//                }

            } else { //未开启新厂同步
                return new IssueRe().setResultCode(200).setResultMsg("未开启新厂同步");
            }
        }else {
            return new IssueRe().setResultCode(400).setResultMsg("停车场校验错误");
        }
    }

    /**
     * 监听新厂数据删除
     * @param logOffReqJson
     * @return
     */
    @Override
    @TenantIgnore
    public IssueRe getBrakeNDelete(String logOffReqJson) {
        LogOffReq logOffReq = JSONObject.parseObject(logOffReqJson, LogOffReq.class);
        if (logOffReq.getParkKey().equals("1731331709324685") && logOffReq.getAppId().equals("ncegrzby")) {

                //判断开启逻辑
                Integer sync = brakeSyncService.getBrakeSync("1").getSync();
                if (sync == 2 || sync == 4) {  //开启新厂同步
                    LogOffData issueData = logOffReq.getData();
                    BrakeDO isBrake = brakeMapper.selectByCarPlateNo(issueData.getLicensePlateNumber());
                    if(isBrake!=null){
                        brakeMapper.deleteByCarPlateNo("数据同步",issueData.getLicensePlateNumber());
                        return new IssueRe().setResultCode(200).setResultMsg("同步成功");
                    }else {
                        return new IssueRe().setResultCode(200).setResultMsg("当前系统无符合数据");
                    }


//                    CloseableHttpClient client = HttpClients.createDefault();
//                    //旧厂测试用
//                    HttpPost http = new HttpPost("http://192.168.2.117:443/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + logOffReq.getData().getLicensePlateNumber());
////                    HttpPost http = new HttpPost("http://dingding.miyutech.cn/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + logOffReq.getData().getLicensePlateNumber());
//                    http.addHeader("Content-Type", "application/json");
//                    String reult = HttpReadUtils.httpRead(client, http);
//                    JSONObject json = JSONObject.parseObject(reult);
//
//                    if (JSONObject.parseObject(json.getString("data")).getString("data").replaceAll("^\\[|\\]$", "").isEmpty()) { //不存在该车牌
//                        return new IssueRe().setResultCode(200).setResultMsg("不存在该车牌");
//                    } else {
//                        String de = "?do=unregisterPlate&car_plate_no=" + logOffReq.getData().getLicensePlateNumber();
//                        return this.modify(de);
//                    }

                }else { //未开启新厂同步
                    return new IssueRe().setResultCode(200).setResultMsg("未开启新厂同步");
                }
            } else {
                return new IssueRe().setResultCode(400).setResultMsg("停车场校验错误");
        }
    }


//    /**
//     * 新厂->旧厂 数据新增/编辑/删除
//     * @param modify
//     * @return
//     */
//    public IssueRe modify(String modify){
//
//        CloseableHttpClient client = HttpClients.createDefault();
//        //正式用
//        //HttpPost httpPost = new HttpPost("http://h2服务器ip/plugin/client.action"+in);
//        //旧厂测试用
//        HttpPost httpPost = new HttpPost("http://192.168.2.117:443/rpc-api/es/brake/client.action" + modify);
////        HttpPost httpPost = new HttpPost("http://dingding.miyutech.cn/rpc-api/es/brake/client.action" + modify);
//        httpPost.addHeader("Content-Type", "application/json");
//        String reultStr = HttpReadUtils.httpRead(client, httpPost);
//        //返回结果转换为json对象
//        JSONObject jsonObjectData = JSONObject.parseObject(reultStr);
//        if(jsonObjectData!=null) {
//            //返回值判断根据接口文档判断按个值是成功的标志
//            if (jsonObjectData.getString("message").contains("成功")) {
//                return new IssueRe().setResultCode(200).setResultMsg("同步成功");
//            } else {
//                throw exception(OPEN_NOT_EXISTS);
//            }
//        }
//        else {return null;}
//    }
}

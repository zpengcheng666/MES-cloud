package com.miyu.module.es.service.brake;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.es.api.brake.dto.BrakeDTO;
import com.miyu.cloud.es.api.brake.dto.BrakeRes;
import com.miyu.cloud.es.api.brakeN.issue.IssueRe;
import com.miyu.module.es.dal.dataobject.monthlyCar.MonthlyCarData;
import com.miyu.module.es.dal.dataobject.monthlyCar.MonthlyData;
import com.miyu.module.es.service.hmac.HmacUtils;
import com.miyu.module.es.service.http.HttpReadUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.text.SimpleDateFormat;
import java.util.*;
import com.miyu.module.es.controller.admin.brake.vo.*;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.es.dal.mysql.brake.BrakeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.cloud.es.enums.ErrorCodeConstants.*;

/**
 * 旧厂车牌数据 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class BrakeServiceImpl implements BrakeService {

    @Resource
    private AdminUserApi userService;

    @Resource
    private BrakeMapper brakeMapper;

    @Resource
    private HmacUtils hmacUtils;

    @Override
    @TenantIgnore
    public String createBrake(BrakeSaveReqVO createReqVO) {
        // 插入
        BrakeDO brake = BeanUtils.toBean(createReqVO, BrakeDO.class);
        brakeMapper.insert(brake);
        // 返回
        return brake.getId();
    }

    @Override
    @TenantIgnore
    public void updateBrake(BrakeSaveReqVO updateReqVO) {
        // 校验存在
        validateBrakeExists(updateReqVO.getId());
        // 更新
        BrakeDO updateObj = BeanUtils.toBean(updateReqVO, BrakeDO.class);
        brakeMapper.updateById(updateObj);
    }

    @Override
    @TenantIgnore
    public void deleteBrake(String id) {
        // 校验存在
        validateBrakeExists(id);
        // 删除
        brakeMapper.deleteById(id);
    }

    private void validateBrakeExists(String id) {
        if (brakeMapper.selectById(id) == null) {
            throw exception(BRAKE_NOT_EXISTS);
        }
    }

    @Override
    public BrakeDO getBrake(String id) {
        return brakeMapper.selectById(id);
    }

    @Override
    public PageResult<BrakeDO> getBrakePage(BrakePageReqVO pageReqVO) {
        return brakeMapper.selectPage(pageReqVO);
    }


    @Override
    @TenantIgnore
    public List<BrakeDO> queryBrakeList(String d,
                                        String carPlateNo,
                                        Integer pageSize,
                                        Integer pageNum) {

        Integer num = (pageNum - 1) * pageSize;

        //查询数据分页
        return brakeMapper.queryBrakeList(carPlateNo, pageSize, num);
    }

    @Override
    public Integer queryCountBrake() {
        return brakeMapper.queryCountBrake();
    }


    @TenantIgnore
    public void syncAllBrake(){
        //时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long s = System.currentTimeMillis();
        Date date = new Date(s);
        String time= simpleDateFormat.format(date);
        List<BrakeDO> brakeDOS = brakeMapper.selectList();
        //新厂同步
        brakeDOS.forEach(i->{
                //逐个同步
                List<MonthlyCarData> list = new ArrayList<>();
                MonthlyCarData monthlyCarData = new MonthlyCarData()
                        .setNo(i.getId())//记录编号
                        .setLicensePlateNumber(i.getRegisterPlate())//车牌号码
                        .setParkingSpotNumber("")//车场车位号
                        .setOwnerParkingSpaceCount(1)//车主车位数
                        .setOwnerName(i.getClientName()!=null ? i.getClientName() : "" )//车主名称
                        .setOwnerNo(i.getClientNo()!=null ? i.getClientNo() : "")//车主编号
                        .setOwnerAddress(i.getAddress()!=null ? i.getAddress() : "")//客户住址
                        .setPhoneNumber(i.getPhoneNumber()!=null ? i.getPhoneNumber() : "")//手机号
                        .setEmail(i.getEmail()!=null ? i.getEmail() : "")//邮箱号
                        .setStartTime(time)//开始时间 无
                        .setEndTime(i.getDeadline() + " 00:00:00")//结束时间
                        .setStoredVehicleBalance(String.valueOf(i.getBalance()))//账户余额
                        .setVehicleTypeNo(hmacUtils.determineLicensePlateColor(i.getRegisterPlate()))
                        .setParkingCardTypeNo("1731331709329145")
                        .setOperatorName("自动同步")
                        .setRemarks(i.getAddress()!=null ? i.getAddress() : "")
                        .setEnableOffline(1)
                        .setIsDeleteOwner(false);

                //证件类型认证
                if(i.getIdentiType()!=null){
                    if (i.getIdentiType().contains("驾驶")) {
                        monthlyCarData.setOwnerLicense(i.getIdentiNumber()).setOwnerIDCard("");
                    } else if (i.getIdentiType().contains("身份")) {
                        monthlyCarData.setOwnerIDCard(i.getIdentiType()).setOwnerLicense("");
                    } else {
                        monthlyCarData.setOwnerIDCard("").setOwnerLicense("");
                    }
                }else {
                    monthlyCarData.setOwnerLicense("").setOwnerIDCard("");
                }

                list.add(monthlyCarData);
                if(!i.getDeleted()) {
                    //MonthlyData构建
                    MonthlyData monthlyData = new MonthlyData().setItems(list).setNoticeType(0);
                    this.syncBrakeEnd(s, monthlyData);
                }else {
                    MonthlyData monthlyData = new MonthlyData().setItems(list).setNoticeType(1);
                    this.syncBrakeEnd(s, monthlyData);
                }
        });

        //旧厂同步
//        brakeDOS.forEach(i-> {
//            if(!i.getDeleted()) {
//                CloseableHttpClient client = HttpClients.createDefault();
//                //旧厂测试用
//                HttpPost http = new HttpPost("http://192.168.2.117:443/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + i.getRegisterPlate());
//                //HttpPost http = new HttpPost("http://dingding.miyutech.cn/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + i.getRegisterPlate());
//                http.addHeader("Content-Type", "application/json");
//                String reult = HttpReadUtils.httpRead(client, http);
//                JSONObject json = JSONObject.parseObject(reult);
//
//                if (JSONObject.parseObject(json.getString("data")).getString("data").replaceAll("^\\[|\\]$", "").isEmpty()) { //不存在该车牌 新增
//                    String in = "?do=registClientPlate&register_plate=" + i.getRegisterPlate()
//                            + "&pass_type_name=" + "固定车"
//                            + "&parking_space_type=" + "专用车位"
//                            + "&car_type_name=" + "小车"
//                            + "&deadline=" + i.getDeadline()
//                            + "&client_no=" + i.getClientNo();
//                    this.modify(in);
//                } else { //存在该车牌 更新
//                    String up = "?do=registClientPlate&client_no=" + i.getClientNo()
//                            + "&client_name=" + i.getClientName()
//                            + "&address=" + i.getAddress()
//                            + "&phone_number=" + i.getPhoneNumber();
//                    this.modify(up);
//                }
//            }else {
//                CloseableHttpClient client = HttpClients.createDefault();
//                    //旧厂测试用
//                    HttpPost http = new HttpPost("http://192.168.2.117:443/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + i.getRegisterPlate());
////                    HttpPost http = new HttpPost("http://dingding.miyutech.cn/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no=" + i.getRegisterPlate());
//                    http.addHeader("Content-Type", "application/json");
//                    String reult = HttpReadUtils.httpRead(client, http);
//                    JSONObject json = JSONObject.parseObject(reult);
//
//                    if (JSONObject.parseObject(json.getString("data")).getString("data").replaceAll("^\\[|\\]$", "").isEmpty()) { //不存在该车牌
//                        new IssueRe().setResultCode(200).setResultMsg("不存在该车牌");
//                    } else {
//                        String de = "?do=unregisterPlate&car_plate_no=" + i.getRegisterPlate();
//                        this.modify(de);
//                    }
//            }
//        });
    }

    /**
     * 同步车牌信息
     * @param ids
     */
    @Override
    @TenantIgnore
    public void syncBrake(List<String> ids) {
        //获取登陆人
        String name = userService.getUser(getLoginUserId()).getData().getNickname();
        //时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long s = System.currentTimeMillis();
        Date date = new Date(s);
        String time= simpleDateFormat.format(date);

        ids.forEach(i->{
            //BusinessCarData数据构建
            List<MonthlyCarData> list = new ArrayList<>();
            CloseableHttpClient client = HttpClients.createDefault();
            //正式用
            //HttpPost httpPost = new HttpPost("http://h2服务器ip/plugin/client.action"+tou);
            //旧厂测试用
            HttpPost httpPost = new HttpPost("http://192.168.2.136:443/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no="+i);
//            HttpPost httpPost = new HttpPost("http://dingding.miyutech.cn/rpc-api/es/brake/getBrakePage?do=queryPassportInfo&page_size=1&page_num=1&car_plate_no="+i);
            httpPost.addHeader("Content-Type", "application/json");
            String reultStr = HttpReadUtils.httpRead(client, httpPost);
            //返回结果转换为json对象
            JSONObject jsonObjectData = JSONObject.parseObject(reultStr);
            String data = JSONObject.parseObject(jsonObjectData.getString("data")).getString("data").replaceAll("^\\[|\\]$", "");

            //转存至数据库
            BrakeDO brakeDO = JSONObject.parseObject(data, BrakeDO.class);

            //判断数据库是否存在该数据
            if(brakeMapper.selectById(brakeDO)!=null){
                brakeMapper.updateById(brakeDO);
            }else {
                brakeMapper.insert(brakeDO);
            }
        });
    }

    /**
     * 旧厂->新厂增改删
     * @param s
     * @param monthlyData
     */
    @TenantIgnore
    public void syncBrakeEnd(long s , MonthlyData monthlyData){

        CloseableHttpClient client = HttpClients.createDefault();
        //新厂测试用
        HttpPost httpPost = new HttpPost("http://192.168.2.117:8888/Api/V2/OpenApi/Notice?Route=MonthlyCar");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(hmacUtils.getBody(s,monthlyData), "utf-8"));
        String reultStr = HttpReadUtils.httpRead(client, httpPost);
        //返回结果转换为json对象
        JSONObject jsonObjectRe = JSONObject.parseObject(reultStr);
        //返回值判断根据接口文档判断按个值是成功的标志
        if(!jsonObjectRe.getString("ResultCode").contains("200")){
            throw exception(SYNCBRAKE_NOT_EXISTS);
        }
    }

    /**
     * 新厂->旧厂 数据新增/编辑/删除
     * @param modify
     * @return
     */
    public IssueRe modify(String modify){

        CloseableHttpClient client = HttpClients.createDefault();
        //正式用
        //HttpPost httpPost = new HttpPost("http://h2服务器ip/plugin/client.action"+in);
        //旧厂测试用
        HttpPost httpPost = new HttpPost("http://192.168.2.117:443/plugin/client.action" + modify);
        httpPost.addHeader("Content-Type", "application/json");
        String reultStr = HttpReadUtils.httpRead(client, httpPost);
        //返回结果转换为json对象
        JSONObject jsonObjectData = JSONObject.parseObject(reultStr);
        if(jsonObjectData!=null) {
            //返回值判断根据接口文档判断按个值是成功的标志
            if (jsonObjectData.getString("message").contains("成功")) {
                return new IssueRe().setResultCode(200).setResultMsg("同步成功");
            } else {
                throw exception(OPEN_NOT_EXISTS);
            }
        }
        else {return null;}
    }


}
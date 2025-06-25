package com.miyu.module.es.service.brakeN;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNData;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNRest;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNDataVO;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNVO;
import com.miyu.module.es.controller.admin.brakeN.vo.QueryCondition;
import com.miyu.module.es.service.hmac.HmacUtils;
import com.miyu.module.es.service.http.HttpReadUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.es.enums.ErrorCodeConstants.OPEN_NOT_EXISTS;

@Service
@Validated
public class BrakeNServiceImpl implements BrakeNService {

    @Resource
    private HmacUtils hmacUtils;

    /**
     * 获得新厂车牌分页
     * @param brakeNVO
     * @return
     */
    @Override
    @TenantIgnore
    public BrakeNData getBrakeNPage(BrakeNVO brakeNVO) {

        //时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long s = System.currentTimeMillis();

        BrakeNDataVO brakeNDataVO = new BrakeNDataVO()
                .setPageIndex(brakeNVO.getPageNo())
                .setPageSize(brakeNVO.getPageSize())
                .setOrderBy("No")
                .setOrderType(0);
        if(brakeNVO.getLicensePlateNumber()!=null && !brakeNVO.getLicensePlateNumber().isEmpty()){
            List<QueryCondition> list = new ArrayList<QueryCondition>();
            list.add(new QueryCondition().setField("LicensePlateNumber").setOperator(0).setValue(brakeNVO.getLicensePlateNumber()));
            brakeNDataVO.setQueryCondition(list);
        }

        return this.BrakeNMonthly(brakeNDataVO,s);

    }

    /**
     * 获得新厂车牌数据
     * @param id
     * @return
     */
    @Override
    @TenantIgnore
    public BrakeNData getBrakeN(String id) {
        //时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long s = System.currentTimeMillis();

        BrakeNDataVO brakeNDataVO = new BrakeNDataVO().setPageIndex(1).setPageSize(1).setOrderBy("No").setOrderType(0);
        List<QueryCondition> list = new ArrayList<QueryCondition>();
        list.add(new QueryCondition().setField("LicensePlateNumber").setOperator(0).setValue(id));
        brakeNDataVO.setQueryCondition(list);

        return this.BrakeNMonthly(brakeNDataVO,s);
    }

    /**
     * 调取新厂查询
     * @param brakeNDataVO
     * @param s
     * @return
     */
    public BrakeNData BrakeNMonthly(BrakeNDataVO brakeNDataVO , long s) {

        CloseableHttpClient client = HttpClients.createDefault();
        //新厂测试用
        HttpPost httpPost = new HttpPost("http://192.168.2.136:8888/Api/V2/OpenApi/Query?Route=MonthlyCar");
        //用户手册 https://note.szymzh.com/docs/parking/parking-1fdlup8vq5iil
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(hmacUtils.getBody(s,brakeNDataVO), "utf-8"));
        String reultStr = HttpReadUtils.httpRead(client, httpPost);
        //返回结果转换为json对象
        JSONObject jsonObjectRe = JSONObject.parseObject(reultStr);
        //返回值判断根据接口文档判断按个值是成功的标志
        if (!jsonObjectRe.getString("ResultCode").contains("200")) {
            throw exception(OPEN_NOT_EXISTS);
        } else {
            String object = JSONObject.parseObject(jsonObjectRe.getString("Data")).toString();
            BrakeNData brakeNData = JSONObject.parseObject(object, BrakeNData.class);
            brakeNData.getResults().forEach(i -> {
                try {
                    i.setOwnerIDCard(hmacUtils.aesDecrypt(i.getOwnerIDCard()));
                    i.setOwnerLicense(hmacUtils.aesDecrypt(i.getOwnerLicense()));
                    i.setPhoneNumber(hmacUtils.aesDecrypt(i.getPhoneNumber()));
                    i.setEmail(hmacUtils.aesDecrypt(i.getEmail()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return brakeNData;
        }

    }
}

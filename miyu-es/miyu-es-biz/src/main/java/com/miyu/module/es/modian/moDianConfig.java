package com.miyu.module.es.modian;

import com.moredian.oapi.DefaultOpenApiClient;
import com.moredian.oapi.IOpenApiClient;
import com.moredian.oapi.model.app.request.ApplyOrgAuthRequest;
import com.moredian.oapi.model.app.request.GetAppOrgAccessTokenRequest;
import com.moredian.oapi.model.app.request.GetAppTokenRequest;
import com.moredian.oapi.model.app.response.ApplyOrgAuthResponse;
import com.moredian.oapi.model.app.response.GetAppOrgAccessTokenResponse;
import com.moredian.oapi.model.app.response.GetAppTokenResponse;
import com.moredian.oapi.model.callback.request.AddOrgCallbackRequest;
import com.moredian.oapi.model.callback.response.AddOrgCallbackResponse;
import com.moredian.oapi.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class moDianConfig {

    @Value("${spring.moDian.orgId}")
    String orgId;
    @Value("${spring.moDian.orgAuthKey}")
    String orgAuthKey;

    /**
     * 钉钉魔点初始化
     */
    @Bean
    public void getAccessToken() {

        //获取appToken
        IOpenApiClient client = new DefaultOpenApiClient(DefaultProfile.getProfile("https://toapi.moredian.com"));
        GetAppTokenRequest request = new GetAppTokenRequest();
        request.setAppId("1813682483955236864");//APPID
        request.setAppKey("T3ra7T8xkEsuSIEYEtJBz2fTnOppyVQA");//APPKEY
        GetAppTokenResponse response = client.getResponse(request);
        String appToken = response.getData().getAppToken();

        //机构授权
        ApplyOrgAuthRequest requestA = new ApplyOrgAuthRequest();
        ApplyOrgAuthRequest.ApplyOrgAuthRequestBody body = new ApplyOrgAuthRequest.ApplyOrgAuthRequestBody();
        body.setCorpId("dingb666cb753371fbf535c2f4657eb6378f");// 开发者后台https://open-dev.dingtalk.com/?hash=%23%2F#/ CorpId
        requestA.setAppToken(appToken);
        requestA.setBody(body);
        ApplyOrgAuthResponse responseA = client.getResponse(requestA);
        System.out.println(responseA.getMessage());

        //获取AccessToken
        GetAppOrgAccessTokenRequest requestB = new GetAppOrgAccessTokenRequest();
        requestB.setAppToken(appToken);
        requestB.setOrgId("1812494126625587200");//OrgID 终端安卓https://open.moredian.com/#/workbench/app/mos
        requestB.setOrgAuthKey("1WtZ2zXgoVFP8R30HeirlZyULZE3gIvL");//OrgAuthKey 终端安卓
        GetAppOrgAccessTokenResponse responseB = client.getResponse(requestB);
        String accessToken = responseB.getData().getAccessToken();

        //注册回调
        AddOrgCallbackRequest addOrgCallbackRequest = new AddOrgCallbackRequest();
        addOrgCallbackRequest.setAccessToken(accessToken);
        AddOrgCallbackRequest.AddOrgCallbackRequestBody addOrgCallbackRequestBody = new AddOrgCallbackRequest.AddOrgCallbackRequestBody();
        addOrgCallbackRequestBody.setCallbackTag("APPOINTMENT_VISITOR_RECORD"); //钉钉云端接口(钉钉渠道)
//        addOrgCallbackRequestBody.setCallbackUrl("http://192.168.2.136:443/rpc-api/es/deviceR/visitCallBack");//测试
        addOrgCallbackRequestBody.setCallbackUrl("http://dingding.miyutech.cn/rpc-api/es/deviceR/visitCallBack");//实际
        addOrgCallbackRequest.setBody(addOrgCallbackRequestBody);
        AddOrgCallbackResponse addOrgCallbackResponse = client.getResponse(addOrgCallbackRequest);
        System.out.println(addOrgCallbackResponse);

    }



}

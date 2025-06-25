package com.miyu.cloud.core.carrytask.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class XxlJobUtil {

    /**
     * 调度中心地址
     */
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    /**
     * 执行器名称
     */
    @Value("${xxl.job.executor.appname}")
    private String appname;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String ADD_URL = "/jobinfo/addJob";
    private static final String UPDATE_URL = "/jobinfo/updateJob";
    private static final String REMOVE_URL = "/jobinfo/removeJob";
    private static final String STOP_URL = "/jobinfo/stopJob";
    private static final String START_URL = "/jobinfo/startJob";
    private static final String GET_GROUP_ID = "/jobgroup/getGroupId";

    /**
     * 获取执行器id
     *
     * @return 执行器id
     */
    public Integer getGroupId() {
        // 查询对应groupId:
        Map<String, Object> param = new HashMap<>();
        param.put("appname", appname);
        String json = JSON.toJSONString(param);
        //根据执行器名称，获取执行器id
        String result = doPost(adminAddresses + GET_GROUP_ID, json);
        return Integer.parseInt(result);
    }

    /**
     * 新建任务
     *
     * @param jobInfo 任务信息
     * @return {"code":200,"msg":null,"content":"3"} content:任务id
     */
    public String add(XxlJobInfo jobInfo) {
        jobInfo.setJobGroup(getGroupId());
        String json2 = JSON.toJSONString(jobInfo);
        //新建任务
        return doPost(adminAddresses + ADD_URL, json2);
    }

    /**
     * 修改任务
     *
     * @param jobInfo 任务信息
     * @return {"code":200,"msg":null,"content":null}
     */
    public String update(XxlJobInfo jobInfo) {
        jobInfo.setJobGroup(getGroupId());
        String json2 = JSON.toJSONString(jobInfo);
        return doPost(adminAddresses + UPDATE_URL, json2);
    }

    /**
     * 删除任务
     *
     * @param id 任务id
     * @return {"code":200,"msg":null,"content":null}
     */
    public String remove(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + REMOVE_URL, json);
    }

    /**
     * 停止任务
     *
     * @param id 任务id
     * @return {"code":200,"msg":null,"content":null}
     */
    public String stop(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + STOP_URL, json);
    }

    /**
     * 启动任务
     *
     * @param id 任务id
     * @return {"code":200,"msg":null,"content":null}
     */
    public String start(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + START_URL, json);
    }

    /**
     * 调用任务调度中心接口
     *
     * @param url  调度中心地址
     * @param json json字符串参数
     * @return
     */
    public String doPost(String url, String json) {
        HttpHeaders headers = new HttpHeaders();
        //必须指定utf-8格式，不然调度中心接受中文为乱码
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        //调用任务调度中心接口
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);
//        return stringResponseEntity.getBody();
        String result = stringResponseEntity.getBody();
        if (Integer.parseInt(JSON.parseObject(result).getString("code")) != 200) {
            throw new RuntimeException("调用定时任务出现错误：" + JSON.parseObject(result).getString("msg"));
        }
        return JSON.parseObject(result).getString("content");
    }
}

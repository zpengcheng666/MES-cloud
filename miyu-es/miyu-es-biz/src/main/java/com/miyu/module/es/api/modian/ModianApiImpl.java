package com.miyu.module.es.api.modian;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.mail.MailSendApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miyu.cloud.es.api.visit.VisitApi;
import com.miyu.cloud.es.api.visit.dto.ModianDTO;
import com.miyu.cloud.es.api.visit.dto.VisitSaveResVO;
import com.miyu.module.es.controller.admin.visit.vo.VisitSaveReqVO;
import com.miyu.module.es.dal.dataobject.modian.MoDianDO;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import com.miyu.module.es.dal.dataobject.visitlicense.VisitLicenseDO;
import com.miyu.module.es.dal.mysql.visit.VisitMapper;
import com.miyu.module.es.dal.mysql.visitlicense.VisitLicenseMapper;
import com.miyu.module.es.service.visit.VisitServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class ModianApiImpl implements VisitApi {

    @Resource
    VisitServiceImpl visitService;

    @Resource
    VisitMapper visitMapper;

    @Resource
    VisitLicenseMapper visitLicenseMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void visitCallBack(ModianDTO object, String nonce, String orgId, String signVersion, String signature, String timestamp) {
        //转为实体类
        ObjectMapper mapper = new ObjectMapper();
        VisitSaveResVO visitSaveResVO = mapper.convertValue(object.getData(), VisitSaveResVO.class);
        //判断是否存在该数据
        if(visitMapper.selectByVisitRecordId(visitSaveResVO.getVisitRecordId()) == null ) {
            //同步至基础表
            VisitDO visitDO = BeanUtils.toBean(visitSaveResVO, VisitDO.class);
            visitMapper.insert(visitDO);
            //同步至车牌表
            if(visitSaveResVO.getLicenseNoList() != null) {
                List<String> list = Arrays.asList(visitSaveResVO.getLicenseNoList());
                list.forEach(i -> {
                    visitLicenseMapper.insert(new VisitLicenseDO().setVisitRecordId(visitSaveResVO.getVisitRecordId()).setLicenseNo(i));
                });
            }
        } else {
            //更新基础表
            VisitDO visitDO = BeanUtils.toBean(visitSaveResVO, VisitDO.class);
            visitMapper.updateByVisitRecordId(visitDO.getVisitRecordId(), visitDO.getVisitorCancelTime(),visitDO.getStatus(),visitDO.getVisitorRecordTime(),visitDO.getDeviceSn());
        }
        //后续同步至第三方系统


    }
}

package com.miyu.module.es.service.xxlJobInfo;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.miyu.module.es.controller.admin.brakeSync.vo.BrakeSyncSaveReqVO;
import com.miyu.module.es.dal.mysql.xxlJobInfo.XxlJobInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.datasource.core.enums.DataSourceEnum.SLAVE;

@Service
@Validated
public class XxlJobInfoServiceImpl {

    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;

    @DSTransactional
    @DS(SLAVE)
    public void xxlJobUpdate(BrakeSyncSaveReqVO updateReqVO) {
        //更新xxj-job配置
        if(updateReqVO.getAutomatic() == 2){
            xxlJobInfoMapper.updateOpen(37, 0);
        }if(updateReqVO.getAutomatic() == 1) {
            String cron = "0 0 0 1/" + updateReqVO.getCycle() + " * ?";
            xxlJobInfoMapper.updateEnd(37,1,cron);
        }
    }

}

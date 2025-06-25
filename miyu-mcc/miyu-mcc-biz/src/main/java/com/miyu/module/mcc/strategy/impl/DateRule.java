package com.miyu.module.mcc.strategy.impl;

import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import com.miyu.module.mcc.strategy.IEncodingRuleStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DateRule implements IEncodingRuleStrategy {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getRuleValue(EncodingRuleDetailDO detailDO, Map<String, String> attributes, List<EncodingRuleDetailDO> detailDOS, EncodingRuleDO ruleDO) {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        if (detailDO.getType().intValue() == 8){
            int year = currentDate.getYear();
            String yearFormatted = String.format("%04d", year);
            return yearFormatted;
        }else if (detailDO.getType().intValue() == 9){
            int month = currentDate.getMonthValue();
            String monthFormatted = String.format("%02d", month);
            return monthFormatted;
        }else {
            int day = currentDate.getDayOfMonth();
            String dayFormatted = String.format("%02d", day);
            return dayFormatted;
        }

    }

}

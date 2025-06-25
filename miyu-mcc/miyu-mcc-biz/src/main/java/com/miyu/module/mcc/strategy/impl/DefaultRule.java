package com.miyu.module.mcc.strategy.impl;

import com.alibaba.excel.util.StringUtils;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import com.miyu.module.mcc.strategy.IEncodingRuleStrategy;
import com.miyu.module.mcc.utils.StringListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
public class DefaultRule implements IEncodingRuleStrategy {
    @Override
    public String getRuleValue(EncodingRuleDetailDO detailDO, Map<String,String> attributes, List<EncodingRuleDetailDO> detailDOS, EncodingRuleDO ruleDO) {
        Integer bitNumber = detailDO.getBitNumber();
        //先判断是否有默认值  如果有默认值 则返回默认值  如果没有默认值则查找配置属性
        if (StringUtils.isNotBlank(detailDO.getDefalutValue())){
            return StringListUtils.getString(detailDO.getDefalutValue(), bitNumber);
        }else if (StringUtils.isNotBlank(detailDO.getEncodingAttribute())){

            if (StringUtils.isNotBlank(attributes.get(detailDO.getEncodingAttribute()))){

                return StringListUtils.getString(attributes.get(detailDO.getEncodingAttribute()), bitNumber);
            }
        }

        return StringListUtils.getString(detailDO.getDefalutValue(), bitNumber);
    }
}

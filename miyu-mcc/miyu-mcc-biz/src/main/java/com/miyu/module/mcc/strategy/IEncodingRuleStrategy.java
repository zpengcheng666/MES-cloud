package com.miyu.module.mcc.strategy;


import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;

import java.util.List;
import java.util.Map;

public interface IEncodingRuleStrategy {

    /***
     * 生成策略
     *
     * @param detailDO  规则详情
     * @param attributes
     * @return
     */
    String  getRuleValue(EncodingRuleDetailDO detailDO, Map<String,String> attributes, List<EncodingRuleDetailDO> detailDOS, EncodingRuleDO ruleDO);

}

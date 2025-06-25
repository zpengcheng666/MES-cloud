package com.miyu.module.mcc.strategy;

import com.miyu.module.mcc.strategy.impl.DateRule;
import com.miyu.module.mcc.strategy.impl.DefaultRule;
import com.miyu.module.mcc.strategy.impl.MaterialTypeRule;
import com.miyu.module.mcc.strategy.impl.SerialNumberRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/***
 * 编码规则生成编码值策略
 * @auther zhp
 * @create 2020/9/23
 */
@Service
public class EncodingRuleFactory {

    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(EncodingRuleFactory.class);
    @Resource
    private DefaultRule defaultRule;
    @Resource
    private MaterialTypeRule materialTypeRule;
    @Resource
    private SerialNumberRule serialNumberRule;
    @Resource
    private DateRule dateRule;
    /**
     *
     * @param state 类型1机构 2编码分类 3物料类别4源码5连接符6数字流水号7字母流水号8年份9月份10日期11自定义
     * @return
     */
    public IEncodingRuleStrategy generatorStrategy(Integer state) {


        IEncodingRuleStrategy strategy = null;

        switch (state) {
            case 3://物料类码
                strategy = materialTypeRule;
                break;
            case 6://数字流水号
                strategy = serialNumberRule;
                break;
            case 8://年份
                strategy = dateRule;
                break;
            case 9://月份
                strategy = dateRule;
                break;
            case 10://日
                strategy = dateRule;
                break;
            default://默认   固定默认值  或者自定义属性
                strategy = defaultRule;
        }
        log.info("创建策略结束");
        return strategy;
    }
}

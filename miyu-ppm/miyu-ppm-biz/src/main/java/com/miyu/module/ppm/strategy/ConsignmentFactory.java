package com.miyu.module.ppm.strategy;

import com.miyu.module.ppm.strategy.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/***
 * 编码规则生成编码值策略
 * @auther zhp
 * @create 2020/9/23
 */
@Service
public class ConsignmentFactory {

    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(ConsignmentFactory.class);
    @Resource
    private PurchaseConsignmentCreate purchaseConsignmentCreate;
    @Resource
    private ShippingInstorageCreate shippingInstorageCreate;
    @Resource
    private PurchaseConsignmentOutCreate purchaseConsignmentOutCreate;
    @Resource
    private ContractConsignmentReturnCreate contractConsignmentReturnCreate;
    @Resource
    private ShippingReturnCreate shippingReturnCreate;
    /**
     *
     * @param state 1采购收货2外协收货3外协原材料退货4委托加工收货5销售退货
     * @return
     */
    public IConsignmentFactory generatorStrategy(Integer state) {


        IConsignmentFactory strategy = null;

        switch (state) {
            case 1://采购收货
                strategy = purchaseConsignmentCreate;
                break;
            case 2://外协收货
                strategy = purchaseConsignmentOutCreate;
                break;
            case 3://外协原材料退货
                strategy = contractConsignmentReturnCreate;
                break;
            case 4://委托加工收货
                strategy = shippingInstorageCreate;
                break;
            case 5://销售退货
                strategy = shippingReturnCreate;
                break;
            default://默认   固定默认值  或者自定义属性
                strategy = purchaseConsignmentCreate;
        }
        log.info("创建策略结束");
        return strategy;
    }
}

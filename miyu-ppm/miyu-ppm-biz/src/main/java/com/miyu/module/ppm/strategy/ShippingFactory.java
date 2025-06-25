package com.miyu.module.ppm.strategy;

import com.miyu.module.ppm.strategy.impl.ContratcConsignmentCreate;
import com.miyu.module.ppm.strategy.impl.PurchaseConsignmentCreate;
import com.miyu.module.ppm.strategy.impl.ShippingCreate;
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
public class ShippingFactory {

    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(ShippingFactory.class);
    @Resource
    private ShippingCreate shippingCreate;
    @Resource
    private ContratcConsignmentCreate contratcConsignmentCreate;
    /**
     *
     * @param state 1采购收货2外协收货3外协原材料退货4委托加工收货5销售退货
     * @return
     */
    public IShippingFactory generatorStrategy(Integer state) {


        IShippingFactory strategy = null;

        switch (state) {
            case 1://销售发货（项目）
                strategy = shippingCreate;
                break;
            case 2://外协发货(合同)
                strategy = contratcConsignmentCreate;
                break;
            case 3://采购退货(合同)
                strategy = contratcConsignmentCreate;
                break;
            case 4://委托加工退货（项目）
                strategy = shippingCreate;
                break;
            default://默认   固定默认值  或者自定义属性
                strategy = shippingCreate;
        }
        log.info("创建收货策略结束");
        return strategy;
    }
}

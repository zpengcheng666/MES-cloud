package com.miyu.module.tms.strategy;

import com.miyu.module.tms.strategy.impl.ToolLoadingRecord;
import com.miyu.module.tms.strategy.impl.ToolRepairsRecord;
import com.miyu.module.tms.strategy.impl.ToolUnLoadingRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/***
 * 刀具配件使用记录策略
 * @auther zhp
 * @create 2020/9/23
 */
@Service
public class AssembleRecordFactory {

    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(AssembleRecordFactory.class);

    @Resource
    private ToolLoadingRecord toolLoadingRecord;
    @Resource
    private ToolUnLoadingRecord toolUnLoadingRecord;
    @Resource
    private ToolRepairsRecord toolRepairsRecord;
    /**
     *
     * @param state 类型1装刀 2 卸刀 3 维修
     * @return
     */
    public IAssembleRecordStrategy generatorStrategy(Integer state) {


        IAssembleRecordStrategy strategy = null;

        switch (state) {
            case 1://装刀
                strategy = toolLoadingRecord;
                break;
            case 2://卸刀
                strategy = toolUnLoadingRecord;
                break;
            case 3://维修
                strategy = toolRepairsRecord;
                break;
            default://默认   固定默认值  或者自定义属性
                strategy = toolLoadingRecord;
        }
        log.info("创建策略结束");
        return strategy;
    }
}

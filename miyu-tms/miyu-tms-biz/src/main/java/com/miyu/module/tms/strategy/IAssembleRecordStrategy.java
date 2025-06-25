package com.miyu.module.tms.strategy;



import com.miyu.module.tms.controller.admin.toolinfo.vo.ToolInfoSaveReqVO;

import java.util.List;
import java.util.Map;

public interface IAssembleRecordStrategy {

    /***
     * 保存策略
     * @return
     */
    void saveRecord(ToolInfoSaveReqVO saveReqVO);

}

package com.miyu.module.tms.service.toolrecord;

import java.util.*;
import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolrecord.vo.*;
import com.miyu.module.tms.dal.dataobject.toolrecord.ToolRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 刀具使用记录 Service 接口
 *
 * @author QianJy
 */
public interface ToolRecordService {

    /**
     * 创建刀具使用记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolRecord(@Valid ToolRecordSaveReqVO createReqVO);

    /**
     * 更新刀具使用记录
     *
     * @param updateReqVO 更新信息
     */
    void updateToolRecord(@Valid ToolRecordSaveReqVO updateReqVO);

    /**
     * 删除刀具使用记录
     *
     * @param id 编号
     */
    void deleteToolRecord(String id);

    /**
     * 获得刀具使用记录
     *
     * @param id 编号
     * @return 刀具使用记录
     */
    ToolRecordDO getToolRecord(String id);

    /**
     * 获得刀具使用记录分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具使用记录分页
     */
    PageResult<ToolRecordDO> getToolRecordPage(ToolRecordPageReqVO pageReqVO);

}
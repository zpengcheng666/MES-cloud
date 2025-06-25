package com.miyu.module.mcc.service.coderecord;

import java.util.*;
import javax.validation.*;
import com.miyu.module.mcc.controller.admin.coderecord.vo.*;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 编码记录 Service 接口
 *
 * @author 上海弥彧
 */
public interface CodeRecordService {

    /**
     * 创建编码记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCodeRecord(@Valid CodeRecordSaveReqVO createReqVO);

    /**
     * 更新编码记录
     *
     * @param updateReqVO 更新信息
     */
    void updateCodeRecord(@Valid CodeRecordSaveReqVO updateReqVO);

    /**
     * 删除编码记录
     *
     * @param id 编号
     */
    void deleteCodeRecord(String id);

    /**
     * 获得编码记录
     *
     * @param id 编号
     * @return 编码记录
     */
    CodeRecordDO getCodeRecord(String id);

    /**
     * 获得编码记录分页
     *
     * @param pageReqVO 分页查询
     * @return 编码记录分页
     */
    PageResult<CodeRecordDO> getCodeRecordPage(CodeRecordPageReqVO pageReqVO);

    /***
     * 获取编码记录
     * @param encodingRuleId
     * @return
     */
    List<CodeRecordDO> getCodeRecordList( String encodingRuleId);


    CodeRecordDO getCodeRecordByCode(String code);

}
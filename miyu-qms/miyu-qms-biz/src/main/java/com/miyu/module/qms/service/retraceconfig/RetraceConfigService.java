package com.miyu.module.qms.service.retraceconfig;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.retraceconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.retraceconfig.RetraceConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 追溯字段配置 Service 接口
 *
 * @author 芋道源码
 */
public interface RetraceConfigService {

    /**
     * 创建追溯字段配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createRetraceConfig(@Valid RetraceConfigSaveReqVO createReqVO);

    /**
     * 更新追溯字段配置
     *
     * @param updateReqVO 更新信息
     */
    void updateRetraceConfig(@Valid RetraceConfigSaveReqVO updateReqVO);

    /**
     * 删除追溯字段配置
     *
     * @param id 编号
     */
    void deleteRetraceConfig(String id);

    /**
     * 获得追溯字段配置
     *
     * @param id 编号
     * @return 追溯字段配置
     */
    RetraceConfigDO getRetraceConfig(String id);

    /**
     * 获得追溯字段配置分页
     *
     * @param pageReqVO 分页查询
     * @return 追溯字段配置分页
     */
    PageResult<RetraceConfigDO> getRetraceConfigPage(RetraceConfigPageReqVO pageReqVO);

    /***
     * 获取配置
     * @return
     */
    List<RetraceConfigDO> getRetraceConfigList();

}
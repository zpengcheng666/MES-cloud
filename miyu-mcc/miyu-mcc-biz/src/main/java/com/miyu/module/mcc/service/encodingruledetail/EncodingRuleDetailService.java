package com.miyu.module.mcc.service.encodingruledetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.mcc.controller.admin.encodingruledetail.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 编码规则配置详情 Service 接口
 *
 * @author 上海弥彧
 */
public interface EncodingRuleDetailService {

    /**
     * 创建编码规则配置详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createEncodingRuleDetail(@Valid EncodingRuleDetailSaveReqVO createReqVO);

    /**
     * 更新编码规则配置详情
     *
     * @param updateReqVO 更新信息
     */
    void updateEncodingRuleDetail(@Valid EncodingRuleDetailSaveReqVO updateReqVO);

    /**
     * 删除编码规则配置详情
     *
     * @param id 编号
     */
    void deleteEncodingRuleDetail(String id);

    /**
     * 获得编码规则配置详情
     *
     * @param id 编号
     * @return 编码规则配置详情
     */
    EncodingRuleDetailDO getEncodingRuleDetail(String id);

    /**
     * 获得编码规则配置详情分页
     *
     * @param pageReqVO 分页查询
     * @return 编码规则配置详情分页
     */
    PageResult<EncodingRuleDetailDO> getEncodingRuleDetailPage(EncodingRuleDetailPageReqVO pageReqVO);

}
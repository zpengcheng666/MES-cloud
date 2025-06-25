package com.miyu.module.mcc.service.encodingrule;

import java.util.*;
import javax.validation.*;

import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.controller.admin.coderecord.vo.CodeRecordSaveReqVO;
import com.miyu.module.mcc.controller.admin.encodingrule.vo.*;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 编码规则配置 Service 接口
 *
 * @author 上海弥彧
 */
public interface EncodingRuleService {

    /**
     * 创建编码规则配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createEncodingRule(@Valid EncodingRuleSaveReqVO createReqVO);

    /**
     * 更新编码规则配置
     *
     * @param updateReqVO 更新信息
     */
    void updateEncodingRule(@Valid EncodingRuleSaveReqVO updateReqVO);

    /**
     * 删除编码规则配置
     *
     * @param id 编号
     */
    void deleteEncodingRule(String id);

    /**
     * 获得编码规则配置
     *
     * @param id 编号
     * @return 编码规则配置
     */
    EncodingRuleDO getEncodingRule(String id);

    /**
     * 获得编码规则配置分页
     *
     * @param pageReqVO 分页查询
     * @return 编码规则配置分页
     */
    PageResult<EncodingRuleDO> getEncodingRulePage(EncodingRulePageReqVO pageReqVO);

    // ==================== 子表（编码规则配置详情） ====================

    /**
     * 获得编码规则配置详情列表
     *
     * @param encodingRuleId 编码规则表ID
     * @return 编码规则配置详情列表
     */
    List<EncodingRuleDetailDO> getEncodingRuleDetailListByEncodingRuleId(String encodingRuleId);

    /***
     * 编码生成
     * @return
     */
    String  generatorCode(GeneratorCodeReqDTO dto,EncodingRuleDO ruleDO,List<EncodingRuleDetailDO> detailDOS, Map<String,String> attributes) throws InterruptedException;

    /***
     * 编码生成
     * @param dto
     * @return
     * @throws InterruptedException
     */
    EncodingRuleDO  generatorCode(GeneratorCodeReqDTO dto) throws InterruptedException;
    EncodingRuleDO  generatorCode1(GeneratorCodeReqDTO dto) throws InterruptedException;

    /***
     * 编码保存
     * @param dto
     * @throws InterruptedException
     */
    void saveCode(GeneratorCodeReqDTO dto, EncodingRuleDO ruleDO, CodeRecordDO recordDO,Integer status) throws InterruptedException;


    List<EncodingRuleDO> getEncodingRuleList();

    /***
     * 更新码记录表状态
     * @param reqVO
     */
    void updateEncodingStatus(CodeRecordStatusReqVO reqVO);
}
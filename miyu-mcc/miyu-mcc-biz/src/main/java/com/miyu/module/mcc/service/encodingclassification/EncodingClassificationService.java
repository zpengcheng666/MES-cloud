package com.miyu.module.mcc.service.encodingclassification;

import java.util.*;
import javax.validation.*;
import com.miyu.module.mcc.controller.admin.encodingclassification.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingclassification.EncodingClassificationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 编码分类 Service 接口
 *
 * @author 上海弥彧
 */
public interface EncodingClassificationService {

    /**
     * 创建编码分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createEncodingClassification(@Valid EncodingClassificationSaveReqVO createReqVO);

    /**
     * 更新编码分类
     *
     * @param updateReqVO 更新信息
     */
    void updateEncodingClassification(@Valid EncodingClassificationSaveReqVO updateReqVO);

    /**
     * 删除编码分类
     *
     * @param id 编号
     */
    void deleteEncodingClassification(String id);

    /**
     * 获得编码分类
     *
     * @param id 编号
     * @return 编码分类
     */
    EncodingClassificationDO getEncodingClassification(String id);

    /**
     * 获得编码分类分页
     *
     * @param pageReqVO 分页查询
     * @return 编码分类分页
     */
    PageResult<EncodingClassificationDO> getEncodingClassificationPage(EncodingClassificationPageReqVO pageReqVO);
    List<EncodingClassificationDO> getEncodingClassificationList();

}
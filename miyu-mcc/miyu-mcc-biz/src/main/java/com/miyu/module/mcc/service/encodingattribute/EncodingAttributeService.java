package com.miyu.module.mcc.service.encodingattribute;

import java.util.*;
import javax.validation.*;
import com.miyu.module.mcc.controller.admin.encodingattribute.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingattribute.EncodingAttributeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 编码自定义属性 Service 接口
 *
 * @author 上海弥彧
 */
public interface EncodingAttributeService {

    /**
     * 创建编码自定义属性
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createEncodingAttribute(@Valid EncodingAttributeSaveReqVO createReqVO);

    /**
     * 更新编码自定义属性
     *
     * @param updateReqVO 更新信息
     */
    void updateEncodingAttribute(@Valid EncodingAttributeSaveReqVO updateReqVO);

    /**
     * 删除编码自定义属性
     *
     * @param id 编号
     */
    void deleteEncodingAttribute(String id);

    /**
     * 获得编码自定义属性
     *
     * @param id 编号
     * @return 编码自定义属性
     */
    EncodingAttributeDO getEncodingAttribute(String id);

    /**
     * 获得编码自定义属性分页
     *
     * @param pageReqVO 分页查询
     * @return 编码自定义属性分页
     */
    PageResult<EncodingAttributeDO> getEncodingAttributePage(EncodingAttributePageReqVO pageReqVO);
    List<EncodingAttributeDO> getEncodingAttributeList();

}
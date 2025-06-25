package com.miyu.module.ppm.service.inboundexceptionhandling;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.*;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 入库异常处理 Service 接口
 *
 * @author 上海弥彧
 */
public interface InboundExceptionHandlingService {

    /**
     * 创建入库异常处理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInboundExceptionHandling(@Valid InboundExceptionHandlingSaveReqVO createReqVO);

    /**
     * 更新入库异常处理
     *
     * @param updateReqVO 更新信息
     */
    void updateInboundExceptionHandling(@Valid InboundExceptionHandlingSaveReqVO updateReqVO);

    /**
     * 删除入库异常处理
     *
     * @param id 编号
     */
    void deleteInboundExceptionHandling(String id);

    /**
     *
     * @param id
     * @param resultType
     */
    void handleInboundException(String id,Integer resultType);

    /**
     * 获得入库异常处理
     *
     * @param id 编号
     * @return 入库异常处理
     */
    InboundExceptionHandlingDO getInboundExceptionHandling(String id);

    /**
     * 获得入库异常处理分页
     *
     * @param pageReqVO 分页查询
     * @return 入库异常处理分页
     */
    PageResult<InboundExceptionHandlingDO> getInboundExceptionHandlingPage(InboundExceptionHandlingPageReqVO pageReqVO);

}
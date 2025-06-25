package com.miyu.cloud.mcs.service.receiptrecord;

import javax.validation.*;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 生产单元签收记录 Service 接口
 *
 * @author miyu
 */
public interface ReceiptRecordService {

    /**
     * 创建生产单元签收记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createReceiptRecord(@Valid ReceiptRecordSaveReqVO createReqVO);

    /**
     * 更新生产单元签收记录
     *
     * @param updateReqVO 更新信息
     */
    void updateReceiptRecord(@Valid ReceiptRecordSaveReqVO updateReqVO);

    /**
     * 删除生产单元签收记录
     *
     * @param id 编号
     */
    void deleteReceiptRecord(String id);

    /**
     * 获得生产单元签收记录
     *
     * @param id 编号
     * @return 生产单元签收记录
     */
    ReceiptRecordDO getReceiptRecord(String id);

    /**
     * 获得生产单元签收记录分页
     *
     * @param pageReqVO 分页查询
     * @return 生产单元签收记录分页
     */
    PageResult<ReceiptRecordRespVO> getReceiptRecordPage(ReceiptRecordPageReqVO pageReqVO);

}

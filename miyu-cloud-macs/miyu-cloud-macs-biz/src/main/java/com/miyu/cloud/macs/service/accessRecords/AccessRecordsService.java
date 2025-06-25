package com.miyu.cloud.macs.service.accessRecords;

import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.controller.admin.accessRecords.vo.*;
import com.miyu.cloud.macs.dal.dataobject.accessRecords.AccessRecordsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.List;

/**
 * 通行记录 Service 接口
 *
 * @author 芋道源码
 */
public interface AccessRecordsService {

    /**
     * 创建通行记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAccessRecords(@Valid AccessRecordsSaveReqVO createReqVO);

    /**
     * 更新通行记录
     *
     * @param updateReqVO 更新信息
     */
    void updateAccessRecords(@Valid AccessRecordsSaveReqVO updateReqVO);

    /**
     * 删除通行记录
     *
     * @param id 编号
     */
    void deleteAccessRecords(String id);

    /**
     * 获得通行记录
     *
     * @param id 编号
     * @return 通行记录
     */
    AccessRecordsDO getAccessRecords(String id);

    /**
     * 获得通行记录分页
     *
     * @param pageReqVO 分页查询
     * @return 通行记录分页
     */
    PageResult<AccessRecordsDO> getAccessRecordsPage(AccessRecordsPageReqVO pageReqVO);

    void add(AccessRecordsDO records, AccessRecordsDO.InstructType validate, String msg);

    List<AccessRecordsDO> list(QueryWrapper<AccessRecordsDO> wrapper);
}

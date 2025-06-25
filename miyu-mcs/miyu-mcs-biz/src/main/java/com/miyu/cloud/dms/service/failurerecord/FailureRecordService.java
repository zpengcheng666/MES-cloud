package com.miyu.cloud.dms.service.failurerecord;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.*;
import com.miyu.cloud.dms.dal.dataobject.failurerecord.FailureRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 异常记录 Service 接口
 *
 * @author miyu
 */
public interface FailureRecordService {

    /**
     * 创建异常记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createFailureRecord(@Valid FailureRecordSaveReqVO createReqVO);

    /**
     * 更新异常记录
     *
     * @param updateReqVO 更新信息
     */
    void updateFailureRecord(@Valid FailureRecordSaveReqVO updateReqVO);

    /**
     * 删除异常记录
     *
     * @param id 编号
     */
    void deleteFailureRecord(String id);

    /**
     * 获得异常记录
     *
     * @param id 编号
     * @return 异常记录
     */
    FailureRecordDO getFailureRecord(String id);

    /**
     * 获得异常记录分页
     *
     * @param pageReqVO 分页查询
     * @return 异常记录分页
     */
    PageResult<FailureRecordDO> getFailureRecordPage(FailureRecordPageReqVO pageReqVO);

    /**
     * 根据设备ID获取异常记录
     * @param id 设备id
     * @return
     */
    List<FailureRecordDO> getFailureRecordList(String id);

    List<FailureRecordDO> list(Wrapper<FailureRecordDO> queryWrapper);
}

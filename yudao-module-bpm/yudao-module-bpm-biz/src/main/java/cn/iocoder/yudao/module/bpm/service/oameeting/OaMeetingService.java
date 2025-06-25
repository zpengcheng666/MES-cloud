package cn.iocoder.yudao.module.bpm.service.oameeting;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oameeting.OaMeetingDO;

import javax.validation.Valid;

/**
 * OA 会议申请 Service 接口
 *
 * @author 芋道源码
 */
public interface OaMeetingService {

    /**
     * 创建OA 会议申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOaMeeting(Long userId, @Valid OaMeetingSaveReqVO createReqVO);

    /**
     * 更新OA 会议申请
     *
     * @param updateReqVO 更新信息
     */
    void updateOaMeeting(@Valid OaMeetingSaveReqVO updateReqVO);

    /**
     * 删除OA 会议申请
     *
     * @param id 编号
     */
    void deleteOaMeeting(Long id);

    /**
     * 获得OA 会议申请
     *
     * @param id 编号
     * @return OA 会议申请
     */
    OaMeetingDO getOaMeeting(Long id);

    /**
     * 获得OA 会议申请分页
     *
     * @param pageReqVO 分页查询
     * @return OA 会议申请分页
     */
    PageResult<OaMeetingDO> getOaMeetingPage(OaMeetingPageReqVO pageReqVO);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateLeaveStatus(Long id, Integer status);

}

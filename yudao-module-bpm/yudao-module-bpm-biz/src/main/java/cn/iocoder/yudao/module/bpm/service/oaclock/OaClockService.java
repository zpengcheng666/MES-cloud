package cn.iocoder.yudao.module.bpm.service.oaclock;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oaclock.OaClockDO;

import javax.validation.Valid;

/**
 * OA 打卡 Service 接口
 *
 * @author 芋道源码
 */
public interface OaClockService {

    /**
     * 创建OA 打卡
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOaClock(@Valid OaClockSaveReqVO createReqVO);

    /**
     * 更新OA 打卡
     *
     * @param updateReqVO 更新信息
     */
    void updateOaClock(@Valid OaClockSaveReqVO updateReqVO);

    /**
     * 删除OA 打卡
     *
     * @param id 编号
     */
    void deleteOaClock(Long id);

    /**
     * 获得OA 打卡
     *
     * @param id 编号
     * @return OA 打卡
     */
    OaClockDO getOaClock(Long id);

    /**
     * 获得OA 打卡分页
     *
     * @param pageReqVO 分页查询
     * @return OA 打卡分页
     */
    PageResult<OaClockDO> getOaClockPage(OaClockPageReqVO pageReqVO);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateLeaveStatus(Long id, Integer status);

}

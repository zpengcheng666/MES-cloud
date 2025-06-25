package com.miyu.cloud.dms.service.calendarshift;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTypeDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 班次类型 Service 接口
 *
 * @author 上海弥彧
 */
public interface ShiftTypeService {

    /**
     * 创建班次类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createShiftType(@Valid ShiftTypeSaveReqVO createReqVO);

    /**
     * 更新班次类型
     *
     * @param updateReqVO 更新信息
     */
    void updateShiftType(@Valid ShiftTypeSaveReqVO updateReqVO);

    /**
     * 删除班次类型
     *
     * @param id 编号
     */
    void deleteShiftType(String id);

    /**
     * 获得班次类型
     *
     * @param id 编号
     * @return 班次类型
     */
    ShiftTypeDO getShiftType(String id);

    /**
     * 获得班次类型分页
     *
     * @param pageReqVO 分页查询
     * @return 班次类型分页
     */
    PageResult<ShiftTypeDO> getShiftTypePage(ShiftTypePageReqVO pageReqVO);

    PageResult<ShiftTypeDO> selectPageWithBasic(ShiftTypePageReqVO pageReqVO);

    // ==================== 子表（班次时间） ====================

    /**
     * 获得班次时间列表
     *
     * @param typeId 类型id
     * @return 班次时间列表
     */
    List<ShiftTimeDO> getShiftTimeListByTypeId(String typeId);

}

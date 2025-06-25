package com.miyu.cloud.dms.service.calendarshift;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTypeDO;
import com.miyu.cloud.dms.dal.mysql.calendarshift.ShiftTimeMapper;
import com.miyu.cloud.dms.dal.mysql.calendarshift.ShiftTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 班次类型 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ShiftTypeServiceImpl implements ShiftTypeService {

    @Resource
    private ShiftTypeMapper shiftTypeMapper;
    @Resource
    private ShiftTimeMapper shiftTimeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createShiftType(ShiftTypeSaveReqVO createReqVO) {
        // 插入
        ShiftTypeDO shiftType = BeanUtils.toBean(createReqVO, ShiftTypeDO.class);
        shiftTypeMapper.insert(shiftType);

        // 插入子表
        createShiftTimeList(shiftType.getId(), createReqVO.getShiftTimes());
        // 返回
        return shiftType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShiftType(ShiftTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateShiftTypeExists(updateReqVO.getId());
        // 更新
        ShiftTypeDO updateObj = BeanUtils.toBean(updateReqVO, ShiftTypeDO.class);
        shiftTypeMapper.updateById(updateObj);

        // 更新子表
        updateShiftTimeList(updateReqVO.getId(), updateReqVO.getShiftTimes());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShiftType(String id) {
        // 校验存在
        validateShiftTypeExists(id);
        // 删除
        shiftTypeMapper.deleteById(id);

        // 删除子表
        deleteShiftTimeByTypeId(id);
    }

    private void validateShiftTypeExists(String id) {
        if (shiftTypeMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500,"班次类型不存在"));
        }
    }

    @Override
    public ShiftTypeDO getShiftType(String id) {
        return shiftTypeMapper.selectById(id);
    }

    @Override
    public PageResult<ShiftTypeDO> getShiftTypePage(ShiftTypePageReqVO pageReqVO) {
        return shiftTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<ShiftTypeDO> selectPageWithBasic(ShiftTypePageReqVO pageReqVO) {
        return shiftTypeMapper.selectPageWithBasic(pageReqVO);
    }

    // ==================== 子表（班次时间） ====================

    @Override
    public List<ShiftTimeDO> getShiftTimeListByTypeId(String typeId) {
        return shiftTimeMapper.selectListByTypeId(typeId);
    }

    private void createShiftTimeList(String typeId, List<ShiftTimeDO> list) {
        list.forEach(o -> o.setTypeId(typeId));
        shiftTimeMapper.insertBatch(list);
    }

    private void updateShiftTimeList(String typeId, List<ShiftTimeDO> list) {
        deleteShiftTimeByTypeId(typeId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createShiftTimeList(typeId, list);
    }

    private void deleteShiftTimeByTypeId(String typeId) {
        shiftTimeMapper.deleteByTypeId(typeId);
    }

}

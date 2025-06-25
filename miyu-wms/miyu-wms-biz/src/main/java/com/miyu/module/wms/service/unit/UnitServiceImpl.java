package com.miyu.module.wms.service.unit;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.unit.vo.*;
import com.miyu.module.wms.dal.dataobject.unit.UnitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.unit.UnitMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 单位 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class UnitServiceImpl implements UnitService {

    @Resource
    private UnitMapper unitMapper;

    @Override
    public String createUnit(UnitSaveReqVO createReqVO) {
        // 插入
        UnitDO unit = BeanUtils.toBean(createReqVO, UnitDO.class);
        unitMapper.insert(unit);
        // 返回
        return unit.getId();
    }

    @Override
    public void updateUnit(UnitSaveReqVO updateReqVO) {
        // 校验存在
        validateUnitExists(updateReqVO.getId());
        // 更新
        UnitDO updateObj = BeanUtils.toBean(updateReqVO, UnitDO.class);
        unitMapper.updateById(updateObj);
    }

    @Override
    public void deleteUnit(String id) {
        // 校验存在
        validateUnitExists(id);
        // 删除
        unitMapper.deleteById(id);
    }

    private void validateUnitExists(String id) {
        if (unitMapper.selectById(id) == null) {
            throw exception(UNIT_NOT_EXISTS);
        }
    }

    @Override
    public UnitDO getUnit(String id) {
        return unitMapper.selectById(id);
    }

    @Override
    public PageResult<UnitDO> getUnitPage(UnitPageReqVO pageReqVO) {
        return unitMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UnitDO> getUnitList() {
        return unitMapper.selectList();
    }

}
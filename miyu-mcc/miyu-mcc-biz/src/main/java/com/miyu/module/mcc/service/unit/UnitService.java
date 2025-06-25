package com.miyu.module.mcc.service.unit;

import java.util.*;
import javax.validation.*;
import com.miyu.module.mcc.controller.admin.unit.vo.*;
import com.miyu.module.mcc.dal.dataobject.unit.UnitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 单位 Service 接口
 *
 * @author 上海弥彧
 */
public interface UnitService {

    /**
     * 创建单位
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createUnit(@Valid UnitSaveReqVO createReqVO);

    /**
     * 更新单位
     *
     * @param updateReqVO 更新信息
     */
    void updateUnit(@Valid UnitSaveReqVO updateReqVO);

    /**
     * 删除单位
     *
     * @param id 编号
     */
    void deleteUnit(String id);

    /**
     * 获得单位
     *
     * @param id 编号
     * @return 单位
     */
    UnitDO getUnit(String id);

    /**
     * 获得单位分页
     *
     * @param pageReqVO 分页查询
     * @return 单位分页
     */
    PageResult<UnitDO> getUnitPage(UnitPageReqVO pageReqVO);


    List<UnitDO> getUnitList();
}
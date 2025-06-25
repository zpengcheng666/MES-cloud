package com.miyu.cloud.macs.service.region;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.controller.admin.region.vo.*;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 区域 Service 接口
 *
 * @author 芋道源码
 */
public interface RegionService {

    /**
     * 创建区域
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createRegion(@Valid RegionSaveReqVO createReqVO);

    /**
     * 更新区域
     *
     * @param updateReqVO 更新信息
     */
    void updateRegion(@Valid RegionSaveReqVO updateReqVO);

    /**
     * 删除区域
     *
     * @param id 编号
     */
    void deleteRegion(String id);

    /**
     * 获得区域
     *
     * @param id 编号
     * @return 区域
     */
    RegionDO getRegion(String id);

    /**
     * 获得区域列表
     *
     * @param listReqVO 查询条件
     * @return 区域列表
     */
    List<RegionDO> getRegionList(RegionListReqVO listReqVO);

    List<RegionDO> list();

    RegionDO getById(String regionId);

    RegionDO getOne(QueryWrapper<RegionDO> wrapper);
}

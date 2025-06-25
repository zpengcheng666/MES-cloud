package com.miyu.cloud.macs.service.visitorRegion;

import javax.validation.*;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.*;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.List;

/**
 * 访客区域权限 Service 接口
 *
 * @author 芋道源码
 */
public interface VisitorRegionService {

    /**
     * 创建访客区域权限
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createVisitorRegion(@Valid VisitorRegionSaveReqVO createReqVO);

    /**
     * 更新访客区域权限
     *
     * @param updateReqVO 更新信息
     */
    void updateVisitorRegion(@Valid VisitorRegionSaveReqVO updateReqVO);

    /**
     * 删除访客区域权限
     *
     * @param id 编号
     */
    void deleteVisitorRegion(String id);

    /**
     * 获得访客区域权限
     *
     * @param id 编号
     * @return 访客区域权限
     */
    VisitorRegionDO getVisitorRegion(String id);

    /**
     * 获得访客区域权限分页
     *
     * @param pageReqVO 分页查询
     * @return 访客区域权限分页
     */
    PageResult<VisitorRegionDO> getVisitorRegionPage(VisitorRegionPageReqVO pageReqVO);

    boolean validateAccess(VisitorDO visitor, RegionDO region);

    PageResult<VisitorRegionRespVO> getVisitorRegionPageByVisitor(VisitorRegionPageReqVO pageReqVO);

    PageResult<VisitorRegionRespVO> getByVisitorAndApplication(VisitorRegionPageReqVO pageReqVO);

    List<VisitorRegionRespVO> regionShowList(String applicationId, String visitorId);
}

package com.miyu.cloud.macs.service.visitorRegion;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.cloud.macs.dal.dataobject.accessApplication.AccessApplicationDO;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import com.miyu.cloud.macs.dal.mysql.accessApplication.AccessApplicationMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.*;
import com.miyu.cloud.macs.dal.dataobject.visitorRegion.VisitorRegionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.visitorRegion.VisitorRegionMapper;

import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 访客区域权限 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class VisitorRegionServiceImpl implements VisitorRegionService {

    @Resource
    private VisitorRegionMapper visitorRegionMapper;
    @Resource
    private AccessApplicationMapper accessApplicationMapper;

    @Override
    public String createVisitorRegion(VisitorRegionSaveReqVO createReqVO) {
        // 插入
        VisitorRegionDO visitorRegion = BeanUtils.toBean(createReqVO, VisitorRegionDO.class);
        visitorRegionMapper.insert(visitorRegion);
        // 返回
        return visitorRegion.getId();
    }

    @Override
    public void updateVisitorRegion(VisitorRegionSaveReqVO updateReqVO) {
        // 校验存在
        validateVisitorRegionExists(updateReqVO.getId());
        // 更新
        VisitorRegionDO updateObj = BeanUtils.toBean(updateReqVO, VisitorRegionDO.class);
        visitorRegionMapper.updateById(updateObj);
    }

    @Override
    public void deleteVisitorRegion(String id) {
        // 校验存在
        validateVisitorRegionExists(id);
        // 删除
        visitorRegionMapper.deleteById(id);
    }

    private void validateVisitorRegionExists(String id) {
        if (visitorRegionMapper.selectById(id) == null) {
            throw exception(VISITOR_REGION_NOT_EXISTS);
        }
    }

    @Override
    public VisitorRegionDO getVisitorRegion(String id) {
        return visitorRegionMapper.selectById(id);
    }

    @Override
    public PageResult<VisitorRegionDO> getVisitorRegionPage(VisitorRegionPageReqVO pageReqVO) {
        return visitorRegionMapper.selectPage(pageReqVO);
    }

    @Override
    public boolean validateAccess(VisitorDO visitor, RegionDO region) {
        Date date = new Date();
        String msg = "";
        QueryWrapper<VisitorRegionDO> wrapper = new QueryWrapper<>();
        wrapper.eq("visitor_id", visitor.getId()).eq("region_id", region.getId());
        wrapper.ge("invalid_date",date).lt("effective_date", date);
        List<VisitorRegionDO> visitorRegionList = visitorRegionMapper.selectList(wrapper);
        if (visitorRegionList.size() == 0) msg = "未授权!";
        for (VisitorRegionDO visitorRegion : visitorRegionList) {
            AccessApplicationDO application = accessApplicationMapper.selectById(visitorRegion.getApplicationId());
            msg = "权限申请中!";
            if ("2".equals(application.getStatus())) return true;
        }
        throw new RuntimeException(msg);
    }

    @Override
    public PageResult<VisitorRegionRespVO> getVisitorRegionPageByVisitor(VisitorRegionPageReqVO pageReqVO) {
        Page<VisitorRegionRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        List<VisitorRegionRespVO> list = visitorRegionMapper.selectPageByVisitor(page, pageReqVO.getVisitorId());
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    public PageResult<VisitorRegionRespVO> getByVisitorAndApplication(VisitorRegionPageReqVO pageReqVO) {
        Page<VisitorRegionRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        List<VisitorRegionRespVO> list = visitorRegionMapper.selectPageByVisitorAndApplication(page, pageReqVO.getVisitorId(), pageReqVO.getApplicationId());
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    public List<VisitorRegionRespVO> regionShowList(String applicationId, String visitorId) {
        return visitorRegionMapper.regionShowList(applicationId, visitorId);
    }
}

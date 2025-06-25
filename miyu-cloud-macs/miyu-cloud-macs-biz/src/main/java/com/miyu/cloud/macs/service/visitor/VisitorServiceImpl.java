package com.miyu.cloud.macs.service.visitor;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.VisitorRegionPageReqVO;
import com.miyu.cloud.macs.restServer.entity.MacsRestUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.visitor.vo.*;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.visitor.VisitorMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 申请角色 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class VisitorServiceImpl implements VisitorService {

    @Resource
    private VisitorMapper visitorMapper;

    @Override
    public String createVisitor(VisitorSaveReqVO createReqVO) {
        // 插入
        VisitorDO visitor = BeanUtils.toBean(createReqVO, VisitorDO.class);
        visitorMapper.insert(visitor);
        // 返回
        return visitor.getId();
    }

    @Override
    public void updateVisitor(VisitorSaveReqVO updateReqVO) {
        // 校验存在
        validateVisitorExists(updateReqVO.getId());
        // 更新
        VisitorDO updateObj = BeanUtils.toBean(updateReqVO, VisitorDO.class);
        visitorMapper.updateById(updateObj);
    }

    @Override
    public void deleteVisitor(String id) {
        // 校验存在
        validateVisitorExists(id);
        // 删除
        visitorMapper.deleteById(id);
    }

    private void validateVisitorExists(String id) {
        if (visitorMapper.selectById(id) == null) {
            throw exception(VISITOR_NOT_EXISTS);
        }
    }

    @Override
    public VisitorDO getVisitor(String id) {
        return visitorMapper.selectById(id);
    }

    @Override
    public PageResult<VisitorDO> getVisitorPage(VisitorPageReqVO pageReqVO) {
        return visitorMapper.selectPage(pageReqVO);
    }

    @Override
    public void update(UpdateWrapper<VisitorDO> wrapper) {
        visitorMapper.update(wrapper);
    }

    /*@Override
    public VisitorDO getVisitorByCharacter(MacsRestUser visitor) {
        String code = visitor.getCode();
        String facialFeatureString = visitor.getFacialFeatureString();
        String fingerprintString = visitor.getFingerprintString();
        if (code != null) {
            return getVisitorByCode(code);
        } else if (facialFeatureString != null) {
            return getVisitorByFacialFeatureString(facialFeatureString);
        } else if (fingerprintString != null) {
            return getVisitorByFingerprintString(fingerprintString);
        }
        return null;
    }*/
    @Override
    public VisitorDO getVisitorByCode(String code) {
        List<VisitorDO> macsVisitors = visitorMapper.selectList(new QueryWrapper<VisitorDO>().eq("code", code));
        if (macsVisitors.size() > 0) {
            if (macsVisitors.size() > 1) throw new RuntimeException("当前卡号存在多名用户!");
            return macsVisitors.get(0);
        }
        return null;
    }

    @Override
    public VisitorDO getVisitorByFacialFeatureString(String facialFeatureString) {
        return null;
    }

    @Override
    public VisitorDO getVisitorByFingerprintString(String fingerprintString) {
        return null;
    }

    @Override
    public PageResult<VisitorDO> getPageByApplicationId(VisitorRegionPageReqVO pageReqVO) {
        Page<VisitorDO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        List<VisitorDO> list = visitorMapper.getPageByApplicationId(page,pageReqVO.getApplicationId());
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    public void visitorDeparture(VisitorSaveReqVO updateReqVO) {
        UpdateWrapper<VisitorDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", 2);
        updateWrapper.set("code", null);
        updateWrapper.set("facial_feature", null);
        updateWrapper.set("fingerprint", null);
        updateWrapper.eq("id", updateReqVO.getId());
        visitorMapper.update(updateWrapper);
    }

    @Override
    public List<VisitorDO> list() {
        return visitorMapper.selectList();
    }

    @Override
    public List<VisitorDO> list(Wrapper<VisitorDO> queryWrapper) {
        return visitorMapper.selectList(queryWrapper);
    }
}

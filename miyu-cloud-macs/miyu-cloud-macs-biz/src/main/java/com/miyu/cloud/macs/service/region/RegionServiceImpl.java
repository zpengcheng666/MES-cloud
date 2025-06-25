package com.miyu.cloud.macs.service.region;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.region.vo.*;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.region.RegionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 区域 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Override
    public String createRegion(RegionSaveReqVO createReqVO) {
        // 校验上级id的有效性
        validateParentRegion(null, createReqVO.getParentId());
        // 校验区域编码的唯一性
        validateRegionCodeUnique(null, createReqVO.getParentId(), createReqVO.getCode());

        // 插入
        RegionDO region = BeanUtils.toBean(createReqVO, RegionDO.class);
        regionMapper.insert(region);
        // 返回
        return region.getId();
    }

    @Override
    public void updateRegion(RegionSaveReqVO updateReqVO) {
        // 校验存在
        validateRegionExists(updateReqVO.getId());
        // 校验上级id的有效性
        validateParentRegion(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验区域编码的唯一性
        validateRegionCodeUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getCode());

        // 更新
        RegionDO updateObj = BeanUtils.toBean(updateReqVO, RegionDO.class);
        regionMapper.updateById(updateObj);
    }

    @Override
    public void deleteRegion(String id) {
        // 校验存在
        validateRegionExists(id);
        // 校验是否有子区域
        if (regionMapper.selectCountByParentId(id) > 0) {
            throw exception(REGION_EXITS_CHILDREN);
        }
        // 删除
        regionMapper.deleteById(id);
    }

    private void validateRegionExists(String id) {
        if (regionMapper.selectById(id) == null) {
            throw exception(REGION_NOT_EXISTS);
        }
    }

    private void validateParentRegion(String id, String parentId) {
        if (parentId == null || RegionDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父区域
        if (Objects.equals(id, parentId)) {
            throw exception(REGION_PARENT_ERROR);
        }
        // 2. 父区域不存在
        RegionDO parentRegion = regionMapper.selectById(parentId);
        if (parentRegion == null) {
            throw exception(REGION_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父区域，如果父区域是自己的子区域，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentRegion.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(REGION_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父区域
            if (parentId == null || RegionDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentRegion = regionMapper.selectById(parentId);
            if (parentRegion == null) {
                break;
            }
        }
    }

    private void validateRegionCodeUnique(String id, String parentId, String code) {
        RegionDO region = regionMapper.selectByParentIdAndCode(parentId, code);
        if (region == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的区域
        if (id == null) {
            throw exception(REGION_CODE_DUPLICATE);
        }
        if (!Objects.equals(region.getId(), id)) {
            throw exception(REGION_CODE_DUPLICATE);
        }
    }

    @Override
    public RegionDO getRegion(String id) {
        return regionMapper.selectById(id);
    }

    @Override
    public List<RegionDO> getRegionList(RegionListReqVO listReqVO) {
        return regionMapper.selectList(listReqVO);
    }

    @Override
    public List<RegionDO> list() {
        return regionMapper.selectList();
    }

    @Override
    public RegionDO getById(String regionId) {
        return regionMapper.selectById(regionId);
    }

    @Override
    public RegionDO getOne(QueryWrapper<RegionDO> wrapper) {
        return regionMapper.selectOne(wrapper);
    }
}

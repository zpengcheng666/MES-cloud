package com.miyu.module.qms.service.samplingstandard;

import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import com.miyu.module.qms.service.samplingrule.SamplingRuleService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.qms.controller.admin.samplingstandard.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.samplingstandard.SamplingStandardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 抽样标准 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class SamplingStandardServiceImpl implements SamplingStandardService {

    @Resource
    private SamplingStandardMapper samplingStandardMapper;

    @Resource
    private SamplingRuleService samplingRuleService;

    @Override
    public String createSamplingStandard(SamplingStandardSaveReqVO createReqVO) {
        // 校验父项目分类ID的有效性
        validateParentSamplingStandard(null, createReqVO.getParentId());
        // 校验抽样标准名称的唯一性
        validateSamplingStandardTypeNameUnique(null, createReqVO.getParentId(), createReqVO.getName());
        // 插入
        SamplingStandardDO samplingStandard = BeanUtils.toBean(createReqVO, SamplingStandardDO.class);
        samplingStandardMapper.insert(samplingStandard);
        // 返回
        return samplingStandard.getId();
    }

    @Override
    public void updateSamplingStandard(SamplingStandardSaveReqVO updateReqVO) {
        // 校验存在
        validateSamplingStandardExists(updateReqVO.getId());
        // 校验父项目分类ID的有效性
        validateParentSamplingStandard(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验抽样标准名称的唯一性
        validateSamplingStandardTypeNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        SamplingStandardDO updateObj = BeanUtils.toBean(updateReqVO, SamplingStandardDO.class);
        samplingStandardMapper.updateById(updateObj);
    }

    @Override
    public void deleteSamplingStandard(String id) {
        // 校验存在
        validateSamplingStandardExists(id);
        // 校验是否有子检测项目分类
        if (samplingStandardMapper.selectCountByParentId(id) > 0) {
            throw exception(SAMPLING_STANDARD_EXITS_CHILDREN);
        }

        // 存在抽样规则
        List<SamplingRuleDO> SamplingRuleDOS = samplingRuleService.getSamplingRuleByStandardId(id);
        if (!CollectionUtils.isEmpty(SamplingRuleDOS)){
            throw exception(SAMPLING_STANDARD_EXITS_RULE);
        }
        // 删除
        samplingStandardMapper.deleteById(id);
    }

    private void validateSamplingStandardExists(String id) {
        if (samplingStandardMapper.selectById(id) == null) {
            throw exception(SAMPLING_STANDARD_NOT_EXISTS);
        }
    }

    @Override
    public SamplingStandardDO getSamplingStandard(String id) {
        return samplingStandardMapper.selectById(id);
    }

    @Override
    public List<SamplingStandardDO> getSamplingStandardList(SamplingStandardListReqVO listReqVO) {
        return samplingStandardMapper.selectList(listReqVO);

    }

    private void validateParentSamplingStandard(String id, String parentId) {
        if (parentId == null || SamplingStandardDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父检测项目分类
        if (Objects.equals(id, parentId)) {
            throw exception(SAMPLING_STANDARD_PARENT_ERROR);
        }
        // 2. 父检测项目分类不存在
        SamplingStandardDO parentparentSamplingStandard = samplingStandardMapper.selectById(parentId);
        if (parentparentSamplingStandard == null) {
            throw exception(SAMPLING_STANDARD_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父检测项目分类，如果父检测项目分类是自己的子检测项目分类，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentparentSamplingStandard.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(SAMPLING_STANDARD_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父检测项目分类
            if (parentId == null || SamplingStandardDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentparentSamplingStandard = samplingStandardMapper.selectById(parentId);
            if (parentparentSamplingStandard == null) {
                break;
            }
        }
    }

    private void validateSamplingStandardTypeNameUnique(String id, String parentId, String itemName) {
        SamplingStandardDO samplingStandard = samplingStandardMapper.selectByParentIdAndItemName(parentId, itemName);
        if (samplingStandard == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的检测项目分类
        if (id == null) {
            throw exception(SAMPLING_STANDARD_NAME_DUPLICATE);
        }
        if (!Objects.equals(samplingStandard.getId(), id)) {
            throw exception(SAMPLING_STANDARD_NAME_DUPLICATE);
        }
    }
}
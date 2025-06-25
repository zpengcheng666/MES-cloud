package com.miyu.module.qms.service.samplingrule;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import com.miyu.module.qms.controller.admin.samplingrule.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.samplingrule.SamplingRuleMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 抽样规则 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SamplingRuleServiceImpl implements SamplingRuleService {

    @Resource
    private SamplingRuleMapper samplingRuleMapper;

    @Override
    public String createSamplingRule(SamplingRuleSaveReqVO createReqVO) {
        // 插入
        SamplingRuleDO samplingRule = BeanUtils.toBean(createReqVO, SamplingRuleDO.class);
        samplingRuleMapper.insert(samplingRule);
        // 返回
        return samplingRule.getId();
    }

    @Override
    public void updateSamplingRule(SamplingRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateSamplingRuleExists(updateReqVO.getId());
        // 更新
        SamplingRuleDO updateObj = BeanUtils.toBean(updateReqVO, SamplingRuleDO.class);
        samplingRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteSamplingRule(String id) {
        // 校验存在
        validateSamplingRuleExists(id);
        // 删除
        samplingRuleMapper.deleteById(id);
    }

    private void validateSamplingRuleExists(String id) {
        if (samplingRuleMapper.selectById(id) == null) {
            throw exception(SAMPLING_STANDARD_RULE_NOT_EXISTS);
        }
    }

    @Override
    public SamplingRuleDO getSamplingRule(String id) {
        return samplingRuleMapper.selectById(id);
    }

    @Override
    public PageResult<SamplingRuleDO> getSamplingRulePage(SamplingRulePageReqVO pageReqVO) {
        return samplingRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SamplingRuleDO> getSamplingRuleByStandardId(String standardId) {
        return samplingRuleMapper.getSamplingRuleByStandardId(standardId);
    }

    @Override
    public List<SamplingRuleInfoRespVO> getSamplingRuleInfo(String standardId) {
        List<SamplingRuleInfoRespVO> list = new ArrayList<>();
        List<SamplingRuleDO> dos = samplingRuleMapper.getSamplingRuleByStandardId(standardId);

        //以范围为主键分组
        Map<Integer,List<SamplingRuleDO>>  map = new HashMap<>();
        for (SamplingRuleDO samplingRuleDO :dos){

            if (CollectionUtils.isEmpty(map.get(samplingRuleDO.getMinValue()))){
                map.put(samplingRuleDO.getMinValue(), Lists.newArrayList(samplingRuleDO));
            }else {
                map.get(samplingRuleDO.getMinValue()).add(samplingRuleDO);
            }
        }
        for (Map.Entry<Integer,List<SamplingRuleDO>> entry : map.entrySet()) {
            SamplingRuleInfoRespVO vo = new SamplingRuleInfoRespVO();
            List<SamplingRuleDO> doList = entry.getValue();
            Map<Integer, SamplingRuleDO> samplingRuleDOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(doList,SamplingRuleDO::getInspectionLevelType);
            vo.setMinValue(entry.getKey());
            vo.setMaxValue(entry.getValue().get(0).getMaxValue());

            vo.setSampleSizeCode1(samplingRuleDOMap.get(1).getSampleSizeCode());
            vo.setSampleSizeCode2(samplingRuleDOMap.get(2).getSampleSizeCode());
            vo.setSampleSizeCode3(samplingRuleDOMap.get(3).getSampleSizeCode());
            vo.setSampleSizeCode4(samplingRuleDOMap.get(4).getSampleSizeCode());
            vo.setSampleSizeCode5(samplingRuleDOMap.get(5).getSampleSizeCode());
            vo.setSampleSizeCode6(samplingRuleDOMap.get(6).getSampleSizeCode());
            vo.setSampleSizeCode7(samplingRuleDOMap.get(7).getSampleSizeCode());
            list.add(vo);
        }

        Collections.sort(list, new Comparator<SamplingRuleInfoRespVO>() {
            @Override
            public int compare(SamplingRuleInfoRespVO o1, SamplingRuleInfoRespVO o2) {

                return  o1.getMinValue().compareTo(o2.getMinValue());
            }
        });
        return list;
    }

}
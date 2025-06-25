package com.miyu.module.qms.service.samplingruleconfig;

import com.google.common.collect.Lists;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.qms.controller.admin.samplingrule.vo.SamplingRuleInfoRespVO;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.qms.controller.admin.samplingruleconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingruleconfig.SamplingRuleConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.samplingruleconfig.SamplingRuleConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 抽样规则（检验抽样方案） Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SamplingRuleConfigServiceImpl implements SamplingRuleConfigService {

    @Resource
    private SamplingRuleConfigMapper samplingRuleConfigMapper;

    @Override
    public String createSamplingRuleConfig(SamplingRuleConfigSaveBatchReqVO createReqVO) {
        // 插入

        if (CollectionUtils.isEmpty(createReqVO.getAqlReqVoList())) {
            throw exception(SAMPLING_RULE_AQL_NOT_EXISTS);
        }
        List<SamplingRuleConfigDO> samplingRuleConfigDOS = new ArrayList<>();
        for (SamplingAQLReqVo vo : createReqVO.getAqlReqVoList()) {
            SamplingRuleConfigDO samplingRuleConfig = BeanUtils.toBean(createReqVO, SamplingRuleConfigDO.class);

            samplingRuleConfig.setAcceptanceQualityLimit(vo.getAcceptanceQualityLimit());
            samplingRuleConfig.setAcceptNum(vo.getAcceptNum());
            samplingRuleConfig.setRejectionNum(vo.getRejectionNum());
            samplingRuleConfigDOS.add(samplingRuleConfig);
        }

        samplingRuleConfigMapper.insertBatch(samplingRuleConfigDOS);
        // 返回
        return "";
    }

    @Override
    public void updateSamplingRuleConfig(SamplingRuleConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateSamplingRuleConfigExists(updateReqVO.getId());
        // 更新
        SamplingRuleConfigDO updateObj = BeanUtils.toBean(updateReqVO, SamplingRuleConfigDO.class);
        samplingRuleConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteSamplingRuleConfig(String id) {
        // 校验存在
        validateSamplingRuleConfigExists(id);
        // 删除
        samplingRuleConfigMapper.deleteById(id);
    }

    private void validateSamplingRuleConfigExists(String id) {
        if (samplingRuleConfigMapper.selectById(id) == null) {
            throw exception(SAMPLING_RULE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public SamplingRuleConfigDO getSamplingRuleConfig(String id) {
        return samplingRuleConfigMapper.selectById(id);
    }

    @Override
    public PageResult<SamplingRuleConfigDO> getSamplingRuleConfigPage(SamplingRuleConfigPageReqVO pageReqVO) {
        return samplingRuleConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SamplingRuleConfigDO> getSamplingRuleConfigList(String code, String standardId, Integer samplingRuleType) {
        return samplingRuleConfigMapper.getSamplingRuleConfigList(code, standardId, samplingRuleType);
    }

    @Override
    public Map<String, Object> getSamplingRuleConfigInfo(String standardId) {

        List<SamplingRuleConfigDO> configList = samplingRuleConfigMapper.getSamplingRuleConfigList(null, standardId, null);
        Map<String, Object> finalMap = new HashMap<>();
        Map<Integer, List<SamplingRuleConfigDO>> listMap = new HashMap<>();
        for (SamplingRuleConfigDO samplingRuleConfigDO :configList){
            if (CollectionUtils.isEmpty(listMap.get(samplingRuleConfigDO.getSamplingRuleType()))) {
                listMap.put(samplingRuleConfigDO.getSamplingRuleType(), Lists.newArrayList(samplingRuleConfigDO));
            } else {
                listMap.get(samplingRuleConfigDO.getSamplingRuleType()).add(samplingRuleConfigDO);
            }

        }

        for (Map.Entry<Integer,List<SamplingRuleConfigDO>> mapInfo : listMap.entrySet()) {
            List<SamplingRuleConfigDO> configDOS = mapInfo.getValue();



            List<BigDecimal> aqls = configDOS.stream().map(SamplingRuleConfigDO::getAcceptanceQualityLimit).collect(Collectors.toList());
            aqls = aqls.stream().distinct().collect(Collectors.toList());
            Collections.sort(aqls, new Comparator<BigDecimal>() {
                @Override
                public int compare(BigDecimal o1, BigDecimal o2) {

                    return o1.compareTo(o2);
                }
            });
            List<Map<String,String>> headMap = new ArrayList<>();
            Map<BigDecimal,Integer> integerMap = new HashMap<>();
            for (int i = 0;i <aqls.size();i++){
                BigDecimal a = aqls.get(i);
                Map<String,String> map1 = new HashMap<>();
                map1.put("prop", "AQL"+i);
                map1.put("label", a.toString());
                headMap.add(map1);
                integerMap.put(a,i);
            }

            Map<String, List<SamplingRuleConfigDO>> map = new HashMap<>();
            for (SamplingRuleConfigDO configDO : configDOS) {
                if (CollectionUtils.isEmpty(map.get(configDO.getSampleSizeCode()))) {
                    map.put(configDO.getSampleSizeCode(), Lists.newArrayList(configDO));
                } else {
                    map.get(configDO.getSampleSizeCode()).add(configDO);
                }
            }


            List<Map<String,String>> objects = new ArrayList<>();
            for (Map.Entry<String,List<SamplingRuleConfigDO>> entry : map.entrySet()) {
                Map<String,String> map1 = new HashMap<>();
                map1.put("code",entry.getKey());
                for (SamplingRuleConfigDO samplingRuleConfigDO :entry.getValue()){
                    map1.put("AQL"+integerMap.get(samplingRuleConfigDO.getAcceptanceQualityLimit())+"Ac",samplingRuleConfigDO.getAcceptNum().toString());
                    map1.put("AQL"+integerMap.get(samplingRuleConfigDO.getAcceptanceQualityLimit())+"Re",samplingRuleConfigDO.getRejectionNum().toString());
                }


                objects.add(map1);
            }

            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("dataList",objects);//数据
            typeMap.put("headList", headMap);//表头


            if (mapInfo.getKey().intValue() ==1){
                finalMap.put("normalInfos",typeMap);
            }else  if (mapInfo.getKey().intValue() ==2){ //加严
                finalMap.put("normalInfos1",typeMap);
            }else if (mapInfo.getKey().intValue() ==3){ //放宽
                finalMap.put("normalInfos2",typeMap);
            }

        }


        return finalMap;
    }

    @Override
    public String createSamplingRuleConfigs(List<SamplingRuleConfigDO> dos) {
        samplingRuleConfigMapper.insertBatch(dos);
        return "";
    }

}
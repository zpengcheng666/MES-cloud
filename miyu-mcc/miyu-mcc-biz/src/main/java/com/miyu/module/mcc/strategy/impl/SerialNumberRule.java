package com.miyu.module.mcc.strategy.impl;

import com.google.common.collect.Lists;
import com.miyu.module.mcc.controller.admin.coderecord.vo.CodeRecordPageReqVO;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import com.miyu.module.mcc.service.coderecord.CodeRecordService;
import com.miyu.module.mcc.strategy.IEncodingRuleStrategy;
import com.miyu.module.mcc.utils.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SerialNumberRule implements IEncodingRuleStrategy {
    @Resource
    private CodeRecordService codeRecordService;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public String getRuleValue(EncodingRuleDetailDO detailDO, Map<String, String> attributes, List<EncodingRuleDetailDO> detailDOS, EncodingRuleDO ruleDO) {
        List<Integer> types = Lists.newArrayList(8, 9, 10);
        List<EncodingRuleDetailDO> dateDos = detailDOS.stream().filter(detailDO1 -> types.contains(detailDO1.getType())).collect(Collectors.toList());
        Integer number = 0;//获取流水号所在位置
        Integer bitNumber = detailDO.getBitNumber();
        for (EncodingRuleDetailDO ruleDetailDO : detailDOS) {

            if (ruleDetailDO.getType().intValue() != 6) {
                number = number + ruleDetailDO.getBitNumber();
            } else {
                break;
            }
        }

        List<CodeRecordDO> list = codeRecordService.getCodeRecordList(ruleDO.getId());
        if (!CollectionUtils.isEmpty(dateDos)) {
            LocalDate currentDate = LocalDate.now();
            //如果有日期定义则 取最新的流水码 + 1
            if (CollectionUtils.isEmpty(list)) {
                redisTemplate.opsForValue().set(ruleDO.getId(), 1);
                return StringListUtils.getString("1", bitNumber);
            } else {
                String code = list.get(0).getCode();

                String date  = StringUtils.substring(code, number-2, number);
                if (Integer.parseInt(date) == currentDate.getDayOfMonth()){

                    if (redisTemplate.opsForValue().get(ruleDO.getId()) != null){
                        Integer s = (int)redisTemplate.opsForValue().get(ruleDO.getId()) + 1;
                        redisTemplate.opsForValue().set(ruleDO.getId(), s);
                        return StringListUtils.getString(s.toString(), bitNumber);
                    }else {
                        String substr = StringUtils.substring(code, number, number + bitNumber);
                        Integer s = Integer.parseInt(substr) + 1;
                        redisTemplate.opsForValue().set(ruleDO.getId(), s);
                        return StringListUtils.getString(s.toString(), bitNumber);
                    }

                }else {//如果没有当前日期的  取1
                    redisTemplate.opsForValue().set(ruleDO.getId(), 1);
                    return StringListUtils.getString("1", bitNumber);
                }

            }

        } else {

            if (CollectionUtils.isEmpty(list)) {
                return StringListUtils.getString("1", bitNumber);
            } else {
                if (ruleDO.getAutoRelease().intValue() == 0) {//如果不释放
                    String code = list.get(0).getCode();
                    String substr = StringUtils.substring(code, number, number + bitNumber);
                    Integer s = Integer.parseInt(substr) + 1;
                    return StringListUtils.getString(s.toString(), bitNumber);
                } else {//如果释放  则查看之前的码是否有未用的

                    Integer total = 10 * bitNumber;
                    List<Integer> numbers = new ArrayList<>();
                    for (CodeRecordDO codeRecordDO : list) {
                        String code = codeRecordDO.getCode();
                        String substr = StringUtils.substring(code, number, number + bitNumber);
                        numbers.add(Integer.parseInt(substr));
                    }
                    Integer code = 0;
                    for (int i = 1; i < total; i++) {

                        if (!numbers.contains(i)) {
                            code = i;
                            break;
                        }
                    }
                    return StringListUtils.getString(code.toString(),bitNumber);

                }

            }


        }


    }


    public static void main(String[] args) {

        String code = "WD202403030001";

        String substr = StringUtils.substring(code, 10, 14);

        Integer s = Integer.parseInt(substr);
        System.out.println(s);
    }
}

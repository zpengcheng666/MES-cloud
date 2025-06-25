package com.miyu.module.mcc.service.encodingrule;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.controller.admin.coderecord.vo.CodeRecordSaveReqVO;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import com.miyu.module.mcc.service.coderecord.CodeRecordService;
import com.miyu.module.mcc.strategy.EncodingRuleFactory;
import com.miyu.module.mcc.strategy.IEncodingRuleStrategy;
import com.miyu.module.mcc.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.miyu.module.mcc.controller.admin.encodingrule.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.encodingrule.EncodingRuleMapper;
import com.miyu.module.mcc.dal.mysql.encodingruledetail.EncodingRuleDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;

/**
 * 编码规则配置 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
@Slf4j
public class EncodingRuleServiceImpl implements EncodingRuleService {

    @Resource
    private EncodingRuleMapper encodingRuleMapper;
    @Resource
    private EncodingRuleDetailMapper encodingRuleDetailMapper;
    @Resource
    private RedisLock redisLock;
    @Resource
    private EncodingRuleFactory encodingRuleFactory;
    @Resource
    private CodeRecordService codeRecordService;
    @Resource
    private RedissonClient redissonClient;
    private static final String REDIS_COST_MODEL_ID_LOCK = "redis_cost_model_id_lock";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createEncodingRule(EncodingRuleSaveReqVO createReqVO) {
        // 插入
        EncodingRuleDO encodingRule = BeanUtils.toBean(createReqVO, EncodingRuleDO.class);
        encodingRuleMapper.insert(encodingRule);

        // 插入子表
        createEncodingRuleDetailList(encodingRule.getId(), createReqVO.getEncodingRuleDetails());
        // 返回
        return encodingRule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEncodingRule(EncodingRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateEncodingRuleExists(updateReqVO.getId());
        // 更新
        EncodingRuleDO updateObj = BeanUtils.toBean(updateReqVO, EncodingRuleDO.class);
        encodingRuleMapper.updateById(updateObj);

        // 更新子表
        updateEncodingRuleDetailList(updateReqVO.getId(), updateReqVO.getEncodingRuleDetails());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEncodingRule(String id) {
        // 校验存在
        validateEncodingRuleExists(id);
        // 删除
        encodingRuleMapper.deleteById(id);

        // 删除子表
        deleteEncodingRuleDetailByEncodingRuleId(id);
    }

    private void validateEncodingRuleExists(String id) {
        if (encodingRuleMapper.selectById(id) == null) {
            throw exception(ENCODING_RULE_NOT_EXISTS);
        }
    }

    @Override
    public EncodingRuleDO getEncodingRule(String id) {
        return encodingRuleMapper.selectById(id);
    }

    @Override
    public PageResult<EncodingRuleDO> getEncodingRulePage(EncodingRulePageReqVO pageReqVO) {
        return encodingRuleMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（编码规则配置详情） ====================

    @Override
    public List<EncodingRuleDetailDO> getEncodingRuleDetailListByEncodingRuleId(String encodingRuleId) {
        return encodingRuleDetailMapper.selectListByEncodingRuleId(encodingRuleId);
    }

    @Override
    public String generatorCode(GeneratorCodeReqDTO dto,EncodingRuleDO ruleDO, List<EncodingRuleDetailDO> detailDOS, Map<String, String> attributes) throws InterruptedException {
        String key = "lockKey";
        String value = ruleDO.getId();

//        //分布式锁
//        boolean isLocked = redisLock.lock(key, value, 10000, TimeUnit.SECONDS);
//
//        String result = null;
//
//        if (isLocked) {
//            //加锁成功
//            result = getCode(dto,ruleDO,detailDOS,attributes);
//            //完成后解锁
//            redisLock.unlock(key, value);
//        } else {
//            log.error("获取分布式锁失败");
//            //没有获取到锁 进行自旋
//            Thread.sleep(100);
//            generatorCode(dto,ruleDO, detailDOS, attributes);
//        }

        return getCode(dto,ruleDO,detailDOS,attributes);

    }



    public String getCode(GeneratorCodeReqDTO dto,EncodingRuleDO ruleDO, List<EncodingRuleDetailDO> detailDOS, Map<String, String> attributes) throws InterruptedException {

        synchronized (this) {
            //业务逻辑代码
            StringBuffer stringBuffer = new StringBuffer();
            for (EncodingRuleDetailDO detailDO : detailDOS) {
                IEncodingRuleStrategy strategy = encodingRuleFactory.generatorStrategy(detailDO.getType());
                stringBuffer.append(strategy.getRuleValue(detailDO, attributes, detailDOS, ruleDO));
            }
            ruleDO.setGeneratorCode(stringBuffer.toString());
            this.saveCode(dto, ruleDO, null, 1);
            return stringBuffer.toString();
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized EncodingRuleDO generatorCode(GeneratorCodeReqDTO dto) throws InterruptedException {


        List<EncodingRuleDO> doList = encodingRuleMapper.selectListByType(dto.getClassificationCode(), dto.getEncodingRuleType(), dto.getMaterialMainTypeCode());
        if (CollectionUtils.isEmpty(doList)) {
            throw exception(ENCODING_RULE_NOT_EXISTS);
        }

        EncodingRuleDO encodingRuleDO = doList.get(0);

        List<EncodingRuleDetailDO> detailDOS = encodingRuleDetailMapper.selectListByEncodingRuleId(encodingRuleDO.getId());

        //判断  如果规则有自定义属性  但是没有传  则需要提示  如果自定义属性的值超过规则限制 则提示
        for (EncodingRuleDetailDO detailDO : detailDOS) {
            if (StringUtils.isNotBlank(detailDO.getEncodingAttribute())) {
                String value = dto.getAttributes().get(detailDO.getEncodingAttribute());
                if (StringUtils.isBlank(value)) {
                    throw exception(new ErrorCode(1 - 014 - 007 - 003, detailDO.getEncodingAttribute() + "自定义属性值不存在"));
                } else {
                    if (value.length() > detailDO.getBitNumber()) {
                        throw exception(new ErrorCode(1 - 014 - 007 - 004, detailDO.getEncodingAttribute() + "自定义属性长度不符合要求"));
                    }
                }

            }

        }

        String code = generatorCode(dto,encodingRuleDO, detailDOS, dto.getAttributes());
        encodingRuleDO.setGeneratorCode(code);

        return encodingRuleDO;
    }

    @Override
    public  EncodingRuleDO generatorCode1(GeneratorCodeReqDTO dto) throws InterruptedException {

        RLock lock = redissonClient.getLock("redissonClient"+dto.getClassificationCode());
        try {
            lock.lock(60, TimeUnit.SECONDS);
//                Thread.sleep(10000);
                return generatorCode(dto);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 判断当前线程是否持有锁
//            if (lock.isHeldByCurrentThread()) {
                //释放当前锁
                lock.unlock();
                log.info(Thread.currentThread().getName() + "释放锁" + LocalDateTime.now());
//            }
        }

    }

    @Override
    public void saveCode(GeneratorCodeReqDTO dto, EncodingRuleDO ruleDO, CodeRecordDO recordDO, Integer status) throws InterruptedException {

        CodeRecordSaveReqVO codeRecordDO = new CodeRecordSaveReqVO();
        codeRecordDO.setCode(ruleDO.getGeneratorCode());
        codeRecordDO.setEncodingRuleId(ruleDO.getId());
        codeRecordDO.setClassificationId(ruleDO.getClassificationId());
        codeRecordDO.setParentId("0");
        if (StringUtils.isNotBlank(dto.getOldBarCode())) {
            if (recordDO == null) {
                recordDO = codeRecordService.getCodeRecordByCode(dto.getOldBarCode());
            }
            codeRecordDO.setParentId(recordDO.getId());
        }
        codeRecordDO.setStatus(status);
        codeRecordDO.setParams(JSONObject.toJSONString(dto));
        codeRecordService.createCodeRecord(codeRecordDO);

    }

    @Override
    public List<EncodingRuleDO> getEncodingRuleList() {
        return encodingRuleMapper.selectList();
    }

    @Override
    public void updateEncodingStatus(CodeRecordStatusReqVO reqVO) {
        CodeRecordDO codeRecordDO = codeRecordService.getCodeRecordByCode(reqVO.getCode());
        if (codeRecordDO == null) {
            throw exception(CODE_RECORD_NOT_EXISTS);
        }
        codeRecordDO.setStatus(reqVO.getStatus());
        codeRecordService.updateCodeRecord(BeanUtils.toBean(codeRecordDO, CodeRecordSaveReqVO.class));
    }

    private void createEncodingRuleDetailList(String encodingRuleId, List<EncodingRuleDetailDO> list) {
        list.forEach(o -> o.setEncodingRuleId(encodingRuleId));
        encodingRuleDetailMapper.insertBatch(list);
    }

    private void updateEncodingRuleDetailList(String encodingRuleId, List<EncodingRuleDetailDO> list) {
        deleteEncodingRuleDetailByEncodingRuleId(encodingRuleId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createEncodingRuleDetailList(encodingRuleId, list);
    }

    private void deleteEncodingRuleDetailByEncodingRuleId(String encodingRuleId) {
        encodingRuleDetailMapper.deleteByEncodingRuleId(encodingRuleId);
    }

}
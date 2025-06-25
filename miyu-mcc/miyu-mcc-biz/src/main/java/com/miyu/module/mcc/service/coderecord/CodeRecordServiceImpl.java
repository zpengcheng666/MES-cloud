package com.miyu.module.mcc.service.coderecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.mcc.controller.admin.coderecord.vo.*;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.coderecord.CodeRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;

/**
 * 编码记录 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class CodeRecordServiceImpl implements CodeRecordService {

    @Resource
    private CodeRecordMapper codeRecordMapper;

    @Override
    public String createCodeRecord(CodeRecordSaveReqVO createReqVO) {
        // 插入
        CodeRecordDO codeRecord = BeanUtils.toBean(createReqVO, CodeRecordDO.class);
        codeRecordMapper.insert(codeRecord);
        // 返回
        return codeRecord.getId();
    }

    @Override
    public void updateCodeRecord(CodeRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateCodeRecordExists(updateReqVO.getId());
        // 更新
        CodeRecordDO updateObj = BeanUtils.toBean(updateReqVO, CodeRecordDO.class);
        codeRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteCodeRecord(String id) {
        // 校验存在
        validateCodeRecordExists(id);
        // 删除
        codeRecordMapper.deleteById(id);
    }

    private void validateCodeRecordExists(String id) {
        if (codeRecordMapper.selectById(id) == null) {
            throw exception(CODE_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public CodeRecordDO getCodeRecord(String id) {
        return codeRecordMapper.selectById(id);
    }

    @Override
    public PageResult<CodeRecordDO> getCodeRecordPage(CodeRecordPageReqVO pageReqVO) {
        return codeRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CodeRecordDO> getCodeRecordList(String encodingRuleId) {
        return codeRecordMapper.getCodeRecordList(encodingRuleId);
    }

    @Override
    public CodeRecordDO getCodeRecordByCode(String code) {
        return codeRecordMapper.selectOne(new QueryWrapper<CodeRecordDO>().eq("code",code).in("status", Lists.newArrayList(1,2)));
    }


}
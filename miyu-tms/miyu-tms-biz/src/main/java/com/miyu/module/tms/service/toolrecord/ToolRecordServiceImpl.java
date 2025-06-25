package com.miyu.module.tms.service.toolrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.tms.controller.admin.toolrecord.vo.*;
import com.miyu.module.tms.dal.dataobject.toolrecord.ToolRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolrecord.ToolRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具使用记录 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class ToolRecordServiceImpl implements ToolRecordService {

    @Resource
    private ToolRecordMapper toolRecordMapper;

    @Override
    public String createToolRecord(ToolRecordSaveReqVO createReqVO) {
        // 插入
        ToolRecordDO toolRecord = BeanUtils.toBean(createReqVO, ToolRecordDO.class);
        toolRecordMapper.insert(toolRecord);
        // 返回
        return toolRecord.getId();
    }

    @Override
    public void updateToolRecord(ToolRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateToolRecordExists(updateReqVO.getId());
        // 更新
        ToolRecordDO updateObj = BeanUtils.toBean(updateReqVO, ToolRecordDO.class);
        toolRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteToolRecord(String id) {
        // 校验存在
        validateToolRecordExists(id);
        // 删除
        toolRecordMapper.deleteById(id);
    }

    private void validateToolRecordExists(String id) {
        if (toolRecordMapper.selectById(id) == null) {
            throw exception(TOOL_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ToolRecordDO getToolRecord(String id) {
        return toolRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ToolRecordDO> getToolRecordPage(ToolRecordPageReqVO pageReqVO) {
        return toolRecordMapper.selectPage(pageReqVO);
    }

}
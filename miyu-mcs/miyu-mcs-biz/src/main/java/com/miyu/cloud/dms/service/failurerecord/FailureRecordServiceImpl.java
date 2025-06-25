package com.miyu.cloud.dms.service.failurerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.failurerecord.FailureRecordDO;
import com.miyu.cloud.dms.dal.mysql.failurerecord.FailureRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.FAILURE_RECORD_NOT_EXISTS;

/**
 * 异常记录 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class FailureRecordServiceImpl implements FailureRecordService {

    @Resource
    private FailureRecordMapper failureRecordMapper;

    @Override
    public String createFailureRecord(FailureRecordSaveReqVO createReqVO) {
        // 插入
        FailureRecordDO failureRecord = BeanUtils.toBean(createReqVO, FailureRecordDO.class);
        failureRecordMapper.insert(failureRecord);
        // 返回
        return failureRecord.getId();
    }

    @Override
    public void updateFailureRecord(FailureRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateFailureRecordExists(updateReqVO.getId());
        // 更新
        FailureRecordDO updateObj = BeanUtils.toBean(updateReqVO, FailureRecordDO.class);
        failureRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteFailureRecord(String id) {
        // 校验存在
        validateFailureRecordExists(id);
        // 删除
        failureRecordMapper.deleteById(id);
    }

    private void validateFailureRecordExists(String id) {
        if (failureRecordMapper.selectById(id) == null) {
            throw exception(FAILURE_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public FailureRecordDO getFailureRecord(String id) {
        return failureRecordMapper.selectById(id);
    }

    @Override
    public PageResult<FailureRecordDO> getFailureRecordPage(FailureRecordPageReqVO pageReqVO) {
        return failureRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FailureRecordDO> getFailureRecordList(String id) {
        return failureRecordMapper.selectList(new LambdaQueryWrapperX<FailureRecordDO>().eq(FailureRecordDO::getDevice, id));
    }

    @Override
    public List<FailureRecordDO> list(Wrapper<FailureRecordDO> queryWrapper) {
        return failureRecordMapper.selectList(queryWrapper);
    }
}

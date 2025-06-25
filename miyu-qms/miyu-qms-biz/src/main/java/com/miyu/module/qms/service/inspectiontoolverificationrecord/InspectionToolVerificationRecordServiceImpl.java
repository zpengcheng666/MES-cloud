package com.miyu.module.qms.service.inspectiontoolverificationrecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectiontoolverificationrecord.InspectionToolVerificationRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectiontoolverificationrecord.InspectionToolVerificationRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验工具校准记录 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionToolVerificationRecordServiceImpl implements InspectionToolVerificationRecordService {

    @Resource
    private InspectionToolVerificationRecordMapper inspectionToolVerificationRecordMapper;

    @Override
    public String createInspectionToolVerificationRecord(InspectionToolVerificationRecordSaveReqVO createReqVO) {
        // 插入
        InspectionToolVerificationRecordDO inspectionToolVerificationRecord = BeanUtils.toBean(createReqVO, InspectionToolVerificationRecordDO.class);
        inspectionToolVerificationRecordMapper.insert(inspectionToolVerificationRecord);
        // 返回
        return inspectionToolVerificationRecord.getId();
    }

    @Override
    public void updateInspectionToolVerificationRecord(InspectionToolVerificationRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionToolVerificationRecordExists(updateReqVO.getId());
        // 更新
        InspectionToolVerificationRecordDO updateObj = BeanUtils.toBean(updateReqVO, InspectionToolVerificationRecordDO.class);
        inspectionToolVerificationRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionToolVerificationRecord(String id) {
        // 校验存在
        validateInspectionToolVerificationRecordExists(id);
        // 删除
        inspectionToolVerificationRecordMapper.deleteById(id);
    }

    private void validateInspectionToolVerificationRecordExists(String id) {
        if (inspectionToolVerificationRecordMapper.selectById(id) == null) {
            throw exception(INSPECTION_TOOL_VERIFICATION_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public InspectionToolVerificationRecordDO getInspectionToolVerificationRecord(String id) {
        return inspectionToolVerificationRecordMapper.selectToolVerificationRecordById(id);
    }

    @Override
    public PageResult<InspectionToolVerificationRecordDO> getInspectionToolVerificationRecordPage(InspectionToolVerificationRecordPageReqVO pageReqVO) {
        return inspectionToolVerificationRecordMapper.selectPage(pageReqVO);
    }

    /**
     * 首页获取待送检集合
     * @return
     */
    @Override
    public PageResult<InspectionToolVerificationRecordDO> getToolVerificationTaskPage(InspectionToolVerificationRecordPageReqVO pageReqVO) {
        // 待送检 待完成
        return inspectionToolVerificationRecordMapper.selectVerificationTaskPage(pageReqVO);
    }
}

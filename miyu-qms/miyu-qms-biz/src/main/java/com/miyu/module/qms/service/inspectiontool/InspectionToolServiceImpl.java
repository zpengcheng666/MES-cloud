package com.miyu.module.qms.service.inspectiontool;

import com.miyu.module.qms.dal.dataobject.inspectiontoolverificationrecord.InspectionToolVerificationRecordDO;
import com.miyu.module.qms.dal.mysql.inspectiontoolverificationrecord.InspectionToolVerificationRecordMapper;
import com.miyu.module.qms.enums.InspectionToolStatusEnum;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import com.miyu.module.qms.controller.admin.inspectiontool.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.dal.mysql.inspectiontool.InspectionToolMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 检测工具 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionToolServiceImpl implements InspectionToolService {

    @Resource
    private InspectionToolMapper inspectionToolMapper;

    @Resource
    private InspectionToolVerificationRecordMapper inspectionToolVerificationRecordMapper;

    @Override
    public String createInspectionTool(InspectionToolSaveReqVO createReqVO) {
        // 插入
        InspectionToolDO inspectionTool = BeanUtils.toBean(createReqVO, InspectionToolDO.class).setVerificationDate(LocalDateTime.now());
        inspectionToolMapper.insert(inspectionTool);
        // 返回
        return inspectionTool.getId();
    }

    @Override
    @LogRecord(type = QMS_TOOL_TYPE, subType = QMS_UPDATE_TOOL_SUB_TYPE, bizNo = "{{#tool.id}}",
            success = QMS_UPDATE_TOOL_SUCCESS)
    public void updateInspectionTool(InspectionToolSaveReqVO updateReqVO) {
        // 校验存在
        InspectionToolDO tool = validateInspectionToolExists(updateReqVO.getId());
        // 更新
        InspectionToolDO updateObj = BeanUtils.toBean(updateReqVO, InspectionToolDO.class);
        inspectionToolMapper.updateById(updateObj);

        // 记录操作日志上下文
        LogRecordContext.putVariable("tool", tool);
    }

    @Override
    public void deleteInspectionTool(String id) {
        // 校验存在
        validateInspectionToolExists(id);
        // 删除
        inspectionToolMapper.deleteById(id);
    }

    private InspectionToolDO validateInspectionToolExists(String id) {

        InspectionToolDO toolDO = inspectionToolMapper.selectById(id);

        if (toolDO == null) {
            throw exception(INSPECTION_TOOL_NOT_EXISTS);
        }
        return toolDO;
    }

    @Override
    public InspectionToolDO getInspectionTool(String id) {
        return inspectionToolMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionToolDO> getInspectionToolPage(InspectionToolPageReqVO pageReqVO) {
        return inspectionToolMapper.selectPage(pageReqVO);
    }

    @Override
    public List<InspectionToolDO> getInspectionToolList() {
        return inspectionToolMapper.selectList();
    }

    /**
     * 定时任务保存检验工具校准记录
     */
    @Override
    public void createInspectionToolVerificationRecord() {
        Collection<Integer> status = new HashSet<>();
        status.add(InspectionToolStatusEnum.ENABLE.getStatus());
        status.add(InspectionToolStatusEnum.NORMAL.getStatus());
        List<InspectionToolDO> list = inspectionToolMapper.selectList(InspectionToolDO::getStatus, status);
        // 校准记录集合
        List<InspectionToolVerificationRecordDO> recordList = new ArrayList<>();
        // 当前日期
        LocalDate today = LocalDate.now(); // 获取当前日期
        // 将当天需要送检的工具生成记录
        for(InspectionToolDO t : list){
            t.getVerificationCycle();
            t.getVerificationDate();
            LocalDate verifyDatePre = t.getVerificationDate().toLocalDate();
            LocalDate verifyDate = verifyDatePre.plusDays(t.getVerificationCycle());
            if(today.isEqual(verifyDate)){
                InspectionToolVerificationRecordDO record = new InspectionToolVerificationRecordDO();
                record.setToolId(t.getId());
                record.setStockId(t.getStockId());
                record.setVerificationDateBegin(today.atStartOfDay());
                record.setStatus(1);
                recordList.add(record);
            }
        }
        if(recordList.size() > 0){
            inspectionToolVerificationRecordMapper.insertBatch(recordList);
        }
    }
}

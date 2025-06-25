package com.miyu.cloud.dms.service.inspectionrecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordAddReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionrecord.InspectionRecordDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.mysql.inspectionrecord.InspectionRecordMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.*;

/**
 * 设备检查记录 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class InspectionRecordServiceImpl implements InspectionRecordService {

    private void check(InspectionRecordSaveReqVO data) {
        if (data.getStartTime() == null) {
            throw exception(INSPECTION_RECORD_START_EMPTY);
        }
        if (data.getEndTime() == null) {
            throw exception(INSPECTION_RECORD_END_EMPTY);
        }
    }

    @Resource
    private InspectionRecordMapper inspectionRecordMapper;

    @Override
    public String createInspectionRecord(InspectionRecordSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        InspectionRecordDO inspectionRecord = BeanUtils.toBean(createReqVO, InspectionRecordDO.class);
        inspectionRecordMapper.insert(inspectionRecord);
        // 返回
        return inspectionRecord.getId();
    }

    @Override
    public void updateInspectionRecord(InspectionRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionRecordExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        InspectionRecordDO updateObj = BeanUtils.toBean(updateReqVO, InspectionRecordDO.class);
        inspectionRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionRecord(String id) {
        // 校验存在
        validateInspectionRecordExists(id);
        // 删除
        inspectionRecordMapper.deleteById(id);
    }

    private void validateInspectionRecordExists(String id) {
        if (inspectionRecordMapper.selectById(id) == null) {
            throw exception(INSPECTION_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public InspectionRecordDO getInspectionRecord(String id) {
        expirationShutdownService();
        return inspectionRecordMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionRecordDO> getInspectionRecordPage(InspectionRecordPageReqVO pageReqVO) {
        return inspectionRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public void addInspectionRecord(InspectionRecordAddReqVO addReqVO) {

        InspectionRecordDO inspectionRecord = new InspectionRecordDO();

        inspectionRecord.setId(addReqVO.getId());
        inspectionRecord.setStatus(1);

        Long userId = WebFrameworkUtils.getLoginUserId();
        inspectionRecord.setCreateBy(userId.toString());

        inspectionRecord.setRemark(addReqVO.getRemark());
        inspectionRecord.setContent(addReqVO.getContent());
        inspectionRecord.setStartTime(addReqVO.getStartTime());
        inspectionRecord.setEndTime(addReqVO.getEndTime());
        //插入新增的记录数据

        inspectionRecordMapper.updateById(inspectionRecord);
    }

    @Resource
    private LedgerMapper ledgerMapper;

    @Override
    public void expirationShutdownService() {
        List<InspectionRecordDO> list = inspectionRecordMapper.selectPreExpirationShutdown();
        if (list == null || list.isEmpty()) {
            return;
        }
        LedgerDO ledger = null;
        LocalDateTime now = LocalDateTime.now();

        for (InspectionRecordDO inspectionRecord : list) {
            if (inspectionRecord.getCreateTime().plusDays(inspectionRecord.getExpirationTime().longValue()).isAfter(now)) {
                continue;
            }
            ledger = ledgerMapper.selectById(inspectionRecord.getDevice());
            if (ledger == null) {
                continue;
            }
            ledger.setStatus(1); //设备状态改为关闭
            ledgerMapper.updateById(ledger);

        }
    }

}

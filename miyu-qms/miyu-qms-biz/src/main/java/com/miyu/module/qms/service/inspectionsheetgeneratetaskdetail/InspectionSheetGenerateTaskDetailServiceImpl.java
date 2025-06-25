package com.miyu.module.qms.service.inspectionsheetgeneratetaskdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验单生成任务明细 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetGenerateTaskDetailServiceImpl implements InspectionSheetGenerateTaskDetailService {

    @Resource
    private InspectionSheetGenerateTaskDetailMapper inspectionSheetGenerateTaskDetailMapper;

    @Override
    public String createInspectionSheetGenerateTaskDetail(InspectionSheetGenerateTaskDetailSaveReqVO createReqVO) {
        // 插入
        InspectionSheetGenerateTaskDetailDO inspectionSheetGenerateTaskDetail = BeanUtils.toBean(createReqVO, InspectionSheetGenerateTaskDetailDO.class);
        inspectionSheetGenerateTaskDetailMapper.insert(inspectionSheetGenerateTaskDetail);
        // 返回
        return inspectionSheetGenerateTaskDetail.getId();
    }

    @Override
    public void updateInspectionSheetGenerateTaskDetail(InspectionSheetGenerateTaskDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetGenerateTaskDetailExists(updateReqVO.getId());
        // 更新
        InspectionSheetGenerateTaskDetailDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetGenerateTaskDetailDO.class);
        inspectionSheetGenerateTaskDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionSheetGenerateTaskDetail(String id) {
        // 校验存在
        validateInspectionSheetGenerateTaskDetailExists(id);
        // 删除
        inspectionSheetGenerateTaskDetailMapper.deleteById(id);
    }

    private void validateInspectionSheetGenerateTaskDetailExists(String id) {
        if (inspectionSheetGenerateTaskDetailMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_GENERATE_TASK_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSheetGenerateTaskDetailDO getInspectionSheetGenerateTaskDetail(String id) {
        return inspectionSheetGenerateTaskDetailMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionSheetGenerateTaskDetailDO> getInspectionSheetGenerateTaskDetailPage(InspectionSheetGenerateTaskDetailPageReqVO pageReqVO) {
        return inspectionSheetGenerateTaskDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<InspectionSheetGenerateTaskDetailDO> getInspectionSheetGenerateTaskDetailListByTaskId(String id) {
        return inspectionSheetGenerateTaskDetailMapper.selectList(InspectionSheetGenerateTaskDetailDO::getTaskId, id);
    }

}

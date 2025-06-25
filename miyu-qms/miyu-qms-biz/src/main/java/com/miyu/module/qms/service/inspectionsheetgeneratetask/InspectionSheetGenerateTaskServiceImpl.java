package com.miyu.module.qms.service.inspectionsheetgeneratetask;

import com.miyu.module.qms.controller.admin.inspectionscheme.vo.InspectionSchemeReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetSaveReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailDO;
import com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailMapper;
import com.miyu.module.qms.service.inspectionscheme.InspectionSchemeService;
import com.miyu.module.qms.service.inspectionsheet.InspectionSheetService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetask.InspectionSheetGenerateTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheetgeneratetask.InspectionSheetGenerateTaskMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验单 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetGenerateTaskServiceImpl implements InspectionSheetGenerateTaskService {

    @Resource
    private InspectionSheetGenerateTaskMapper inspectionSheetGenerateTaskMapper;

    @Resource
    private InspectionSheetGenerateTaskDetailMapper inspectionSheetGenerateTaskDetailMapper;

    @Resource
    private InspectionSheetService inspectionSheetService;

    @Resource
    private InspectionSchemeService inspectionSchemeService;

    @Override
    @Transactional
    public String createInspectionSheetGenerateTask(InspectionSheetGenerateTaskSaveReqVO createReqVO) {

        // 通过物料类型和方案类型查询检验方案
        InspectionSchemeReqVO reqVO = new InspectionSchemeReqVO();
        reqVO.setMaterialConfigId(createReqVO.getMaterialConfigId());
        reqVO.setTechnologyId(createReqVO.getTechnologyId());
        reqVO.setProcessId(createReqVO.getProcessId());
        reqVO.setSchemeType(createReqVO.getSchemeType());
        List<InspectionSchemeDO> list = inspectionSchemeService.getInspectionSchemeList4InspectionSheet(reqVO);
        // 插入
        InspectionSheetGenerateTaskDO inspectionSheetGenerateTask = BeanUtils.toBean(createReqVO, InspectionSheetGenerateTaskDO.class).setStatus(0);
        inspectionSheetGenerateTaskMapper.insert(inspectionSheetGenerateTask);

        List<InspectionSheetGenerateTaskDetailDO> inspectionSheetGenerateTaskDetailList = BeanUtils.toBean(createReqVO.getDetails(), InspectionSheetGenerateTaskDetailDO.class, vo -> {
            vo.setTaskId(inspectionSheetGenerateTask.getId());
        });
        inspectionSheetGenerateTaskDetailMapper.insertBatch(inspectionSheetGenerateTaskDetailList);

        // 获取到一个检验方案
        if (list.size() == 1) {
            InspectionSheetSaveReqVO sheetSaveReqVO = new InspectionSheetSaveReqVO();
            sheetSaveReqVO.setRecordNumber(createReqVO.getRecordNumber());
            sheetSaveReqVO.setQuantity(createReqVO.getDetails().size());
            sheetSaveReqVO.setMaterialConfigId(createReqVO.getMaterialConfigId());
            sheetSaveReqVO.setSchemeType(createReqVO.getSchemeType());
            sheetSaveReqVO.setBatchNumber(createReqVO.getBatchNumber());
            sheetSaveReqVO.setSourceType(createReqVO.getSourceType());
            sheetSaveReqVO.setTechnologyId(createReqVO.getTechnologyId());
            sheetSaveReqVO.setProcessId(createReqVO.getProcessId());
            sheetSaveReqVO.setRecordId(createReqVO.getRecordId());
            // 检验方案Id
            sheetSaveReqVO.setSchemeId(list.get(0).getId());
            sheetSaveReqVO.setTaskId(inspectionSheetGenerateTask.getId());
            return inspectionSheetService.createInspectionSheetTask(sheetSaveReqVO);
        }
        // 返回
        return inspectionSheetGenerateTask.getId();
    }

    @Override
    public void updateInspectionSheetGenerateTask(InspectionSheetGenerateTaskSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetGenerateTaskExists(updateReqVO.getId());
        // 更新
        InspectionSheetGenerateTaskDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetGenerateTaskDO.class);
        inspectionSheetGenerateTaskMapper.updateById(updateObj);
    }

    /**
     * 删除待生成检验单任务
     * @param id 编号
     */
    @Override
    @Transactional
    public void deleteInspectionSheetGenerateTask(String id) {
        // 校验存在
        validateInspectionSheetGenerateTaskExists(id);
        // 删除检验单任务
        inspectionSheetGenerateTaskMapper.deleteById(id);
        // 删除检验单任务详细
        inspectionSheetGenerateTaskDetailMapper.delete(InspectionSheetGenerateTaskDetailDO::getTaskId, id);
    }

    private void validateInspectionSheetGenerateTaskExists(String id) {
        if (inspectionSheetGenerateTaskMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_GENERATE_TASK_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSheetGenerateTaskDO getInspectionSheetGenerateTask(String id) {
        return inspectionSheetGenerateTaskMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionSheetGenerateTaskDO> getInspectionSheetGenerateTaskPage(InspectionSheetGenerateTaskPageReqVO pageReqVO) {
        return inspectionSheetGenerateTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<InspectionSheetGenerateTaskDO> getInspectionSheetGenerateTaskListPage(InspectionSheetGenerateTaskPageReqVO pageReqVO) {
        return inspectionSheetGenerateTaskMapper.selectInspectionSheetGenerateTaskPage(pageReqVO);
    }

}

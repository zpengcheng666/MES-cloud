package com.miyu.module.pdm.api.processPlan;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.api.processPlan.dto.PartRespDTO;
import com.miyu.module.pdm.api.processPlan.dto.ProcessRespDTO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessRespVO;
import com.miyu.module.pdm.dal.dataobject.master.PartMasterDO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.dal.mysql.process.ProcessMapper;
import com.miyu.module.pdm.dal.mysql.processTask.ProcessTaskMapper;
import com.miyu.module.pdm.service.part.PartService;
import com.miyu.module.pdm.service.process.processService;
import jodd.util.StringUtil;
import org.apache.tomcat.jni.Proc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Validated
public class ProcessPlanApiImpl implements ProcessPlanApi {

    @Resource
    private processService processService;

    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Resource
    private PartService partService;

    @Override
    public CommonResult<String> updateProcessTaskStatus(String businessKey, Integer status) {
        processService.updateProcessInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<String> pushProcess(String projectCode,String partNumber, String partName,String processCondition, LocalDateTime processPreparationTime) {
        if(StringUtil.isEmpty(projectCode) || StringUtil.isEmpty(partNumber) || StringUtil.isEmpty(partName) || StringUtil.isEmpty(processCondition)) {
            return CommonResult.error(500, "参数不能为空");
        }
        processTaskMapper.deleteProcessMessage(projectCode,partNumber,partName,processCondition);
        //加工状态：①A状态-粗加工 ②A状态 ③锻件粗加工
        String partProcessCondition = processCondition;
        if(processCondition.indexOf("-")>=0) {
            partProcessCondition = processCondition.substring(0,processCondition.indexOf("-"));
        } else {
            if(processCondition.indexOf("状态")>=0) {
                partProcessCondition = processCondition.substring(0,processCondition.indexOf("状态")+2);
            } else if(processCondition.indexOf("件")>=0) {
                partProcessCondition = processCondition.substring(0,processCondition.indexOf("件")+1);
            }
        }
        List<ProcessTaskDO> list = processTaskMapper.selectPTaskList(partNumber, partProcessCondition);
        if (list.size() > 0) {
            ProcessTaskDO firstProcessTask = list.get(0);
            String partVersionId = firstProcessTask.getPartVersionId();
            String id = IdUtil.fastSimpleUUID();
            processTaskMapper.insertProcessTask(id, projectCode, partNumber, partName, processCondition,partVersionId,processPreparationTime);
        }
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<List<ProcessRespDTO>> getInformationList(List<String> processSchemes) {
        List<ProcessRespVO> processRespVOList = new ArrayList<>();
        for (String processScheme : processSchemes) {
            ProcessRespVO processRespVO = processService.selectProcessById(processScheme);
            processRespVOList.add(processRespVO);
        }
        List<ProcessRespDTO> processRespDTOList = BeanUtils.toBean(processRespVOList, ProcessRespDTO.class);
        return CommonResult.success(processRespDTOList);
    }

    @Override
    public CommonResult<List<PartRespDTO>> getProcessConditionList(String partNumber) {
        List<PartMasterDO> partMasterDOList = partService.getPartInfoList(partNumber);
        List<PartRespDTO> partRespDTOList = BeanUtils.toBean(partMasterDOList, PartRespDTO.class);
        return CommonResult.success(partRespDTOList);
    }

}

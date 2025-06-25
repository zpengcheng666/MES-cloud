package com.miyu.module.pdm.service.processPlanDetail;

import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;

import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.*;

import com.miyu.module.pdm.controller.admin.processTask.vo.ProcedureRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcedureSchemaItemDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessChangeDO;
import org.apache.ibatis.annotations.Param;

import javax.validation.Valid;
import java.util.List;

public interface ProcessPlanDetailService {
     // 根据工艺规程版本id获取工序列表(接口用)
     List<ProcessPlanDetailRespVO> getProcedureListByProcessVersionId(ProcessPlanDetailReqVO reqVo);

     //工艺更改单
     List<ProcessChangeRespVO> getChangeOrderList(ProcessChangeReqVO reqVO);
     List<ProcessChangeRespVO> getChangeDetailList(String processChangeId);

     //获取更改单信息
     ProcessChangeRespVO getProcessChangeById(String id);

     //工艺更改单动态详细项
     List<ProcessChangeRespVO> getChangeDetailItem(ProcessChangeReqVO reqVO);

     //获取工序更改单详细detail
     List<ProcessChangeRespVO> getProcessChangeDetailById(ProcessChangeReqVO reqVO);
     String saveOrderItem(ProcessChangeReqVO createReqVO);

     void updateOrderItem(ProcessChangeReqVO updateReqVO);

     void deleteOrderItem(String id);

     //获得工艺修改单
     ProcessChangeDO getChangeOrderById(String id);
     //删除工艺修改单
     void deleteChangeOrderById(String id);
     //删除工艺修改单详细
     void deleteOrderDetailById(String id);

     //工艺更改单流程发起
     void startProcessChangeInstance(@Valid ProcessChangeReqVO updateReqVO);

     void updateProcessChangeInstanceStatus(String id,Integer status);

     //数控程序
     List<NcRespVO> getNc(ProcessVersionNcReqVO req1VO);
     void saveNc(NcReqVO reqVO,ProcessVersionNcReqVO req1VO);
     void deleteNc(NcReqVO reqVO, ProcessVersionNcReqVO req1VO);

     //更新工步
     void updateStep(@Valid ProcessPlanDetailReqVO updateReqVO);

     //删除工步
     void deleteStep(String id);

     //更新工序
     void updateProcedure(@Valid ProcessPlanDetailReqVO updateReqVO);

     //删除工序
     void deleteProcedure(String id);
     //添加工序
     String addProcedure(ProcessPlanDetailReqVO addReqVO);

     //添加工步
     String addStep(ProcessPlanDetailReqVO addReqVO);
     List<ProcessPlanDetailRespVO> getPartListByProcessPlanDetailId(String projectCode);

     List<ProjPartBomTreeRespVO> getProjPartBomTreeList(ProjPartBomReqVO reqVO);
     List<ProjPartBomTreeRespVO> getProjPartBomTreeByTaskId(ProjPartBomReqVO reqVO);

     List<ProcessPlanDetailRespVO> selectProcessPlanDetailList(ProcessPlanDetailReqVO reqVO);

     ProcessPlanDetailRespVO getProcessPlanDetail(String id);

     //获取树列表方法
     List<ProcessPlanDetailRespVO> getProcessPlanDetailTreeList(ProcessPlanDetailReqVO reqVO);

     ProcessPlanDetailRespVO getProcess(@Param("id") String id);

//     ProcessPlanDetailRespVO getProcedure(@Param("id") String id);
     ProcedureRespVO getProcedure(@Param("id") String id);
//     ProcessPlanDetailRespVO getStep(@Param("id") String id);
     StepRespVO getStep(@Param("id") String id);

     List<ProcedureDetailRespVO> getResourceListByProcedure(ResourceSelectedReqVO reqVO);
     void deleteSelectedDeviceProcedure(ProcedureDetailReqVO reqVO);
     void saveSelectedResourceProcedure(ResourceSelectedReqVO reqVO);
     List<StepDetailRespVO> getResourceListByStep(ResourceSelectedReqVO reqVO);
     void deleteSelectedDeviceStep(StepDetailReqVO reqVO);
     void saveSelectedResourceStep(ResourceSelectedReqVO reqVO);

     List<ProcedureFileRespVO> getProcedureFiles(ProcedureFileSaveReqVO reqVO);

     ProcedureFileDO getProcedureFileById(String id);

     void saveSelectedResource(ProcedureFileSaveReqVO reqVO);

     void deleteSelectedResource(String id);

     void startProcessInstance(@Valid ProcessDetailTaskReqVO updateReqVO);
     void updateProcessInstanceStatus(String id,Integer status);

     void saveSelectedStepFile(StepFileSaveReqVO reqVO);

     List<StepFileRespVO> getStepFiles(StepFileSaveReqVO reqVO);

     void deleteSelectedStepFile(String id);

//     Service
     List<ProcessPlanDetailRespVO> getProcessPlanList(ProcessPlanDetailRespVO reqVO);

     void updateProcessDetailTaskStatus(@Valid ProcessDetailTaskReqVO updateReqVO);

     /**修改刀简号*/
     void updateCutternum(StepDetailReqVO reqVO);
//     default Map<String, StepDO> getStepMap(Collection<Long> slotNumber) {
//          List<StepDO> list = getDeptList(slotNumber);
//          return CollectionUtils.convertMap(list, StepDO::getId);
//     }
//
//     List<StepDO> getDeptList(Collection<Long> slotNumber);

     List<ProcedureSchemaItemRespVO> getProcedureSchemaItemList(ProcedureSchemaItemReqVO reqVO);

     String saveProcedureSchemaItem(List<ProcedureSchemaItemReqVO> createReqVO);

     void updateProcedureSchemaItem(ProcedureSchemaItemReqVO updateReqVO);

     void deleteProcedureSchemaItem(String id);

     ProcedureSchemaItemDO getProcedureSchemaItem(String id);

     Long getStepCountByCategoryId(Long categoryId);

     List<CustomizedAttributeValRespVO> getStepAttributeValList(String objectId);
}


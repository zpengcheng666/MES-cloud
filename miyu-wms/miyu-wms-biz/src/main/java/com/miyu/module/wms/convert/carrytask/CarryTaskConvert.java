package com.miyu.module.wms.convert.carrytask;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarrySubTaskIssueVO;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarryTaskIssueVO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.enums.DictConstants;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static com.miyu.module.wms.enums.DictConstants.*;

@Mapper
public interface CarryTaskConvert {

    CarryTaskConvert INSTANCE = Mappers.getMapper(CarryTaskConvert.class);

    default CarryTaskIssueVO convertCarryTaskDOToIssueVO(CarryTaskDO carryTask,Integer containerType){
        List<CarrySubTaskDO> carrySubTaskList = carryTask.getCarrySubTask();
        CarryTaskIssueVO carryTaskIssue = new CarryTaskIssueVO();
        carryTaskIssue.setTaskNo(carryTask.getTaskCode());
        carryTaskIssue.setPriority(carryTask.getPriority());
        carryTaskIssue.setContainerType(String.valueOf(containerType));
        List<CarrySubTaskIssueVO> subTaskIssueList = new ArrayList<>();
        for (CarrySubTaskDO carrySubTaskDO : carrySubTaskList) {
            Integer insType = carrySubTaskDO.getInsType();
            String operation = convertOperation(insType);
            // 如果为 null 说明是上下架操作，跳过
            if (operation == null) {
                continue;
            }

            CarrySubTaskIssueVO subTaskIssueVO = new CarrySubTaskIssueVO();
            subTaskIssueVO.setSite(carrySubTaskDO.getLocationId());
            subTaskIssueVO.setSort(carrySubTaskDO.getExecuteOrder());
            subTaskIssueVO.setTrigEvent(carrySubTaskDO.getTrigEvent());
            subTaskIssueVO.setOperation(operation);
            subTaskIssueList.add(subTaskIssueVO);
        }
        carryTaskIssue.setSiteList(subTaskIssueList);
        return carryTaskIssue;
    }


    default String convertOperation(Integer insType) {
        if(insType == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_MOVE){
            return WMS_CARRY_TASK_OPERATION_MOVE;
        }
        if(insType == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_PUT){
            return WMS_CARRY_TASK_OPERATION_PUT;
        }
        if(insType == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE){
            return WMS_CARRY_TASK_OPERATION_TAKE;
        }
        return null;
    }

    /**
     * 将 CarryTaskIssueVO 转换为 JSONArray
     * @param carryTaskIssue
     * @return
     */
    default JSONObject convertIssueVOToJson(CarryTaskIssueVO carryTaskIssue){
        JSONObject carryTaskJson = new JSONObject();
        carryTaskJson.put("TaskNo", carryTaskIssue.getTaskNo());
        carryTaskJson.put("Priority", carryTaskIssue.getPriority());
        carryTaskJson.put("ContainerType", carryTaskIssue.getContainerType());
        JSONArray jsonArray = new JSONArray();
        List<CarrySubTaskIssueVO> siteList = carryTaskIssue.getSiteList();
        for (CarrySubTaskIssueVO subTaskIssueVO : siteList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Site", subTaskIssueVO.getSite());
            jsonObject.put("Sort", subTaskIssueVO.getSort());
            jsonObject.put("TrigEvent", subTaskIssueVO.getTrigEvent());
            jsonObject.put("Operation", subTaskIssueVO.getOperation());
            jsonArray.add(jsonObject);
        }
        carryTaskJson.put("SiteList", jsonArray);
        return carryTaskJson;
    }

    default JSONObject convertIssueVOToJsonContinueTask(CarryTaskIssueVO carryTaskIssue){
        JSONObject carryTaskJson = new JSONObject();
        carryTaskJson.put("TaskNo", carryTaskIssue.getTaskNo());
        carryTaskJson.put("Priority", carryTaskIssue.getPriority());
        carryTaskJson.put("ContainerType", carryTaskIssue.getContainerType());
        JSONArray jsonArray = new JSONArray();
        List<CarrySubTaskIssueVO> siteList = carryTaskIssue.getSiteList();
        for (CarrySubTaskIssueVO subTaskIssueVO : siteList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Site", subTaskIssueVO.getSite());
            jsonObject.put("Sort", subTaskIssueVO.getSort());
            jsonObject.put("TrigEvent", subTaskIssueVO.getTrigEvent());
            jsonObject.put("Operation", subTaskIssueVO.getOperation());
            jsonArray.add(jsonObject);
        }
        carryTaskJson.put("SiteList", jsonArray);
        return carryTaskJson;
    }
}

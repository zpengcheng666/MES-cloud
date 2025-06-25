package com.miyu.module.ppm.api.contract;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.contract.dto.ContractOrderDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 合同信息")
public interface ContractApi {

    String PREFIX = ApiConstants.PREFIX + "/contract";

    /***
     * 根据合同ID查询合同信息
     * @param ids
     * @return
     */
    @GetMapping(PREFIX + "/list")
    @Operation(summary = "通过合同 ID 查询合同信息")
    @Parameter(name = "ids", description = "合同ID数组", example = "1,2", required = true)
    CommonResult<List<ContractRespDTO>> getContractList(@RequestParam("ids") Collection<String> ids);


    /**
     * 获得合同 Map
     *
     * @param ids 合同ID数组
     * @return 合同 Map
     */
    default Map<String, ContractRespDTO> getContractMap(Collection<String> ids) {
        List<ContractRespDTO> contractRespDTOS = getContractList(ids).getCheckedData();
        return CollectionUtils.convertMap(contractRespDTOS, ContractRespDTO::getId);
    }


    /***
     * 根据合同查询合同订单信息
     * @param id
     * @return
     */
    @GetMapping(PREFIX + "/getOrder")
    @Operation(summary = "通过合同 ID 查询订单信息")
    @Parameter(name = "id", description = "合同ID", example = "1", required = true)
    CommonResult<List<ContractOrderRespDTO>> getOrderList(@RequestParam("id") String id);


    @PostMapping(PREFIX + "/update")
    @Operation(summary = "更新合同状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updateContractAuditStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);

    /**
     * 合同id获取合同信息
     * @param id
     * @return
     */

    @PostMapping(PREFIX + "/getContractInfoById")
    @Operation(summary = "合同id查询合同信息")
    @Parameter(name = "ids", description = "合同ID数组", example = "1", required = true)
    CommonResult<ContractRespDTO> getContractDetailInfoById(@RequestParam("id") String id);

    /***
     * 查询所有合同
     * @return
     */
    @GetMapping(PREFIX + "/getContractListAll")
    @Operation(summary = "通过合同 ID 查询订单信息")
    CommonResult<List<ContractRespDTO>> getContractListAll();

    /**
     * 项目ID集合查询合同集合
     * @param ids
     * @return
     */
    @GetMapping(PREFIX + "/getContractListByProjectIds")
    @Operation(summary = "项目ID集合查询合同集合")
    @Parameter(name = "ids", description = "项目id集合", required = true, example = "1")
    CommonResult<List<ContractRespDTO>> getContractListByProjectIds(@RequestParam("ids") Collection<String> ids,@RequestParam("type")Integer type);


    default Map<String, List<ContractRespDTO>> getContractMapByProjectIds(Collection<String> ids,Integer type) {
        List<ContractRespDTO> contractRespDTOS = getContractListByProjectIds(ids,type).getCheckedData();
        Map<String, List<ContractRespDTO>> map = new HashMap<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(contractRespDTOS)){
            for (ContractRespDTO respDTO :contractRespDTOS){

                if (!org.springframework.util.CollectionUtils.isEmpty(map.get(respDTO.getProjectId()))){
                    map.get(respDTO.getProjectId()).add(respDTO);
                }else {
                    List<ContractRespDTO> list = new ArrayList<>();
                    list.add(respDTO);
                    map.put(respDTO.getProjectId(), list);
                }
            }

        }
        return map;
    }

    /**8
     *
     * @param ids   项目id集合
     * @param type  合同类型
     * @return
     */
    @GetMapping(PREFIX + "/getContractOrderListByProjectIds")
    @Operation(summary = "项目ID集合查询合同订单集合")
    @Parameter(name = "ids", description = "项目id集合", required = true, example = "1")
    CommonResult<List<ContractOrderDTO>> getContractOrderListByProjectIds(@RequestParam("ids") Collection<String> ids,@RequestParam("type") Integer type);

    default Map<String, List<ContractOrderDTO>> getContractOrderMapByProjectIds(Collection<String> ids,Integer type) {
        List<ContractOrderDTO> contractRespDTOS = getContractOrderListByProjectIds(ids,type).getCheckedData();
        Map<String, List<ContractOrderDTO>> map = new HashMap<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(contractRespDTOS)){
            for (ContractOrderDTO respDTO :contractRespDTOS){

                if (!org.springframework.util.CollectionUtils.isEmpty(map.get(respDTO.getProjectId()))){
                    map.get(respDTO.getProjectId()).add(respDTO);
                }else {
                    List<ContractOrderDTO> list = new ArrayList<>();
                    list.add(respDTO);
                    map.put(respDTO.getProjectId(), list);
                }
            }

        }
        return map;
    }
}

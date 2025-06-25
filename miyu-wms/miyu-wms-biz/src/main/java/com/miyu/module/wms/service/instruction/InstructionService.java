package com.miyu.module.wms.service.instruction;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionPageReqVO;
import com.miyu.module.wms.controller.admin.instruction.vo.InstructionSaveReqVO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 指令 Service 接口
 *
 * @author 王正浩
 */
public interface InstructionService {

    /**
     * 创建指令
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInstruction(@Valid InstructionSaveReqVO createReqVO);

    /**
     * 更新指令
     *
     * @param updateReqVO 更新信息
     */
    void updateInstruction(@Valid InstructionSaveReqVO updateReqVO);

    /**
     * 删除指令
     *
     * @param id 编号
     */
    void deleteInstruction(String id);

    /**
     * 获得指令
     *
     * @param id 编号
     * @return 指令
     */
    InstructionDO getInstruction(String id);

    /**
     * 获得指令分页
     *
     * @param pageReqVO 分页查询
     * @return 指令分页
     */
    PageResult<InstructionDO> getInstructionPage(InstructionPageReqVO pageReqVO);


//    InstructionDO createInstruction(@NotEmpty String carryStockId, @NotEmpty String startLocationId);

//    InstructionDO createInstruction( String carryStockId, String startLocationId, String targetLocationId);
    /******************************************************************************************************************/
    /*                                               新增内容                                                          */
    /******************************************************************************************************************/

    /**
     * 查询起始和目标位置是否被其他指令占用  todo：此方法将会被循环调用 待优化
     *
     * @param startLocation  起始库位
     * @param targetLocation 目标库位
     * @return 是否存在未完成指令
     */
    boolean hasUnfinishedInstruction(String startLocation, String targetLocation);

    /**
     * ②下架指令
     * 1. 查询是否存在未完成的指令任务
     * 2. 若存在 则提示用户 请先完成该任务
     * 3. 若不存在 则下发下架指令
     *
     * @param materialStockId  物料
     * @param startLocationId  起始库位id
     * @param targetLocationId 目标库位id
     */
    InstructionDO offShelfInstruction(String materialStockId, String startLocationId, String targetLocationId);
    InstructionDO offShelfInstruction(String materialStockId, String targetLocationId);

    /**
     * 上架指令   上架指令无目标位置 具体位置由WCS决定   todo： 待改
     * 1. 查询是否存在未完成的指令任务
     * 2. 若存在 则提示用户 请先完成该任务
     * 3. 若不存在 则下发上架指令
     *
     * @param materialStockId 物料库存id
     * @param startLocationId
     * @param targetLocationId
     */
    InstructionDO onShelfInstruction(String materialStockId, String startLocationId,String targetLocationId);
    InstructionDO onShelfInstruction(String materialStockId,String targetLocationId);

    /**
     * ②-①下架完成 测试接口  todo：等着 这得加事务
     * 1. 更新指令任务状态
     * 2. 更新物料库位
     * 3. 通知AGV 开始搬运 激活任务状态
     *
     * @param insCode 指令编码
     */
    void stockoffshelfComplete(String insCode);

    /**
     * 上架完成 测试接口  todo：等着 这得加事务
     * 1. 更新指令任务状态
     * 2. 更新物料库位
     * 3. 通知AGV 开始搬运 激活任务状态
     *
     * @param insCode        指令编码
     */
    void stockonshelfComplete(String insCode);

    /**
     * 根据指令编码查询指令
     * @param insCode
     * @return
     */
    InstructionDO getInstructionByInsCode(String insCode);


    InstructionDO updateInsStatusByInsCodeAndInsStatus(String insCode, Integer insStatus);

    /**
     * 根据指令编码和目标库位id更新指令状态  并更新目标库位id
     * @param insCode
     * @param insStatus
     * @param targetLocationId
     * @return
     */
//    InstructionDO updateInsStatusByInsCodeAndInsStatus(String insCode, Integer insStatus, String targetLocationId);

    /**
     * 根据物料库存id查询未完成的指令
     *
     * @param containerStockId
     * @return
     */
    InstructionDO getNotFinishedInstructionByMaterialStockId(String containerStockId);

    /**
     * 查询所有未完成的指令
     * @return
     */
    List<InstructionDO> getNotFinishedInstruction();

    /**
     * 下发指令
     * @param
     * @return
     */
    CommonResult<?> instructionIssue(InstructionDO instruction);

    /**
     * 创建上下架指令
     * @param currSubTask
     * @param nextSubTask
     * @return
     */
    CommonResult<?> createCarrySubTaskInstruction(CarrySubTaskDO currSubTask, CarrySubTaskDO nextSubTask);

    void updateInsStatusByInsCodeAndInsStatus(String taskNo, String taskStatus, LocalDateTime parse, String message);
}
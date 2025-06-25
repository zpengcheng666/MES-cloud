package com.miyu.module.tms.service.toolinfo;

import javax.validation.*;

import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleTaskPageReqVO;
import com.miyu.module.tms.controller.admin.toolinfo.vo.*;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBalanceDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBaseDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import java.util.Collection;
import java.util.List;

/**
 * 刀组信息 Service 接口
 *
 * @author QianJy
 */
public interface ToolInfoService {

    /**
     * 创建刀组信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolInfo(@Valid ToolInfoSaveReqVO createReqVO);

    /**
     * 更新刀组信息
     *
     * @param updateReqVO 更新信息
     */
    void updateToolInfo(@Valid ToolInfoSaveReqVO updateReqVO);

    /**
     * 删除刀组信息
     *
     * @param id 编号
     */
    void deleteToolInfo(String id);

    /**
     * 获得刀组信息
     *
     * @param id 编号
     * @return 刀组信息
     */
    ToolInfoDO getToolInfo(String id);

    /**
     * 获得刀组信息分页
     *
     * @param pageReqVO 分页查询
     * @return 刀组信息分页
     */
    PageResult<ToolInfoDO> getToolInfoPage(ToolInfoPageReqVO pageReqVO);

    // ==================== 子表（刀具动平衡） ====================

    /**
     * 获得刀具动平衡分页
     *
     * @param pageReqVO 分页查询
     * @param toolInfoId 成品刀具id
     * @return 刀具动平衡分页
     */
    PageResult<ToolBalanceDO> getToolBalancePage(PageParam pageReqVO, String toolInfoId);

    /**
     * 创建刀具动平衡
     *
     * @param toolBalance 创建信息
     * @return 编号
     */
    String createToolBalance(@Valid ToolBalanceDO toolBalance);

    /**
     * 更新刀具动平衡
     *
     * @param toolBalance 更新信息
     */
    void updateToolBalance(@Valid ToolBalanceDO toolBalance);

    /**
     * 删除刀具动平衡
     *
     * @param id 编号
     */
    void deleteToolBalance(String id);

	/**
	 * 获得刀具动平衡
	 *
	 * @param id 编号
     * @return 刀具动平衡
	 */
    ToolBalanceDO getToolBalance(String id);

    // ==================== 子表（对刀数据） ====================

    /**
     * 获得对刀数据分页
     *
     * @param pageReqVO 分页查询
     * @param toolInfoId 成品刀具id
     * @return 对刀数据分页
     */
    PageResult<ToolBaseDO> getToolBasePage(PageParam pageReqVO, String toolInfoId);

    /**
     * 创建对刀数据
     *
     * @param toolBase 创建信息
     * @return 编号
     */
    String createToolBase(@Valid ToolBaseDO toolBase);

    /**
     * 更新对刀数据
     *
     * @param toolBase 更新信息
     */
    void updateToolBase(@Valid ToolBaseDO toolBase);

    /**
     * 删除对刀数据
     *
     * @param id 编号
     */
    void deleteToolBase(String id);

	/**
	 * 获得对刀数据
	 *
	 * @param id 编号
     * @return 对刀数据
	 */
    ToolBaseDO getToolBase(String id);

    // ==================== 子表（刀具装配记录） ====================

    /**
     * 获得刀具装配记录分页
     *
     * @param pageReqVO 分页查询
     * @param toolInfoId 成品刀具id
     * @return 刀具装配记录分页
     */
    PageResult<AssembleRecordDO> getAssembleRecordPage(PageParam pageReqVO, String toolInfoId);

    /**
     * 创建刀具装配记录
     *
     * @param assembleRecord 创建信息
     * @return 编号
     */
    String createAssembleRecord(@Valid AssembleRecordDO assembleRecord);

    /**
     * 更新刀具装配记录
     *
     * @param assembleRecord 更新信息
     */
    void updateAssembleRecord(@Valid AssembleRecordDO assembleRecord);

    /**
     * 删除刀具装配记录
     *
     * @param id 编号
     */
    void deleteAssembleRecord(String id);

	/**
	 * 获得刀具装配记录
	 *
	 * @param id 编号
     * @return 刀具装配记录
	 */
    AssembleRecordDO getAssembleRecord(String id);


    /**
     * 获得刀具装配任务记录分页
     * @param pageReqVO
     * @return
     */
    PageResult<ToolInfoDO> getAssembleTaskRecordPage(AssembleTaskPageReqVO pageReqVO);
//    List<ToolInfoDO> getAssembleTaskRecordPage2(AssembleTaskPageReqVO pageReqVO);

    /**
     * 获得刀具装配任务记录列表
     * @param assembleTaskId
     * @return
     */
    List<AssembleRecordDO> getAssembleRecordListByAssembleTaskId(String assembleTaskId);



    List<ToolInfoDO> getToolInfoById(String id);




    void saveUpdateAssembleRecord(ToolInfoSaveReqVO  saveReqVO);

    /**
     * 获得刀具装配记录列表
     * @param ids
     * @param type
     * @return
     */
    List<AssembleRecordDO> getAssembleRecordByIds(Collection<String> ids, Integer type);


    List<AssembleRecordDO> getAssembleRecordListByToolInfoId(String toolInfoId,Integer type);


    /**
     * 获得当前刀具装配记录
     * @param toolInfoId
     * @return
     */
    List<AssembleRecordDO> getCurrentAssembleRecordByToolInfoId(String toolInfoId);

    /**
     * 批量卸刀生成记录
     * @param assembleRecordDOS
     * @return
     */
    Boolean batchCreateAssembleRecord(Collection<AssembleRecordDO> assembleRecordDOS);
}
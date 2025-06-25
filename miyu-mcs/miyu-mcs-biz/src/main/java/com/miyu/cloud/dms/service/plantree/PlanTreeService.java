package com.miyu.cloud.dms.service.plantree;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreePageReqVO;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.plantree.PlanTreeDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 计划关联树 Service 接口
 *
 * @author 王正浩
 */
public interface PlanTreeService {

    /**
     * 创建计划关联树
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createPlanTree(@Valid PlanTreeSaveReqVO createReqVO);

    /**
     * 更新计划关联树
     *
     * @param updateReqVO 更新信息
     */
    void updatePlanTree(@Valid PlanTreeSaveReqVO updateReqVO);

    /**
     * 删除计划关联树
     *
     * @param id 编号
     */
    void deletePlanTree(String id);

    /**
     * 获得计划关联树
     *
     * @param id 编号
     * @return 计划关联树
     */
    PlanTreeDO getPlanTree(String id);

    /**
     * 获得计划关联树分页
     *
     * @param pageReqVO 分页查询
     * @return 计划关联树分页
     */
    PageResult<PlanTreeDO> getPlanTreePage(PlanTreePageReqVO pageReqVO);

    /**
     * 获得计划关联树列表
     *
     * @return 计划关联树列表
     */
    List<PlanTreeDO> getPlanTreeList();

}
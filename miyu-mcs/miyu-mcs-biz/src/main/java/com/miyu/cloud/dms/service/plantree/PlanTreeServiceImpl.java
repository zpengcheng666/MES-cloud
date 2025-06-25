package com.miyu.cloud.dms.service.plantree;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreePageReqVO;
import com.miyu.cloud.dms.controller.admin.plantree.vo.PlanTreeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.plantree.PlanTreeDO;
import com.miyu.cloud.dms.dal.mysql.plantree.PlanTreeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.PLAN_ID_NAME_EMPTY;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.PLAN_TREE_NOT_EXISTS;

/**
 * 计划关联树 Service 实现类
 *
 * @author 王正浩
 */
@Service
@Validated
public class PlanTreeServiceImpl implements PlanTreeService {

    private void check(PlanTreeSaveReqVO data) {
        if (StringUtils.isBlank(data.getName())) {
            throw exception(PLAN_ID_NAME_EMPTY);
        }
    }

    @Resource
    private PlanTreeMapper planTreeMapper;

    @Override
    public String createPlanTree(PlanTreeSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        PlanTreeDO planTree = BeanUtils.toBean(createReqVO, PlanTreeDO.class);
        planTreeMapper.insert(planTree);
        // 返回
        return planTree.getId();
    }

    @Override
    public void updatePlanTree(PlanTreeSaveReqVO updateReqVO) {
        // 校验存在
        validatePlanTreeExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        PlanTreeDO updateObj = BeanUtils.toBean(updateReqVO, PlanTreeDO.class);
        planTreeMapper.updateById(updateObj);
    }

    @Override
    public void deletePlanTree(String id) {
        // 校验存在
        validatePlanTreeExists(id);
        // 删除
        planTreeMapper.deleteById(id);
    }

    private void validatePlanTreeExists(String id) {
        if (planTreeMapper.selectById(id) == null) {
            throw exception(PLAN_TREE_NOT_EXISTS);
        }
    }

    @Override
    public PlanTreeDO getPlanTree(String id) {
        return planTreeMapper.selectById(id);
    }

    @Override
    public PageResult<PlanTreeDO> getPlanTreePage(PlanTreePageReqVO pageReqVO) {
        return planTreeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PlanTreeDO> getPlanTreeList() {
        return planTreeMapper.selectList();
    }

}
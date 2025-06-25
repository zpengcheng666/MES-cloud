package cn.iocoder.yudao.module.bpm.service.oasupply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyListDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.oasupply.OaSupplyListMapper;
import cn.iocoder.yudao.module.bpm.dal.mysql.oasupply.OaSupplyMapper;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.OA_SUPPLY_NOT_EXISTS;

/**
 * OA 物品领用 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OaSupplyServiceImpl implements OaSupplyService {

    @Resource
    private OaSupplyMapper oaSupplyMapper;
    @Resource
    private OaSupplyListMapper oaSupplyListMapper;

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "boa_supply";

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOaSupply(Long userId,OaSupplySaveReqVO createReqVO) {
        // 插入
        OaSupplyDO oaSupply = BeanUtils.toBean(createReqVO, OaSupplyDO.class)
                .setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        oaSupplyMapper.insert(oaSupply);

        // 插入子表
        createOaSupplyListList(oaSupply.getId(), createReqVO.getOaSupplyLists());

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(oaSupply.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();

        // 将工作流的编号，更新到 OA 请假单中
        oaSupplyMapper.updateById(new OaSupplyDO().setId(oaSupply.getId()).setProcessInstanceId(processInstanceId));

        // 返回
        return oaSupply.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOaSupply(OaSupplySaveReqVO updateReqVO) {
        // 校验存在
        validateOaSupplyExists(updateReqVO.getId());
        // 更新
        OaSupplyDO updateObj = BeanUtils.toBean(updateReqVO, OaSupplyDO.class);
        oaSupplyMapper.updateById(updateObj);

        // 更新子表
        updateOaSupplyListList(updateReqVO.getId(), updateReqVO.getOaSupplyLists());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOaSupply(Long id) {
        // 校验存在
        validateOaSupplyExists(id);
        // 删除
        oaSupplyMapper.deleteById(id);

        // 删除子表
        deleteOaSupplyListBySupplyId(id);
    }

    private void validateOaSupplyExists(Long id) {
        if (oaSupplyMapper.selectById(id) == null) {
            throw exception(OA_SUPPLY_NOT_EXISTS);
        }
    }

    @Override
    public OaSupplyDO getOaSupply(Long id) {
        return oaSupplyMapper.selectById(id);
    }

    @Override
    public PageResult<OaSupplyDO> getOaSupplyPage(OaSupplyPageReqVO pageReqVO) {
        return oaSupplyMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（OA 物品领用表-物品清单） ====================

    @Override
    public List<OaSupplyListDO> getOaSupplyListListBySupplyId(Long supplyId) {
        return oaSupplyListMapper.selectListBySupplyId(supplyId);
    }

    private void createOaSupplyListList(Long supplyId, List<OaSupplyListDO> list) {
        list.forEach(o -> o.setSupplyId(supplyId));
        oaSupplyListMapper.insertBatch(list);
    }

    private void updateOaSupplyListList(Long supplyId, List<OaSupplyListDO> list) {
        deleteOaSupplyListBySupplyId(supplyId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createOaSupplyListList(supplyId, list);
    }

    private void deleteOaSupplyListBySupplyId(Long supplyId) {
        oaSupplyListMapper.deleteBySupplyId(supplyId);
    }



    @Override
    public void updateLeaveStatus(Long id, Integer status) {
        validateLeaveExists(id);
        oaSupplyMapper.updateById(new OaSupplyDO().setId(id).setStatus(status));
    }

    private void validateLeaveExists(Long id) {
        if (oaSupplyMapper.selectById(id) == null) {
            throw exception(OA_SUPPLY_NOT_EXISTS);
        }
    }

}

package cn.iocoder.yudao.module.bpm.service.oapurchase;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseListDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.oapurchase.OaPurchaseListMapper;
import cn.iocoder.yudao.module.bpm.dal.mysql.oapurchase.OaPurchaseMapper;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.OA_PURCHASE_NOT_EXISTS;

/**
 * OA 采购申请 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OaPurchaseServiceImpl implements OaPurchaseService {

    @Resource
    private OaPurchaseMapper oaPurchaseMapper;
    @Resource
    private OaPurchaseListMapper oaPurchaseListMapper;

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "boa_purchase";

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOaPurchase(Long userId,OaPurchaseSaveReqVO createReqVO) {
        // 插入
        OaPurchaseDO oaPurchase = BeanUtils.toBean(createReqVO, OaPurchaseDO.class)
                .setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        oaPurchaseMapper.insert(oaPurchase);

        // 插入子表
        createOaPurchaseListList(oaPurchase.getId(), createReqVO.getOaPurchaseLists());

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(oaPurchase.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();
        // 将工作流的编号，更新到 OA 请假单中
        oaPurchaseMapper.updateById(new OaPurchaseDO().setId(oaPurchase.getId()).setProcessInstanceId(processInstanceId));

        // 返回
        return oaPurchase.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOaPurchase(OaPurchaseSaveReqVO updateReqVO) {
        // 校验存在
        validateOaPurchaseExists(updateReqVO.getId());
        // 更新
        OaPurchaseDO updateObj = BeanUtils.toBean(updateReqVO, OaPurchaseDO.class);
        oaPurchaseMapper.updateById(updateObj);

        // 更新子表
        updateOaPurchaseListList(updateReqVO.getId(), updateReqVO.getOaPurchaseLists());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOaPurchase(Long id) {
        // 校验存在
        validateOaPurchaseExists(id);
        // 删除
        oaPurchaseMapper.deleteById(id);

        // 删除子表
        deleteOaPurchaseListByPurchaseId(id);
    }

    private void validateOaPurchaseExists(Long id) {
        if (oaPurchaseMapper.selectById(id) == null) {
            throw exception(OA_PURCHASE_NOT_EXISTS);
        }
    }

    @Override
    public OaPurchaseDO getOaPurchase(Long id) {
        return oaPurchaseMapper.selectById(id);
    }

    @Override
    public PageResult<OaPurchaseDO> getOaPurchasePage(OaPurchasePageReqVO pageReqVO) {
        return oaPurchaseMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（OA 采购申请） ====================

    @Override
    public List<OaPurchaseListDO> getOaPurchaseListListByPurchaseId(Long purchaseId) {
        return oaPurchaseListMapper.selectListByPurchaseId(purchaseId);
    }

    private void createOaPurchaseListList(Long purchaseId, List<OaPurchaseListDO> list) {
        list.forEach(o -> o.setPurchaseId(purchaseId));
        oaPurchaseListMapper.insertBatch(list);
    }

    private void updateOaPurchaseListList(Long purchaseId, List<OaPurchaseListDO> list) {
        deleteOaPurchaseListByPurchaseId(purchaseId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createOaPurchaseListList(purchaseId, list);
    }

    private void deleteOaPurchaseListByPurchaseId(Long purchaseId) {
        oaPurchaseListMapper.deleteByPurchaseId(purchaseId);
    }

    @Override
    public void updateLeaveStatus(Long id, Integer status) {
        validateLeaveExists(id);
        oaPurchaseMapper.updateById(new OaPurchaseDO().setId(id).setStatus(status));
    }

    private void validateLeaveExists(Long id) {
        if (oaPurchaseMapper.selectById(id) == null) {
            throw exception(OA_PURCHASE_NOT_EXISTS);
        }
    }

}

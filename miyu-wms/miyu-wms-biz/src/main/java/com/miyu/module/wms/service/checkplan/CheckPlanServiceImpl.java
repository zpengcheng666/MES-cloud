package com.miyu.module.wms.service.checkplan;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.checkcontainer.CheckContainerService;
import com.miyu.module.wms.service.checkdetail.CheckDetailService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.checkplan.vo.*;
import com.miyu.module.wms.dal.dataobject.checkplan.CheckPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.checkplan.CheckPlanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存盘点计划 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
@Transactional(rollbackFor = Exception.class)
public class CheckPlanServiceImpl implements CheckPlanService {

    @Resource
    private CheckPlanMapper checkPlanMapper;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private CheckContainerService checkContainerService;
    @Resource
    private CheckDetailService checkDetailService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;

    @Override
    public String createCheckPlan(CheckPlanSaveReqVO createReqVO) {
        // 插入
        CheckPlanDO checkPlan = BeanUtils.toBean(createReqVO, CheckPlanDO.class);
        // 校验是否可以生成盘点计划
        this.validateCanCreateCheckPlan(checkPlan);
        checkPlanMapper.insert(checkPlan);
        parseCheckPlan(checkPlan);
        // 返回
        return checkPlan.getId();
    }

    @Override
    public void updateCheckPlan(CheckPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateCheckPlanExists(updateReqVO.getId());
        // 更新
        CheckPlanDO updateObj = BeanUtils.toBean(updateReqVO, CheckPlanDO.class);
        parseCheckPlan(updateObj);
        // 校验是否可以生成盘点计划
        this.validateCanCreateCheckPlan(updateObj);
        checkPlanMapper.updateById(updateObj);
    }

    /**
     * 库存盘点计划解析
     * @param checkPlanDO 编号
     */
    private void parseCheckPlan(CheckPlanDO checkPlanDO) {
        if(checkPlanDO == null || checkPlanDO.getCheckStatus() != DictConstants.WMS_CHECK_STATUS_NOT_START){
            return;
        }
        List<CheckContainerDO> checkContainerList = checkContainerService.getCheckContainerByCheckPlanId(checkPlanDO.getId());
        if(!CollectionUtils.isAnyEmpty(checkContainerList)){
            return;
        }
        // 获取盘点区域
        String checkAreaId = checkPlanDO.getCheckAreaId();

        // 获取在此库区的所有容器
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByCheckAreaIdAndMaterialConfigIds(checkAreaId, StringListUtils.stringToArrayList(checkPlanDO.getMaterialConfigIds()));

        // 生成盘点容器列表
        checkContainerService.insertCheckContainer(checkPlanDO.getId(), materialStockList);
    }

    @Override
    public void deleteCheckPlan(String id) {
        // 校验存在
        validateCheckPlanExists(id);
        // 删除
        checkPlanMapper.deleteById(id);
    }

    private void validateCheckPlanExists(String id) {
        if (checkPlanMapper.selectById(id) == null) {
            throw exception(CHECK_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public CheckPlanDO getCheckPlan(String id) {
        return checkPlanMapper.selectById(id);
    }

    @Override
    public PageResult<CheckPlanDO> getCheckPlanPage(CheckPlanPageReqVO pageReqVO) {
        return checkPlanMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<CheckPlanDO> getCheckTaskPage(CheckPlanPageReqVO pageReqVO) {
        return checkPlanMapper.selectTaskPage(pageReqVO);
    }

    @Override
    public boolean updateCheckPlanStatus(String checkPlanId, Integer checkPlanStatus) {
        return checkPlanMapper.updateCheckStatus(checkPlanId, checkPlanStatus);
    }

    @Override
    @Transactional
    public void submitCheckPlan(String checkPlanId) {
        List<CheckContainerDO> checkContainerList = checkContainerService.getCheckContainerByCheckPlanId(checkPlanId);
        Set<String> checkContainerIds = new HashSet<>();
        for (CheckContainerDO checkContainerDO : checkContainerList) {
            if(Objects.equals(checkContainerDO.getCheckStatus(), DictConstants.WMS_CHECK_DETAIL_STATUS_WAITCHECK)){
                throw exception(CHECK_PLAN_NOT_CHECK_ALL_LOCATION);
            }
            // 已经提交的就不要再提交了
            if(Objects.equals(checkContainerDO.getCheckStatus(), DictConstants.WMS_CHECK_DETAIL_STATUS_CHECKED)){
                checkContainerIds.add(checkContainerDO.getId());
            }
        }
        if(!checkContainerIds.isEmpty()){
            List<CheckDetailDO> checkDetailDOS = checkDetailService.getCheckDetailByCheckContainerIds(checkContainerIds);
            checkDetailService.submitCheckPlanItem(checkDetailDOS);
        }

        // 更新盘点计划状态为已完成
        if(!this.updateCheckPlanStatus(checkPlanId, DictConstants.WMS_CHECK_STATUS_FINISHED)){
            throw exception(CHECK_PLAN_UPDATE_ERROR);
        }
    }

    @Override
    public boolean updateCheckPlanStatusByCheckContainerId(String checkContainerId) {
        CheckPlanDO checkPlan = this.getCheckPlanByCheckContainerId(checkContainerId);
        if(Objects.equals(checkPlan.getCheckStatus(), DictConstants.WMS_CHECK_STATUS_NOT_START)){
            checkPlan.setCheckStatus(DictConstants.WMS_CHECK_STATUS_RUNNING);
            return checkPlanMapper.updateById(checkPlan) > 0;
        }
        return true;
    }

    @Override
    public CheckPlanDO getCheckPlanByCheckContainerId(String checkContainerId) {
        return checkPlanMapper.getCheckPlanByCheckContainerId(checkContainerId);
    }

    @Override
    public void validateCanCreateCheckPlan(CheckPlanDO checkPlan) {
        String checkAreaId = checkPlan.getCheckAreaId();
        // 查看是否有未完成的盘点计划
        List<CheckPlanDO> checkPlanDOS = this.getNotFinishedCheckPlanByCheckAreaId(checkPlan.getCheckAreaId());
        if(!CollectionUtils.isAnyEmpty(checkPlanDOS)){
            throw exception(CHECK_PLAN_EXIST_NOT_FINISHED);
        }
        // 查看是否锁盘
        if(!checkPlan.getCheckLocked()){
            return;
        }
        // 查看是否有未完成的 出入库单
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailService.getNotFinishedInWarehouseDetailListByAreaId(checkAreaId);
        if(!CollectionUtils.isAnyEmpty(inWarehouseDetailDOS)){
            throw exception(CHECK_PLAN_EXIST_NOT_FINISHED);
        }
        List<OutWarehouseDetailDO> outWarehouseDetailDOS = outWarehouseDetailService.getNotFinishedOutWarehouseDetailListByAreaId(checkAreaId);
        if(!CollectionUtils.isAnyEmpty(outWarehouseDetailDOS)){
            throw exception(CHECK_PLAN_EXIST_NOT_FINISHED);
        }
        List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveWarehouseDetailService.getNotFinishedMoveWarehouseDetailListByAreaId(checkAreaId);
        if(!CollectionUtils.isAnyEmpty(moveWarehouseDetailDOS)){
            throw exception(CHECK_PLAN_EXIST_NOT_FINISHED);
        }

    }

    @Override
    public List<CheckPlanDO> getNotFinishedCheckPlanByCheckAreaId(String checkAreaId) {
        return checkPlanMapper.getNotFinishedCheckPlanByCheckAreaId(checkAreaId);
    }

    @Override
    public List<CheckPlanDO> getNotFinishedCheckPlanAndLocked() {
        return checkPlanMapper.selectNotFinishedCheckPlanAndLocked();
    }

}
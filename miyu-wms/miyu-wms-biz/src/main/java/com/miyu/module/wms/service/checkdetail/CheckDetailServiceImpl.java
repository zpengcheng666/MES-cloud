package com.miyu.module.wms.service.checkdetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.controller.admin.checkcontainer.vo.CheckContainerSaveReqVO;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.checkcontainer.CheckContainerService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.checkdetail.vo.*;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.checkdetail.CheckDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存盘点详情 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class CheckDetailServiceImpl implements CheckDetailService {

    @Resource
    private CheckDetailMapper checkDetailMapper;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private CheckContainerService checkContainerService;

    @Override
    public String createCheckDetail(CheckDetailSaveReqVO createReqVO) {
        // 插入
        CheckDetailDO checkDetail = BeanUtils.toBean(createReqVO, CheckDetailDO.class);
        checkDetailMapper.insert(checkDetail);
        // 返回
        return checkDetail.getId();
    }

    @Override
    public List<CheckDetailDO> createCheckDetailByCheckContainerId(String checkContainerId,String containerStockId) {
        List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(containerStockId);
        List<CheckDetailDO> checkDetailDOS = new ArrayList<>();
        for (MaterialStockDO materialStockDO : allMaterialStockList) {
            if(materialStockDO.getId().equals(containerStockId)){
                continue;
            }
            CheckDetailDO checkDetail = new CheckDetailDO();
            checkDetail.setCheckContainerId(checkContainerId);
            checkDetail.setMaterialStockId(materialStockDO.getId());
            checkDetail.setRealTotality(materialStockDO.getTotality());
            checkDetail.setCheckTotality(0);
            checkDetail.setCheckType(DictConstants.WMS_CHECK_TYPE_NORMAL);
            checkDetailDOS.add(checkDetail);

            checkDetail.setMaterialManage(materialStockDO.getMaterialManage());
            checkDetail.setMaterialNumber(materialStockDO.getMaterialNumber());
            checkDetail.setBarCode(materialStockDO.getBarCode());
            checkDetail.setBatchNumber(materialStockDO.getBatchNumber());
        }
        // 插入
        checkDetailMapper.insertBatch(checkDetailDOS);
        // 返回
        return checkDetailDOS;
    }

    @Override
    public void updateCheckDetail(CheckDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateCheckDetailExists(updateReqVO.getId());
        // 更新
        CheckDetailDO updateObj = BeanUtils.toBean(updateReqVO, CheckDetailDO.class);
        checkDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteCheckDetail(String id) {
        // 校验存在
        validateCheckDetailExists(id);
        // 删除
        checkDetailMapper.deleteById(id);
    }

    private void validateCheckDetailExists(String id) {
        if (checkDetailMapper.selectById(id) == null) {
            throw exception(CHECK_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public CheckDetailDO getCheckDetail(String id) {
        return checkDetailMapper.selectById(id);
    }

    @Override
    public PageResult<CheckDetailDO> getCheckDetailPage(CheckDetailPageReqVO pageReqVO) {
        return checkDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CheckDetailDO> getCheckDetailByCheckContainerIds(Collection<String> checkContainerIds) {
        if(CollectionUtils.isAnyEmpty(checkContainerIds)){
            return Collections.emptyList();
        }
        return checkDetailMapper.selectByCheckContainerIds(checkContainerIds);
    }

    @Override
    @Transactional
    public Boolean saveCheckDetailItem(List<CheckDetailDO> checkDetailList) {
        Boolean b = checkDetailMapper.insertOrUpdateBatch(checkDetailList);
        if(b){
            String checkContainerId = checkDetailList.get(0).getCheckContainerId();
            CheckContainerSaveReqVO updateReqVO = new CheckContainerSaveReqVO();
            updateReqVO.setId(checkContainerId);
            // 盘点状态为已盘点
            updateReqVO.setCheckStatus(DictConstants.WMS_CHECK_DETAIL_STATUS_CHECKED);
            checkContainerService.updateCheckContainer(updateReqVO);
        }
        return b;
    }


    @Override
    @Transactional
    public void submitCheckPlanItem(List<CheckDetailDO> checkDetailList) {
        saveSubmitCheckDetailItem(checkDetailList);
        // 盘盈map
        Map<String, CheckDetailDO> checkProfitMap = new HashMap<>();
        // 盘亏map
        Map<String, CheckDetailDO> checkLossMap = new HashMap<>();
        for (CheckDetailDO checkDetail : checkDetailList) {
            Integer checkTotality = checkDetail.getCheckTotality();
            Integer realTotality = checkDetail.getRealTotality();
            // 盘盈
            if(checkTotality > realTotality || Objects.equals(checkDetail.getCheckType(), DictConstants.WMS_CHECK_TYPE_PROFIT)){
                checkProfitMap.put(checkDetail.getMaterialStockId(),checkDetail);
            }else if(checkTotality < realTotality){
                // 盘亏
                checkLossMap.put(checkDetail.getMaterialStockId(),checkDetail);
            }
        }
        List<String> materielStockIds = new ArrayList<>();
        materielStockIds.addAll(checkProfitMap.keySet());
        materielStockIds.addAll(checkLossMap.keySet());
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockByIds(materielStockIds);
        for (MaterialStockDO materialStockDO : materialStockList) {
            if(checkProfitMap.containsKey(materialStockDO.getId())){
                // 盘盈
                materialStockService.increaseMaterialStock(materialStockDO, checkProfitMap.get(materialStockDO.getId()));
            }else if(checkLossMap.containsKey(materialStockDO.getId())){
                // 盘亏
                materialStockService.decreaseMaterialStockt(materialStockDO, checkLossMap.get(materialStockDO.getId()));
            }
        }
    }

    private void saveSubmitCheckDetailItem(List<CheckDetailDO> checkDetailList) {
        Boolean b = checkDetailMapper.insertOrUpdateBatch(checkDetailList);
        Set<String> checkContainerIds = CollectionUtils.convertSet(checkDetailList, CheckDetailDO::getCheckContainerId);
        if(!b){
            throw exception(CHECK_DETAIL_UPDATE_ERROR);
        }
        List<CheckContainerDO> updateDOList = new ArrayList<>();
        for (String checkContainerId : checkContainerIds) {
            CheckContainerDO updateDO = new CheckContainerDO();
            updateDO.setId(checkContainerId);
            // 盘点状态为已提交
            updateDO.setCheckStatus(DictConstants.WMS_CHECK_DETAIL_STATUS_SUBMITTED);
            updateDOList.add(updateDO);
        }
        // 更新盘点容器状态为已提交
        Boolean bb = checkContainerService.updateBatchCheckContainer(updateDOList);
        if(!bb){
            throw exception(CHECK_CONTAINER_UPDATE_ERROR);
        }
    }

}
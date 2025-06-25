package com.miyu.module.wms.service.checkcontainer;

import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.checkcontainer.vo.*;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.checkcontainer.CheckContainerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存盘点容器 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class CheckContainerServiceImpl implements CheckContainerService {

    @Resource
    private CheckContainerMapper checkContainerMapper;

    @Override
    public String createCheckContainer(CheckContainerSaveReqVO createReqVO) {
        // 插入
        CheckContainerDO checkContainer = BeanUtils.toBean(createReqVO, CheckContainerDO.class);
        checkContainerMapper.insert(checkContainer);
        // 返回
        return checkContainer.getId();
    }

    @Override
    public void updateCheckContainer(CheckContainerSaveReqVO updateReqVO) {
        // 校验存在
        validateCheckContainerExists(updateReqVO.getId());
        // 更新
        CheckContainerDO updateObj = BeanUtils.toBean(updateReqVO, CheckContainerDO.class);
        checkContainerMapper.updateById(updateObj);
    }

    @Override
    public void insertCheckContainer(String checkPlanId, List<MaterialStockDO> materialStockList) {
        if(materialStockList.isEmpty())return;
        ArrayList<CheckContainerDO> checkContainerList = new ArrayList<>();
        materialStockList.forEach(s-> {
            CheckContainerDO checkContainer = new CheckContainerDO();
            checkContainer.setCheckPlanId(checkPlanId);
            checkContainer.setContainerStockId(s.getId());
            checkContainer.setCheckStatus(DictConstants.WMS_CHECK_DETAIL_STATUS_WAITCHECK);
            checkContainerList.add(checkContainer);
        });
        if(!checkContainerMapper.insertBatch(checkContainerList)){
            throw exception(CHECK_CONTAINER_INSERT_ERROR);
        }
    }

    @Override
    public void deleteCheckContainer(String id) {
        // 校验存在
        validateCheckContainerExists(id);
        // 删除
        checkContainerMapper.deleteById(id);
    }

    private void validateCheckContainerExists(String id) {
        if (checkContainerMapper.selectById(id) == null) {
            throw exception(CHECK_CONTAINER_NOT_EXISTS);
        }
    }

    @Override
    public CheckContainerDO getCheckContainer(String id) {
        return checkContainerMapper.selectById(id);
    }

    @Override
    public PageResult<CheckContainerDO> getCheckContainerPage(CheckContainerPageReqVO pageReqVO) {
        return checkContainerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CheckContainerDO> getCheckContainerByCheckPlanId(String checkPlanId) {
        return checkContainerMapper.selectList(CheckContainerDO::getCheckPlanId, checkPlanId);
    }

    @Override
    public List<CheckContainerDO> getCheckContainerAndLocationIdByCheckPlanId(String checkPlanId) {
        return checkContainerMapper.selectCheckContainerAndLocationIdByCheckPlanId(checkPlanId);
    }

    @Override
    public int updateCheckContainerStatus(String checkContainerId, Integer checkStatus) {
        return checkContainerMapper.updateCheckContainerStatus(checkContainerId, checkStatus);
    }

    @Override
    public Boolean updateBatchCheckContainer(List<CheckContainerDO> updateDOList) {
        if(updateDOList.isEmpty())return false;
        return checkContainerMapper.updateBatch(updateDOList);
    }

}
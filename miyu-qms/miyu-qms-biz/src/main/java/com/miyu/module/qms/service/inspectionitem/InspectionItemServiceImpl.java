package com.miyu.module.qms.service.inspectionitem;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.qms.controller.admin.inspectionitem.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionitem.InspectionItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 检测项目 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InspectionItemServiceImpl implements InspectionItemService {

    @Resource
    private InspectionItemMapper inspectionItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createInspectionItem(InspectionItemSaveReqVO createReqVO) {
        // 插入
        InspectionItemDO inspectionItem = BeanUtils.toBean(createReqVO, InspectionItemDO.class);
        inspectionItemMapper.insert(inspectionItem);
        // 返回
        return inspectionItem.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = QMS_ITEM_TYPE, subType = QMS_UPDATE_ITEM_SUB_TYPE, bizNo = "{{#item.id}}",
            success = QMS_UPDATE_ITEM_SUCCESS)
    public void updateInspectionItem(InspectionItemSaveReqVO updateReqVO) {
        // 校验存在
        InspectionItemDO item = validateInspectionItemExists(updateReqVO.getId());
        // 更新
        InspectionItemDO updateObj = BeanUtils.toBean(updateReqVO, InspectionItemDO.class);
        inspectionItemMapper.updateById(updateObj);

        // 记录操作日志上下文
        LogRecordContext.putVariable("item", item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionItem(String id) {
        // 校验存在
        validateInspectionItemExists(id);
        // 删除
        inspectionItemMapper.deleteById(id);


    }

    private InspectionItemDO validateInspectionItemExists(String id) {
        InspectionItemDO itemDO = inspectionItemMapper.selectById(id);
        if (itemDO == null) {
            throw exception(INSPECTION_ITEM_NOT_EXISTS);
        }
        return itemDO;
    }

    @Override
    public InspectionItemDO getInspectionItem(String id) {
        return inspectionItemMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionItemDO> getInspectionItemPage(InspectionItemPageReqVO pageReqVO) {
        return inspectionItemMapper.selectPage(pageReqVO);
    }



    @Override
    public List<InspectionItemDO> getInspectionItemByTypeId(String inspectionItemTypeId) {
        return inspectionItemMapper.selectListByTypeId(inspectionItemTypeId);
    }


}

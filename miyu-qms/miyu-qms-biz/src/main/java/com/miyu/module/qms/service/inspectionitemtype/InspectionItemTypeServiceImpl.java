package com.miyu.module.qms.service.inspectionitemtype;

import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.service.inspectionitem.InspectionItemService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.dal.mysql.inspectionitemtype.InspectionItemTypeMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 检测项目分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InspectionItemTypeServiceImpl implements InspectionItemTypeService {

    @Resource
    private InspectionItemTypeMapper inspectionItemTypeMapper;

    @Resource
    private InspectionItemService inspectionItemService;

    @Override
    public String createInspectionItemType(InspectionItemTypeSaveReqVO createReqVO) {
        // 校验父项目分类ID的有效性
        validateParentInspectionItemType(null, createReqVO.getParentId());
        // 校验检测项目分类名称的唯一性
        validateInspectionItemTypeItemTypeNameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // 插入
        InspectionItemTypeDO inspectionItemType = BeanUtils.toBean(createReqVO, InspectionItemTypeDO.class);
        inspectionItemTypeMapper.insert(inspectionItemType);
        // 返回
        return inspectionItemType.getId();
    }

    @Override
    @LogRecord(type = QMS_ITEM_CLASS_TYPE, subType = QMS_UPDATE_ITEM_CLASS_SUB_TYPE, bizNo = "{{#itemType.id}}",
            success = QMS_UPDATE_ITEM_CLASS_SUCCESS)
    public void updateInspectionItemType(InspectionItemTypeSaveReqVO updateReqVO) {
        // 校验存在
        InspectionItemTypeDO itemType = validateInspectionItemTypeExists(updateReqVO.getId());
        // 校验父项目分类ID的有效性
        validateParentInspectionItemType(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验检测项目分类名称的唯一性
        validateInspectionItemTypeItemTypeNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        InspectionItemTypeDO updateObj = BeanUtils.toBean(updateReqVO, InspectionItemTypeDO.class);
        inspectionItemTypeMapper.updateById(updateObj);

        // 记录操作日志上下文
        LogRecordContext.putVariable("itemType", itemType);
    }

    @Override
    public void deleteInspectionItemType(String id) {
        // 校验存在
        validateInspectionItemTypeExists(id);
        // 校验是否有子检测项目分类
        if (inspectionItemTypeMapper.selectCountByParentId(id) > 0) {
            throw exception(INSPECTION_ITEM_TYPE_EXITS_CHILDREN);
        }


        List<InspectionItemDO> inspectionItemDOS = inspectionItemService.getInspectionItemByTypeId(id);
        if (!CollectionUtils.isEmpty(inspectionItemDOS)){
            throw exception(INSPECTION_ITEM_TYPE_EXITS_ITEM);
        }
        // 删除
        inspectionItemTypeMapper.deleteById(id);
    }

    private InspectionItemTypeDO validateInspectionItemTypeExists(String id) {
        InspectionItemTypeDO itemType = inspectionItemTypeMapper.selectById(id);
        if (itemType == null) {
            throw exception(INSPECTION_ITEM_TYPE_NOT_EXISTS);
        }
        return itemType;
    }

    private void validateParentInspectionItemType(String id, String parentId) {
        if (parentId == null || InspectionItemTypeDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父检测项目分类
        if (Objects.equals(id, parentId)) {
            throw exception(INSPECTION_ITEM_TYPE_PARENT_ERROR);
        }
        // 2. 父检测项目分类不存在
        InspectionItemTypeDO parentInspectionItemType = inspectionItemTypeMapper.selectById(parentId);
        if (parentInspectionItemType == null) {
            throw exception(INSPECTION_ITEM_TYPE_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父检测项目分类，如果父检测项目分类是自己的子检测项目分类，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentInspectionItemType.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(INSPECTION_ITEM_TYPE_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父检测项目分类
            if (parentId == null || InspectionItemTypeDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentInspectionItemType = inspectionItemTypeMapper.selectById(parentId);
            if (parentInspectionItemType == null) {
                break;
            }
        }
    }

    private void validateInspectionItemTypeItemTypeNameUnique(String id, String parentId, String itemTypeName) {
        InspectionItemTypeDO inspectionItemType = inspectionItemTypeMapper.selectByParentIdAndItemTypeName(parentId, itemTypeName);
        if (inspectionItemType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的检测项目分类
        if (id == null) {
            throw exception(INSPECTION_ITEM_TYPE_ITEM_TYPE_NAME_DUPLICATE);
        }
        if (!Objects.equals(inspectionItemType.getId(), id)) {
            throw exception(INSPECTION_ITEM_TYPE_ITEM_TYPE_NAME_DUPLICATE);
        }
    }

    @Override
    public InspectionItemTypeDO getInspectionItemType(String id) {
        return inspectionItemTypeMapper.selectById(id);
    }

    @Override
    public List<InspectionItemTypeDO> getInspectionItemTypeList(InspectionItemTypeListReqVO listReqVO) {
        return inspectionItemTypeMapper.selectList(listReqVO);
    }

}

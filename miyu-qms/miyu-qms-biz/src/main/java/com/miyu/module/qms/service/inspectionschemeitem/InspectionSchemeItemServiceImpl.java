package com.miyu.module.qms.service.inspectionschemeitem;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.qms.controller.admin.inspectionschemeitem.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionschemeitem.InspectionSchemeItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验方案检测项目详情 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InspectionSchemeItemServiceImpl implements InspectionSchemeItemService {

    @Resource
    private InspectionSchemeItemMapper inspectionSchemeItemMapper;
    @Override
    public String createInspectionSchemeItem(InspectionSchemeItemSaveReqVO createReqVO) {
        // 插入
        InspectionSchemeItemDO inspectionSchemeItem = BeanUtils.toBean(createReqVO, InspectionSchemeItemDO.class);
        inspectionSchemeItemMapper.insert(inspectionSchemeItem);
        // 返回
        return inspectionSchemeItem.getId();
    }

    @Override
    public void updateInspectionSchemeItem(InspectionSchemeItemSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSchemeItemExists(updateReqVO.getId());
        // 更新
        InspectionSchemeItemDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSchemeItemDO.class);
        inspectionSchemeItemMapper.updateById(updateObj);
    }


    @Override
    public String createInspectionSchemeItemWithDetail(InspectionSchemeItemSaveReqVO createReqVO) {
        // 插入
        InspectionSchemeItemDO inspectionSchemeItem = BeanUtils.toBean(createReqVO, InspectionSchemeItemDO.class);
        inspectionSchemeItemMapper.insert(inspectionSchemeItem);
        // 返回
        return inspectionSchemeItem.getId();
    }

    @Override
    public void updateInspectionSchemeItemWithDetail(InspectionSchemeItemSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSchemeItemExists(updateReqVO.getId());
        // 更新
        InspectionSchemeItemDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSchemeItemDO.class);
        inspectionSchemeItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionSchemeItem(String id) {
        // 校验存在
        validateInspectionSchemeItemExists(id);
        // 删除
        inspectionSchemeItemMapper.deleteById(id);
    }

    private void validateInspectionSchemeItemExists(String id) {
        if (inspectionSchemeItemMapper.selectById(id) == null) {
            throw exception(INSPECTION_SCHEME_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSchemeItemDO getInspectionSchemeItem(String id) {
        return inspectionSchemeItemMapper.getInspectionSchemeItem(id);
    }

    @Override
    public PageResult<InspectionSchemeItemDO> getInspectionSchemeItemPage(InspectionSchemeItemPageReqVO pageReqVO) {
        return inspectionSchemeItemMapper.selectPage(pageReqVO);
    }
}
package com.miyu.module.qms.service.inspectionitemconfig;

import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.service.inspectionitem.InspectionItemService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.qms.controller.admin.inspectionitemconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitemconfig.InspectionItemConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionitemconfig.InspectionItemConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检测项配置表（检测内容名称） Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InspectionItemConfigServiceImpl implements InspectionItemConfigService {

    @Resource
    private InspectionItemConfigMapper inspectionItemConfigMapper;

    @Resource
    private InspectionItemService inspectionItemService;

    @Override
    public String createInspectionItemConfig(InspectionItemConfigSaveReqVO createReqVO) {
        // 插入
        InspectionItemConfigDO inspectionItemConfig = BeanUtils.toBean(createReqVO, InspectionItemConfigDO.class);
        inspectionItemConfigMapper.insert(inspectionItemConfig);
        // 返回
        return inspectionItemConfig.getId();
    }

    @Override
    public void updateInspectionItemConfig(InspectionItemConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionItemConfigExists(updateReqVO.getId());
        // 更新
        InspectionItemConfigDO updateObj = BeanUtils.toBean(updateReqVO, InspectionItemConfigDO.class);
        inspectionItemConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionItemConfig(String id) {
        // 校验存在
        validateInspectionItemConfigExists(id);
        // 删除
        inspectionItemConfigMapper.deleteById(id);
    }

    private void validateInspectionItemConfigExists(String id) {
        if (inspectionItemConfigMapper.selectById(id) == null) {
            throw exception(INSPECTION_ITEM_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public InspectionItemConfigDO getInspectionItemConfig(String id) {
        return inspectionItemConfigMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionItemConfigDO> getInspectionItemConfigPage(InspectionItemConfigPageReqVO pageReqVO) {
        return inspectionItemConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<InspectionItemConfigDO> getInspectionItemConfigList() {
        return inspectionItemConfigMapper.selectList();
    }

}
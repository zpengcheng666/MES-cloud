package com.miyu.module.qms.service.managementtree;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.qms.controller.admin.managementtree.vo.*;
import com.miyu.module.qms.dal.dataobject.managementtree.ManagementTreeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.managementtree.ManagementTreeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 质量管理关联树 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class ManagementTreeServiceImpl implements ManagementTreeService {

    @Resource
    private ManagementTreeMapper managementTreeMapper;

    @Override
    public String createManagementTree(ManagementTreeSaveReqVO createReqVO) {
        // 插入
        ManagementTreeDO managementTree = BeanUtils.toBean(createReqVO, ManagementTreeDO.class);
        // 根节点
        if(StringUtils.isBlank(managementTree.getParent())){
            managementTree.setNodeType(0);
        }
        else {
            ManagementTreeDO parent = managementTreeMapper.selectById(managementTree.getParent());
            managementTree.setNodeType(parent.getNodeType() + 1);
        }

        managementTreeMapper.insert(managementTree);
        // 返回
        return managementTree.getId();
    }

    @Override
    public void updateManagementTree(ManagementTreeSaveReqVO updateReqVO) {
        // 校验存在
        validateManagementTreeExists(updateReqVO.getId());
        // 更新
        ManagementTreeDO updateObj = BeanUtils.toBean(updateReqVO, ManagementTreeDO.class);
        managementTreeMapper.updateById(updateObj);
    }

    @Override
    public void deleteManagementTree(String id) {
        // 校验存在
        validateManagementTreeExists(id);
        // 删除
        managementTreeMapper.deleteById(id);
    }

    private void validateManagementTreeExists(String id) {
        if (managementTreeMapper.selectById(id) == null) {
            throw exception(MANAGEMENT_TREE_NOT_EXISTS);
        }
    }

    @Override
    public ManagementTreeDO getManagementTree(String id) {
        return managementTreeMapper.selectById(id);
    }

    @Override
    public PageResult<ManagementTreeDO> getManagementTreePage(ManagementTreePageReqVO pageReqVO) {
        return managementTreeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ManagementTreeDO> getManagementTreeList() {
        return managementTreeMapper.selectList();
    }

}
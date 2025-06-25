package com.miyu.module.ppm.service.material;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.ppm.controller.admin.material.vo.*;
import com.miyu.module.ppm.dal.dataobject.material.MaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.material.MaterialMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 物料基本信息 Service 实现类
 *
 * @author zhangyunfei
 */
@Service
@Validated
public class MaterialServiceImpl implements MaterialService {

    @Resource
    private MaterialMapper materialMapper;

    @Override
    public String createMaterial(MaterialSaveReqVO createReqVO) {
        // 插入
        MaterialDO material = BeanUtils.toBean(createReqVO, MaterialDO.class);
        materialMapper.insert(material);
        // 返回
        return material.getId();
    }

    @Override
    public void updateMaterial(MaterialSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialExists(updateReqVO.getId());
        // 更新
        MaterialDO updateObj = BeanUtils.toBean(updateReqVO, MaterialDO.class);
        materialMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterial(String id) {
        // 校验存在
        validateMaterialExists(id);
        // 删除
        materialMapper.deleteById(id);
    }

    private void validateMaterialExists(String id) {
        if (materialMapper.selectById(id) == null) {
            throw exception(MATERIAL_NOT_EXISTS);
        }
    }

    @Override
    public MaterialDO getMaterial(String id) {
        return materialMapper.selectById(id);
    }

    @Override
    public PageResult<MaterialDO> getMaterialPage(MaterialPageReqVO pageReqVO) {
        return materialMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaterialDO> getMaterialList() {
        return materialMapper.selectList();
    }
}
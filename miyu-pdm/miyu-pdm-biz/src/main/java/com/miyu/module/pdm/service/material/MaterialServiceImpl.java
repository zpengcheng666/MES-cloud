package com.miyu.module.pdm.service.material;

import cn.hutool.core.collection.CollUtil;
import com.miyu.module.pdm.controller.admin.material.vo.MaterialListReqVO;
import com.miyu.module.pdm.dal.dataobject.material.MaterialDO;
import com.miyu.module.pdm.dal.mysql.material.MaterialMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * PDM 工装-临时 Service 实现类
 *
 * @author Liuy
 */
@Service
@Validated
public class MaterialServiceImpl implements MaterialService {

    @Resource
    private MaterialMapper materialMapper;

    @Override
    public List<MaterialDO> getMaterialList(MaterialListReqVO listReqVO) {
        return materialMapper.selectList(listReqVO);
    }

    @Override
    public List<MaterialDO> getMaterialListByMaterialIds(Collection<String> materialIds) {
        if (CollUtil.isEmpty(materialIds)) {
            return Collections.emptyList();
        }
        return materialMapper.selectListByMaterialIds(materialIds);
    }
}

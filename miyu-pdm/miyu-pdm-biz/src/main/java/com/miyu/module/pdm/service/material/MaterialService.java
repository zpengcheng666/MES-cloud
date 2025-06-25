package com.miyu.module.pdm.service.material;

import com.miyu.module.pdm.controller.admin.material.vo.MaterialListReqVO;
import com.miyu.module.pdm.dal.dataobject.material.MaterialDO;

import java.util.Collection;
import java.util.List;

/**
 * PDM 工装-临时 Service 接口
 *
 * @author Liuy
 */
public interface MaterialService {

    /**
     * 获得工装列表
     *
     * @param listReqVO 查询条件
     * @return 工装列表
     */
    List<MaterialDO> getMaterialList(MaterialListReqVO listReqVO);

    /**
     * 获得指定工装id的工装列表
     *
     * @param materialIds 工装id数组
     * @return 工装列表
     */
    List<MaterialDO> getMaterialListByMaterialIds(Collection<String> materialIds);
}

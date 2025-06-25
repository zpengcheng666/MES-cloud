package com.miyu.module.ppm.service.material;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.material.vo.*;
import com.miyu.module.ppm.dal.dataobject.material.MaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料基本信息 Service 接口
 *
 * @author zhangyunfei
 */
public interface MaterialService {

    /**
     * 创建物料基本信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterial(@Valid MaterialSaveReqVO createReqVO);

    /**
     * 更新物料基本信息
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterial(@Valid MaterialSaveReqVO updateReqVO);

    /**
     * 删除物料基本信息
     *
     * @param id 编号
     */
    void deleteMaterial(String id);

    /**
     * 获得物料基本信息
     *
     * @param id 编号
     * @return 物料基本信息
     */
    MaterialDO getMaterial(String id);

    /**
     * 获得物料基本信息分页
     *
     * @param pageReqVO 分页查询
     * @return 物料基本信息分页
     */
    PageResult<MaterialDO> getMaterialPage(MaterialPageReqVO pageReqVO);

    /**
     * 获取物料集合
     * @return
     */
    List<MaterialDO> getMaterialList();
}
package com.miyu.module.mcc.service.materialtype;

import java.util.*;
import javax.validation.*;
import com.miyu.module.mcc.controller.admin.materialtype.vo.*;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;

/**
 * 编码类别属性表(树形结构) Service 接口
 *
 * @author 上海弥彧
 */
public interface MaterialTypeService {

    /**
     * 创建编码类别属性表(树形结构)
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaterialType(@Valid MaterialTypeSaveReqVO createReqVO);

    /**
     * 更新编码类别属性表(树形结构)
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialType(@Valid MaterialTypeSaveReqVO updateReqVO);

    /**
     * 删除编码类别属性表(树形结构)
     *
     * @param id 编号
     */
    void deleteMaterialType(String id);

    /**
     * 获得编码类别属性表(树形结构)
     *
     * @param id 编号
     * @return 编码类别属性表(树形结构)
     */
    MaterialTypeDO getMaterialType(String id);

    /**
     * 获得编码类别属性表(树形结构)列表
     *
     * @param listReqVO 查询条件
     * @return 编码类别属性表(树形结构)列表
     */
    List<MaterialTypeDO> getMaterialTypeList(MaterialListReqVO listReqVO);


    /**
     * 获得编码类别属性表(树形结构)列表，增加一级物料类型
     * @param listReqVO
     * @return
     */
    List<MaterialTypeDO> getMaterialTypeConfigList(MaterialListReqVO listReqVO);

    List<MaterialTypeDO> getMaterialTypeByCode(String code);

}

package com.miyu.module.pdm.service.structure;

import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureListReqVO;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureDO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureExcelDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据包结构 Service 接口
 *
 * @author liuy
 */
public interface StructureService {

    /**
     * 创建数据包结构
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStructure(@Valid StructureSaveReqVO createReqVO);

    /**
     * 更新数据包结构
     *
     * @param updateReqVO 更新信息
     */
    void updateStructure(@Valid StructureSaveReqVO updateReqVO);

    /**
     * 删除数据包结构
     *
     * @param id 编号
     */
    void deleteStructure(Long id);

    /**
     * 获得数据包结构
     *
     * @param id 编号
     * @return 数据包结构
     */
    StructureDO getStructure(Long id);
    /**
     * 筛选数据包结构列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 数据包结构列表
     */
    List<StructureDO> getStructureList(StructureListReqVO reqVO);
    /**
     * 获得excel规则详情列表
     *
     * @param structureId 结构id
     * @return excel规则详情列表
     */
    List<StructureExcelDO> getStructureExcelListByStructureId(Long structureId);
    /**
     * 筛选数据包结构子级列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 数据包结构子级列表
     */
    List<StructureDO> getStructureChildList(StructureListReqVO reqVO);
}

package com.miyu.module.pdm.service.structure;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureListReqVO;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureDO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureExcelDO;
import com.miyu.module.pdm.dal.mysql.structure.StructureExcelMapper;
import com.miyu.module.pdm.dal.mysql.structure.StructureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;

/**
 * 数据包结构 Service 实现类
 *
 * @author liuy
 */
@Service
@Validated
@Slf4j
public class StructureServiceImpl implements StructureService{

    @Resource
    private StructureMapper structureMapper;

    @Resource
    private StructureExcelMapper structureExcelMapper;

    @Override
    public Long createStructure(StructureSaveReqVO createReqVO) {
        // 插入
        StructureDO structure = BeanUtils.toBean(createReqVO, StructureDO.class);
        structureMapper.insert(structure);

        if(createReqVO.getFileType() != null && createReqVO.getFileType()==2) {
            // 插入excel子表
            createStructureExcelList(structure.getId(), createReqVO.getStructureExcels());
        }

        // 返回
        return structure.getId();
    }

    @Override
    public void updateStructure(StructureSaveReqVO updateReqVO) {
        // 校验存在
        validateStructureExists(updateReqVO.getId());

        // 更新
        StructureDO updateObj = BeanUtils.toBean(updateReqVO, StructureDO.class);
        structureMapper.updateById(updateObj);

        if(updateReqVO.getFileType() != null && updateReqVO.getFileType()==2) {
            // 更新excel子表
            updateStructureExcelList(updateReqVO.getId(), updateReqVO.getStructureExcels());
        }
    }

    private void updateStructureExcelList(Long structureId, List<StructureExcelDO> list) {
        deleteStructureExcelByStructureId(structureId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createStructureExcelList(structureId, list);
    }

    private void deleteStructureExcelByStructureId(Long structureId) {
        structureExcelMapper.deleteByStructureId(structureId);
    }

    private void createStructureExcelList(Long structureId, List<StructureExcelDO> list) {
        list.forEach(o -> o.setStructureId(structureId));
        structureExcelMapper.insertBatch(list);
    }

    @Override
    public void deleteStructure(Long id) {
        // 1.1 校验存在
        validateStructureExists(id);
        // 1.2 校验是否有子产品分类
        if (structureMapper.selectCountByParentId(id) > 0) {
            throw exception(STRUCTURE_EXITS_CHILDREN);
        }
        // 2. 删除
        structureMapper.deleteById(id);

        // 3. 删除关联excel详细
        structureExcelMapper.deleteByStructureId(id);
    }

    private void validateStructureExists(Long id) {
        if (structureMapper.selectById(id) == null) {
            throw exception(STRUCTURE_NOT_EXISTS);
        }
    }

    @Override
    public StructureDO getStructure(Long id) {
        return structureMapper.selectById(id);
    }
    
    @Override
    public List<StructureDO> getStructureList(StructureListReqVO reqVO) {
        List<StructureDO> list = structureMapper.selectList(reqVO);
        list.sort(Comparator.comparing(StructureDO::getCreateTime));
        return list;
    }

    @Override
    public List<StructureExcelDO> getStructureExcelListByStructureId(Long structureId) {
        return structureExcelMapper.selectListByStructureId(structureId);
    }

    @Override
    public List<StructureDO> getStructureChildList(StructureListReqVO reqVO) {
        List<StructureDO> mergedList = new ArrayList<>();
        //先处理当前节点
        List<StructureDO> list = structureMapper.selectList(reqVO);
        //递归找子级节点
        List<StructureDO> childList = new ArrayList<>();
        List<StructureDO> childListOne = structureMapper.selectChildList(reqVO);
        if(childListOne != null && childListOne.size() > 0) {
            childList.addAll(childListOne);
            for(StructureDO child: childListOne) {
                reqVO.setId(child.getId());
                List<StructureDO> childListNew = getChildList(reqVO);
                if(childListNew != null && childListNew.size() > 0) {
                    childList.addAll(childListNew);
                }
            }
            mergedList = Stream.concat(list.stream(), childList.stream())
                    .collect(Collectors.toList());
        }
        return mergedList;
    }

    public List<StructureDO> getChildList(StructureListReqVO reqVO) {
        List<StructureDO> childList = structureMapper.selectChildList(reqVO);
        if(childList == null || childList.isEmpty()) {
            return null;
        }
        return childList;
    }
}

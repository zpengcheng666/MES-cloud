package com.miyu.module.tms.service.toolgroup;

import cn.hutool.core.collection.CollUtil;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.ToolGroupDetailPageReqVO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import com.miyu.module.tms.dal.mysql.toolgroupdetail.ToolGroupDetailMapper;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.tms.controller.admin.toolgroup.vo.*;
import com.miyu.module.tms.dal.dataobject.toolgroup.ToolGroupDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolgroup.ToolGroupMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具组装 Service 实现类
 *
 * @author zhangyunfei
 */
@Service
@Validated
public class ToolGroupServiceImpl implements ToolGroupService {

    @Resource
    private ToolGroupMapper toolGroupMapper;

    @Resource
    private ToolGroupDetailMapper toolGroupDetailMapper;

    @Resource
    private MaterialConfigApi materialConfigApi;

    @Override
    @Transactional
    public String createToolGroup(ToolGroupSaveReqVO createReqVO) {
        // 验证成品刀具是否存在配置
        List<ToolGroupDO> groupList = getGroupByMainConfigId(createReqVO.getMainConfigId());
        if(groupList.size() > 0){
            throw exception(TOOL_GROUP_DUPLICATE);
        }

        // 插入
        ToolGroupDO toolGroup = BeanUtils.toBean(createReqVO, ToolGroupDO.class);
        toolGroupMapper.insert(toolGroup);
        // 保存详情
        createReqVO.setId(toolGroup.getId());
        createToolGroupDetail(createReqVO);

        return toolGroup.getId();
    }


    /**
     * 保存刀具组装详细
     * @param createReqVO
     */
    private void createToolGroupDetail(ToolGroupSaveReqVO createReqVO){
        // 刀柄集合
        List<ToolGroupDetailDO> handleList = BeanUtils.toBean(createReqVO.getHandle(), ToolGroupDetailDO.class);
        // 刀具集合
        List<ToolGroupDetailDO> toolList = BeanUtils.toBean(createReqVO.getTool(), ToolGroupDetailDO.class);
        // 配件集合
        List<ToolGroupDetailDO> accessoriesList = BeanUtils.toBean(createReqVO.getAccessories(), ToolGroupDetailDO.class);
        // 验证刀柄
        validDB(handleList);
        // 验证刀头
        validDT(toolList);
        // 验证刀片刀位
        validDW(toolList);

        List<ToolGroupDetailDO> groupList = new ArrayList<>();
        groupList.addAll(handleList);
        groupList.addAll(toolList);
        groupList.addAll(accessoriesList);

        if (CollUtil.isNotEmpty(groupList)) {
            groupList.forEach(item -> {
                item.setId(null);
                item.setToolGroupId(createReqVO.getId());
                item.setMainConfigId(createReqVO.getMainConfigId());
            });
            toolGroupDetailMapper.insertBatch(groupList);
        }
    }
    /**
     * 验证刀柄
     */
    private void validDB(List<ToolGroupDetailDO> handleList){
        // 只能选择一个刀柄
        if(handleList.size() > 1){
            throw exception(GROUP_TOOL_DB_COUNT_ERROR);
        }
    }

    /**
     * 验证刀头
     */
    private void validDT(List<ToolGroupDetailDO> toolList){
        // 验证刀头只能选一个
        List<ToolGroupDetailDO> dtList = toolList.stream().filter(item -> "DT".equals(item.getMaterialCode())).collect(Collectors.toList());
        // 必须选择一个刀头
        if(dtList.size() == 0){
            throw exception(GROUP_TOOL_DT_NOT_EXISTS);
        }
        // 只能选择一个刀头
        if(dtList.size() > 1){
            throw exception(GROUP_TOOL_DT_COUNT_ERROR);
        }
    }

    /**
     * 验证刀片刀位
     * @param toolList
     */
    private void validDW(List<ToolGroupDetailDO> toolList){
        List<ToolGroupDetailDO> toolDPList = toolList.stream().filter(item -> !"DT".equals(item.getMaterialCode())).collect(Collectors.toList());
        // 刀位去重
        List<Integer> count = toolDPList.stream().map(ToolGroupDetailDO::getSite).distinct().collect(Collectors.toList());
        // 刀位有重复
        if(toolDPList.size() > count.size()){
            throw exception(GROUP_TOOL_SITE_DUPLICATE);
        }
    }

    @Override
    @Transactional
    public void updateToolGroup(ToolGroupSaveReqVO updateReqVO) {
        // 校验存在
        validateToolGroupExists(updateReqVO.getId());
        // 验证成品刀具是否存在配置
        List<ToolGroupDO> groupList = getGroupByMainConfigId(updateReqVO.getMainConfigId());
        // 过滤出非当前组装的集合
        List<ToolGroupDO> validList = groupList.stream().filter(item -> !item.getId().equals(updateReqVO.getId())).collect(Collectors.toList());
        if(validList.size() > 0){
            throw exception(TOOL_GROUP_DUPLICATE);
        }
        // 更新
        ToolGroupDO updateObj = BeanUtils.toBean(updateReqVO, ToolGroupDO.class);
        toolGroupMapper.updateById(updateObj);

        // 刀具适配集合
        List<ToolGroupDetailDO> deleteDetailList = toolGroupDetailMapper.selectList(ToolGroupDetailDO::getToolGroupId, updateReqVO.getId());
        if (deleteDetailList.size() > 0) {
            toolGroupDetailMapper.deleteBatchIds(deleteDetailList);
        }

        // 保存详情
        createToolGroupDetail(updateReqVO);
    }

    @Override
    @Transactional
    public void deleteToolGroup(String id) {
        // 校验存在
        validateToolGroupExists(id);
        // 删除
        toolGroupMapper.deleteById(id);

        List<ToolGroupDetailDO> list = toolGroupDetailMapper.selectList(ToolGroupDetailDO::getToolGroupId, id);
        Collection<String> detailIds = list.stream().map(ToolGroupDetailDO::getId).collect(Collectors.toList());
        if (detailIds.size() > 0) {
            toolGroupDetailMapper.deleteBatchIds(detailIds);
        }

    }

    private void validateToolGroupExists(String id) {
        if (toolGroupMapper.selectById(id) == null) {
            throw exception(TOOL_GROUP_NOT_EXISTS);
        }
    }

    @Override
    public ToolGroupDO getToolGroup(String id) {
        return toolGroupMapper.selectById(id);
    }

    @Override
    public PageResult<ToolGroupDO> getToolGroupPage(ToolGroupPageReqVO pageReqVO) {
        return toolGroupMapper.selectPage(pageReqVO);
    }


    @Override
    public List<ToolGroupDetailDO> getToolGroupDetailList(ToolGroupDetailPageReqVO pageReqVO) {
        List<ToolGroupDetailDO> groupDOS = toolGroupDetailMapper.selectToolGroupDetailList(pageReqVO);
        Set<String> collect = groupDOS.stream().map(ToolGroupDetailDO::getAccessoryConfigId).collect(Collectors.toSet());
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(collect);
        if(materialConfigMap != null)groupDOS.forEach(groupDO -> {
            if(materialConfigMap.containsKey(groupDO.getAccessoryConfigId())){
                MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(groupDO.getAccessoryConfigId());
                groupDO.setMaterialNumber(materialConfigRespDTO.getMaterialNumber());
                groupDO.setMaterialCode(materialConfigRespDTO.getMaterialCode());
                groupDO.setMaterialName(materialConfigRespDTO.getMaterialName());
            }});
        return groupDOS;
    }

    @Override
    public ToolGroupDO getToolGroupById(String id) {
        return toolGroupMapper.getToolGroupById(id);
    }

    /**
     * 成品刀具类型id查询刀具组装
     * @param mainConfigId
     * @return
     */
    @Override
    public List<ToolGroupDO> getGroupByMainConfigId(String mainConfigId) {
        return toolGroupMapper.selectList(ToolGroupDO::getMainConfigId, mainConfigId);
    }
}

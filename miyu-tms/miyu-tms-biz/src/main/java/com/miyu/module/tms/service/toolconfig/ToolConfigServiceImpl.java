package com.miyu.module.tms.service.toolconfig;

import cn.hutool.core.collection.CollUtil;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import com.miyu.module.tms.dal.mysql.fitconfig.FitConfigMapper;
import com.miyu.module.tms.dal.mysql.toolconfigparameter.ToolConfigParameterMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.tms.controller.admin.toolconfig.vo.*;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolconfig.ToolConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具类型 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class ToolConfigServiceImpl implements ToolConfigService {

    @Resource
    private ToolConfigMapper toolConfigMapper;

    @Resource
    private FitConfigMapper fitConfigMapper;

    @Resource
    private ToolConfigParameterMapper toolConfigParameterMapper;

    @Override
    public String createToolConfig(ToolConfigSaveReqVO createReqVO) {
        // 插入
        ToolConfigDO toolConfig = BeanUtils.toBean(createReqVO, ToolConfigDO.class);
        toolConfigMapper.insert(toolConfig);
        // 返回
        return toolConfig.getId();
    }

    @Override
    @Transactional
    public void updateToolConfig(ToolConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateToolConfigExists(updateReqVO.getId());
        // 更新
        ToolConfigDO updateObj = BeanUtils.toBean(updateReqVO, ToolConfigDO.class);

        // 处理规格
        StringBuilder sbf = new StringBuilder();

        //  几何参数
        List<ToolConfigParameterDO> geoParamList = BeanUtils.toBean(updateReqVO.getGeoParamList(), ToolConfigParameterDO.class);
        geoParamList.sort((a,b)->{return a.getSort().compareTo(b.getSort());});
        for (ToolConfigParameterDO geoParamDto : geoParamList) {
            geoParamDto.setToolConfigId(updateObj.getId());
            sbf.append(geoParamDto.getValue()).append("*");
        }

        // 切削参数
        List<ToolConfigParameterDO> cutParamList = BeanUtils.toBean(updateReqVO.getCutParamList(), ToolConfigParameterDO.class);
        cutParamList.sort((a,b)->{return a.getSort().compareTo(b.getSort());});
        for (ToolConfigParameterDO cutParamDto : cutParamList) {
            cutParamDto.setToolConfigId(updateObj.getId());
            sbf.append(cutParamDto.getValue()).append("*");
        }

        String spec = sbf.toString();
        if(StringUtils.isNotEmpty(spec)){
            updateObj.setSpec(spec.substring(0, spec.length() - 1));
        }

        List<ToolConfigParameterDO> toolConfigParameterDOList = new ArrayList<>();
        toolConfigParameterDOList.addAll(geoParamList);
        toolConfigParameterDOList.addAll(cutParamList);

        // 删除原参数关系
        List<ToolConfigParameterDO> deleteList = toolConfigParameterMapper.selectList(ToolConfigParameterDO::getToolConfigId, updateObj.getId());
        if(deleteList.size() > 0){
            toolConfigParameterMapper.deleteBatchIds(deleteList);
        }

        // 新增参数关系
        if(toolConfigParameterDOList.size() > 0){
            toolConfigParameterMapper.insertBatch(toolConfigParameterDOList);
        }

        // 刀具适配集合
        List<FitConfigDO> deleteFitConfigList = fitConfigMapper.selectList(FitConfigDO::getToolConfigId, updateReqVO.getId());
        if (deleteFitConfigList.size() > 0) {
            fitConfigMapper.deleteBatchIds(deleteFitConfigList);
        }
        List<FitConfigDO> fitConfigList = updateReqVO.getFitConfigList();
        if (CollUtil.isNotEmpty(fitConfigList)) {
            fitConfigList.forEach(item -> {
                item.setId(null);
                // item.setTemplateId(updateReqVO.getId());
                item.setToolConfigId(updateReqVO.getId());
            });
            fitConfigMapper.insertBatch(fitConfigList);
        }

        toolConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteToolConfig(String id) {
        // 校验存在
        validateToolConfigExists(id);
        // 删除
        toolConfigMapper.deleteById(id);

        // 删除原参数关系
        List<ToolConfigParameterDO> deleteList = toolConfigParameterMapper.selectList(ToolConfigParameterDO::getToolConfigId, id);
        if(deleteList.size() > 0){
            toolConfigParameterMapper.deleteBatchIds(deleteList);
        }

        // 删除适配关系
        List<FitConfigDO> deleteFitConfigList = fitConfigMapper.selectList(FitConfigDO::getToolConfigId, id);
        if (deleteFitConfigList.size() > 0) {
            fitConfigMapper.deleteBatchIds(deleteFitConfigList);
        }

    }

    private void validateToolConfigExists(String id) {
        if (toolConfigMapper.selectById(id) == null) {
            throw exception(TOOL_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public ToolConfigDO getToolConfig(String id) {
        return toolConfigMapper.selectById(id);
    }

    @Override
    public PageResult<ToolConfigDO> getToolConfigPage(ToolConfigPageReqVO pageReqVO) {
        return toolConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public ToolConfigDO getToolConfigByMaterialConfigId(String materialConfigId) {
        return toolConfigMapper.selectOne(ToolConfigDO::getMaterialConfigId, materialConfigId);
    }

    @Override
    public PageResult<ToolConfigDO> getFitToolConfigPageByType(ToolConfigPageReqVO pageReqVO) {
        return toolConfigMapper.getFitToolConfigPageByType(pageReqVO);
    }

    @Override
    public PageResult<ToolConfigDO> getToolConfigPageByType(ToolConfigPageReqVO pageReqVO) {

        if(StringUtils.isNotBlank(pageReqVO.getIds()) && pageReqVO.getQueryType() != null){
            // 查询适配关系
            List<String> ids = Arrays.asList(pageReqVO.getIds().split(","));
            // 刀具匹配的刀柄
            List<FitConfigDO> fitConfigForList = fitConfigMapper.selectList(FitConfigDO::getToolConfigId, ids);
            List<String> fitConfigIds = fitConfigForList.stream().map(item -> item.getFitToolConfigId()).collect(Collectors.toList());
            // 刀柄匹配的刀具
            List<FitConfigDO> fitConfigRevList = fitConfigMapper.selectList(FitConfigDO::getFitToolConfigId, ids);
            List<String> fitConfigRevIds = fitConfigRevList.stream().map(item -> item.getToolConfigId()).collect(Collectors.toList());

            fitConfigIds.addAll(fitConfigRevIds);
            fitConfigIds = fitConfigIds.stream().distinct().collect(Collectors.toList());;
            pageReqVO.setFitConfigIds(fitConfigIds);
        }

        PageResult<ToolConfigDO> result = toolConfigMapper.getToolConfigPageByType(pageReqVO);
        if(result.getTotal() == 0){
            pageReqVO.setFitConfigIds(null);
            return toolConfigMapper.getToolConfigPageByType(pageReqVO);
        }
        return result;
    }

}

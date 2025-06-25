package com.miyu.module.tms.service.toolparamtemplate;

import cn.hutool.core.collection.CollUtil;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import com.miyu.module.tms.dal.mysql.toolparamtemplatedetail.ToolParamTemplateDetailMapper;
import io.seata.common.util.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.tms.controller.admin.toolparamtemplate.vo.*;
import com.miyu.module.tms.dal.dataobject.toolparamtemplate.ToolParamTemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolparamtemplate.ToolParamTemplateMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具参数模板 Service 实现类
 *
 * @author zhangyunfei
 */
@Service
@Validated
public class ToolParamTemplateServiceImpl implements ToolParamTemplateService {

    @Resource
    private ToolParamTemplateMapper toolParamTemplateMapper;

    @Resource
    private ToolParamTemplateDetailMapper toolParamTemplateDetailMapper;

    @Override
    @Transactional
    public String createToolParamTemplate(ToolParamTemplateSaveReqVO createReqVO) {
        // 验证物料类别不能重复添加模板
        validateToolParamTemplateDuplicate(createReqVO);
        // 插入
        ToolParamTemplateDO toolParamTemplate = BeanUtils.toBean(createReqVO, ToolParamTemplateDO.class);
        toolParamTemplateMapper.insert(toolParamTemplate);

        List<ToolParamTemplateDetailDO> detaiList = createReqVO.getDetailList();
        if (CollUtil.isNotEmpty(detaiList)) {
            detaiList.forEach(item -> item.setParamTemplateId(toolParamTemplate.getId()));
            toolParamTemplateDetailMapper.insertBatch(detaiList);
        }

        // 返回
        return toolParamTemplate.getId();
    }

    @Override
    @Transactional
    public void updateToolParamTemplate(ToolParamTemplateSaveReqVO updateReqVO) {
        // 校验存在
        validateToolParamTemplateExists(updateReqVO.getId());
        // 验证物料类别不能重复添加模板
        validateToolParamTemplateDuplicate(updateReqVO);

        // 删除从表原有数据
        List<ToolParamTemplateDetailDO> deleteDetailList = toolParamTemplateDetailMapper.selectList(ToolParamTemplateDetailDO::getParamTemplateId, updateReqVO.getId());
        if (deleteDetailList.size() > 0) {
            toolParamTemplateDetailMapper.deleteBatchIds(deleteDetailList);
        }
        List<ToolParamTemplateDetailDO> detaiList = updateReqVO.getDetailList();
        if (CollUtil.isNotEmpty(detaiList)) {
            detaiList.forEach(item -> {
                item.setId(null);
                item.setParamTemplateId(updateReqVO.getId());
            });
            toolParamTemplateDetailMapper.insertBatch(detaiList);
        }

        // 更新
        ToolParamTemplateDO updateObj = BeanUtils.toBean(updateReqVO, ToolParamTemplateDO.class);
        updateObj.setVersion(updateObj.getVersion() + 1);
        toolParamTemplateMapper.updateById(updateObj);
    }

    @Override
    public void deleteToolParamTemplate(String id) {
        // 校验存在
        validateToolParamTemplateExists(id);
        // 删除
        toolParamTemplateMapper.deleteById(id);
        // 删除从表
        List<ToolParamTemplateDetailDO> list = toolParamTemplateDetailMapper.selectList(ToolParamTemplateDetailDO::getParamTemplateId, id);
        Collection<String> collect = list.stream().map(ToolParamTemplateDetailDO::getId).collect(Collectors.toList());
        if (collect.size() > 0) {
            toolParamTemplateDetailMapper.deleteBatchIds(collect);
        }
    }

    private void validateToolParamTemplateExists(String id) {
        if (toolParamTemplateMapper.selectById(id) == null) {
            throw exception(TOOL_PARAM_TEMPLATE_NOT_EXISTS);
        }
    }

    /**
     * 验证一个物料类别只能添加一个模板
     * @param reqVO
     */
    private void validateToolParamTemplateDuplicate(ToolParamTemplateSaveReqVO reqVO) {
        // 验证物料类别不能重复添加模板
        List<ToolParamTemplateDO> list = toolParamTemplateMapper.selectList(ToolParamTemplateDO::getMaterialTypeId, reqVO.getMaterialTypeId());
        // 修改
        if (StringUtils.isNotBlank(reqVO.getId())) {
            if(list.size() > 1){
                throw exception(TOOL_PARAM_TEMPLATE_DUPLICATE);
            }
        }
        else {
            if (list.size() > 0) {
                throw exception(TOOL_PARAM_TEMPLATE_DUPLICATE);
            }
        }
    }

    @Override
    public ToolParamTemplateDO getToolParamTemplate(String id) {
        ToolParamTemplateDO paramTemplateDO = toolParamTemplateMapper.selectById(id);
        List<ToolParamTemplateDetailDO> list = toolParamTemplateDetailMapper.selectList(ToolParamTemplateDetailDO::getParamTemplateId, id);
        paramTemplateDO.setDetailList(list);
        return paramTemplateDO;
    }

    @Override
    public PageResult<ToolParamTemplateDO> getToolParamTemplatePage(ToolParamTemplatePageReqVO pageReqVO) {
        return toolParamTemplateMapper.selectPage(pageReqVO);
    }

    /**
     * 物料类别ID获得刀具参数模板
     * @param materialTypeId
     * @return
     */
    @Override
    public List<ToolParamTemplateDetailDO> getToolParamTemplateByMaterialTypeId(String materialTypeId) {
        // 查询模板
        List<ToolParamTemplateDO> templateList = toolParamTemplateMapper.selectList(ToolParamTemplateDO::getMaterialTypeId, materialTypeId);

        List<ToolParamTemplateDetailDO> detailList = new ArrayList<>();

        if (CollUtil.isNotEmpty(templateList)) {
            // 查询模板详情
            detailList = toolParamTemplateDetailMapper.selectList(ToolParamTemplateDetailDO::getParamTemplateId, templateList.get(0).getId());
        }

        return detailList;
    }

}

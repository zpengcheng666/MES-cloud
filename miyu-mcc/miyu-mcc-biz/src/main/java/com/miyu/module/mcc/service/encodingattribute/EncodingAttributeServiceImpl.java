package com.miyu.module.mcc.service.encodingattribute;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.mcc.controller.admin.encodingattribute.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingattribute.EncodingAttributeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.encodingattribute.EncodingAttributeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;

/**
 * 编码自定义属性 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class EncodingAttributeServiceImpl implements EncodingAttributeService {

    @Resource
    private EncodingAttributeMapper encodingAttributeMapper;

    @Override
    public String createEncodingAttribute(EncodingAttributeSaveReqVO createReqVO) {
        // 插入
        EncodingAttributeDO encodingAttribute = BeanUtils.toBean(createReqVO, EncodingAttributeDO.class);
        encodingAttributeMapper.insert(encodingAttribute);
        // 返回
        return encodingAttribute.getId();
    }

    @Override
    public void updateEncodingAttribute(EncodingAttributeSaveReqVO updateReqVO) {
        // 校验存在
        validateEncodingAttributeExists(updateReqVO.getId());
        // 更新
        EncodingAttributeDO updateObj = BeanUtils.toBean(updateReqVO, EncodingAttributeDO.class);
        encodingAttributeMapper.updateById(updateObj);
    }

    @Override
    public void deleteEncodingAttribute(String id) {
        // 校验存在
        validateEncodingAttributeExists(id);
        // 删除
        encodingAttributeMapper.deleteById(id);
    }

    private void validateEncodingAttributeExists(String id) {
        if (encodingAttributeMapper.selectById(id) == null) {
            throw exception(ENCODING_ATTRIBUTE_NOT_EXISTS);
        }
    }

    @Override
    public EncodingAttributeDO getEncodingAttribute(String id) {
        return encodingAttributeMapper.selectById(id);
    }

    @Override
    public PageResult<EncodingAttributeDO> getEncodingAttributePage(EncodingAttributePageReqVO pageReqVO) {
        return encodingAttributeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<EncodingAttributeDO> getEncodingAttributeList() {
        return encodingAttributeMapper.selectList();
    }

}
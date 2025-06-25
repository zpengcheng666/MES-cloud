package com.miyu.module.mcc.service.encodingclassification;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.mcc.controller.admin.encodingclassification.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingclassification.EncodingClassificationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.encodingclassification.EncodingClassificationMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;

/**
 * 编码分类 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class EncodingClassificationServiceImpl implements EncodingClassificationService {

    @Resource
    private EncodingClassificationMapper encodingClassificationMapper;

    @Override
    public String createEncodingClassification(EncodingClassificationSaveReqVO createReqVO) {
        // 插入
        EncodingClassificationDO encodingClassification = BeanUtils.toBean(createReqVO, EncodingClassificationDO.class);
        encodingClassificationMapper.insert(encodingClassification);
        // 返回
        return encodingClassification.getId();
    }

    @Override
    public void updateEncodingClassification(EncodingClassificationSaveReqVO updateReqVO) {
        // 校验存在
        validateEncodingClassificationExists(updateReqVO.getId());
        // 更新
        EncodingClassificationDO updateObj = BeanUtils.toBean(updateReqVO, EncodingClassificationDO.class);
        encodingClassificationMapper.updateById(updateObj);
    }

    @Override
    public void deleteEncodingClassification(String id) {
        // 校验存在
        validateEncodingClassificationExists(id);
        // 删除
        encodingClassificationMapper.deleteById(id);
    }

    private void validateEncodingClassificationExists(String id) {
        if (encodingClassificationMapper.selectById(id) == null) {
            throw exception(ENCODING_CLASSIFICATION_NOT_EXISTS);
        }
    }

    @Override
    public EncodingClassificationDO getEncodingClassification(String id) {
        return encodingClassificationMapper.selectById(id);
    }

    @Override
    public PageResult<EncodingClassificationDO> getEncodingClassificationPage(EncodingClassificationPageReqVO pageReqVO) {
        return encodingClassificationMapper.selectPage(pageReqVO);
    }

    @Override
    public List<EncodingClassificationDO> getEncodingClassificationList() {
        return encodingClassificationMapper.selectList();
    }

}
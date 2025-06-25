package com.miyu.module.mcc.service.encodingruledetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.mcc.controller.admin.encodingruledetail.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.mcc.dal.mysql.encodingruledetail.EncodingRuleDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.*;

/**
 * 编码规则配置详情 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class EncodingRuleDetailServiceImpl implements EncodingRuleDetailService {

    @Resource
    private EncodingRuleDetailMapper encodingRuleDetailMapper;

    @Override
    public String createEncodingRuleDetail(EncodingRuleDetailSaveReqVO createReqVO) {
        // 插入
        EncodingRuleDetailDO encodingRuleDetail = BeanUtils.toBean(createReqVO, EncodingRuleDetailDO.class);
        encodingRuleDetailMapper.insert(encodingRuleDetail);
        // 返回
        return encodingRuleDetail.getId();
    }

    @Override
    public void updateEncodingRuleDetail(EncodingRuleDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateEncodingRuleDetailExists(updateReqVO.getId());
        // 更新
        EncodingRuleDetailDO updateObj = BeanUtils.toBean(updateReqVO, EncodingRuleDetailDO.class);
        encodingRuleDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteEncodingRuleDetail(String id) {
        // 校验存在
        validateEncodingRuleDetailExists(id);
        // 删除
        encodingRuleDetailMapper.deleteById(id);
    }

    private void validateEncodingRuleDetailExists(String id) {
        if (encodingRuleDetailMapper.selectById(id) == null) {
            throw exception(ENCODING_RULE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public EncodingRuleDetailDO getEncodingRuleDetail(String id) {
        return encodingRuleDetailMapper.selectById(id);
    }

    @Override
    public PageResult<EncodingRuleDetailDO> getEncodingRuleDetailPage(EncodingRuleDetailPageReqVO pageReqVO) {
        return encodingRuleDetailMapper.selectPage(pageReqVO);
    }

}
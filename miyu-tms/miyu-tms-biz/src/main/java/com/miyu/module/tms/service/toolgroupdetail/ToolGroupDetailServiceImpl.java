package com.miyu.module.tms.service.toolgroupdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.*;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.tms.dal.mysql.toolgroupdetail.ToolGroupDetailMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具组装 Service 实现类
 *
 * @author zhangyunfei
 */
@Service
@Validated
public class ToolGroupDetailServiceImpl implements ToolGroupDetailService {

    @Resource
    private ToolGroupDetailMapper toolGroupDetailMapper;

    @Override
    public String createToolGroupDetail(ToolGroupDetailSaveReqVO createReqVO) {
        // 插入
        ToolGroupDetailDO toolGroupDetail = BeanUtils.toBean(createReqVO, ToolGroupDetailDO.class);
        toolGroupDetailMapper.insert(toolGroupDetail);
        // 返回
        return toolGroupDetail.getId();
    }

    @Override
    public void updateToolGroupDetail(ToolGroupDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateToolGroupDetailExists(updateReqVO.getId());
        // 更新
        ToolGroupDetailDO updateObj = BeanUtils.toBean(updateReqVO, ToolGroupDetailDO.class);
        toolGroupDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteToolGroupDetail(String id) {
        // 校验存在
        validateToolGroupDetailExists(id);
        // 删除
        toolGroupDetailMapper.deleteById(id);
    }

    private void validateToolGroupDetailExists(String id) {
        if (toolGroupDetailMapper.selectById(id) == null) {
            throw exception(TOOL_GROUP_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ToolGroupDetailDO getToolGroupDetail(String id) {
        return toolGroupDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ToolGroupDetailDO> getToolGroupDetailPage(ToolGroupDetailPageReqVO pageReqVO) {
        return toolGroupDetailMapper.selectPage(pageReqVO);
    }

    /**
     * 刀具组装id获取刀具组装详情
     * @param groupId
     * @return
     */
    @Override
    public List<ToolGroupDetailDO> getToolGroupDetailListByGroupId(String groupId) {
        return toolGroupDetailMapper.getToolGroupDetailListByGroupId(groupId);
    }

}

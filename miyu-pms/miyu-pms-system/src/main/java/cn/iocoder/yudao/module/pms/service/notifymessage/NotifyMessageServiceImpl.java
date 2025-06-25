package cn.iocoder.yudao.module.pms.service.notifymessage;

import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessagePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.notifymessage.NotifyMessageDO;
import cn.iocoder.yudao.module.pms.dal.mysql.notifymessage.NotifyMessageMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.NOTIFY_MESSAGE_NOT_EXISTS;


/**
 * 简易版,项目计划提醒用 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class NotifyMessageServiceImpl implements NotifyMessageService {

    @Resource
    private NotifyMessageMapper notifyMessageMapper;

    @Override
    public Long createNotifyMessage(NotifyMessageSaveReqVO createReqVO) {
        // 插入
        NotifyMessageDO notifyMessage = BeanUtils.toBean(createReqVO, NotifyMessageDO.class);
        notifyMessageMapper.insert(notifyMessage);
        // 返回
        return notifyMessage.getId();
    }

    @Override
    public void updateNotifyMessage(NotifyMessageSaveReqVO updateReqVO) {
        // 校验存在
        validateNotifyMessageExists(updateReqVO.getId());
        // 更新
        NotifyMessageDO updateObj = BeanUtils.toBean(updateReqVO, NotifyMessageDO.class);
        notifyMessageMapper.updateById(updateObj);
    }

    @Override
    public void deleteNotifyMessage(Long id) {
        // 校验存在
        validateNotifyMessageExists(id);
        // 删除
        notifyMessageMapper.deleteById(id);
    }

    private void validateNotifyMessageExists(Long id) {
        if (notifyMessageMapper.selectById(id) == null) {
            throw exception(NOTIFY_MESSAGE_NOT_EXISTS);
        }
    }

    @Override
    public NotifyMessageDO getNotifyMessage(Long id) {
        return notifyMessageMapper.selectById(id);
    }

    @Override
    public PageResult<NotifyMessageDO> getNotifyMessagePage(NotifyMessagePageReqVO pageReqVO) {
//        return notifyMessageMapper.selectPage(pageReqVO);
        return notifyMessageMapper.selectPage2(pageReqVO);
    }

    @Override
    public List<NotifyMessageDO> selectListByEntity(NotifyMessageReqVO req) {
        return notifyMessageMapper.selectListByEntity(req);
    }

}

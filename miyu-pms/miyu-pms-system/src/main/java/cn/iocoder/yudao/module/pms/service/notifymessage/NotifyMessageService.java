package cn.iocoder.yudao.module.pms.service.notifymessage;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessagePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.notifymessage.NotifyMessageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 简易版,项目计划提醒用 Service 接口
 *
 * @author 上海弥彧
 */
public interface NotifyMessageService {

    /**
     * 创建简易版,项目计划提醒用
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createNotifyMessage(@Valid NotifyMessageSaveReqVO createReqVO);

    /**
     * 更新简易版,项目计划提醒用
     *
     * @param updateReqVO 更新信息
     */
    void updateNotifyMessage(@Valid NotifyMessageSaveReqVO updateReqVO);

    /**
     * 删除简易版,项目计划提醒用
     *
     * @param id 编号
     */
    void deleteNotifyMessage(Long id);

    /**
     * 获得简易版,项目计划提醒用
     *
     * @param id 编号
     * @return 简易版,项目计划提醒用
     */
    NotifyMessageDO getNotifyMessage(Long id);

    /**
     * 获得简易版,项目计划提醒用分页
     *
     * @param pageReqVO 分页查询
     * @return 简易版,项目计划提醒用分页
     */
    PageResult<NotifyMessageDO> getNotifyMessagePage(NotifyMessagePageReqVO pageReqVO);

    //查询
    List<NotifyMessageDO> selectListByEntity(NotifyMessageReqVO req);

}

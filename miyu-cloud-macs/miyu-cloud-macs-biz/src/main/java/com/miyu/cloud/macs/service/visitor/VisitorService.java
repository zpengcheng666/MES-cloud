package com.miyu.cloud.macs.service.visitor;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.controller.admin.visitor.vo.*;
import com.miyu.cloud.macs.controller.admin.visitorRegion.vo.VisitorRegionPageReqVO;
import com.miyu.cloud.macs.dal.dataobject.visitor.VisitorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.macs.restServer.entity.MacsRestUser;

/**
 * 申请角色 Service 接口
 *
 * @author 芋道源码
 */
public interface VisitorService {

    /**
     * 创建申请角色
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createVisitor(@Valid VisitorSaveReqVO createReqVO);

    /**
     * 更新申请角色
     *
     * @param updateReqVO 更新信息
     */
    void updateVisitor(@Valid VisitorSaveReqVO updateReqVO);

    /**
     * 删除申请角色
     *
     * @param id 编号
     */
    void deleteVisitor(String id);

    /**
     * 获得申请角色
     *
     * @param id 编号
     * @return 申请角色
     */
    VisitorDO getVisitor(String id);

    /**
     * 获得申请角色分页
     *
     * @param pageReqVO 分页查询
     * @return 申请角色分页
     */
    PageResult<VisitorDO> getVisitorPage(VisitorPageReqVO pageReqVO);

    void update(UpdateWrapper<VisitorDO> wrapper);

//    VisitorDO getVisitorByCharacter(MacsRestUser restUser);

    VisitorDO getVisitorByCode(String code);

    VisitorDO getVisitorByFacialFeatureString(String facialFeatureString);

    VisitorDO getVisitorByFingerprintString(String fingerprintString);

    PageResult<VisitorDO> getPageByApplicationId(VisitorRegionPageReqVO pageReqVO);

    void visitorDeparture(VisitorSaveReqVO updateReqVO);

    List<VisitorDO> list();

    List<VisitorDO> list(Wrapper<VisitorDO> queryWrapper);
}

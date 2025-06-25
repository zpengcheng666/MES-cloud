package com.miyu.module.tms.service.fitconfig;

import javax.validation.*;
import com.miyu.module.tms.controller.admin.fitconfig.vo.*;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 刀具适配 Service 接口
 *
 * @author zhangyunfei
 */
public interface FitConfigService {

    /**
     * 创建刀具适配
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createFitConfig(@Valid FitConfigSaveReqVO createReqVO);

    /**
     * 更新刀具适配
     *
     * @param updateReqVO 更新信息
     */
    void updateFitConfig(@Valid FitConfigSaveReqVO updateReqVO);

    /**
     * 删除刀具适配
     *
     * @param id 编号
     */
    void deleteFitConfig(String id);

    /**
     * 获得刀具适配
     *
     * @param id 编号
     * @return 刀具适配
     */
    FitConfigDO getFitConfig(String id);

    /**
     * 获得刀具适配分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具适配分页
     */
    PageResult<FitConfigDO> getFitConfigPage(FitConfigPageReqVO pageReqVO);

    /**
     * 通过模板id和刀具类型id获取适配关系集合
     * @param reqVO
     * @return
     */
    // List<FitConfigDO> getTemplateFitConfigList(FitConfigReqVO reqVO);
}

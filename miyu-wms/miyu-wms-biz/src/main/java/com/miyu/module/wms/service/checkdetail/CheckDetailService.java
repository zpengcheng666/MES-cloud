package com.miyu.module.wms.service.checkdetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.wms.controller.admin.checkdetail.vo.*;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 库存盘点详情 Service 接口
 *
 * @author QianJy
 */
public interface CheckDetailService {

    /**
     * 创建库存盘点详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCheckDetail(@Valid CheckDetailSaveReqVO createReqVO);

    List<CheckDetailDO> createCheckDetailByCheckContainerId(String checkContainerId,String containerStockId);


    /**
     * 更新库存盘点详情
     *
     * @param updateReqVO 更新信息
     */
    void updateCheckDetail(@Valid CheckDetailSaveReqVO updateReqVO);

    /**
     * 删除库存盘点详情
     *
     * @param id 编号
     */
    void deleteCheckDetail(String id);

    /**
     * 获得库存盘点详情
     *
     * @param id 编号
     * @return 库存盘点详情
     */
    CheckDetailDO getCheckDetail(String id);

    /**
     * 获得库存盘点详情分页
     *
     * @param pageReqVO 分页查询
     * @return 库存盘点详情分页
     */
    PageResult<CheckDetailDO> getCheckDetailPage(CheckDetailPageReqVO pageReqVO);

    List<CheckDetailDO> getCheckDetailByCheckContainerIds(Collection<String> checkContainerIds);

    /**
     * 保存盘点计划明细  保存盘点数量
     * @param checkDetailList
     * @return
     */
    Boolean saveCheckDetailItem(List<CheckDetailDO> checkDetailList);

    /**
     * 提交盘点任务 更新库存数量
     * @param checkDetailList
     * @return
     */
    void submitCheckPlanItem(List<CheckDetailDO> checkDetailList);
}
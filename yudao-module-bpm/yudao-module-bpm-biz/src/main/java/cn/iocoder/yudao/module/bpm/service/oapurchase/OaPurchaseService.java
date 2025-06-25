package cn.iocoder.yudao.module.bpm.service.oapurchase;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oapurchase.OaPurchaseListDO;

import javax.validation.Valid;
import java.util.List;

/**
 * OA 采购申请 Service 接口
 *
 * @author 芋道源码
 */
public interface OaPurchaseService {

    /**
     * 创建OA 采购申请
     * @param userId 用户编号
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOaPurchase(Long userId, @Valid OaPurchaseSaveReqVO createReqVO);

    /**
     * 更新OA 采购申请
     *
     * @param updateReqVO 更新信息
     */
    void updateOaPurchase(@Valid OaPurchaseSaveReqVO updateReqVO);

    /**
     * 删除OA 采购申请
     *
     * @param id 编号
     */
    void deleteOaPurchase(Long id);

    /**
     * 获得OA 采购申请
     *
     * @param id 编号
     * @return OA 采购申请
     */
    OaPurchaseDO getOaPurchase(Long id);

    /**
     * 获得OA 采购申请分页
     *
     * @param pageReqVO 分页查询
     * @return OA 采购申请分页
     */
    PageResult<OaPurchaseDO> getOaPurchasePage(OaPurchasePageReqVO pageReqVO);

    // ==================== 子表（OA 采购申请） ====================

    /**
     * 获得OA 采购申请列表
     *
     * @param purchaseId 采购父表id
     * @return OA 采购申请列表
     */
    List<OaPurchaseListDO> getOaPurchaseListListByPurchaseId(Long purchaseId);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateLeaveStatus(Long id, Integer status);

}

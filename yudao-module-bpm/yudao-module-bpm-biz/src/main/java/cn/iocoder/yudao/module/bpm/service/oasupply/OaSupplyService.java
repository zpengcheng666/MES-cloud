package cn.iocoder.yudao.module.bpm.service.oasupply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyDO;
import cn.iocoder.yudao.module.bpm.dal.dataobject.oasupply.OaSupplyListDO;

import javax.validation.Valid;
import java.util.List;

/**
 * OA 物品领用 Service 接口
 *
 * @author 芋道源码
 */
public interface OaSupplyService {

    /**
     * 创建OA 物品领用
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOaSupply(Long userId, @Valid OaSupplySaveReqVO createReqVO);

    /**
     * 更新OA 物品领用
     *
     * @param updateReqVO 更新信息
     */
    void updateOaSupply(@Valid OaSupplySaveReqVO updateReqVO);

    /**
     * 删除OA 物品领用
     *
     * @param id 编号
     */
    void deleteOaSupply(Long id);

    /**
     * 获得OA 物品领用
     *
     * @param id 编号
     * @return OA 物品领用
     */
    OaSupplyDO getOaSupply(Long id);

    /**
     * 获得OA 物品领用分页
     *
     * @param pageReqVO 分页查询
     * @return OA 物品领用分页
     */
    PageResult<OaSupplyDO> getOaSupplyPage(OaSupplyPageReqVO pageReqVO);

    // ==================== 子表（OA 物品领用表-物品清单） ====================

    /**
     * 获得OA 物品领用表-物品清单列表
     *
     * @param supplyId 采购父表id
     * @return OA 物品领用表-物品清单列表
     */
    List<OaSupplyListDO> getOaSupplyListListBySupplyId(Long supplyId);

    /**
     * 更新状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateLeaveStatus(Long id, Integer status);

}

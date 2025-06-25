package com.miyu.module.pdm.service.combination;

import com.miyu.module.pdm.controller.admin.combination.vo.CombinationListReqVO;
import com.miyu.module.pdm.dal.dataobject.combination.CombinationDO;

import java.util.Collection;
import java.util.List;

/**
 * PDM 刀组-临时 Service 接口
 *
 * @author Liuy
 */
public interface CombinationService {

    /**
     * 获得刀组列表
     *
     * @param listReqVO 查询条件
     * @return 刀组列表
     */
    List<CombinationDO> getCombinationList(CombinationListReqVO listReqVO);

    /**
     * 获得指定刀组id的刀组列表
     *
     * @param combinationIds 刀组id数组
     * @return 刀组列表
     */
    List<CombinationDO> getCombinationListByCombinationIds(Collection<String> combinationIds);
}

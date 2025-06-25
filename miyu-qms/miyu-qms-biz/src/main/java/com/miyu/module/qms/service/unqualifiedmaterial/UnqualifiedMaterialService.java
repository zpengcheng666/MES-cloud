package com.miyu.module.qms.service.unqualifiedmaterial;

import javax.validation.*;

import com.miyu.module.qms.api.dto.UnqualifiedMaterialReqDTO;
import com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo.*;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.Collection;
import java.util.List;

/**
 * 不合格品产品 Service 接口
 *
 * @author Zhangyunfei
 */
public interface UnqualifiedMaterialService {

    /**
     * 创建不合格品产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createUnqualifiedMaterial(@Valid UnqualifiedMaterialSaveReqVO createReqVO);

    /**
     * 更新不合格品产品
     *
     * @param updateReqVO 更新信息
     */
    void updateUnqualifiedMaterial(@Valid UnqualifiedMaterialSaveReqVO updateReqVO);

    /**
     * 删除不合格品产品
     *
     * @param id 编号
     */
    void deleteUnqualifiedMaterial(String id);

    /**
     * 获得不合格品产品
     *
     * @param id 编号
     * @return 不合格品产品
     */
    UnqualifiedMaterialDO getUnqualifiedMaterial(String id);

    /**
     * 获得不合格品产品分页
     *
     * @param pageReqVO 分页查询
     * @return 不合格品产品分页
     */
    PageResult<UnqualifiedMaterialDO> getUnqualifiedMaterialPage(UnqualifiedMaterialPageReqVO pageReqVO);

    /**
     * 批量更新不合格品审批
     * @param reqDTOList
     */
    void updateUnqualifiedMaterialApproveResultBatch(List<UnqualifiedMaterialReqDTO> reqDTOList);

    /**
     * 物料条码集合查询不合格品处理结果集合
     * @param barCodes
     * @return
     */
    List<UnqualifiedMaterialDO> getUnqualifiedMaterialListByBarCodes(Collection<String> barCodes);
}

package com.miyu.module.qms.service.unqualifiedregistration;

import java.util.*;
import javax.validation.*;

import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.unqualifiedregistration.vo.*;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import com.miyu.module.qms.dal.dataobject.unqualifiedregistration.UnqualifiedRegistrationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 不合格品登记 Service 接口
 *
 * @author Zhangyunfei
 */
public interface UnqualifiedRegistrationService {

    /**
     * 创建不合格品登记
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createUnqualifiedRegistration(@Valid UnqualifiedRegistrationSaveReqVO createReqVO);

    /**
     * 更新不合格品登记
     *
     * @param updateReqVO 更新信息
     */
    void updateUnqualifiedRegistration(@Valid UnqualifiedRegistrationSaveReqVO updateReqVO);

    /**
     * 删除不合格品登记
     *
     * @param id 编号
     */
    void deleteUnqualifiedRegistration(String id);

    /**
     * 获得不合格品登记
     *
     * @param id 编号
     * @return 不合格品登记
     */
    UnqualifiedRegistrationDO getUnqualifiedRegistration(String id);

    /**
     * 获得不合格品登记分页
     *
     * @param pageReqVO 分页查询
     * @return 不合格品登记分页
     */
    PageResult<UnqualifiedRegistrationDO> getUnqualifiedRegistrationPage(UnqualifiedRegistrationPageReqVO pageReqVO);

    /**
     * 批量保存不合格品登记
     * @param createReqVO
     * @return
     */
    void saveUnqualifiedRegistrationBatch(UnqualifiedRegistrationSaveReqVO createReqVO);

    /**
     * 检验任务ID获取不合格产品等级集合
     * @param id
     * @return
     */
    List<UnqualifiedMaterialDO> getUnqualifiedRegistrationListBySheetSchemeId(String id);



    /***
     * 根据时间和物料类型获取该段时间内的缺陷信息
     * @param vo
     * @return
     */
    List<UnqualifiedRegistrationDO> getDefectives(AnalysisReqVO vo);

    /**
     * 批量保存不合格品登记并提交审核
     * @param createReqVO
     */
    void saveAndAuditUnqualifiedRegistrationBatch(UnqualifiedRegistrationSaveReqVO createReqVO);
}
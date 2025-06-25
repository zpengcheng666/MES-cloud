package com.miyu.module.qms.service.samplingstandard;

import java.util.*;
import javax.validation.*;

import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.InspectionItemTypeListReqVO;
import com.miyu.module.qms.controller.admin.samplingstandard.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 抽样标准 Service 接口
 *
 * @author Zhangyunfei
 */
public interface SamplingStandardService {

    /**
     * 创建抽样标准
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createSamplingStandard(@Valid SamplingStandardSaveReqVO createReqVO);

    /**
     * 更新抽样标准
     *
     * @param updateReqVO 更新信息
     */
    void updateSamplingStandard(@Valid SamplingStandardSaveReqVO updateReqVO);

    /**
     * 删除抽样标准
     *
     * @param id 编号
     */
    void deleteSamplingStandard(String id);

    /**
     * 获得抽样标准
     *
     * @param id 编号
     * @return 抽样标准
     */
    SamplingStandardDO getSamplingStandard(String id);

    /**
     * 获得抽样标准列表
     * @param listReqVO
     * @return
     */
    List<SamplingStandardDO> getSamplingStandardList(SamplingStandardListReqVO listReqVO);
}
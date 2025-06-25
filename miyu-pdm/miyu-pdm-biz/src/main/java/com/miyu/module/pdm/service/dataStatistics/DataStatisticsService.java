package com.miyu.module.pdm.service.dataStatistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomReqVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomRespVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsPageReqVO;

import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;

import java.util.List;

/**
 *设计数据包接收记录 Service 接口
 */

public interface DataStatisticsService {

    /**
     * 删除
     */
    void deleteDataStatistics(String id);

    /**
     * 查
     * 获得数据包的接受记录
     * @param id
     * @return
     */
     DataStatisticsDO getDataStatistics(String id);

    /**
     * 获得数据包的接受分页数据
     */
    PageResult<DataStatisticsDO> getDataStatisticsPage(DataStatisticsPageReqVO pageReqVO);

    /**
     *零件数量
     */
    Long countPart(String id);

    /**
     * 获得数据包列表
     */
    List<DataStatisticsDO> getDataStatisticsList(DataStatisticsPageReqVO reqVO);

    /**
     * 通过选中数据包获取零件列表
     */
    List<BomRespVO> getPartListByReceiveInfoId(BomReqVO reqVO);
}

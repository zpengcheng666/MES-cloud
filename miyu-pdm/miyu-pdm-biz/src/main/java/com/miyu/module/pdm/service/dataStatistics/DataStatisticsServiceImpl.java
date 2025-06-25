package com.miyu.module.pdm.service.dataStatistics;


import cn.iocoder.yudao.framework.common.pojo.PageResult;

import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomReqVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.BomRespVO;
import com.miyu.module.pdm.controller.admin.dataStatistics.vo.DataStatisticsPageReqVO;
import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;
import com.miyu.module.pdm.dal.mysql.dataStatistics.BomMapper;
import com.miyu.module.pdm.dal.mysql.dataStatistics.DataStatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.STRUCTURE_PARENT_NOT_EXITS;

@Service
@Validated
public class DataStatisticsServiceImpl implements DataStatisticsService{
    @Resource
        private DataStatisticsMapper dataStatisticsMapper;
    @Resource
    private BomMapper bomMapper;
    @Override
    public void deleteDataStatistics(String id) {
        //校验存在
        validateDataStatisticsExists(id);
        //删除
        dataStatisticsMapper.deleteById(id);
    }

    private void validateDataStatisticsExists(String id) {
        if (dataStatisticsMapper.selectById(id) == null){
            throw exception(STRUCTURE_PARENT_NOT_EXITS);
        }
    }

    @Override
    public DataStatisticsDO getDataStatistics(String id) {
        return dataStatisticsMapper.selectById(id);
    }

    @Override
    public PageResult<DataStatisticsDO> getDataStatisticsPage(DataStatisticsPageReqVO pageReqVO) {
        return dataStatisticsMapper.selectPage(pageReqVO);
    }



    /**
     * 统计零件的数量
     * @param id
     * @return
     */
    @Override
    public Long countPart(String id) {
    return bomMapper.countPart(id);
    }

    @Override
    public List<DataStatisticsDO> getDataStatisticsList(DataStatisticsPageReqVO reqVO) {
        return dataStatisticsMapper.selectList(reqVO);
    }

    @Override
    public List<BomRespVO> getPartListByReceiveInfoId(BomReqVO reqVO) {
        String receiveInfoId = reqVO.getReceiveInfoId();
        String partNumber = reqVO.getPartNumber();
        return bomMapper.selectPartList(receiveInfoId, partNumber);
    }

}

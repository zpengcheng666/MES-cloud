package com.miyu.cloud.mcs.service.problemreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportPageReqVO;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportSaveReqVO;
import com.miyu.cloud.mcs.dal.dataobject.problemreport.ProblemReportDO;
import com.miyu.cloud.mcs.dal.mysql.problemreport.ProblemReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.PROBLEM_REPORT_NOT_EXISTS;

/**
 * 问题上报 Service 实现类
 *
 * @author 王正浩
 */
@Service
@Validated
public class ProblemReportServiceImpl implements ProblemReportService {

    @Resource
    private ProblemReportMapper problemReportMapper;

    @Override
    public String createProblemReport(ProblemReportSaveReqVO createReqVO) {
        // 插入
        ProblemReportDO problemReport = BeanUtils.toBean(createReqVO, ProblemReportDO.class);
        problemReportMapper.insert(problemReport);
        // 返回
        return problemReport.getId();
    }

    @Override
    public void updateProblemReport(ProblemReportSaveReqVO updateReqVO) {
        // 校验存在
        validateProblemReportExists(updateReqVO.getId());
        // 更新
        ProblemReportDO updateObj = BeanUtils.toBean(updateReqVO, ProblemReportDO.class);
        problemReportMapper.updateById(updateObj);
    }

    @Override
    public void deleteProblemReport(String id) {
        // 校验存在
        validateProblemReportExists(id);
        // 删除
        problemReportMapper.deleteById(id);
    }

    private void validateProblemReportExists(String id) {
        if (problemReportMapper.selectById(id) == null) {
            throw exception(PROBLEM_REPORT_NOT_EXISTS);
        }
    }

    @Override
    public ProblemReportDO getProblemReport(String id) {
        return problemReportMapper.selectById(id);
    }

    @Override
    public PageResult<ProblemReportDO> getProblemReportPage(ProblemReportPageReqVO pageReqVO) {
        return problemReportMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProblemReportDO> getProblemReportList(String stationId) {
        return problemReportMapper.selectList(new LambdaQueryWrapperX<ProblemReportDO>()
                .eqIfPresent(ProblemReportDO::getStationId, stationId)
                .ne(ProblemReportDO::getStatus, 2)
        );
    }

}
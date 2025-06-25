package com.miyu.module.pdm.service.processPlanNew;

import com.miyu.module.pdm.controller.admin.processPlanNew.vo.ProcessTaskStatisticsReqVO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import com.miyu.module.pdm.dal.mysql.processPlanNew.ProcessTaskStatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProcessTaskStatisticsServiceImpl implements ProcessTaskStatisticsService {

    @Resource
    private ProcessTaskStatisticsMapper processTaskStatisticsMapper;

    @Override
    public List<ProcessTaskDO> getProcessTaskStatistics(ProcessTaskStatisticsReqVO reqVO) {
        String startTime = null;
        String endTime = null;
        String reviewedBy = reqVO.getReviewedBy();
        LocalDateTime[] updateTime = reqVO.getUpdateTime();

        if (updateTime != null && updateTime.length > 0) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String formattedUpdateTimes = Arrays.stream(updateTime)
                    .map(localDateTime -> localDateTime.format(formatter))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            String[] splitUpdateTimes = formattedUpdateTimes.split(", ");
            if (splitUpdateTimes.length == 2) {
                startTime = splitUpdateTimes[0];
                endTime = splitUpdateTimes[1];
            }
        }
        return processTaskStatisticsMapper.getProcessTaskStatistics(reviewedBy, startTime, endTime);
    }



}

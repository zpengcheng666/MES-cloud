package cn.iocoder.yudao.module.pms.dal.mysql.assessmentdevice;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDevicePageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice.AssessmentDeviceDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评审子表，关联的设备 Mapper
 *
 * @author 技术部长
 */
@Mapper
public interface AssessmentDeviceMapper extends BaseMapperX<AssessmentDeviceDO> {

    default PageResult<AssessmentDeviceDO> selectPage(AssessmentDevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AssessmentDeviceDO>()
                .eqIfPresent(AssessmentDeviceDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(AssessmentDeviceDO::getResourcesType, reqVO.getResourcesType())
                .eqIfPresent(AssessmentDeviceDO::getResourcesTypeId, reqVO.getResourcesTypeId())
                .eqIfPresent(AssessmentDeviceDO::getAmount, reqVO.getAmount())
                .eqIfPresent(AssessmentDeviceDO::getPredictPrice, reqVO.getPredictPrice())
                .betweenIfPresent(AssessmentDeviceDO::getProcessingTime, reqVO.getProcessingTime())
                .eqIfPresent(AssessmentDeviceDO::getAssessmentId, reqVO.getAssessmentId())
                .eqIfPresent(AssessmentDeviceDO::getPartNumber, reqVO.getPartNumber())
                .betweenIfPresent(AssessmentDeviceDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AssessmentDeviceDO::getSuggestion, reqVO.getSuggestion())
                .orderByDesc(AssessmentDeviceDO::getId));
    }

    @Delete("DELETE FROM project_assessment_device where project_code = #{projectCode}")
    public void deleteByProjectCode(String projectCode);

    /**
     * 通过评审id删除,一般用这个，不用上面那个
     * @param assessmentId
     */
    @Delete("DELETE FROM project_assessment_device where assessment_id = #{assessmentId}")
    public void deleteByAssessmentId(String assessmentId);

}

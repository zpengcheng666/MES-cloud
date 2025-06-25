package cn.iocoder.yudao.module.pms.service.assessmentdevice;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.TechnologyAssessmentRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDevicePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDeviceSaveReqVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice.AssessmentDeviceDO;
import com.miyu.module.pdm.api.projectAssessment.dto.TechnologyAssessmentRespDTO;

/**
 * 评审子表，关联的设备 Service 接口
 *
 * @author 技术部长
 */
public interface AssessmentDeviceService {

    /**
     * 创建评审子表，关联的设备
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAssessmentDevice(@Valid AssessmentDeviceSaveReqVO createReqVO);

    /**
     * 更新评审子表，关联的设备
     *
     * @param updateReqVO 更新信息
     */
    void updateAssessmentDevice(@Valid AssessmentDeviceSaveReqVO updateReqVO);

    /**
     * 删除评审子表，关联的设备
     *
     * @param id 编号
     */
    void deleteAssessmentDevice(String id);

    /**
     * 获得评审子表，关联的设备
     *
     * @param id 编号
     * @return 评审子表，关联的设备
     */
    AssessmentDeviceDO getAssessmentDevice(String id);

    /**
     * 获得评审子表，关联的设备分页
     *
     * @param pageReqVO 分页查询
     * @return 评审子表，关联的设备分页
     */
    PageResult<AssessmentDeviceDO> getAssessmentDevicePage(AssessmentDevicePageReqVO pageReqVO);

    /**
     * 创建项目评审相关数据
     * @param technology
     * @return
     */
    public boolean createTechnologyAssessment(TechnologyAssessmentRespVO technology);

    /**
     * 通过评审id获取所有设备
     * @param assessmentId
     * @return
     */
    public List<AssessmentDeviceDO> getAssessmentDeviceByAssessmentId(String assessmentId);

}

package cn.iocoder.yudao.module.pms.service.assessmentdevice;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.*;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDevicePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo.AssessmentDeviceSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice.AssessmentDeviceDO;
import cn.iocoder.yudao.module.pms.dal.mysql.assessmentdevice.AssessmentDeviceMapper;
import com.miyu.module.pdm.api.projectAssessment.dto.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.ASSESSMENT_DEVICE_NOT_EXISTS;

/**
 * 评审子表，关联的设备 Service 实现类
 *
 * @author 技术部长
 */
@Service
@Validated
public class AssessmentDeviceServiceImpl implements AssessmentDeviceService {

    @Resource
    private AssessmentDeviceMapper assessmentDeviceMapper;

    @Override
    public String createAssessmentDevice(AssessmentDeviceSaveReqVO createReqVO) {
        // 插入
        AssessmentDeviceDO assessmentDevice = BeanUtils.toBean(createReqVO, AssessmentDeviceDO.class);
        assessmentDeviceMapper.insert(assessmentDevice);
        // 返回
        return assessmentDevice.getId();
    }

    @Override
    public void updateAssessmentDevice(AssessmentDeviceSaveReqVO updateReqVO) {
        // 校验存在
        validateAssessmentDeviceExists(updateReqVO.getId());
        // 更新
        AssessmentDeviceDO updateObj = BeanUtils.toBean(updateReqVO, AssessmentDeviceDO.class);
        assessmentDeviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteAssessmentDevice(String id) {
        // 校验存在
        validateAssessmentDeviceExists(id);
        // 删除
        assessmentDeviceMapper.deleteById(id);
//        assessmentDeviceMapper.deleteById()
//        assessmentDeviceMapper.deleteByProjectCode(technology.getProjectCode());
    }

    private void validateAssessmentDeviceExists(String id) {
        if (assessmentDeviceMapper.selectById(id) == null) {
            throw exception(ASSESSMENT_DEVICE_NOT_EXISTS);
        }
    }

    @Override
    public AssessmentDeviceDO getAssessmentDevice(String id) {
        return assessmentDeviceMapper.selectById(id);
    }

    @Override
    public PageResult<AssessmentDeviceDO> getAssessmentDevicePage(AssessmentDevicePageReqVO pageReqVO) {
        return assessmentDeviceMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTechnologyAssessment(TechnologyAssessmentRespVO technology) {

        List<AssessmentDeviceDO> save = new ArrayList<>();
//        vo.setProjectCode(technology.getProjectCode());
//        vo.setAssessmentId(technology.getAssessmentId());
//        vo.setId(null);
        //1.设备存储
        List<DeviceRespVO> deviceList = technology.getDeviceList();
        List<AssessmentDeviceDO> deviceDOList = BeanUtils.toBean(deviceList, AssessmentDeviceDO.class);
        save.addAll(deviceDOList);
        //2.刀具存储
        List<CombinationRespVO> combinationList = technology.getCombinationList();
        List<AssessmentDeviceDO> combinationDOList = BeanUtils.toBean(combinationList, AssessmentDeviceDO.class);
        save.addAll(combinationDOList);
        //3.工装存储
        List<MaterialRespVO> materialList = technology.getMaterialList();
        List<AssessmentDeviceDO> materialDOList = BeanUtils.toBean(materialList, AssessmentDeviceDO.class);
        save.addAll(materialDOList);
        //4.采购设备存储,和设备合并了
//        List<DemandDeviceRespDTO> demandDeviceList = technology.getDemandDeviceList();
//        List<AssessmentDeviceDO> demandDeviceDOList = BeanUtils.toBean(demandDeviceList, AssessmentDeviceDO.class);
//        save.addAll(demandDeviceDOList);
        //5.采购刀具存储
        List<DemandCutterRespVO> demandCutterList = technology.getDemandCutterList();
        List<AssessmentDeviceDO> demandCutterDOList = BeanUtils.toBean(demandCutterList, AssessmentDeviceDO.class);
        save.addAll(demandCutterDOList);
        //6.采购刀柄存储
        List<DemandHiltRespVO> demandHiltList = technology.getDemandHiltList();
        List<AssessmentDeviceDO> demandHiltDOList = BeanUtils.toBean(demandHiltList, AssessmentDeviceDO.class);
        save.addAll(demandHiltDOList);
        //7.采购工装存储,和工装合并了
//        List<DemandMaterialRespDTO> demandMaterialList = technology.getDemandMaterialList();
//        List<AssessmentDeviceDO> demandMaterialDOList = BeanUtils.toBean(demandMaterialList, AssessmentDeviceDO.class);
//        save.addAll(demandMaterialDOList);

        for (AssessmentDeviceDO assessmentDeviceDO : save) {
            assessmentDeviceDO.setProjectCode(technology.getProjectCode());
            assessmentDeviceDO.setAssessmentId(technology.getAssessmentId());
            assessmentDeviceDO.setId(null);
        }
        //有数据才存,没有就过去了
        if(save.size()>0){
            //用项目编号不太妥当,应该用评审id
            assessmentDeviceMapper.deleteByAssessmentId(technology.getAssessmentId());
            assessmentDeviceMapper.insertBatch(save);
        }

        return true;
    }

    //获取同一评审下的所有设备
    @Override
    public List<AssessmentDeviceDO> getAssessmentDeviceByAssessmentId(String assessmentId) {
        LambdaQueryWrapperX<AssessmentDeviceDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(AssessmentDeviceDO::getAssessmentId,assessmentId);
        return assessmentDeviceMapper.selectList(wrapperX);
    }

}

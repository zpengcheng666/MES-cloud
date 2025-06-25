package com.miyu.module.qms.service.inspectionsheetscheme;

import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSchemeUpdateReqVO;
import com.miyu.module.qms.dal.dataobject.inspectionsheet.InspectionSheetDO;
import com.miyu.module.qms.dal.mysql.inspectionsheet.InspectionSheetMapper;
import com.miyu.module.qms.dal.mysql.inspectionsheetschemematerial.InspectionSheetSchemeMaterialMapper;
import com.miyu.module.qms.enums.InspectionSelfAssignmentStatusEnum;
import com.miyu.module.qms.enums.InspectionSheetSchemeMaterialStatusEnum;
import com.miyu.module.qms.enums.InspectionSheetSchemeStatusEnum;
import com.miyu.module.qms.enums.InspectionSheetStatusEnum;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.inspectionsheetscheme.InspectionSheetSchemeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 检验单方案任务计划 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class InspectionSheetSchemeServiceImpl implements InspectionSheetSchemeService {

    @Resource
    private InspectionSheetSchemeMapper inspectionSheetSchemeMapper;

    @Resource
    private InspectionSheetMapper inspectionSheetMapper;

    @Resource
    private InspectionSheetSchemeMaterialMapper inspectionSheetSchemeMaterialMapper;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private AdminUserApi userApi;

    @Override
    public String createInspectionSheetScheme(InspectionSheetSchemeSaveReqVO createReqVO) {
        // 插入
        InspectionSheetSchemeDO inspectionSheetScheme = BeanUtils.toBean(createReqVO, InspectionSheetSchemeDO.class);
        inspectionSheetSchemeMapper.insert(inspectionSheetScheme);
        // 返回
        return inspectionSheetScheme.getId();
    }

    @Override
    public void updateInspectionSheetScheme(InspectionSheetSchemeSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionSheetSchemeExists(updateReqVO.getId());
        // 更新
        InspectionSheetSchemeDO updateObj = BeanUtils.toBean(updateReqVO, InspectionSheetSchemeDO.class);
        inspectionSheetSchemeMapper.updateById(updateObj);
    }

    @Override
    public void deleteInspectionSheetScheme(String id) {
        // 校验存在
        validateInspectionSheetSchemeExists(id);
        // 删除
        inspectionSheetSchemeMapper.deleteById(id);
    }

    private void validateInspectionSheetSchemeExists(String id) {
        if (inspectionSheetSchemeMapper.selectById(id) == null) {
            throw exception(INSPECTION_SHEET_SCHEME_NOT_EXISTS);
        }
    }

    @Override
    public InspectionSheetSchemeDO getInspectionSheetScheme(String id) {
        return inspectionSheetSchemeMapper.selectById(id);
    }

    /**
     * 分配检验人员
     * @param inspectionSheetScheme
     */
    @Override
    public void updateInspectionSheetSchemeAssign(InspectionSheetSchemeDO inspectionSheetScheme) {
        InspectionSheetSchemeDO scheme = inspectionSheetSchemeMapper.selectById(inspectionSheetScheme.getId());
        if(ObjectUtils.equalsAny(scheme.getStatus(), InspectionSheetStatusEnum.INSPECTING.getStatus(), InspectionSheetStatusEnum.INSPECTED.getStatus())){
            throw exception(INSPECTION_SHEET_SCHEME_ASSIGN_FAIL);
        }
        InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(inspectionSheetScheme.getId());
        updSheetScheme.setAssignmentType(inspectionSheetScheme.getAssignmentType());
        // 分配人员
        if(inspectionSheetScheme.getAssignmentType() == 1){
            updSheetScheme.setAssignmentId(inspectionSheetScheme.getAssignmentId());
            updSheetScheme.setStatus(InspectionSheetStatusEnum.TOINSPECT.getStatus());
            updSheetScheme.setAssignmentDate(LocalDateTime.now());
            inspectionSheetSchemeMapper.updateById(updSheetScheme);
            // 分配完成更新检验单状态
            updateInspectionSheetToInspect(scheme);
        }
        // 分配班组
        else {
            updSheetScheme.setAssignmentTeamId(inspectionSheetScheme.getAssignmentId());
            inspectionSheetSchemeMapper.updateById(updSheetScheme);
        }
    }

    /**
     * 更新检验单状态为待检验
     * @param scheme
     */
    private void updateInspectionSheetToInspect(InspectionSheetSchemeDO scheme){
        // 获取检验单下的所有检验任务
        List<InspectionSheetSchemeDO> schemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, scheme.getInspectionSheetId());
        // 最小的状态
        Integer minStatus = schemeList.stream().mapToInt(InspectionSheetSchemeDO::getStatus).min().getAsInt();
        // 获取检验单
        InspectionSheetDO sheet = inspectionSheetMapper.selectById(scheme.getInspectionSheetId());
        // 更新检验单状态
        if(sheet.getStatus().compareTo(minStatus) < 0 ){
            InspectionSheetDO updSheet = new InspectionSheetDO();
            updSheet.setId(scheme.getInspectionSheetId());
            updSheet.setStatus(minStatus);
            updSheet.setHeader(getLoginUserId().toString());
            inspectionSheetMapper.updateById(updSheet);
        }
    }

    /**
     * 自检完成
     * 分配检验人员
     * @param inspectionSheetScheme
     */
    @Override
    public void updateInspectionSheetSchemeSelfAssign(InspectionSheetSchemeDO inspectionSheetScheme) {
        InspectionSheetSchemeDO scheme = inspectionSheetSchemeMapper.selectById(inspectionSheetScheme.getId());
        // 自检检验任务非完成
        if(scheme.getStatus() != InspectionSheetStatusEnum.INSPECTED.getStatus()){
            throw exception(INSPECTION_SHEET_SCHEME_ASSIGN_FAIL);
        }
        InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(inspectionSheetScheme.getId());
        updSheetScheme.setSelfAssignmentId(inspectionSheetScheme.getSelfAssignmentId());
        updSheetScheme.setSelfAssignmentType(inspectionSheetScheme.getSelfAssignmentType());
        updSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.ASSIGNED.getStatus());
        updSheetScheme.setSelfAssignmentDate(LocalDateTime.now());
        inspectionSheetSchemeMapper.updateById(updSheetScheme);
    }

    /**
     * 自检任务认领
     * @param inspectionSheetScheme
     */
    @Override
    public void updateInspectionSheetSchemeClaim(InspectionSheetSchemeDO inspectionSheetScheme) {
        // 检验单ID获取检验任务
        InspectionSheetSchemeDO scheme = inspectionSheetSchemeMapper.selectById(inspectionSheetScheme.getId());
        // 检验任务类型是来料或成品
        if(scheme.getSchemeType() == 1 || scheme.getSchemeType() == 3 || (scheme.getSchemeType() == 2 && scheme.getSelfInspection() == null)){
            // 状态非待分配
            if(scheme.getStatus() != InspectionSheetStatusEnum.TOASSIGN.getStatus()){
                throw exception(INSPECTION_SHEET_SCHEME_CLAIM_FAIL);
            }
        }
        // 生产检验 状态是待认领
        else {
            // 检验任务专检 状态不是待认领
            if("1".equals(scheme.getIsSpecInspect()) && scheme.getSelfAssignmentStatus() != InspectionSelfAssignmentStatusEnum.TOCLAIM.getStatus()){
                throw exception(INSPECTION_SHEET_SCHEME_CLAIM_FAIL);
            }
        }

        InspectionSheetSchemeDO updSheetScheme = new InspectionSheetSchemeDO();
        updSheetScheme.setId(scheme.getId());
        // 来料或成品任务领取
        // 生产非自检
        if(scheme.getSchemeType() == 1 || scheme.getSchemeType() == 3 || (scheme.getSchemeType() == 2 && scheme.getSelfInspection() == null)){
            // 检验人员ID
            updSheetScheme.setAssignmentId(getLoginUserId().toString());
            updSheetScheme.setAssignmentType(1);
            // 状态为待检验
            updSheetScheme.setStatus(InspectionSheetStatusEnum.TOINSPECT.getStatus());
            updSheetScheme.setAssignmentDate(LocalDateTime.now());
            inspectionSheetSchemeMapper.updateById(updSheetScheme);
            // 分配完成更新检验单状态
            updateInspectionSheetToInspect(scheme);
        }
        // 生产 专检任务领取
        else {
            // 专检人员ID
            updSheetScheme.setSpecAssignmentId(getLoginUserId().toString());
            // 状态为待专检
            updSheetScheme.setSelfAssignmentStatus(InspectionSelfAssignmentStatusEnum.TOINSPECT_SPEC.getStatus());
            updSheetScheme.setSelfAssignmentDate(LocalDateTime.now());
            inspectionSheetSchemeMapper.updateById(updSheetScheme);
        }
    }

    /**
     * 检验单任务ID获取检验单产品集合
     * @param schemeId
     * @return
     */
    @Override
    public List<InspectionSheetSchemeDO> getInspectionSheetSchemeInfoById(String schemeId) {
        return inspectionSheetSchemeMapper.selectBySheetSchemeInfoById(schemeId);
    }

    /**
     * 更新检验任务检验结果
     * @param updateReqVO
     */
    @Override
    public void updateInspectionSchemeResult(InspectionSchemeUpdateReqVO updateReqVO) {
        // 检验任务ID获取检验产品集合
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, updateReqVO.getSheetSchemeId());
        // 合格数量大于总数量
        if(updateReqVO.getQualifiedQuantity() > materialList.size()){
            throw exception(INSPECTION_SHEET_SCHEME_QUALIFIED_QUANTITY_ERROR);
        }

        // 更新检验任务检验结果
        InspectionSheetSchemeDO scheme = new InspectionSheetSchemeDO();
        scheme.setId(updateReqVO.getSheetSchemeId());
        scheme.setPassRule(updateReqVO.getPassRule());
        scheme.setQualifiedQuantity(updateReqVO.getQualifiedQuantity());
        scheme.setInspectionResult(updateReqVO.getInspectionResult());
        scheme.setEndTime(LocalDateTime.now());
        scheme.setStatus(InspectionSheetSchemeStatusEnum.INSPECTED.getStatus());
        inspectionSheetSchemeMapper.updateById(scheme);
        // 获取检验单下的所有检验任务
        List<InspectionSheetSchemeDO> sheetSchemeList = inspectionSheetSchemeMapper.selectList(InspectionSheetSchemeDO::getInspectionSheetId, updateReqVO.getSheetId());
        // 过滤未检测的记录
        List<InspectionSheetSchemeDO> toInspectList = sheetSchemeList.stream().filter(s -> s.getInspectionResult() == null).collect(Collectors.toList());
        // 当前检测任务全部都检测
        // 更新结束时间并完成检验单
        if(toInspectList.size() == 0){
            InspectionSheetDO updSheet = new InspectionSheetDO();
            updSheet.setId(updateReqVO.getSheetId());
            // 结束时间
            updSheet.setEndTime(LocalDateTime.now());
            // 获取当前检验单
            InspectionSheetDO sheet = inspectionSheetMapper.selectById(updateReqVO.getSheetId());
            // 最小的状态 不包含待核验状态
            Integer minStatus = sheetSchemeList.stream().mapToInt(InspectionSheetSchemeDO::getStatus).min().getAsInt();
            if(sheet.getStatus().compareTo(minStatus) < 0){
                updSheet.setStatus(minStatus);
            }
            updSheet.setStatus(InspectionSheetStatusEnum.INSPECTED.getStatus());
            inspectionSheetMapper.updateById(updSheet);
        }
    }

    @Override
    public List<InspectionSheetSchemeDO> getInspectionSheetSchemes(AnalysisReqVO vo) {

        return inspectionSheetSchemeMapper.getInspectionSheetSchemes(vo);
    }

    @Override
    public List<InspectionSheetSchemeDO> getInspectionSheetSchemeAnalysis(AnalysisReqVO vo,Boolean process) {
        return inspectionSheetSchemeMapper.getInspectionSheetSchemeAnalysis(vo,process);
    }

    @Override
    public List<InspectionSheetSchemeDO> getInspectionSheetSchemeList4Terminal(InspectionSchemeTerminalReqVO reqVO) {
        // 查出当前登录人所在班组
        AdminUserRespDTO user = userApi.getUser(getLoginUserId()).getCheckedData();
        reqVO.setDeptId(user.getDeptId().toString());
        return inspectionSheetSchemeMapper.getInspectionSheetSchemeList4Terminal(reqVO);
    }

    @Override
    public InspectionSheetSchemeDO getInspectionSheetSchemeListByBarCode4Terminal(InspectionSchemeTerminalReqVO reqVO) {
        List<InspectionSheetSchemeDO> list = inspectionSheetSchemeMapper.getInspectionSheetSchemeList4Terminal(reqVO);
        // 遍历检验任务集合 找出包含当前barCode的任务
        for(InspectionSheetSchemeDO scheme : list){
            if(StringUtils.isBlank(scheme.getBarCodes())){
                continue;
            }
            List<String> barCodeList = Arrays.asList(scheme.getBarCodes().split(","));
            if(barCodeList.contains(reqVO.getBarCode())){
                return scheme;
            }
        }
        return null;
    }

    /**
     * 生产操作终端验证barCode
     * @param reqVO
     */
    @Override
    public void validInspectionSchemeBarCode(InspectionSchemeTerminalValidReqVO reqVO) {
        // 主键获取检验单任务
        InspectionSheetSchemeDO sheetScheme = inspectionSheetSchemeMapper.selectById(reqVO.getSchemeId());
        // barCode获取物料类型
        // 物料条码查询库存
        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCode(reqVO.getBarCode()).getCheckedData();
        // 物料条码未查询到对应库存
        if(materialStockList.size() == 0){
            throw exception(INSPECTION_SHEET_SCHEME_MATERIAL_STOCK_NOT_EXISTS);
        }
        List<MaterialConfigRespDTO> materialConfigList =  materialMCCApi.getMaterialConfigList(Lists.newArrayList(materialStockList.get(0).getMaterialConfigId())).getCheckedData();

        // 物料类型不同扫码失败
        if(!sheetScheme.getMaterialConfigId().equals(materialConfigList.get(0).getId()) && !sheetScheme.getMaterialConfigId().equals(materialConfigList.get(0).getMaterialSourceId())) {
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }

        // 检验单任务ID查询检验产品集合
        List<InspectionSheetSchemeMaterialDO> materialList = inspectionSheetSchemeMaterialMapper.selectList(InspectionSheetSchemeMaterialDO::getInspectionSheetSchemeId, reqVO.getSchemeId());
        // 检验产品为大于1才验证重复
        // 生产检验自检 互检 专检为同一个产品不进行验证
        if (materialList.size() > 1){
            // 物料管理模式
            Integer materialManage = materialConfigList.get(0).getMaterialManage();
            // 单件 验证条码不能重复
            if(materialManage == 1){
                // 过滤出检验完成 且 barcode相同的数据
                List<InspectionSheetSchemeMaterialDO> list = materialList.stream().filter(a -> StringUtils.isNotBlank(a.getBarCode()) && a.getBarCode().equals(reqVO.getBarCode()) && InspectionSheetSchemeMaterialStatusEnum.FINISH.getStatus().equals(a.getStatus())).collect(Collectors.toList());
                if(list.size() > 0){
                    throw exception(INSPECTION_SHEET_SCHEME_MATERIAL_BAR_CODE_DUPLICATE);
                }
            }

        }

        // 生产检验
        // 验证barCode
        if(sheetScheme.getSchemeType() == 2){
            // 条码不同
            if(!reqVO.getBarCode().equals(materialList.get(0).getBarCode())){
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }
    }

    @Override
    public List<InspectionSheetSchemeDO> getInspectionSheetSchemeAnalysisWorker(AnalysisReqVO vo) {
        return inspectionSheetSchemeMapper.getInspectionSheetSchemeAnalysisWorker(vo);
    }
}

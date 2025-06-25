package com.miyu.module.qms.service.defectivecode;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.qms.controller.admin.defectivecode.vo.*;
import com.miyu.module.qms.dal.dataobject.defectivecode.DefectiveCodeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.defectivecode.DefectiveCodeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 缺陷代码 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class DefectiveCodeServiceImpl implements DefectiveCodeService {

    @Resource
    private DefectiveCodeMapper defectiveCodeMapper;

    @Override
    public String createDefectiveCode(DefectiveCodeSaveReqVO createReqVO) {
        // 插入
        DefectiveCodeDO defectiveCode = BeanUtils.toBean(createReqVO, DefectiveCodeDO.class);
        // 验证缺陷代码不能重复
        validateDefectiveCodeDup(createReqVO.getCode());
        defectiveCodeMapper.insert(defectiveCode);
        // 返回
        return defectiveCode.getId();
    }

    @Override
    @LogRecord(type = QMS_DEFECTIVE_CODE_TYPE, subType = QMS_UPDATE_DEFECTIVE_CODE_SUB_TYPE, bizNo = "{{#defectiveCode.id}}",
            success = QMS_UPDATE_DEFECTIVE_CODE_SUCCESS)
    public void updateDefectiveCode(DefectiveCodeSaveReqVO updateReqVO) {
        // 校验存在
        DefectiveCodeDO defectiveCode = validateDefectiveCodeExists(updateReqVO.getId());
        // 验证缺陷代码不能重复
        validateDefectiveCodeDup(updateReqVO.getCode());
        // 更新
        DefectiveCodeDO updateObj = BeanUtils.toBean(updateReqVO, DefectiveCodeDO.class);
        defectiveCodeMapper.updateById(updateObj);

        // 记录操作日志上下文
        LogRecordContext.putVariable("defectiveCode", defectiveCode);
    }

    @Override
    public void deleteDefectiveCode(String id) {
        // 校验存在
        validateDefectiveCodeExists(id);
        // 删除
        defectiveCodeMapper.deleteById(id);
    }

    private DefectiveCodeDO validateDefectiveCodeExists(String id) {
        DefectiveCodeDO defectiveCode = defectiveCodeMapper.selectById(id);
        if (defectiveCode == null) {
            throw exception(DEFECTIVE_CODE_NOT_EXISTS);
        }
        return defectiveCode;
    }

    private void validateDefectiveCodeDup(String code) {
        // 验证code不能重复
        List<DefectiveCodeDO> codeDOList = defectiveCodeMapper.selectList(DefectiveCodeDO::getCode, code);
        if(codeDOList.size() > 1){
            throw exception(DEFECTIVE_CODE_DUPLICATE);
        }
    }


    @Override
    public DefectiveCodeDO getDefectiveCode(String id) {
        return defectiveCodeMapper.selectById(id);
    }

    @Override
    public PageResult<DefectiveCodeDO> getDefectiveCodePage(DefectiveCodePageReqVO pageReqVO) {
        return defectiveCodeMapper.selectPage(pageReqVO);
    }

    /**
     * 获取缺陷代码集合
     * @return
     */
    @Override
    public List<DefectiveCodeDO> getDefectiveCodeList() {
        return defectiveCodeMapper.selectList();
    }

}

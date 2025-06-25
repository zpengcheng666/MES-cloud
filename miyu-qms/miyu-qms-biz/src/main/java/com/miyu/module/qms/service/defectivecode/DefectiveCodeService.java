package com.miyu.module.qms.service.defectivecode;

import java.util.*;
import javax.validation.*;
import com.miyu.module.qms.controller.admin.defectivecode.vo.*;
import com.miyu.module.qms.dal.dataobject.defectivecode.DefectiveCodeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 缺陷代码 Service 接口
 *
 * @author Zhangyunfei
 */
public interface DefectiveCodeService {

    /**
     * 创建缺陷代码
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createDefectiveCode(@Valid DefectiveCodeSaveReqVO createReqVO);

    /**
     * 更新缺陷代码
     *
     * @param updateReqVO 更新信息
     */
    void updateDefectiveCode(@Valid DefectiveCodeSaveReqVO updateReqVO);

    /**
     * 删除缺陷代码
     *
     * @param id 编号
     */
    void deleteDefectiveCode(String id);

    /**
     * 获得缺陷代码
     *
     * @param id 编号
     * @return 缺陷代码
     */
    DefectiveCodeDO getDefectiveCode(String id);

    /**
     * 获得缺陷代码分页
     *
     * @param pageReqVO 分页查询
     * @return 缺陷代码分页
     */
    PageResult<DefectiveCodeDO> getDefectiveCodePage(DefectiveCodePageReqVO pageReqVO);

    /**
     * 获得缺陷代码集合
     * @return
     */
    List<DefectiveCodeDO> getDefectiveCodeList();
}
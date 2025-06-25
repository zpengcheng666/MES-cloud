package com.miyu.cloud.dms.service.inspectionrecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordAddReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordPageReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionrecord.InspectionRecordDO;

import javax.validation.Valid;

/**
 * 设备检查记录 Service 接口
 *
 * @author miyu
 */
public interface InspectionRecordService {

    /**
     * 创建设备检查记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionRecord(@Valid InspectionRecordSaveReqVO createReqVO);

    /**
     * 更新设备检查记录
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionRecord(@Valid InspectionRecordSaveReqVO updateReqVO);

    /**
     * 删除设备检查记录
     *
     * @param id 编号
     */
    void deleteInspectionRecord(String id);

    /**
     * 获得设备检查记录
     *
     * @param id 编号
     * @return 设备检查记录
     */
    InspectionRecordDO getInspectionRecord(String id);

    /**
     * 获得设备检查记录分页
     *
     * @param pageReqVO 分页查询
     * @return 设备检查记录分页
     */
    PageResult<InspectionRecordDO> getInspectionRecordPage(InspectionRecordPageReqVO pageReqVO);

    /**
     * 添加检查记录
     *
     * @param addReqVO 添加数据
     */
    void addInspectionRecord(InspectionRecordAddReqVO addReqVO);

    /**
     * 超期检查服务,每天运行一次
     */
    void expirationShutdownService();
}

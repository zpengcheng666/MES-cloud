package com.miyu.cloud.dms.service.maintainapplication;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.maintainapplication.vo.MaintainApplicationPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintainapplication.vo.MaintainApplicationSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintainapplication.MaintainApplicationDO;

import javax.validation.Valid;

/**
 * 设备维修申请 Service 接口
 *
 * @author miyu
 */
public interface MaintainApplicationService {

    /**
     * 创建设备维修申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createMaintainApplication(@Valid MaintainApplicationSaveReqVO createReqVO);

    /**
     * 更新设备维修申请
     *
     * @param updateReqVO 更新信息
     */
    void updateMaintainApplication(@Valid MaintainApplicationSaveReqVO updateReqVO);

    /**
     * 删除设备维修申请
     *
     * @param id 编号
     */
    void deleteMaintainApplication(String id);

    /**
     * 获得设备维修申请
     *
     * @param id 编号
     * @return 设备维修申请
     */
    MaintainApplicationDO getMaintainApplication(String id);

    /**
     * 获得设备维修申请分页
     *
     * @param pageReqVO 分页查询
     * @return 设备维修申请分页
     */
    PageResult<MaintainApplicationDO> getMaintainApplicationPage(MaintainApplicationPageReqVO pageReqVO);

}

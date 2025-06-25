package com.miyu.cloud.dms.service.linestationgroup;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupPageReqVO;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 产线/工位组 Service 接口
 *
 * @author 芋道源码
 */
public interface LineStationGroupService {

    /**
     * 创建产线/工位组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createLineStationGroup(@Valid LineStationGroupSaveReqVO createReqVO);

    /**
     * 更新产线/工位组
     *
     * @param updateReqVO 更新信息
     */
    void updateLineStationGroup(@Valid LineStationGroupSaveReqVO updateReqVO);

    /**
     * 删除产线/工位组
     *
     * @param id 编号
     */
    void deleteLineStationGroup(String id);

    /**
     * 获得产线/工位组
     *
     * @param id 编号
     * @return 产线/工位组
     */
    LineStationGroupDO getLineStationGroup(String id);

    /**
     * 获得产线/工位组分页
     *
     * @param pageReqVO 分页查询
     * @return 产线/工位组分页
     */
    PageResult<LineStationGroupDO> getLineStationGroupPage(LineStationGroupPageReqVO pageReqVO);

    /**
     * 获得产线/工位组列表
     *
     * @return 列表内容
     */
    List<LineStationGroupDO> getLineStationGroupList();

    CommonDevice getDeviceUnit(String deviceUnitId);

    CommonDevice getDeviceUnit(String unitId, String deviceId);

    List<LineStationGroupDO> getLineStationGroupBatch(Collection<String> unitIds);

    LineStationGroupDO getOneByCode(String materialCode);

    LineStationGroupDO getLineStationGroupByCode(String lineCode);
}

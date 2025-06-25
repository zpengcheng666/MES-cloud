package com.miyu.cloud.dms.service.linestationgroup;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupPageReqVO;
import com.miyu.cloud.dms.controller.admin.linestationgroup.vo.LineStationGroupSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.LINE_STATION_GROUP_NOT_EXISTS;

/**
 * 产线/工位组 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class LineStationGroupServiceImpl implements LineStationGroupService {

    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private LedgerMapper ledgerMapper;

    @Override
    public String createLineStationGroup(LineStationGroupSaveReqVO createReqVO) {
        // 插入
        LineStationGroupDO lineStationGroup = BeanUtils.toBean(createReqVO, LineStationGroupDO.class);
        lineStationGroupMapper.insert(lineStationGroup);
        // 返回
        return lineStationGroup.getId();
    }

    @Override
    public void updateLineStationGroup(LineStationGroupSaveReqVO updateReqVO) {
        // 校验存在
        validateLineStationGroupExists(updateReqVO.getId());
        // 更新
        LineStationGroupDO updateObj = BeanUtils.toBean(updateReqVO, LineStationGroupDO.class);
        lineStationGroupMapper.updateById(updateObj);
    }

    @Override
    public void deleteLineStationGroup(String id) {
        // 校验存在
        validateLineStationGroupExists(id);
        // 删除
        lineStationGroupMapper.deleteById(id);
    }

    private void validateLineStationGroupExists(String id) {
        if (lineStationGroupMapper.selectById(id) == null) {
            throw exception(LINE_STATION_GROUP_NOT_EXISTS);
        }
    }

    @Override
    public LineStationGroupDO getLineStationGroup(String id) {
        return lineStationGroupMapper.selectById(id);
    }

    @Override
    public PageResult<LineStationGroupDO> getLineStationGroupPage(LineStationGroupPageReqVO pageReqVO) {
        return lineStationGroupMapper.selectPage(pageReqVO);
    }

    @Override
    public List<LineStationGroupDO> getLineStationGroupList() {
        return lineStationGroupMapper.selectList(new LambdaQueryWrapper<LineStationGroupDO>().eq(LineStationGroupDO::getEnable, 1));
    }

    @Override
    public CommonDevice getDeviceUnit(String deviceUnitId) {
        LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(deviceUnitId);
        if (lineStationGroupDO == null) {
            LedgerDO ledgerDO = ledgerMapper.selectById(deviceUnitId);
            if (ledgerDO == null) return null;
            return BeanUtils.toBean(ledgerDO,CommonDevice.class);
        } else {
            return BeanUtils.toBean(lineStationGroupDO, CommonDevice.class);
        }
    }

    @Override
    public CommonDevice getDeviceUnit(String unitId, String deviceId) {
        LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(unitId);
        if (lineStationGroupDO == null) {
            LedgerDO ledgerDO = ledgerMapper.selectById(deviceId);
            if (ledgerDO == null) return null;
            return BeanUtils.toBean(ledgerDO,CommonDevice.class);
        } else {
            return BeanUtils.toBean(lineStationGroupDO, CommonDevice.class);
        }
    }

    @Override
    public List<LineStationGroupDO> getLineStationGroupBatch(Collection<String> unitIds) {
        return lineStationGroupMapper.selectBatchIds(unitIds);
    }

    @Override
    public LineStationGroupDO getOneByCode(String materialCode) {
        List<LineStationGroupDO> list = lineStationGroupMapper.selectList(LineStationGroupDO::getCode, materialCode);
        if (list.size() != 1) return null;
        return list.get(0);
    }

    @Override
    public LineStationGroupDO getLineStationGroupByCode(String lineCode) {
        List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectList(LineStationGroupDO::getCode, lineCode);
        return lineStationGroupDOS.size() == 1 ? lineStationGroupDOS.get(0) : null;
    }
}

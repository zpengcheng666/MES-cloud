package com.miyu.module.wms.service.agv;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.wms.controller.admin.agv.vo.*;
import com.miyu.module.wms.dal.dataobject.agv.AGVDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.agv.AGVMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * AGV 信息 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class AGVServiceImpl implements AGVService {

    @Resource
    private AGVMapper aGVMapper;

    @Override
    public String createAGV(AGVSaveReqVO createReqVO) {
        // 插入
        AGVDO aGV = BeanUtils.toBean(createReqVO, AGVDO.class);
        aGVMapper.insert(aGV);
        // 返回
        return aGV.getId();
    }

    @Override
    public void updateAGV(AGVSaveReqVO updateReqVO) {
        // 校验存在
        validateAGVExists(updateReqVO.getId());
        // 更新
        AGVDO updateObj = BeanUtils.toBean(updateReqVO, AGVDO.class);
        aGVMapper.updateById(updateObj);
    }

    @Override
    public void deleteAGV(String id) {
        // 校验存在
        validateAGVExists(id);
        // 删除
        aGVMapper.deleteById(id);
    }

    private void validateAGVExists(String id) {
        if (aGVMapper.selectById(id) == null) {
            throw exception(AGV_NOT_EXISTS);
        }
    }

    @Override
    public AGVDO getAGV(String id) {
        return aGVMapper.selectById(id);
    }

    @Override
    public PageResult<AGVDO> getAGVPage(AGVPageReqVO pageReqVO) {
        return aGVMapper.selectPage(pageReqVO);
    }

    @Override
    public AGVDO getAGVByCarNo(String carNo) {
        return aGVMapper.selectOne(AGVDO::getCarNo, carNo);
    }

}
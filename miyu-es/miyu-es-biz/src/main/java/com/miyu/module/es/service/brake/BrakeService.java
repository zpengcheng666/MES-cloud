package com.miyu.module.es.service.brake;

import java.util.*;
import javax.validation.*;
import com.miyu.module.es.controller.admin.brake.vo.*;
import com.miyu.module.es.dal.dataobject.brake.BrakeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 旧厂车牌数据 Service 接口
 *
 * @author 上海弥彧
 */
public interface BrakeService {

    /**
     * 创建旧厂车牌数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBrake(@Valid BrakeSaveReqVO createReqVO);

    /**
     * 更新旧厂车牌数据
     *
     * @param updateReqVO 更新信息
     */
    void updateBrake(@Valid BrakeSaveReqVO updateReqVO);

    /**
     * 删除旧厂车牌数据
     *
     * @param id 编号
     */
    void deleteBrake(String id);

    /**
     * 获得旧厂车牌数据
     *
     * @param id 编号
     * @return 旧厂车牌数据
     */
    BrakeDO getBrake(String id);

    /**
     * 获得旧厂车牌数据分页
     *
     * @param pageReqVO 分页查询
     * @return 旧厂车牌数据分页
     */
    PageResult<BrakeDO> getBrakePage(BrakePageReqVO pageReqVO);

    /**
     * 获得旧厂车牌数据分页
     *
     * @return 旧厂车牌数据分页
     */
    List<BrakeDO> queryBrakeList(String d,
                                 String carPlateNo,
                                 Integer pageSize,
                                 Integer pageNum);



    Integer queryCountBrake();

    void syncBrake(List<String> ids);



}
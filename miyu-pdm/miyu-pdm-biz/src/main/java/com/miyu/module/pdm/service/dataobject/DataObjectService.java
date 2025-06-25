package com.miyu.module.pdm.service.dataobject;

import java.util.*;

import com.miyu.module.pdm.controller.admin.dataobject.vo.*;
import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.validation.Valid;

public interface DataObjectService {

    /**
     * 获得产品数据对象
     *
     * @param id 编号
     * @return 产品数据对象
     */
    DataObjectDO getDataObject(String id);


    /**
     * 获得产品数据对象分页
     *
     * @param pageReqVO 分页查询
     * @return 产品数据对象分页
     */
    PageResult<DataObjectDO> getDataObjectPage(DataObjectPageReqVO pageReqVO);

    /**
     * 获得客户化标识列表
     * @param rootproductId
     * @return
     */
    List<DataObjectRespVO> getCustomizedIndicesByRootProductId(String rootproductId);


    int insertIndex(DataObjectSaveReqVO createReqVO);

    int delIndex( DataObjectPageReqVO pageReqVO) throws Exception;

    int insertOrUpdate(DataObjectSaveReqVO createReqVO);

     void updateIndex(DataObjectSaveReqVO createReqVO);

     int createIndex(DataObjectSaveReqVO createReqVO);


    List<Map<String, Object>> selectOneById(String id);

    int savePM(Map<String, Object> map);
    int savePV(Map<String, Object> pvMap);
    int savePartMaster(Map<String, Object> pmMap);
    int savePartVersion(Map<String, Object> pvMap);
}
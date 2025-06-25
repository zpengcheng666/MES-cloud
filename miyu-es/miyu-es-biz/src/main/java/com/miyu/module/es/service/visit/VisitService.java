package com.miyu.module.es.service.visit;

import java.util.*;
import javax.validation.*;
import com.miyu.module.es.controller.admin.visit.vo.*;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 访客记录 Service 接口
 *
 * @author 上海弥彧
 */
public interface VisitService {


    /**
     * 获得访客记录
     *
     * @param id 编号
     * @return 访客记录
     */
    VisitRespVO getVisit(String id);

    /**
     * 获得访客记录分页
     *
     * @param pageReqVO 分页查询
     * @return 访客记录分页
     */
    PageResult<VisitDO> getVisitPage(VisitPageReqVO pageReqVO);

}
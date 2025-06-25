package com.miyu.module.es.dal.dataobject.modian;

import com.miyu.module.es.controller.admin.visit.vo.VisitSaveReqVO;
import com.miyu.module.es.dal.dataobject.visit.VisitDO;
import lombok.Data;

@Data
public class MoDianDO {

    String callbackTag;

    VisitSaveReqVO data;
}

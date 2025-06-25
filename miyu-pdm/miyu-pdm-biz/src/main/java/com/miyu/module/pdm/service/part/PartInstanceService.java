package com.miyu.module.pdm.service.part;

import com.miyu.module.pdm.controller.admin.part.vo.PartInstanceListReqVO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;

import java.util.*;
public interface PartInstanceService {

    PartInstanceDO getPartInstance(String id);


    List<PartInstanceDO> getPartInstanceList(PartInstanceListReqVO listReqVO);

}
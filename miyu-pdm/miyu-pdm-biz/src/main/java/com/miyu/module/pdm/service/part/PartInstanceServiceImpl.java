package com.miyu.module.pdm.service.part;

import com.miyu.module.pdm.controller.admin.part.vo.PartInstanceListReqVO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.dal.mysql.part.PartInstanceMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * pdm__part_instance Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PartInstanceServiceImpl implements PartInstanceService {

    @Resource
    private PartInstanceMapper partInstanceMapper;



    @Override
    public PartInstanceDO getPartInstance(String id) {
        return partInstanceMapper.selectById(id);
    }

    @Override
    public List<PartInstanceDO> getPartInstanceList(PartInstanceListReqVO listReqVO) {
        return partInstanceMapper.selectList(listReqVO);
    }

}
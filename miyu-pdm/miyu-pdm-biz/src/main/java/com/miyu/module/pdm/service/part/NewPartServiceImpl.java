package com.miyu.module.pdm.service.part;


import com.miyu.module.pdm.controller.admin.part.vo.NewPartReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.NewPartRespVO;
import com.miyu.module.pdm.dal.mysql.part.NewPartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class NewPartServiceImpl implements NewPartService {

    @Autowired
    private NewPartMapper newPartMapper;

    @Override
    public List<NewPartRespVO> getTreeList(NewPartReqVO reqVO) {
            return newPartMapper.selectPartTreeList(reqVO);
        }

    }


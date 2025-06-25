package com.miyu.module.pdm.service.combination;

import cn.hutool.core.collection.CollUtil;
import com.miyu.module.pdm.controller.admin.combination.vo.CombinationListReqVO;
import com.miyu.module.pdm.dal.dataobject.combination.CombinationDO;
import com.miyu.module.pdm.dal.mysql.combination.CombinationMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * PDM 刀组-临时 Service 实现类
 *
 * @author Liuy
 */
@Service
@Validated
public class CombinationServiceImpl implements CombinationService {

    @Resource
    private CombinationMapper combinationMapper;

    @Override
    public List<CombinationDO> getCombinationList(CombinationListReqVO listReqVO) {
        return combinationMapper.selectList(listReqVO);
    }

    @Override
    public List<CombinationDO> getCombinationListByCombinationIds(Collection<String> combinationIds) {
        if (CollUtil.isEmpty(combinationIds)) {
            return Collections.emptyList();
        }
        return combinationMapper.selectListByCombinationIds(combinationIds);
    }

}

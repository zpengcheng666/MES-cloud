package com.miyu.module.qms.service.unqualifiedmaterial;

import com.miyu.module.qms.api.dto.UnqualifiedMaterialReqDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo.*;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.qms.dal.mysql.unqualifiedmaterial.UnqualifiedMaterialMapper;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

/**
 * 不合格品产品 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class UnqualifiedMaterialServiceImpl implements UnqualifiedMaterialService {

    @Resource
    private UnqualifiedMaterialMapper unqualifiedMaterialMapper;

    @Resource
    private MaterialStockApi materialStockApi;

    @Override
    public String createUnqualifiedMaterial(UnqualifiedMaterialSaveReqVO createReqVO) {
        // 插入
        UnqualifiedMaterialDO unqualifiedMaterial = BeanUtils.toBean(createReqVO, UnqualifiedMaterialDO.class);
        unqualifiedMaterialMapper.insert(unqualifiedMaterial);
        // 返回
        return unqualifiedMaterial.getId();
    }

    @Override
    public void updateUnqualifiedMaterial(UnqualifiedMaterialSaveReqVO updateReqVO) {
        // 校验存在
        validateUnqualifiedMaterialExists(updateReqVO.getId());
        // 更新
        UnqualifiedMaterialDO updateObj = BeanUtils.toBean(updateReqVO, UnqualifiedMaterialDO.class);
        unqualifiedMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteUnqualifiedMaterial(String id) {
        // 校验存在
        validateUnqualifiedMaterialExists(id);
        // 删除
        unqualifiedMaterialMapper.deleteById(id);
    }

    private void validateUnqualifiedMaterialExists(String id) {
        if (unqualifiedMaterialMapper.selectById(id) == null) {
            throw exception(UNQUALIFIED_MATERIAL_NOT_EXISTS);
        }
    }

    @Override
    public UnqualifiedMaterialDO getUnqualifiedMaterial(String id) {
        return unqualifiedMaterialMapper.selectById(id);
    }

    @Override
    public PageResult<UnqualifiedMaterialDO> getUnqualifiedMaterialPage(UnqualifiedMaterialPageReqVO pageReqVO) {
        return unqualifiedMaterialMapper.selectPage(pageReqVO);
    }

    /**
     * 批量更新不合格品审批
     * @param reqDTOList
     */
    @Override
    public void updateUnqualifiedMaterialApproveResultBatch(List<UnqualifiedMaterialReqDTO> reqDTOList) {
        unqualifiedMaterialMapper.updateBatch(BeanUtils.toBean(reqDTOList, UnqualifiedMaterialDO.class));
    }

    /**
     * 物料条码集合查询不合格品处理结果集合
     * @param barCodes
     * @return
     */
    @Override
    public List<UnqualifiedMaterialDO> getUnqualifiedMaterialListByBarCodes(Collection<String> barCodes) {
        // 库存集合
        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCodes(barCodes).getCheckedData();
        // 库存集合
        List<String> materialIds = materialStockList.stream().map(a -> a.getId()).collect(Collectors.toList());

        if(materialIds.size() == 0){
            return new ArrayList<>();
        }

        List<UnqualifiedMaterialDO> list = unqualifiedMaterialMapper.getUnqualifiedMaterialListByMaterialIds(materialIds);

        List<UnqualifiedMaterialDO> collect = new ArrayList<>(list.stream().collect(Collectors.toMap(
                UnqualifiedMaterialDO::getMaterialId,
                v -> v,
                (v1, v2) -> {
                    java.time.LocalDateTime dataTime = v1.getCreateTime();
                    java.time.LocalDateTime startDate1 = v2.getCreateTime();
                    if (dataTime == null || startDate1 == null) {
                        return dataTime != null ? v1 : v2;
                    }
                    return dataTime.isAfter(startDate1) || dataTime.equals(startDate1) ? v1 : v2;
                }
        )).values());

        return collect;
    }

}

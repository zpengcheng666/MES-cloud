package com.miyu.module.wms.service.materialstorage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.wms.controller.admin.materialstorage.vo.MaterialStoragePageReqVO;
import com.miyu.module.wms.controller.admin.materialstorage.vo.MaterialStorageSaveReqVO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.mysql.materialstorage.MaterialStorageMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料储位 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class MaterialStorageServiceImpl implements MaterialStorageService {

    @Resource
    private MaterialStorageMapper materialStorageMapper;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    @Lazy
    private MaterialStockService materialStockService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;

    @Override
    public String createMaterialStorage(MaterialStorageSaveReqVO createReqVO) {
        // 插入
        MaterialStorageDO materialStorage = BeanUtils.toBean(createReqVO, MaterialStorageDO.class);
        materialStorageMapper.insert(materialStorage);
        // 返回
        return materialStorage.getId();
    }

    @Override
    public void updateMaterialStorage(MaterialStorageSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialStorageExists(updateReqVO.getId());
        // 更新
        MaterialStorageDO updateObj = BeanUtils.toBean(updateReqVO, MaterialStorageDO.class);
        materialStorageMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterialStorage(String id) {
        // 校验存在
        validateMaterialStorageExists(id);
        // 删除
        materialStorageMapper.deleteById(id);
    }

    private void validateMaterialStorageExists(String id) {
        if (materialStorageMapper.selectById(id) == null) {
            throw exception(MATERIAL_STORAGE_NOT_EXISTS);
        }
    }

    @Override
    public MaterialStorageDO getMaterialStorage(String id) {
        return materialStorageMapper.selectById(id);
    }

    @Override
    public PageResult<MaterialStorageDO> getMaterialStoragePage(MaterialStoragePageReqVO pageReqVO) {
        return materialStorageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaterialStorageDO> getMaterialStorageList() {
        return materialStorageMapper.selectList();
    }

    @Override
    public Boolean createBatchMaterialStorage(MaterialStockDO materialStock) {
        MaterialConfigDO materialConfig = materialConfigService.getMaterialConfig(materialStock.getMaterialConfigId());
        // 是容器
        if (materialConfig != null && materialConfig.getMaterialContainer() == DictConstants.INFRA_BOOLEAN_TINYINT_YES) {
            // 不是 单储位 批量创建储位 编码 并新建实体
            if (materialConfig.getMaterialStorage() == DictConstants.INFRA_BOOLEAN_TINYINT_NO) {
                Integer materialLayer = materialConfig.getMaterialLayer();
                Integer materialRow = materialConfig.getMaterialRow();
                Integer materialCol = materialConfig.getMaterialCol();
                if (materialLayer == null && materialRow == null && materialCol == null) {
                    throw exception(MATERIAL_TYPE_ALL_NULL_LRC);
                }
                Map<String, Object> codeMap = codeGeneratorService.generateCodes("#|-L|-R|-C", "#|-层|-列|-行", new String[]{materialStock.getBarCode()}, new String[]{materialStock.getBarCode()}, new Integer[]{materialLayer, materialRow, materialCol});
                List<String> codes = (List<String>) codeMap.get("codes");
                List<String> names = (List<String>) codeMap.get("names");
                List<Integer[]> numbers = (List<Integer[]>) codeMap.get("numbers");

                List<MaterialStorageDO> materialStorageDOS = new ArrayList<>();
                for (int i = 0; i < codes.size(); i++) {
                    MaterialStorageDO materialStorageDO = new MaterialStorageDO();
                    materialStorageDO.setStorageCode(codes.get(i));
                    materialStorageDO.setStorageName(names.get(i));

                    materialStorageDO.setLayer(numbers.get(i)[0]);
                    materialStorageDO.setRow(numbers.get(i)[1]);
                    materialStorageDO.setCol(numbers.get(i)[2]);

                    materialStorageDO.setMaterialStockId(materialStock.getId());
                    materialStorageDO.setValid(DictConstants.INFRA_BOOLEAN_TINYINT_YES);
                    materialStorageDOS.add(materialStorageDO);
                }
                return this.materialStorageMapper.insertBatch(materialStorageDOS);
            } else {
                // 是单储位  直接使用 物料条码错位储位编码
                MaterialStorageDO materialStorageDO = new MaterialStorageDO();
                materialStorageDO.setStorageCode(materialStock.getBarCode());
                materialStorageDO.setStorageName(materialStock.getBarCode());
                materialStorageDO.setMaterialStockId(materialStock.getId());
                materialStorageDO.setValid(DictConstants.INFRA_BOOLEAN_TINYINT_YES);
                return this.materialStorageMapper.insert(materialStorageDO) > 0;
            }

        }
        return true;
    }

    @Override
    public int deleteByMaterialStockId(String materialStockId) {
        return this.materialStorageMapper.delete(Wrappers.lambdaUpdate(MaterialStorageDO.class).eq(MaterialStorageDO::getMaterialStockId, materialStockId));
    }

    @Override
    public List<MaterialStorageDO> getOccupyMaterialStockListByTrayIds(List<String> trayIds) {
        return this.materialStorageMapper.selectOccupyMaterialStockListByTrayIds(trayIds);
    }

    @Override
    public List<MaterialStorageDO> getDetailMaterialStockListByTrayIds(List<String> trayIds) {
        return this.materialStorageMapper.selectDetailMaterialStockListByTrayIds(trayIds);
    }

    @Override
    public List<MaterialStorageDO> getFreeMaterialStockListByTrayId(String id) {
        return materialStorageMapper.selectFreeMaterialStockListByTrayId(id);
    }

    @Override
    public List<MaterialStorageDO> getMaterialStorageListByContainerStockId(String containerStockId) {
        return this.materialStorageMapper.selectMaterialStorageListByContainerStockId(containerStockId);
    }


    @Override
    public List<MaterialStorageDO> getAllMaterialStorageListByContainerStockId(String containerStockId) {
        return this.materialStorageMapper.selectAllMaterialStorageListByContainerStockId(containerStockId);
    }

    @Override
    public List<MaterialStorageDO> getMaterialStorageListByContainerStockIds(List<String> containerStockIds) {
        return this.materialStorageMapper.selectMaterialStorageListByContainerStockIds(containerStockIds);
    }

    @Override
    public MaterialStorageDO getMaterialStorageByStorageCode(String storageCode) {
        return this.materialStorageMapper.selectOne(Wrappers.lambdaQuery(MaterialStorageDO.class).eq(MaterialStorageDO::getStorageCode, storageCode));
    }

    @Override
    public String getStorageIdByLocationId(String locationId) {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockByLocationId(locationId);
        if (materialStockList.size() == 1) {
            List<MaterialStorageDO> materialStorageList = this.getMaterialStorageListByContainerStockId(materialStockList.get(0).getId());
            if (materialStorageList.size() == 1) {
                return materialStorageList.get(0).getId();
            }
        }
        return null;
    }

    @Override
    public String getStorageIdByMaterialStockIdAndSite(String materialStockId, int site) {
        MaterialConfigDO materialConfigDO = materialConfigService.getMaterialConfigByMaterialStockId(materialStockId);
        if (materialConfigDO == null) {
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }

        List<MaterialStorageDO> materialStorageList = this.getMaterialStorageListByContainerStockId(materialStockId);
        if (site < 1 || site > materialStorageList.size()) {
            throw exception(MATERIAL_STORAGE_BIN_NOT_EXISTS);
        }

        int layer = materialConfigDO.getMaterialLayer() == null ? 1 : materialConfigDO.getMaterialLayer();
        int row = materialConfigDO.getMaterialRow() == null ? 1 : materialConfigDO.getMaterialRow();
        int col = materialConfigDO.getMaterialCol() == null ? 1 : materialConfigDO.getMaterialCol();

        // 根据 层 行 列 和给定储位号 匹配 储位编码
        int count = 0;
        int l = 1;
        int r = 1;
        int c = 1;
        for (l = 1; l < layer + 1; l++) {
            for (r = 1; r < row + 1; r++) {
                for (c = 1; c < col + 1; c++) {
                    count++;
                    if (count == site) {
                        break;
                    }
                }
            }
        }

        for (MaterialStorageDO materialStorageDO : materialStorageList) {
            int ll = materialStorageDO.getLayer() == null ? 1 : materialStorageDO.getLayer();
            int rr = materialStorageDO.getRow() == null ? 1 : materialStorageDO.getRow();
            int cc = materialStorageDO.getCol() == null ? 1 : materialStorageDO.getCol();
            if (ll == l && rr == r && cc == c) {
                if (Objects.equals(materialStorageDO.getValid(), DictConstants.INFRA_BOOLEAN_TINYINT_YES)) {
                    return materialStorageDO.getId();
                }
                throw exception(MATERIAL_STORAGE_BIN_NOT_EXISTS);
            }

        }

        throw exception(MATERIAL_STORAGE_BIN_NOT_EXISTS);

    }

}
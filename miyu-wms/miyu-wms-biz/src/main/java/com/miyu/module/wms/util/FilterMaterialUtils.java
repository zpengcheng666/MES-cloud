package com.miyu.module.wms.util;

import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterMaterialUtils {

    public static Map<String, MaterialStockDO> filter_Tp_Gz(Map<String, MaterialStockDO> allMaterialStockMap) {
        // 将托盘和工装去掉
        return allMaterialStockMap.entrySet().stream()
               .filter(entry ->!DictConstants.WMS_MATERIAL_TYPE_GZ.equals(entry.getValue().getMaterialType()) &&
                       !DictConstants.WMS_MATERIAL_TYPE_TP.equals(entry.getValue().getMaterialType()))
               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<MaterialStockDO> filter_Tp(List<MaterialStockDO> allMaterialStockList) {
        // 将托盘去掉
        return allMaterialStockList.stream()
               .filter(materialStock ->!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType()))
               .collect(Collectors.toList());
    }

    public static List<MaterialStockDO> filter_Tp_Gz(List<MaterialStockDO> allMaterialStockList) {
        // 将托盘和工装去掉
        return allMaterialStockList.stream()
                .filter(materialStock ->!DictConstants.WMS_MATERIAL_TYPE_GZ.equals(materialStock.getMaterialType()) &&
                        !DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType()))
                .collect(Collectors.toList());
    }
}

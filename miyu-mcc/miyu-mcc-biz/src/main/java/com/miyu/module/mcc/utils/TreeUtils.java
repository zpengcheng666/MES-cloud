package com.miyu.module.mcc.utils;

import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils{


    public static int getLevel(MaterialTypeDO node, List<MaterialTypeDO> nodeList) {
        int level = 0;
        String parentId = node.getParentId();

        while (!parentId.equals("0")) {
            for (MaterialTypeDO n : nodeList) {
                if (n.getId().equals(parentId) ) {
                    parentId = n.getParentId();
                    level++;
                    break;
                }
            }
        }
        return level;
    }


    public static String getCode(MaterialTypeDO node, List<MaterialTypeDO> nodeList) {
        String code = null;
        List<String> codes = new ArrayList<>();
        String parentId = node.getParentId();

        while (!parentId.equals("0")) {
            for (MaterialTypeDO n : nodeList) {
                if (n.getId().equals(parentId) ) {
                    parentId = n.getParentId();
                    codes.add(n.getCode());
                    break;
                }
            }
        }
        return code;
    }
}

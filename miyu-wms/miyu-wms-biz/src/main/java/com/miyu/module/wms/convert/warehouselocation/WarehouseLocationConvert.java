package com.miyu.module.wms.convert.warehouselocation;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WarehouseLocationConvert {

    WarehouseLocationConvert INSTANCE = Mappers.getMapper(WarehouseLocationConvert.class);

    // "#|#|C|-G|-L|-S"  -> CPDJK#CPDJCCQ#C1-G1-L1-S1
    default String convertNodeName(String code){
        String C = code.substring(code.lastIndexOf('C'), code.lastIndexOf('G'));
        String G = code.substring(code.lastIndexOf('G'), code.lastIndexOf('L'));
        String L = code.substring(code.lastIndexOf('L'), code.lastIndexOf('S'));
        String S = code.substring(code.lastIndexOf('S'), code.length()-1);
        System.out.println(C + G + L + S);
        return C + G + L + S;
    }
}

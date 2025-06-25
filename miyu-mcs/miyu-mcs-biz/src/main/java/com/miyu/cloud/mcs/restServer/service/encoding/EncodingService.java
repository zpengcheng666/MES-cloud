package com.miyu.cloud.mcs.restServer.service.encoding;

import com.miyu.cloud.mcs.dto.resource.McsMaterialConfigDTO;

public interface EncodingService {

    McsMaterialConfigDTO getMaterialConfig(McsMaterialConfigDTO materialConfigDTO);

    //配送生码
    String getDistributionCode();

    String getCodeyType(String typeCode);

    void updateMaterialConfigCodeStatus(String materialConfigNumber);
}

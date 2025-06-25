package com.miyu.module.pdm.service.part;


import com.miyu.module.pdm.controller.admin.part.vo.AddPartReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.PartAttachmentRespVO;
import com.miyu.module.pdm.controller.admin.part.vo.PartAttrReqVO;
import com.miyu.module.pdm.controller.admin.part.vo.PartInstanceListReqVO;
import com.miyu.module.pdm.dal.dataobject.attachment.PartAttachmentDO;
import com.miyu.module.pdm.dal.dataobject.document.PartDocumentVersionDO;
import com.miyu.module.pdm.dal.dataobject.master.PartMasterDO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.dal.dataobject.version.PartVersionDO;

import javax.validation.*;
import java.util.List;
import java.util.Map;


public interface PartService {

    PartVersionDO selectPartInfoById(String partVersionId);


//    List<Map<String, Object>> selectDetailsByPartVersionId(String partVersionId);

    List<PartInstanceDO> getPartInstanceList(PartInstanceListReqVO listReqVO);

    List<Map<String, Object>> selectDetailsByPartVersionId(String partVersionId);

    List<Map<String, Object>> getCombinedDataByPartVersionId(String partVersionId);

    PartDocumentVersionDO getDocumentInfoByPartVersionId(String partVersionId);

    List<Map<String, Object>> getPartAttrs(PartAttrReqVO reqVO);

    String getModelUrl(String fileName, String fileType);

    PartAttachmentDO findAttachmentsByDatapackageBomId(String id);

    void saveNewFile(PartAttachmentRespVO saveReqVO);

    void deleteNewFile(PartAttachmentRespVO saveReqVO);

    void addPart(AddPartReqVO reqVO);

    void deletePart(AddPartReqVO reqVO);

    List<PartMasterDO> getPartInfoList(String partNumber);
}

package com.miyu.module.pdm.service.dataImport;

import com.miyu.module.pdm.controller.admin.dataImport.vo.DataImportReqVO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 数据包导入 Service 接口
 *
 * @author liuy
 */
public interface DataImportService {

    /**
     * 数据包导入
     *
     * @param dataImportReqVO 导入信息
     */
    void dataImport(MultipartFile file, DataImportReqVO dataImportReqVO) throws Exception;
}

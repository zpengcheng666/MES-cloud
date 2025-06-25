package com.miyu.module.pdm.service.dataImport;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.google.gson.Gson;
import com.miyu.module.pdm.controller.admin.dataImport.vo.DataImportReqVO;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureListReqVO;
import com.miyu.module.pdm.dal.dataobject.dataStatistics.DataStatisticsDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureDO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureExcelDO;
import com.miyu.module.pdm.dal.mysql.dataStatistics.DataStatisticsMapper;
import com.miyu.module.pdm.dal.mysql.product.ProductMapper;
import com.miyu.module.pdm.dal.mysql.structure.StructureExcelMapper;
import com.miyu.module.pdm.dal.mysql.structure.StructureMapper;
import com.miyu.module.pdm.enums.DictConstants;
import com.miyu.module.pdm.netty.FileUtil;
import com.miyu.module.pdm.netty.SaccNettyClient;
import com.miyu.module.pdm.netty.ZipUtil;
import common.bean.SaccNettyBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.FileInputStream;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;

/**
 * 数据包导入 Service 实现类
 *
 * @author liuy
 */
@Service
@Validated
@Slf4j
public class DataImportServiceImpl implements DataImportService {

    @Value("${VAULT_DATAPAGE_SERVERIP}")
    private String vaultDataPageIp;

    @Value("${VAULT_DATAPAGE_BINDPORT}")
    private String vaultDatapageBindPort;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private StructureMapper structureMapper;

    @Resource
    private StructureExcelMapper structureExcelMapper;

    @Resource
    private DataStatisticsMapper dataStatisticsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public void dataImport(MultipartFile file, DataImportReqVO dataImportReqVO) throws Exception {
        //校验1：只允许导入zip格式文件
        if(file.getOriginalFilename().lastIndexOf(".zip") == -1) {
            throw exception(DATA_IMPORT_SUFFIX_ERROR);
        }
        //校验1：zip包是否存在
        DataStatisticsDO datapackage = dataStatisticsMapper.selectByName(file.getOriginalFilename());
        if(datapackage != null) {
            throw exception(DATA_IMPORT_EXITS);
        }

        //校验2：zip包内文件与数据包结构内文件匹配
        //2.1 预解压zip包，处理文件相对路径
//        File newFile = FileUtil.MultipartFileToFile(file);
        File newFile = FileUtil.convert(file);
        String zipPath = newFile.getAbsolutePath();
        System.out.println("数据包路径：" + zipPath);
        System.out.println("数据包解压开始......");
        String destDirPath = zipPath.substring(0, zipPath.lastIndexOf(".")).replaceAll("\\s*", "");
        //解压前先删除已存在文件夹及其子文件
        File destDir = new File(destDirPath);
        deleteFolder(destDir);
        Thread.sleep(2000);
        String unzipPath = ZipUtil.unZip(zipPath, destDirPath);
        System.out.println("数据包解压结束......");
        System.out.println("解压到：" + unzipPath);
        //用于截取zip包下文件相对路径
        List<String> zipFilePathList = new ArrayList<>();
        //zip包相对路径
        String tempPath = unzipPath.substring(0, unzipPath.lastIndexOf("\\")+1);
        getFiles(unzipPath, tempPath, zipFilePathList);
//        for(String zipFilePath: zipFilePathList) {
//            System.out.println("zip包内文件路径：" + zipFilePath);
//        }

        //2.2 处理数据包结构内文件路径
        StructureListReqVO reqVO = new StructureListReqVO();
        reqVO.setId(dataImportReqVO.getStructureId());
        //递归找子级节点
        List<StructureDO> childList = new ArrayList<>();
        List<StructureDO> childListOne = structureMapper.selectChildList(reqVO);
        if(childListOne != null && childListOne.size() > 0) {
            childList.addAll(childListOne);
            for(StructureDO child: childListOne) {
                reqVO.setId(child.getId());
                List<StructureDO> childListNew = getChildList(reqVO);
                if(childListNew != null && childListNew.size() > 0) {
                    childList.addAll(childListNew);
                }
            }
            //只过滤文件节点
            childList = childList.stream().filter(child -> (child.getType()==3)).collect(Collectors.toList());
        }
//        //数据包结构内文件路径
//        List<String> structureFilePathList = new ArrayList<>();
//        if(childList != null && childList.size() > 0) {
//            for(StructureDO child: childList) {
//                structureFilePathList.add(child.getAbsolutePath());
//                System.out.println("数据包结构内文件路径：" + child.getAbsolutePath());
//            }
//        }

        //2.3 校验文件类型、文件路径是否匹配
        List<StructureDO> fileTypeList = childList.stream().filter(child -> (child.getFileType()==DictConstants.FILE_TYPE_0)).collect(Collectors.toList());
        if(fileTypeList != null && fileTypeList.size() > 0) {
            List<String> zipFilePathListNew = zipFilePathList.stream().filter(zipFilePath -> (zipFilePath.substring(zipFilePath.lastIndexOf(".") + 1).equals(DictConstants.FILE_SUFFIX_0))).collect(Collectors.toList());
            if(zipFilePathListNew == null || zipFilePathListNew.size() == 0) {
                throw exception(DATA_IMPORT_MATCH_ERROR0);
            } else {
                for(StructureDO fileType: fileTypeList) {
                    String structureFilePathArr[] = fileType.getAbsolutePath().split("\\\\");
                    for(String zipFilePath: zipFilePathListNew) {
                        String zipFilePathArr[] = zipFilePath.split("\\\\");
                        if(structureFilePathArr.length != zipFilePathArr.length) {
                            throw exception(DATA_IMPORT_MATCH_PATH_ERROR0);
                        }
                    }
                }
            }
        }
        fileTypeList = childList.stream().filter(child -> (child.getFileType()==DictConstants.FILE_TYPE_1)).collect(Collectors.toList());
        if(fileTypeList != null && fileTypeList.size() > 0) {
            List<String> zipFilePathListNew = zipFilePathList.stream().filter(zipFilePath -> (zipFilePath.substring(zipFilePath.lastIndexOf(".") + 1).equals(DictConstants.FILE_SUFFIX_1))).collect(Collectors.toList());
            if(zipFilePathListNew == null || zipFilePathListNew.size() == 0) {
                throw exception(DATA_IMPORT_MATCH_ERROR1);
            } else {
                for(StructureDO fileType: fileTypeList) {
                    String structureFilePathArr[] = fileType.getAbsolutePath().split("\\\\");
                    for(String zipFilePath: zipFilePathListNew) {
                        String zipFilePathArr[] = zipFilePath.split("\\\\");
                        if(structureFilePathArr.length != zipFilePathArr.length) {
                            throw exception(DATA_IMPORT_MATCH_PATH_ERROR1);
                        }
                    }
                }
            }
        }
        fileTypeList = childList.stream().filter(child -> (child.getFileType()==DictConstants.FILE_TYPE_2)).collect(Collectors.toList());
        if(fileTypeList != null && fileTypeList.size() > 0) {
            List<String> zipFilePathListNew = zipFilePathList.stream().filter(zipFilePath -> (zipFilePath.substring(zipFilePath.lastIndexOf(".") + 1).equals(DictConstants.FILE_SUFFIX_2))).collect(Collectors.toList());
            if(zipFilePathListNew == null || zipFilePathListNew.size() == 0) {
                throw exception(DATA_IMPORT_MATCH_ERROR2);
            } else {
                //xlsx文件只允许有一个
                if(zipFilePathListNew.size() > 1) {
                    throw exception(DATA_IMPORT_MATCH_NUM_ERROR2);
                }
                for(StructureDO fileType: fileTypeList) {
                    String structureFilePathArr[] = fileType.getAbsolutePath().split("\\\\");
                    for(String zipFilePath: zipFilePathListNew) {
                        String zipFilePathArr[] = zipFilePath.split("\\\\");
                        if(structureFilePathArr.length != zipFilePathArr.length) {
                            throw exception(DATA_IMPORT_MATCH_PATH_ERROR2);
                        }
                    }
                }

                //2.4 校验excel规则是否匹配
                String excelFilePath = tempPath + zipFilePathListNew.get(0);
                System.out.println("预解压xlsx路径：" + excelFilePath);
                //读取excel
                FileInputStream fis = null;
                XSSFWorkbook wb = null;
                try {
                    File excelFile = new File(excelFilePath);
                    fis = new FileInputStream(excelFile);
                    //创建XSSFWorkbook对象
                    wb = new XSSFWorkbook(fis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //获取第一个sheet页
                XSSFSheet sheet = wb.getSheetAt(0);

                //获取excel规则详情
                Long structureId = fileTypeList.get(0).getId();
                List<StructureExcelDO> structureExcelList = structureExcelMapper.selectListByStructureId(structureId);
                structureExcelList.sort(Comparator.comparing(StructureExcelDO::getRowNum));
                structureExcelList.sort(Comparator.comparing(StructureExcelDO::getColumnNum));
                //按照excel各项不重复
                List excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_0)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配图号所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_0)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR0);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_1)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配单机数量所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_1)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR1);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_2)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配数模版次所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_2)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR2);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_3)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配类别所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_3)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR3);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_4)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配架次所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_4)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR4);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_5)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配EDRN编号所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_5)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR5);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_6)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配EDRN版次所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_6)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR6);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_7)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配状态表所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_7)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR7);
                    }
                }
                excelList = structureExcelList.stream().filter(excel -> (excel.getContentType()==DictConstants.CONTENT_TYPE_8)).collect(Collectors.toList());
                if(excelList != null && excelList.size() > 0) {
                    //匹配状态表更改单号所在行列
                    StructureExcelDO excel = (StructureExcelDO) excelList.get(0);
                    XSSFRow rows = sheet.getRow(excel.getRowNum()-1);
                    XSSFCell cell = rows.getCell(excel.getColumnNum()-1);
                    if(!cell.getStringCellValue().equals(DictConstants.EXCEL_CONTENT_8)) {
                        throw exception(DATA_IMPORT_MATCH_EXCEL_ERROR8);
                    }
                }
                if(fis != null){
                    fis.close();
                }
            }
        }
        fileTypeList = childList.stream().filter(child -> (child.getFileType()==DictConstants.FILE_TYPE_3)).collect(Collectors.toList());
        if(fileTypeList != null && fileTypeList.size() > 0) {
            List<String> zipFilePathListNew = zipFilePathList.stream().filter(zipFilePath -> (zipFilePath.substring(zipFilePath.lastIndexOf(".") + 1).equals(DictConstants.FILE_SUFFIX_3))).collect(Collectors.toList());
            if(zipFilePathListNew == null || zipFilePathListNew.size() == 0) {
                throw exception(DATA_IMPORT_MATCH_ERROR3);
            } else {
                //xml文件只允许有一个
                if(zipFilePathListNew.size() > 1) {
                    throw exception(DATA_IMPORT_MATCH_NUM_ERROR3);
                }
                for(StructureDO fileType: fileTypeList) {
                    String structureFilePathArr[] = fileType.getAbsolutePath().split("\\\\");
                    for(String zipFilePath: zipFilePathListNew) {
                        String zipFilePathArr[] = zipFilePath.split("\\\\");
                        if(structureFilePathArr.length != zipFilePathArr.length) {
                            throw exception(DATA_IMPORT_MATCH_PATH_ERROR3);
                        }
                    }
                }
            }
        }

        //各规则校验成功后，连接netty服务上传zip包
        ProductDO product = productMapper.selectById(dataImportReqVO.getRootProductId());
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("projectCode", dataImportReqVO.getProjectCode());
        map.put("rootProductId", dataImportReqVO.getRootProductId());
        map.put("companyId", dataImportReqVO.getCompanyId());
        map.put("companyName", dataImportReqVO.getCompanyName());
        map.put("structureId", dataImportReqVO.getStructureId());
        map.put("productNumber", product.getProductNumber());
        map.put("issuanceType", "0");
        map.put("datapackageType", "0");
        map.put("operator", SecurityFrameworkUtils.getLoginUser().getId());

        SaccNettyBean wnb = new SaccNettyBean();
        wnb.setFile(newFile);
        wnb.setFileLength(file.getSize());
        wnb.setFileName(file.getOriginalFilename());
        wnb.setStarPos(0);
        if(product.getProductNumber().equals("A220")) {
            wnb.setMethodName("Sacc_A220");
        } else if(product.getProductNumber().equals("B777客改货")) {
            wnb.setMethodName("Sacc_B777");
        }
        wnb.setUpOrDownLoadFlag("upload");
        wnb.setParamJsonStr(new Gson().toJson(map));
        try {
            new SaccNettyClient().connect(new Integer(vaultDatapageBindPort), vaultDataPageIp, wnb);
        } catch(Exception ex) {
            throw exception(DATA_IMPORT_CONNECT_ERROR);
        }
    }

    public static void getFiles(String path, String tempPath, List<String> filePathList) {
        File file = new File(path);
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(File f : files) {
                if(f.isDirectory()) {
                    getFiles(f.getPath(), tempPath, filePathList);
                } else {
                    filePathList.add(f.getPath().substring(tempPath.length(), f.getPath().length()));
                }
            }
        } else {
            filePathList.add(file.getPath().substring(tempPath.length(), file.getPath().length()));
        }
    }

    public List<StructureDO> getChildList(StructureListReqVO reqVO) {
        List<StructureDO> childList = structureMapper.selectChildList(reqVO);
        if(childList == null || childList.isEmpty()) {
            return null;
        }
        return childList;
    }

    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        folder.delete();
    }
}

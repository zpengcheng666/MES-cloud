package com.miyu.module.pdm.netty;

import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataObjectUtil {
    public static List<DataObjectDO> getDO6Forinsert(String rootproductId, String customizedIndex){
        List<DataObjectDO> reList=new ArrayList<DataObjectDO>();

        DataObjectDO partmaster =new DataObjectDO();
        partmaster.setId(UUIDUtil.randomUUID32());
        partmaster.setRootproductId(rootproductId);
        partmaster.setStdDataObject("PartMaster");
        partmaster.setCustomizedDataObject(customizedIndex+"_part_master");
        partmaster.setCustomizedIndex(customizedIndex);
        partmaster.setCustomizedType(1);
        partmaster.setTableName("pdm_"+customizedIndex+"_part_master");
        partmaster.setDescription("");
        partmaster.setStatus("0");
        partmaster.setIntrinsicAttrs("[{\"attr_name\":\"part_number\",\"attr_alias\":\"零部件图号\",\"attr_type\":\"3\",\"attr_length\":\"50\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"part_name\",\"attr_alias\":\"零件名称\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"rootproduct_id\",\"attr_alias\":\"产品根节点ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"part_type\",\"attr_alias\":\"零部件类型(CATPart零件 GT1一批多件 GT2成组件 Tooling工装)\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4}]");
        partmaster.setSerialNumber(1);
        partmaster.setCreateTime(LocalDateTime.now());

        DataObjectDO partversion =new DataObjectDO();
        partversion.setId(UUIDUtil.randomUUID32());
        partversion.setRootproductId(rootproductId);
        partversion.setStdDataObject("PartVersion");
        partversion.setCustomizedDataObject(customizedIndex+"_part_version");
        partversion.setCustomizedIndex(customizedIndex);
        partversion.setCustomizedType(1);
        partversion.setTableName("pdm_"+customizedIndex+"_part_version");
        partversion.setDescription("");
        partversion.setStatus("0");
        partversion.setIntrinsicAttrs("[{\"attr_name\":\"part_master_id\",\"attr_alias\":\"零部件id\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"part_version\",\"attr_alias\":\"零部件版本\",\"attr_type\":\"3\",\"attr_length\":\"5\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"document_version_id\",\"attr_alias\":\"文档版本ID-主文档ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3}]");
        partversion.setSerialNumber(2);
        partversion.setCreateTime(LocalDateTime.now());

        DataObjectDO documentmaster =new DataObjectDO();
        documentmaster.setId(UUIDUtil.randomUUID32());
        documentmaster.setRootproductId(rootproductId);
        documentmaster.setStdDataObject("DocumentMaster");
        documentmaster.setCustomizedDataObject(customizedIndex+"_document_master");
        documentmaster.setCustomizedIndex(customizedIndex);
        documentmaster.setCustomizedType(1);
        documentmaster.setTableName("pdm_"+customizedIndex+"_document_master");
        documentmaster.setDescription("");
        documentmaster.setStatus("0");
        documentmaster.setIntrinsicAttrs("[{\"attr_name\":\"document_name\",\"attr_alias\":\"文档名称\",\"attr_type\":\"3\",\"attr_length\":\"50\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"document_type\",\"attr_alias\":\"文档类型\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"rootproduct_id\",\"attr_alias\":\"产品表ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3}]");
        documentmaster.setSerialNumber(4);
        documentmaster.setCreateTime(LocalDateTime.now());

        DataObjectDO documentversion =new DataObjectDO();
        documentversion.setId(UUIDUtil.randomUUID32());
        documentversion.setRootproductId(rootproductId);
        documentversion.setStdDataObject("DocumentVersion");
        documentversion.setCustomizedDataObject(customizedIndex+"_document_version");
        documentversion.setCustomizedIndex(customizedIndex);
        documentversion.setCustomizedType(1);
        documentversion.setTableName("pdm_"+customizedIndex+"_document_version");
        documentversion.setDescription("");
        documentversion.setStatus("0");
        documentversion.setIntrinsicAttrs("[{\"attr_name\":\"document_master_id\",\"attr_alias\":\"DMID-文档id\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"document_version\",\"attr_alias\":\"版本号\",\"attr_type\":\"3\",\"attr_length\":\"10\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2}]");
        documentversion.setSerialNumber(5);
        documentversion.setCreateTime(LocalDateTime.now());

        DataObjectDO documentrevision =new DataObjectDO();
        documentrevision.setId(UUIDUtil.randomUUID32());
        documentrevision.setRootproductId(rootproductId);
        documentrevision.setStdDataObject("DocumentRevision");
        documentrevision.setCustomizedDataObject(customizedIndex+"_document_revision");
        documentrevision.setCustomizedIndex(customizedIndex);
        documentrevision.setCustomizedType(1);
        documentrevision.setTableName("pdm_"+customizedIndex+"_document_revision");
        documentrevision.setDescription("");
        documentrevision.setStatus("0");
        documentrevision.setIntrinsicAttrs("[{\"attr_name\":\"document_version_id\",\"attr_alias\":\"文档版本id\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"revision\",\"attr_alias\":\"修订号\",\"attr_type\":\"3\",\"attr_length\":\"2\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"file_id\",\"attr_alias\":\"文件ID-主文件ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3}]");
        documentrevision.setSerialNumber(6);
        documentrevision.setCreateTime(LocalDateTime.now());

        DataObjectDO documentfile =new DataObjectDO();
        documentfile.setId(UUIDUtil.randomUUID32());
        documentfile.setRootproductId(rootproductId);
        documentfile.setStdDataObject("DocumentFile");
        documentfile.setCustomizedDataObject(customizedIndex+"_document_file");
        documentfile.setCustomizedIndex(customizedIndex);
        documentfile.setCustomizedType(1);
        documentfile.setTableName("pdm_"+customizedIndex+"_document_file");
        documentfile.setDescription("");
        documentfile.setStatus("0");
        documentfile.setIntrinsicAttrs("[{\"attr_name\":\"file_name\",\"attr_alias\":\"文件名\",\"attr_type\":\"3\",\"attr_length\":\"100\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"file_type\",\"attr_alias\":\"文件类型\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"vault_url\",\"attr_alias\":\"电子仓库地址(不带后缀)\",\"attr_type\":\"3\",\"attr_length\":\"200\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"md5\",\"attr_alias\":\"文件MD5\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4}]");
        documentfile.setSerialNumber(7);
        documentfile.setCreateTime(LocalDateTime.now());
        reList.add(partmaster);
        reList.add(partversion);
        reList.add(documentmaster);
        reList.add(documentversion);
        reList.add(documentrevision);
        reList.add(documentfile);
        return reList;
    }

    public static List<DataObjectDO> getDO7Forinsert(String rootproductId,String customizedIndex){
        List<DataObjectDO> reList=new ArrayList<DataObjectDO>();

        DataObjectDO partmaster =new DataObjectDO();
        partmaster.setId(UUIDUtil.randomUUID32());
        partmaster.setRootproductId(rootproductId);
        partmaster.setStdDataObject("PartMaster");
        partmaster.setCustomizedDataObject(customizedIndex+"_part_master");
        partmaster.setCustomizedIndex(customizedIndex);
        partmaster.setCustomizedType(0);
        partmaster.setTableName("pdm_"+customizedIndex+"_part_master");
        partmaster.setDescription("");
        partmaster.setStatus("0");
        partmaster.setIntrinsicAttrs("[{\"attr_name\":\"part_number\",\"attr_alias\":\"零部件图号\",\"attr_type\":\"3\",\"attr_length\":\"50\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"part_name\",\"attr_alias\":\"零件名称\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"rootproduct_id\",\"attr_alias\":\"产品根节点ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"part_type\",\"attr_alias\":\"零部件类型(CATPart零件 GT1一坯多件 GT2成组件)\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4},{\"attr_name\":\"create_time\",\"attr_alias\":\"创建时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":5},{\"attr_name\":\"last_update_time\",\"attr_alias\":\"最后更新时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":6}]");
        partmaster.setCustomizedAttrs("");
        partmaster.setSerialNumber(1);
        partmaster.setCreateTime(LocalDateTime.now());

        DataObjectDO partversion =new DataObjectDO();
        partversion.setId(UUIDUtil.randomUUID32());
        partversion.setRootproductId(rootproductId);
        partversion.setStdDataObject("PartVersion");
        partversion.setCustomizedDataObject(customizedIndex+"_part_version");
        partversion.setCustomizedIndex(customizedIndex);
        partversion.setCustomizedType(0);
        partversion.setTableName("pdm_"+customizedIndex+"_part_version");
        partversion.setDescription("");
        partversion.setStatus("0");
        partversion.setIntrinsicAttrs("[{\"attr_name\":\"part_master_id\",\"attr_alias\":\"零部件id\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"part_version\",\"attr_alias\":\"零部件版本\",\"attr_type\":\"3\",\"attr_length\":\"5\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"document_version_id\",\"attr_alias\":\"文档版本ID-主文档ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"create_time\",\"attr_alias\":\"创建时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4},{\"attr_name\":\"last_update_time\",\"attr_alias\":\"最后更新时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":5}]");
        partversion.setSerialNumber(2);
        partversion.setCreateTime(LocalDateTime.now());

        DataObjectDO partinstance =new DataObjectDO();
        partinstance.setId(UUIDUtil.randomUUID32());
        partinstance.setRootproductId(rootproductId);
        partinstance.setStdDataObject("PartInstance");
        partinstance.setCustomizedDataObject(customizedIndex+"_part_instance");
        partinstance.setCustomizedIndex(customizedIndex);
        partinstance.setCustomizedType(0);
        partinstance.setTableName("pdm_"+customizedIndex+"_part_instance");
        partinstance.setDescription("");
        partinstance.setStatus("0");
        partinstance.setIntrinsicAttrs("[\r\n" +
                "	{\"attr_name\":\"name\",\"attr_alias\":\"实例号\",\"attr_type\":\"3\",\"attr_length\":\"100\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},\r\n" +
                "	{\"attr_name\":\"rootproduct_id\",\"attr_alias\":\"产品根节点ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},\r\n" +
                "	{\"attr_name\":\"part_version_id\",\"attr_alias\":\"零部件版本ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},\r\n" +
                "	{\"attr_name\":\"parent_id\",\"attr_alias\":\"父节点ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4},\r\n" +
                "	{\"attr_name\":\"customized_index\",\"attr_alias\":\"客户化标识\",\"attr_type\":\"3\",\"attr_length\":\"100\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":5},\r\n" +
                "	{\"attr_name\":\"serial_number\",\"attr_alias\":\"排序\",\"attr_type\":\"1\",\"attr_length\":\"11\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":6},\r\n" +
                "	{\"attr_name\":\"vmatrix01\",\"attr_alias\":\"位置坐标系，坐标原点X值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":7},\r\n" +
                "	{\"attr_name\":\"vmatrix02\",\"attr_alias\":\"位置坐标系，坐标原点Y值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":8},\r\n" +
                "	{\"attr_name\":\"vmatrix03\",\"attr_alias\":\"位置坐标系，坐标原点Z值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":9},\r\n" +
                "	{\"attr_name\":\"vmatrix04\",\"attr_alias\":\"位置坐标系，X轴方向向量X值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":10},\r\n" +
                "	{\"attr_name\":\"vmatrix05\",\"attr_alias\":\"位置坐标系，X轴方向向量Y值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":11},\r\n" +
                "	{\"attr_name\":\"vmatrix06\",\"attr_alias\":\"位置坐标系，X轴方向向量Z值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":12},\r\n" +
                "	{\"attr_name\":\"vmatrix07\",\"attr_alias\":\"位置坐标系，Y轴方向向量X值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":13},\r\n" +
                "	{\"attr_name\":\"vmatrix08\",\"attr_alias\":\"位置坐标系，Y轴方向向量Y值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":14},\r\n" +
                "	{\"attr_name\":\"vmatrix09\",\"attr_alias\":\"位置坐标系，Y轴方向向量Z值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":15},\r\n" +
                "	{\"attr_name\":\"vmatrix10\",\"attr_alias\":\"位置坐标系，Z轴方向向量X值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":16},\r\n" +
                "	{\"attr_name\":\"vmatrix11\",\"attr_alias\":\"位置坐标系，Z轴方向向量Y值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":17},\r\n" +
                "	{\"attr_name\":\"vmatrix12\",\"attr_alias\":\"位置坐标系，Z轴方向向量Z值\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":18},\r\n" +
                "	{\"attr_name\":\"type\",\"attr_alias\":\"节点类型，root产品跟节点，comp部件，part零件\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":19},\r\n" +
                "	{\"attr_name\":\"target_id\",\"attr_alias\":\"对象id(节点类型对应部件时存部件id)\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":20}\r\n" +
                "]");
        partinstance.setSerialNumber(3);
        partinstance.setCreateTime(LocalDateTime.now());

        DataObjectDO documentmaster =new DataObjectDO();
        documentmaster.setId(UUIDUtil.randomUUID32());
        documentmaster.setRootproductId(rootproductId);
        documentmaster.setStdDataObject("DocumentMaster");
        documentmaster.setCustomizedDataObject(customizedIndex+"_document_master");
        documentmaster.setCustomizedIndex(customizedIndex);
        documentmaster.setCustomizedType(0);
        documentmaster.setTableName("pdm_"+customizedIndex+"_document_master");
        documentmaster.setDescription("");
        documentmaster.setStatus("0");
        documentmaster.setIntrinsicAttrs("[{\"attr_name\":\"document_name\",\"attr_alias\":\"文档名称\",\"attr_type\":\"3\",\"attr_length\":\"50\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"document_type\",\"attr_alias\":\"文档类型\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"rootproduct_id\",\"attr_alias\":\"产品表ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"create_time\",\"attr_alias\":\"创建时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4},{\"attr_name\":\"last_update_time\",\"attr_alias\":\"最后更新时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":5}]");
        documentmaster.setSerialNumber(4);
        documentmaster.setCreateTime(LocalDateTime.now());

        DataObjectDO documentversion =new DataObjectDO();
        documentversion.setId(UUIDUtil.randomUUID32());
        documentversion.setRootproductId(rootproductId);
        documentversion.setStdDataObject("DocumentVersion");
        documentversion.setCustomizedDataObject(customizedIndex+"_document_version");
        documentversion.setCustomizedIndex(customizedIndex);
        documentversion.setCustomizedType(0);
        documentversion.setTableName("pdm_"+customizedIndex+"_document_version");
        documentversion.setDescription("");
        documentversion.setStatus("0");
        documentversion.setIntrinsicAttrs("[{\"attr_name\":\"document_master_id\",\"attr_alias\":\"DMID-文档id\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"document_version\",\"attr_alias\":\"版本号\",\"attr_type\":\"3\",\"attr_length\":\"10\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"create_time\",\"attr_alias\":\"创建时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"last_update_time\",\"attr_alias\":\"最后更新时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4}]");
        documentversion.setSerialNumber(5);
        documentversion.setCreateTime(LocalDateTime.now());

        DataObjectDO documentrevision =new DataObjectDO();
        documentrevision.setId(UUIDUtil.randomUUID32());
        documentrevision.setRootproductId(rootproductId);
        documentrevision.setStdDataObject("DocumentRevision");
        documentrevision.setCustomizedDataObject(customizedIndex+"_document_revision");
        documentrevision.setCustomizedIndex(customizedIndex);
        documentrevision.setCustomizedType(0);
        documentrevision.setTableName("pdm_"+customizedIndex+"_document_revision");
        documentrevision.setDescription("");
        documentrevision.setStatus("0");
        documentrevision.setIntrinsicAttrs("[{\"attr_name\":\"document_version_id\",\"attr_alias\":\"文档版本id\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"revision\",\"attr_alias\":\"修订号\",\"attr_type\":\"3\",\"attr_length\":\"2\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"file_id\",\"attr_alias\":\"文件ID-主文件ID\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"create_time\",\"attr_alias\":\"创建时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":4},{\"attr_name\":\"last_update_time\",\"attr_alias\":\"最后更新时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":5}]");
        documentrevision.setSerialNumber(6);
        documentrevision.setCreateTime(LocalDateTime.now());

        DataObjectDO documentfile =new DataObjectDO();
        documentfile.setId(UUIDUtil.randomUUID32());
        documentfile.setRootproductId(rootproductId);
        documentfile.setStdDataObject("DocumentFile");
        documentfile.setCustomizedDataObject(customizedIndex+"_document_file");
        documentfile.setCustomizedIndex(customizedIndex);
        documentfile.setCustomizedType(0);
        documentfile.setTableName("pdm_"+customizedIndex+"_document_file");
        documentfile.setDescription("");
        documentfile.setStatus("0");
        documentfile.setIntrinsicAttrs("[{\"attr_name\":\"file_name\",\"attr_alias\":\"文件名\",\"attr_type\":\"3\",\"attr_length\": \"100\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":1},{\"attr_name\":\"file_type\",\"attr_alias\":\"文件类型\",\"attr_type\":\"3\",\"attr_length\":\"20\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":2},{\"attr_name\":\"vault_url\",\"attr_alias\":\"电子仓库地址(不带后缀)\",\"attr_type\":\"3\",\"attr_length\":\"200\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":3},{\"attr_name\":\"md5\",\"attr_alias\":\"文件MD5\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\": 4},{\"attr_name\":\"create_time\",\"attr_alias\":\"创建时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":5},{\"attr_name\":\"last_update_time\",\"attr_alias\":\"最后更新时间\",\"attr_type\":\"3\",\"attr_length\":\"32\",\"attr_unit\":\"\",\"attr_default\":\"\",\"is_more_value\":\"\",\"is_effective\":\"\",\"effective_value\":\"\",\"LAY_TABLE_INDEX\":6}]");
        documentfile.setSerialNumber(7);
        documentfile.setCreateTime(LocalDateTime.now());
        reList.add(partmaster);
        reList.add(partversion);
        reList.add(partinstance);
        reList.add(documentmaster);
        reList.add(documentversion);
        reList.add(documentrevision);
        reList.add(documentfile);
        return reList;
    }
}

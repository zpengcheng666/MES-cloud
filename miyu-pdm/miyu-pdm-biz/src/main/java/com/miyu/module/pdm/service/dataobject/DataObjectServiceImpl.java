package com.miyu.module.pdm.service.dataobject;


import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miyu.module.pdm.controller.admin.dataobject.vo.DataObjectPageReqVO;
import com.miyu.module.pdm.controller.admin.dataobject.vo.DataObjectRespVO;
import com.miyu.module.pdm.controller.admin.dataobject.vo.DataObjectSaveReqVO;
import com.miyu.module.pdm.controller.admin.dataobject.vo.WinDataObjectAttrs;
import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;
import com.miyu.module.pdm.dal.dataobject.part.PartInstanceDO;
import com.miyu.module.pdm.dal.mysql.dataobject.DataObjectMapper;
import com.miyu.module.pdm.netty.DataObjectUtil;
import com.miyu.module.pdm.netty.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.DATA_OBJECT_EXISTS;
import static org.reflections.Reflections.log;


@Service
@Validated
public class DataObjectServiceImpl implements DataObjectService {

    @Resource
    private DataObjectMapper dataObjectMapper;

    @Override
    public DataObjectDO getDataObject(String id) {
        return dataObjectMapper.selectById(id);
    }

    @Override
    public PageResult<DataObjectDO> getDataObjectPage(DataObjectPageReqVO pageReqVO) {
        return dataObjectMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DataObjectRespVO> getCustomizedIndicesByRootProductId(String rootproductId) {
        List<DataObjectDO> list = dataObjectMapper.getCustomizedIndicesByRootProductId(rootproductId);
        return BeanUtils.toBean(list, DataObjectRespVO.class);
    }

    @Override
    public int insertIndex(DataObjectSaveReqVO createReqVO) {
        String customizedIndex = createReqVO.getCustomizedIndex();
        String rootproductId = createReqVO.getRootproductId();
        int count = dataObjectMapper.selectCustomizedIndexCount(customizedIndex);
        if (count > 0) {
            throw exception(DATA_OBJECT_EXISTS);
        }
        int i = 1;
        List<DataObjectDO> list = DataObjectUtil.getDO6Forinsert(rootproductId, customizedIndex);
        for (DataObjectDO pdm_data_object : list) {
            i = i & dataObjectMapper.insertSelective(pdm_data_object);
        }
        return i;
    }

    @Override
    public int delIndex(DataObjectPageReqVO pageReqVO) {
        String rootproductId = pageReqVO.getRootproductId();
        String customizedIndex = pageReqVO.getCustomizedIndex();
        DataObjectPageReqVO obj = new DataObjectPageReqVO();
        obj.setRootproductId(rootproductId);
        obj.setCustomizedIndex(customizedIndex);
        return dataObjectMapper.delIndex(obj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertOrUpdate(DataObjectSaveReqVO createReqVO) {
        //根据老的客户化标识和产品id 删除老客户化标识的数据
        //然后新增新的客户化标识的数据并且判断是否和原来的客户化标识一致 如果一致不做任何操作
        try {
            // 判断新老客户化标识是否一致
            //如果不一致则删除老客户化标识的数据
            if (!createReqVO.getCustomizedIndex().equals(createReqVO.getCustomizedIndexOld())) {
                DataObjectPageReqVO deleteCriteria = new DataObjectPageReqVO();
                deleteCriteria.setCustomizedIndex(createReqVO.getCustomizedIndexOld());
                deleteCriteria.setRootproductId(createReqVO.getRootproductId());
                dataObjectMapper.delIndex(deleteCriteria);
                // 更新数据
                String customizedIndex = createReqVO.getCustomizedIndex();
                String rootproductId = createReqVO.getRootproductId();
                int count = dataObjectMapper.selectCustomizedIndexCount(customizedIndex);
                if (count > 0) {
                    throw exception(DATA_OBJECT_EXISTS);
                }
                int i = 1;
                List<DataObjectDO> list = DataObjectUtil.getDO6Forinsert(rootproductId, customizedIndex);
                for (DataObjectDO pdm_data_object : list) {
                    i = i & dataObjectMapper.insertSelective(pdm_data_object);
                }
                return i;

            } else {
                // 如果新老客户化标识一致，不做任何操作，直接返回0或特定代码表示无需更新
                return 0;
            }
        } catch (Exception e) {
            // 异常处理逻辑
            log.error("An error occurred while processing the update or create operation", e);
            return -1; // 返回一个错误代码或自定义异常处理
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIndex(DataObjectSaveReqVO createReqVO) {
        DataObjectDO updateObj = BeanUtils.toBean(createReqVO, DataObjectDO.class);
        //如果createReqVO对象中的customizedAttrsList不为空，则将其转换为JSON字符串 最后更新到数据库中
        if(createReqVO.getCustomizedAttrsList() != null) {
            String jsonStr = new Gson().toJson(createReqVO.getCustomizedAttrsList());
            updateObj.setCustomizedAttrs(jsonStr);
        }
        dataObjectMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createIndex(DataObjectSaveReqVO createReqVO) {
        String customizedIndex = createReqVO.getCustomizedIndex();
        int reInt = 1;
        PartInstanceDO obl = new PartInstanceDO();
        List<DataObjectDO> list = dataObjectMapper.selectAllByRIDAndCID(createReqVO);
        for (DataObjectDO obj : list) {
            List<WinDataObjectAttrs> attrsList = new ArrayList<WinDataObjectAttrs>();
            String iStr = obj.getIntrinsicAttrs();
            if (StringUtils.isNotBlank(iStr)) {
                List<WinDataObjectAttrs> ilist = new Gson().fromJson(iStr,
                        new TypeToken<List<WinDataObjectAttrs>>() {
                        }.getType());
                attrsList.addAll(ilist);
            }
            String cStr = obj.getCustomizedAttrs();
            if (StringUtils.isNotBlank(cStr)) {
                List<WinDataObjectAttrs> clist = new Gson().fromJson(cStr,
                        new TypeToken<List<WinDataObjectAttrs>>() {
                        }.getType());
                attrsList.addAll(clist);
            }
            String tableName = obj.getTableName();
            System.out.println(tableName);
            dataObjectMapper.createTable(tableName, attrsList);
            DataObjectDO obk = new DataObjectDO();
            obk.setId(obj.getId());
            obk.setStatus("1");
            reInt = reInt & dataObjectMapper.updateByPrimaryKeySelective(obk);
            if ("PartInstance".equals(obj.getStdDataObject())) {
                obl.setId(UUIDUtil.randomUUID32());
                obl.setName(obj.getProductNumber());
                obl.setRootproductId(obj.getRootproductId());
                obl.setParentId("0");
                obl.setCustomizedIndex(obj.getCustomizedIndex());
                obl.setSerialNumber("1");
                obl.setVmatrix01("0");
                obl.setVmatrix02("0");
                obl.setVmatrix03("0");
                obl.setVmatrix04("1");
                obl.setVmatrix05("0");
                obl.setVmatrix06("0");
                obl.setVmatrix07("0");
                obl.setVmatrix08("1");
                obl.setVmatrix09("0");
                obl.setVmatrix10("0");
                obl.setVmatrix11("0");
                obl.setVmatrix12("1");
                obl.setType("root");
            }
        }
        dataObjectMapper.createMiddleTable(customizedIndex);
        if (list.get(0).getCustomizedType() == 0) {
            dataObjectMapper.insertPartInstanceSelective(obl);
        }
        return reInt;
    }

    @Override
    public List<Map<String, Object>> selectOneById(String id) {
        // 产品数据对象动态属性
        List<Map<String, Object>> dataAttrs = new ArrayList<>();
        // 查询数据
        Map<String, Object> resultMap = dataObjectMapper.selectOneById(id);
        if (resultMap != null) {
            // 尝试从结果中获取"customized_attrs"并转换为List
            String customizedAttrsJson = (String) resultMap.get("customized_attrs");
            if (customizedAttrsJson != null && !customizedAttrsJson.isEmpty()) {
                // 使用Gson将JSON字符串转换为List
                if (StringUtils.isNotBlank(customizedAttrsJson)) {
                    dataAttrs = new Gson().fromJson(customizedAttrsJson,
                            new TypeToken<List<Map<String, Object>>>() {
                            }.getType());
                }
            }
        }
        return dataAttrs;
    }

    @Override
    public int savePM(Map<String, Object> map) {
        return dataObjectMapper.savePM(map);
    }
    @Override
    public int savePV(Map<String, Object> pvMap) {
        return dataObjectMapper.savePV(pvMap);
    }

    @Override
    public int savePartMaster(Map<String, Object> pmMap) {
        return dataObjectMapper.savePartMaster(pmMap);
    }

    @Override
    public int savePartVersion(Map<String, Object> pvMap) {
        return dataObjectMapper.savePartVersion(pvMap);
    }
}
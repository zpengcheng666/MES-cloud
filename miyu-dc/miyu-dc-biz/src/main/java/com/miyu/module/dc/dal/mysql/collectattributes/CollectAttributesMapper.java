package com.miyu.module.dc.dal.mysql.collectattributes;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.dc.controller.admin.device.vo.DeviceSaveReqVO;
import com.miyu.module.dc.dal.dataobject.collectattributes.CollectAttributesDO;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 采集属性 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CollectAttributesMapper extends BaseMapperX<CollectAttributesDO> {


    default List<CollectAttributesDO> getAttributesList(String id){
        return selectList(new LambdaQueryWrapperX<CollectAttributesDO>()
                .eq(CollectAttributesDO::getProductTypeId,id));
    }


    @Delete("delete from dc_collect_attributes where product_type_id = #{id}")
    void deleteAll(@Param("id") String id);

    @TenantIgnore
    default List<CollectAttributesDO> getListByProductTypeId(String id){
        return selectList(new LambdaQueryWrapperX<CollectAttributesDO>()
                .eq(CollectAttributesDO::getProductTypeId, id));
    }


}
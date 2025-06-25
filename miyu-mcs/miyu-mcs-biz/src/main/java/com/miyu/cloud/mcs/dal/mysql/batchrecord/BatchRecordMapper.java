package com.miyu.cloud.mcs.dal.mysql.batchrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.batchrecord.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 批次工序任务 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BatchRecordMapper extends BaseMapperX<BatchRecordDO> {

    default PageResult<BatchRecordDO> selectPage(BatchRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchRecordDO>()
                .orderByDesc(BatchRecordDO::getId));
    }

    @Select("DELETE FROM `mcs_batch_record` WHERE id = #{id} ")
    void deleteByIdPhy(@Param("id") String id);

    @Select("DELETE FROM `mcs_batch_record` ${ew.customSqlSegment} ")
    void deleteBatchIdsPhy(@Param("ew") QueryWrapper<BatchRecordDO> wrapper);

}

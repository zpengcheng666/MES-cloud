package com.miyu.cloud.dms.dal.mysql.maintainapplication;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.dal.dataobject.maintainapplication.MaintainApplicationDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.dms.controller.admin.maintainapplication.vo.*;

/**
 * 设备维修申请 Mapper
 *
 * @author miyu
 */
@Mapper
public interface MaintainApplicationMapper extends BaseMapperX<MaintainApplicationDO> {

    default PageResult<MaintainApplicationDO> selectPage(MaintainApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaintainApplicationDO>()
                .eqIfPresent(MaintainApplicationDO::getModel, reqVO.getModel())
                .eqIfPresent(MaintainApplicationDO::getCode, reqVO.getCode())
                .likeIfPresent(MaintainApplicationDO::getName, reqVO.getName())
                .eqIfPresent(MaintainApplicationDO::getImportant, reqVO.getImportant())
                .eqIfPresent(MaintainApplicationDO::getProcessingUnitNumber, reqVO.getProcessingUnitNumber())
                .eqIfPresent(MaintainApplicationDO::getType, reqVO.getType())
                .eqIfPresent(MaintainApplicationDO::getDescribe1, reqVO.getDescribe1())
                .eqIfPresent(MaintainApplicationDO::getDuration, reqVO.getDuration())
                .eqIfPresent(MaintainApplicationDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MaintainApplicationDO::getApplicant, reqVO.getApplicant())
                .betweenIfPresent(MaintainApplicationDO::getApplicationTime, reqVO.getApplicationTime())
                .betweenIfPresent(MaintainApplicationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MaintainApplicationDO::getId));
    }

}

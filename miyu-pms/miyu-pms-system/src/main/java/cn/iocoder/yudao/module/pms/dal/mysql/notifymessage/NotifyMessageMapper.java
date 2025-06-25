package cn.iocoder.yudao.module.pms.dal.mysql.notifymessage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessagePageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.notifymessage.NotifyMessageDO;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 简易版,项目计划提醒用 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface NotifyMessageMapper extends BaseMapperX<NotifyMessageDO> {

    default PageResult<NotifyMessageDO> selectPage(NotifyMessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NotifyMessageDO>()
                .likeIfPresent(NotifyMessageDO::getTemplateNickname, reqVO.getTemplateNickname())
                .eqIfPresent(NotifyMessageDO::getReadStatus, reqVO.getReadStatus())
                .betweenIfPresent(NotifyMessageDO::getReadTime, reqVO.getReadTime())
                .betweenIfPresent(NotifyMessageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(NotifyMessageDO::getId));
    }

    default PageResult<NotifyMessageDO> selectPage2(NotifyMessagePageReqVO reqVO){
        MPJLambdaWrapper<NotifyMessageDO> wrapper = JoinWrappers.lambda(NotifyMessageDO.class)
                .selectAll(NotifyMessageDO.class)
                .likeIfExists(NotifyMessageDO::getTemplateNickname, reqVO.getTemplateNickname())
                .eqIfExists(NotifyMessageDO::getReadStatus, reqVO.getReadStatus())
                .orderByDesc(NotifyMessageDO::getId);

        return selectPage(reqVO,wrapper);
    }

    //通过实体类查询
    default List<NotifyMessageDO> selectListByEntity(NotifyMessageReqVO reqVO){
        MPJLambdaWrapper<NotifyMessageDO> wrapper = JoinWrappers.lambda(NotifyMessageDO.class)
                .selectAll(NotifyMessageDO.class)
                .likeIfExists(NotifyMessageDO::getTemplateNickname, reqVO.getTemplateNickname())
                .eqIfExists(NotifyMessageDO::getReadStatus, reqVO.getReadStatus())
                .orderByDesc(NotifyMessageDO::getId);

        return selectList(wrapper);
    }



}

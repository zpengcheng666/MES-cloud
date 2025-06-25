package cn.iocoder.yudao.module.pms.dal.mysql.order;

import java.util.*;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * 项目订单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PmsOrderMapper extends BaseMapperX<PmsOrderDO> {

    default PageResult<PmsOrderDO> selectPage2(PmsOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PmsOrderDO>()
                .eqIfPresent(PmsOrderDO::getProjectId, reqVO.getProjectId())
                .orderByDesc(PmsOrderDO::getId));
    }

    default PageResult<PmsOrderDO> selectPage(PmsOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PmsOrderDO>()
                .likeIfPresent(PmsOrderDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(PmsOrderDO::getProjectId, reqVO.getProjectId())
                .likeIfPresent(PmsOrderDO::getMaterialNumber, reqVO.getMaterialNumber())
                .likeIfPresent(PmsOrderDO::getPartNumber, reqVO.getPartNumber())
                .eqIfPresent(PmsOrderDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(PmsOrderDO::getProcessType, reqVO.getProcessType())
                .eqIfPresent(PmsOrderDO::getOrderStatus, reqVO.getOrderStatus())
                .eqIfPresent(PmsOrderDO::getOrderType, reqVO.getOrderType())
                .likeIfPresent(PmsOrderDO::getPartName,reqVO.getPartName())
                .betweenIfPresent(PmsOrderDO::getMaterialDeliveryTime, reqVO.getMaterialDeliveryTime())
                .betweenIfPresent(PmsOrderDO::getFproDeliveryTime, reqVO.getFproDeliveryTime())
                .betweenIfPresent(PmsOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PmsOrderDO::getId));
    }

    default List<PmsOrderDO> selectAll(){
        MPJLambdaWrapper<PmsOrderDO> wrapper = JoinWrappers.lambda(PmsOrderDO.class)
                .selectAll(PmsOrderDO.class)
                .selectAs(PmsApprovalDO::getProjectName,PmsOrderDO::getProjectName)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsOrderDO::getProjectId);

        return selectJoinList(PmsOrderDO.class,wrapper);
    }

    /**
     * 根据项目id查询项目订单
     * @param projectId
     * @return
     */
    default List<PmsOrderDO> selectListByProjectId(String projectId){
//        MPJLambdaWrapper<PmsOrderDO> wrapper = JoinWrappers.lambda(PmsOrderDO.class)
//                .selectAll(PmsOrderDO.class)
//                .leftJoin(OrderListDO.class,OrderListDO::getProjectOrderId,PmsOrderDO::getId);
//        wrapper.eqIfExists(PmsOrderDO::getProjectId,projectId);
        return selectList(PmsOrderDO::getProjectId,projectId);
    }

    /**
     * 根据项目ids查询项目订单
     * @param projectIds
     * @return
     */
    default List<PmsOrderDO> selectListByProjectIds(List<String> projectIds){
//        MPJLambdaWrapper<PmsOrderDO> wrapper = JoinWrappers.lambda(PmsOrderDO.class)
//                .selectAll(PmsOrderDO.class)
//                .leftJoin(OrderListDO.class,OrderListDO::getProjectOrderId,PmsOrderDO::getId);
//        if(projectIds.size()>0){
//            //条件是必填，没有就返空
//            wrapper.in(PmsOrderDO::getProjectId,projectIds);
//        }else {
//            return new ArrayList<PmsOrderDO>();
//        }
        LambdaQueryWrapperX<PmsOrderDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.inIfPresent(PmsOrderDO::getProjectId,projectIds)
                .orderByDesc(PmsOrderDO::getId);


        return selectList(wrapperX);
    }
    @Update("update project_order" +
            " set order_status = #{order.orderStatus}," +
            "project_id = #{order.projectId}," +
            "project_code = #{order.projectCode}," +
            "project_name = #{order.projectName}" +
            " where id = #{order.id}"
    )
    public void updateOutOrder(@Param("order")PmsOrderDO order);

    default List<PmsOrderDO> selectListWithProjectInfo(Collection<Integer> orderStatusList){
        MPJLambdaWrapper<PmsOrderDO> wrapper = JoinWrappers.lambda(PmsOrderDO.class)
                .selectAll(PmsOrderDO.class)
                .selectAs(PmsApprovalDO::getProjectClient,PmsOrderDO::getCompanyId)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsOrderDO::getProjectId)
                .in(PmsOrderDO::getOrderStatus,orderStatusList);
        return selectJoinList(PmsOrderDO.class,wrapper);

    }
}

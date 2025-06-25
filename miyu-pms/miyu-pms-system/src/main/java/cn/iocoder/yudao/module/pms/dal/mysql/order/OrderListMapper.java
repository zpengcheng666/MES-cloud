package cn.iocoder.yudao.module.pms.dal.mysql.order;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目订单表子 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderListMapper extends BaseMapperX<OrderListDO> {

    default PageResult<OrderListDO> selectPage(PageParam reqVO, String projectOrderId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderListDO>()
            .eq(OrderListDO::getProjectOrderId, projectOrderId)
            .orderByDesc(OrderListDO::getId));
    }

    default int deleteByProjectOrderId(String projectOrderId) {
        return delete(OrderListDO::getProjectOrderId, projectOrderId);
    }

    default List<OrderListDO> selectListByProjectOrderId(String projectOrderId) {
        return selectList(OrderListDO::getProjectOrderId, projectOrderId);
    }

    /**
     * 批量删除,物理
     */
    @Delete(
        "<script>"+
        "delete from project_order_list where id in" +
        "<foreach collection='list' item='id' separator=',' open='(' close=')'> "+
              "#{id}"  +
        "</foreach>"+
        "</script>"
    )
    void deleteOrderListDOs(@Param("list") Collection<String> list);

}

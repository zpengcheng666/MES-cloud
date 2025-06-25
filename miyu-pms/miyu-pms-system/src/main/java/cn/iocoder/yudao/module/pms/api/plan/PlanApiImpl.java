package cn.iocoder.yudao.module.pms.api.plan;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.api.assessment.AssessmentApi;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PlanItemMapper;
import cn.iocoder.yudao.module.pms.service.assessment.AssessmentService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
public class PlanApiImpl implements PlanApi{
    @Resource
    private OrderMaterialRelationMapper relationMapper;

    @Resource
    private PlanItemMapper planItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> closePlanItem(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return success("null");
        }
        for (String id : ids) {
            //也是初始化
            LambdaUpdateWrapper<OrderMaterialRelationDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(OrderMaterialRelationDO::getPlanId,null);
            updateWrapper.set(OrderMaterialRelationDO::getPlanItemId,null);
            //这俩是入库信息,生产单关闭也不能清
            //updateWrapper.set(OrderMaterialRelationDO::getMaterialCode,null);
            //updateWrapper.set(OrderMaterialRelationDO::getVariableCode,null);
            updateWrapper.set(OrderMaterialRelationDO::getContractId,null);
            updateWrapper.set(OrderMaterialRelationDO::getAidMill,null);
            updateWrapper.set(OrderMaterialRelationDO::getProductCode,null);
            updateWrapper.set(OrderMaterialRelationDO::getPlanType,null);
            updateWrapper.set(OrderMaterialRelationDO::getOrderNumber,null);
            updateWrapper.set(OrderMaterialRelationDO::getStep,null);
            //updateWrapper.set(OrderMaterialRelationDO::getMaterialTypeId,null);
            updateWrapper.set(OrderMaterialRelationDO::getMaterialStatus,1);
            updateWrapper.eq(OrderMaterialRelationDO::getPlanItemId,id);
            relationMapper.update(updateWrapper);
            //删除子计划
            planItemMapper.deleteById(id);
        }

        return success("ok");
    }
}

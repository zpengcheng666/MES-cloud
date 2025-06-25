package cn.iocoder.yudao.module.pms.service.orderMaterialRelation;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单物料关系表 Service 接口
 *
 * @author 上海弥彧
 */
public interface OrderMaterialRelationService {

    /**
     * 创建订单物料关系表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createOrderMaterialRelation(@Valid OrderMaterialRelationSaveReqVO createReqVO);

    /**
     * 更新订单物料关系表
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderMaterialRelation(@Valid OrderMaterialRelationSaveReqVO updateReqVO);

    /**
     * 删除订单物料关系表
     *
     * @param id 编号
     */
    void deleteOrderMaterialRelation(String id);

    /**
     * 获得订单物料关系表
     *
     * @param id 编号
     * @return 订单物料关系表
     */
    OrderMaterialRelationDO getOrderMaterialRelation(String id);

    /**
     * 根据订单id
     * 获得订单物料关系表
     *
     * @param id 编号
     * @return 订单物料关系表
     */
    List<OrderMaterialRelationDO> getRelationListByOrderId(String id);

    /**
     * 获得订单物料关系表分页
     *
     * @param pageReqVO 分页查询
     * @return 订单物料关系表分页
     */
    PageResult<OrderMaterialRelationDO> getOrderMaterialRelationPage(OrderMaterialRelationPageReqVO pageReqVO);

    List<OrderMaterialRelationDO> getOrderMaterialRelationWith(OrderMaterialRelationSaveReqVO req);

    //新增单个
    void insertOrderMaterialRelation(OrderMaterialRelationDO relationDO);
    //新增多个
    void insertOrderMaterialRelationBatch(List<OrderMaterialRelationDO> relationDOList);
    //更新
    void updateRelation(OrderMaterialRelationDO relationDO);

    /**
     * barCode 对应关系表的materialCode
     * @return
     */
    public List<OrderMaterialRelationDO> getRelationByBarCode(List<String> list);

    public void prepareUpdate(OrderMaterialRelationSaveReqVO createReqVO);

    public List<MaterialStockRespDTO> selectCompleteMaterialCodeByRelationId(String id);
    //selectCompleteMaterialCodeByRelationId的替换策略
    public List<MaterialStockRespDTO> selectCompleteMaterialCodeByRelationId2(String id,String planId);
    public List<MaterialStockRespDTO> selectStorageMaterialCodeByRelationId(String id);

    public void outsourceComplete(OrderMaterialRelationSaveReqVO createReqVO);

    public void outsourceInStorage(OrderMaterialRelationSaveReqVO createReqVO);

    /**
     * 创建订单物料关系表
     * 原物料废弃时使用，
     * 额外添加一条物料关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String addRelation(OrderMaterialRelationSaveReqVO createReqVO);

    String releaseCode(String id);

}

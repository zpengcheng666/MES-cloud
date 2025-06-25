package com.miyu.cloud.dms.service.sparepart;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartPageReqVO;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.sparepart.SparePartDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 备品/备件 Service 接口
 *
 * @author 王正浩
 */
public interface SparePartService {

    /**
     * 创建备品/备件
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createSparePart(@Valid SparePartSaveReqVO createReqVO);

    /**
     * 更新备品/备件
     *
     * @param updateReqVO 更新信息
     */
    void updateSparePart(@Valid SparePartSaveReqVO updateReqVO);

    /**
     * 删除备品/备件
     *
     * @param id 编号
     */
    void deleteSparePart(String id);

    /**
     * 获得备品/备件
     *
     * @param id 编号
     * @return 备品/备件
     */
    SparePartDO getSparePart(String id);

    /**
     * 获得备品/备件分页
     *
     * @param pageReqVO 分页查询
     * @return 备品/备件分页
     */
    PageResult<SparePartDO> getSparePartPage(SparePartPageReqVO pageReqVO);

    /**
     * 使用备用零件(更新数量)
     *
     * @param id     零件id
     * @param number 使用数量
     */
    void useSparePart(String id, Integer number);

    /**
     * 获得备品/备件列表
     *
     * @return 列表
     */
    List<SparePartDO> getSparePartList();

}
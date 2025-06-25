package com.miyu.cloud.dms.service.sparepart;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartPageReqVO;
import com.miyu.cloud.dms.controller.admin.sparepart.vo.SparePartSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.sparepart.SparePartDO;
import com.miyu.cloud.dms.dal.mysql.sparepart.SparePartMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.SPARE_PART_NOT_EXISTS;

/**
 * 备品/备件 Service 实现类
 *
 * @author 王正浩
 */
@Service
@Validated
public class SparePartServiceImpl implements SparePartService {

    private void check(SparePartSaveReqVO data) {
        if (sparePartMapper.exists(new LambdaQueryWrapperX<SparePartDO>().eq(SparePartDO::getCode, data.getCode()).ne(data.getId() != null, SparePartDO::getId, data.getId()))) {
            throw exception(new ErrorCode(5001, "备件编号重复"));
        }
    }

    @Resource
    private SparePartMapper sparePartMapper;

    @Override
    public String createSparePart(SparePartSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        SparePartDO sparePart = BeanUtils.toBean(createReqVO, SparePartDO.class);
        sparePartMapper.insert(sparePart);
        // 返回
        return sparePart.getId();
    }

    @Override
    public void updateSparePart(SparePartSaveReqVO updateReqVO) {
        // 校验存在
        validateSparePartExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        SparePartDO updateObj = BeanUtils.toBean(updateReqVO, SparePartDO.class);
        sparePartMapper.updateById(updateObj);
    }

    @Override
    public void deleteSparePart(String id) {
        // 校验存在
        validateSparePartExists(id);
        // 删除
        sparePartMapper.deleteById(id);
    }

    private void validateSparePartExists(String id) {
        if (sparePartMapper.selectById(id) == null) {
            throw exception(SPARE_PART_NOT_EXISTS);
        }
    }

    @Override
    public SparePartDO getSparePart(String id) {
        return sparePartMapper.selectById(id);
    }

    @Override
    public PageResult<SparePartDO> getSparePartPage(SparePartPageReqVO pageReqVO) {
        return sparePartMapper.selectPage(pageReqVO);
    }

    /**
     * 使用备用零件(更新数量)
     *
     * @param id     零件id
     * @param number 使用数量
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void useSparePart(String id, Integer number) {
        validateSparePartExists(id);
        if (number < 0) {
            throw exception(new ErrorCode(5001, "数量不能为负数"));
        }

        SparePartDO sparePart = sparePartMapper.selectById(id);
        if (sparePart.getNumber() < number) {
            throw exception(new ErrorCode(5001, "零件：" + sparePart.getCode() + "数量不足"));
        }
        sparePart.setNumber(sparePart.getNumber() - number);
        sparePartMapper.updateById(sparePart);
    }

    /**
     * 获得备品/备件列表
     *
     * @return 列表
     */
    @Override
    public List<SparePartDO> getSparePartList() {
        return sparePartMapper.selectList(null);
    }

}
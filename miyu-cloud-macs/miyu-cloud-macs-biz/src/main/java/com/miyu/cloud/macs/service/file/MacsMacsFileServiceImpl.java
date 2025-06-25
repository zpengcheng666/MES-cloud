package com.miyu.cloud.macs.service.file;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.miyu.cloud.macs.controller.admin.file.vo.*;
import com.miyu.cloud.macs.dal.dataobject.file.MacsFileDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.file.MacsFileMapper;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 文件 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class MacsMacsFileServiceImpl implements MacsFileService {

    @Resource
    private MacsFileMapper macsFileMapper;

    @Override
    public String createFile(MacsFileSaveReqVO createReqVO) {
        // 插入
        MacsFileDO file = BeanUtils.toBean(createReqVO, MacsFileDO.class);
        macsFileMapper.insert(file);
        // 返回
        return file.getId();
    }

    @Override
    public void updateFile(MacsFileSaveReqVO updateReqVO) {
        // 校验存在
        validateFileExists(updateReqVO.getId());
        // 更新
        MacsFileDO updateObj = BeanUtils.toBean(updateReqVO, MacsFileDO.class);
        macsFileMapper.updateById(updateObj);
    }

    @Override
    public void deleteFile(String id) {
        // 校验存在
        validateFileExists(id);
        // 删除
        macsFileMapper.deleteById(id);
    }

    private void validateFileExists(String id) {
        if (macsFileMapper.selectById(id) == null) {
            throw exception(new ErrorCode(401, "文件不存在"));
        }
    }

    @Override
    public MacsFileDO getFile(String id) {
        return macsFileMapper.selectById(id);
    }

    @Override
    public PageResult<MacsFileDO> getFilePage(MacsFilePageReqVO pageReqVO) {
        return macsFileMapper.selectPage(pageReqVO);
    }

    @Override
    public int save(MacsFileDO fileDO) {
        return macsFileMapper.insert(fileDO);
    }

    @Override
    public List<MacsFileDO> list(Wrapper<MacsFileDO> queryWrapper) {
        return macsFileMapper.selectList(queryWrapper);
    }
}

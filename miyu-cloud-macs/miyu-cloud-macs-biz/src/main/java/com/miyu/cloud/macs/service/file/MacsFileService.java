package com.miyu.cloud.macs.service.file;

import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.controller.admin.file.vo.*;
import com.miyu.cloud.macs.dal.dataobject.file.MacsFileDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.List;

/**
 * 文件 Service 接口
 *
 * @author miyu
 */
public interface MacsFileService {

    /**
     * 创建文件
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createFile(@Valid MacsFileSaveReqVO createReqVO);

    /**
     * 更新文件
     *
     * @param updateReqVO 更新信息
     */
    void updateFile(@Valid MacsFileSaveReqVO updateReqVO);

    /**
     * 删除文件
     *
     * @param id 编号
     */
    void deleteFile(String id);

    /**
     * 获得文件
     *
     * @param id 编号
     * @return 文件
     */
    MacsFileDO getFile(String id);

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<MacsFileDO> getFilePage(MacsFilePageReqVO pageReqVO);

    int save(MacsFileDO fileDO);

    List<MacsFileDO> list(Wrapper<MacsFileDO> queryWrapper);
}

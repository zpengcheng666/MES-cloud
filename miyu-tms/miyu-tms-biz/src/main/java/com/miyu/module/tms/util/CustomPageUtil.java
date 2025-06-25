package com.miyu.module.tms.util;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class CustomPageUtil {

    /**
     * 集合数据分页
     *
     * @param pageReqVO
     * @param sourceList
     * @return
     */
    public static <T> PageResult<T> listToPage(PageParam pageReqVO, List<T> sourceList) {
        if(sourceList == null || sourceList.isEmpty()){
            return PageResult.empty();
        }
        PageResult<T> pageResult = new PageResult<>();
        int count = sourceList.size(); // 总记录数
        // 计算总页数
        int pages = count % pageReqVO.getPageSize() == 0 ? count / pageReqVO.getPageSize() : count / pageReqVO.getPageSize() + 1;
        // 起始位置
        int start = pageReqVO.getPageNo() <= 0 ? 0 : (pageReqVO.getPageNo() > pages ? (pages - 1) * pageReqVO.getPageSize() : (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize());
        // 终止位置
        int end = pageReqVO.getPageNo() <= 0 ? (pageReqVO.getPageSize() <= count ? pageReqVO.getPageSize() : count) : (pageReqVO.getPageSize() * pageReqVO.getPageNo() <= count ? pageReqVO.getPageSize() * pageReqVO.getPageNo() : count);

        pageResult.setTotal((long)count);
        pageResult.setList(sourceList.subList(start, end));
        return pageResult;
    }
}

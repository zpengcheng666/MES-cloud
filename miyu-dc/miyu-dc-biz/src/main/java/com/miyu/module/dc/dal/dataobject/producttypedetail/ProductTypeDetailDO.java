package com.miyu.module.dc.dal.dataobject.producttypedetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("dc_product_type_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeDetailDO {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 产品类型id
     */
    private String productTypeId;

    /**
     * 采集类型id
     */
    private String collectTypeId;


}

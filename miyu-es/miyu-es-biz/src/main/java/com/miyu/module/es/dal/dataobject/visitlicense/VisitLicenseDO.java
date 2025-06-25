package com.miyu.module.es.dal.dataobject.visitlicense;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("es_visit_license")
@Data
public class VisitLicenseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 访问记录ID
     */
    private String visitRecordId;

    /**
     * 车牌号码
     */
    private String licenseNo;
}

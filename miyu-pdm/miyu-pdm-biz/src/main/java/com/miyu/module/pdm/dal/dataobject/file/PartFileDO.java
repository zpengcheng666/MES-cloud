package com.miyu.module.pdm.dal.dataobject.file;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("pdm_file")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartFileDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件来源
     */
    private String source;

    /**
     * 描述
     */
    private String description;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 电子仓库地址(不带后缀)
     */
    private String vaultUrl;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 文件分类id
     */
    private String fileClassId;

    @TableField(exist = false)
    private String datapackageBomId;

}

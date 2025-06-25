package com.miyu.module.pdm.dal.dataobject.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@TableName(value = "capp_step", autoResultMap = true)
@KeySequence("capp_step_seq")
        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public class StepDO extends BaseDO {

        public static final String PROCEDURE_ID_ROOT = "1";

        private String id;

        private String processVersionId;

        private String procedureId;

        private String stepNum;

        private String stepName;

        private Integer stepProperty;

        private String description;

        private String descriptionPreview;

        private Integer processingTime;

        private Long categoryId;

        private String creator;

        private String updater;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;


        /** 源工序id-用于工艺规程升版 */
        @TableField(exist = false)
        private String procedureIdSource;

        /** 源工步id-用于工艺规程升版 */
        @TableField(exist = false)
        private String stepIdSource;
        }

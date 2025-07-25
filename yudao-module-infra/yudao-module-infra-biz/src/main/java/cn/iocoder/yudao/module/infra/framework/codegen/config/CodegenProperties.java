package cn.iocoder.yudao.module.infra.framework.codegen.config;

import cn.iocoder.yudao.module.infra.enums.codegen.CodegenFrontTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@ConfigurationProperties(prefix = "yudao.codegen")
@Validated
@Data
public class CodegenProperties {

    /**
     * 生成代码的前缀 moduleName
     */
    @NotNull(message = "生成代码的前缀 moduleName 不能为空")
    private String preModuleName;

    /**
     * 生成的 Java 代码的基础包
     */
    @NotNull(message = "Java 代码的基础包不能为空")
    private String basePackage;

    /**
     * 数据库名数组
     */
    @NotEmpty(message = "数据库不能为空")
    private Collection<String> dbSchemas;

    /**
     * 代码生成的前端类型（默认）
     *
     * 枚举 {@link CodegenFrontTypeEnum#getType()}
     */
    @NotNull(message = "代码生成的前端类型不能为空")
    private Integer frontType;

}

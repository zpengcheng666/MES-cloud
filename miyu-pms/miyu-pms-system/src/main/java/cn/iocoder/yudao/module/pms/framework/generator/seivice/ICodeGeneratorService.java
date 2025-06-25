package cn.iocoder.yudao.module.pms.framework.generator.seivice;

import java.util.List;

public interface ICodeGeneratorService {

    List<String> generateCodes(String rule, String[] codes, Integer[] lengths);
}

package com.miyu.module.mcc.strategy.impl;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.mcc.controller.admin.materialtype.vo.MaterialListReqVO;
import com.miyu.module.mcc.controller.admin.materialtype.vo.MaterialListTreeVO;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import com.miyu.module.mcc.enums.EncodingRuleEnum;
import com.miyu.module.mcc.service.materialtype.MaterialTypeService;
import com.miyu.module.mcc.strategy.IEncodingRuleStrategy;
import com.miyu.module.mcc.utils.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/****
 * 物料类别
 */
@Service
public class MaterialTypeRule implements IEncodingRuleStrategy {
    @Resource
    private MaterialTypeService materialTypeService;
    @Override
    public String getRuleValue(EncodingRuleDetailDO detailDO, Map<String, String> attributes,List<EncodingRuleDetailDO> detailDOS, EncodingRuleDO ruleDO) {


        //获取传入的物料类别
        String code = attributes.get(detailDO.getEncodingAttribute());

        MaterialListReqVO reqVO = new MaterialListReqVO();
        List<MaterialTypeDO> doList = materialTypeService.getMaterialTypeList(reqVO);
        List<MaterialListTreeVO> list= BeanUtils.toBean(doList,MaterialListTreeVO.class);
        List<MaterialListTreeVO> treeVOS = streamMethod(list);

        MaterialListTreeVO vo = treeVOS.stream().filter(materialListTreeVO -> materialListTreeVO.getId().equals(detailDO.getMaterialTypeId())).collect(Collectors.toList()).get(0);

        if (StringUtils.isBlank(code)){//如果自定义属性没有值 则默认为主类别
            code = vo.getCode();
        }

        //如果规则是当前类别 则直接赋值返回
        if (EncodingRuleEnum.NOW.getStatus().equals(detailDO.getRuleType())){
            return code;
        }else {//如果是全部类别 需要从树形结构中查找指定位置  并拼装成最终结构  位数不够补0

            Map<String,String> codeMap = new HashMap<>();
            codeMap.put(vo.getId(),vo.getCode());
            String finalCode=null;
            if (code.equals(vo.getCode())){
                finalCode = code;
            }else {
                finalCode = getCode(vo.getChildren(),code,codeMap,finalCode);

            }

            //return StringUtils.rightPad(finalCode, detailDO.getBitNumber(), "0");
            return finalCode;
        }

    }


    /***
     * 递归获取全部类码拼装
     * @param voList
     * @param code
     * @param codeMap
     * @param finalCode
     */
   String getCode(List<MaterialListTreeVO> voList, String code,Map<String,String> codeMap,String finalCode){

        for (MaterialListTreeVO vo :voList){
            if (vo.getCode().equals(code)){
                finalCode = codeMap.get(vo.getParentId())+ code;
                return finalCode;
            }else {
                codeMap.put(vo.getId(),codeMap.get(vo.getParentId())+vo.getCode());
                getCode(vo.getChildren(),code,codeMap,finalCode);
            }
        }

        return finalCode;
    }


    /***
     * 转树形结构
     * @param treeList
     * @return
     */
    public static List<MaterialListTreeVO> streamMethod(List<MaterialListTreeVO> treeList) {
        List<MaterialListTreeVO> list = treeList.stream()
                // 筛选出父节点
                .filter(t -> t.getParentId().equals("0"))
                // 设置父节点的子节点列表
                .map(item -> {item.setChildren(streamGetChildren(item, treeList)); return item;})
                .collect(Collectors.toList());
        return list;
    }
    /**
     * stream 方式递归查找子节点列表
     * @return
     */
    public static List<MaterialListTreeVO> streamGetChildren(MaterialListTreeVO tree, List<MaterialListTreeVO> treeList) {
        List<MaterialListTreeVO> list = treeList.stream()
                .filter(t -> t.getParentId().equals(tree.getId()))
                .map(item -> {item.setChildren(streamGetChildren(item, treeList)); return item;})
                .collect(Collectors.toList());
        return list;
    }
}

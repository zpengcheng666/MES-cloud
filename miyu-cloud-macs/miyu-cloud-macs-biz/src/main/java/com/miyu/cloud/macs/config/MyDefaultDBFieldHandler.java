package com.miyu.cloud.macs.config;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.SneakyThrows;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 通用参数填充实现类
 *
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author hexiaowu
 */
public class MyDefaultDBFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Object parameter = metaObject.getOriginalObject();
        List<Field> fieldList = getAllFields(parameter);
        try {
            for (Field field : fieldList) {
                if ("createBy".equals(field.getName())) {
                    field.setAccessible(true);
                    Object localCreateBy = field.get(parameter);
                    field.setAccessible(false);
                    if (localCreateBy == null || "".equals(localCreateBy)) {
                        if (loginUser != null) {
                            // 登录人账号
                            field.setAccessible(true);
                            field.set(parameter, loginUser.getId()+"");//todo 现有框架无name
                            field.setAccessible(false);
                        }
                    }
                }
                // 注入创建时间
                if ("createTime".equals(field.getName())) {
                    field.setAccessible(true);
                    Object localCreateDate = field.get(parameter);
                    field.setAccessible(false);
                    if (localCreateDate == null || "".equals(localCreateDate)) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
                    }
                }
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Object parameter = metaObject.getOriginalObject();
        List<Field> fields = null;
        if (parameter instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
            //update-begin-author:scott date:20190729 for:批量更新报错issues/IZA3Q--
            String et = "et";
            if (p.containsKey(et)) {
                parameter = p.get(et);
            } else {
                parameter = p.get("param1");
            }
            //update-end-author:scott date:20190729 for:批量更新报错issues/IZA3Q-

            //update-begin-author:scott date:20190729 for:更新指定字段时报错 issues/#516-
            if (parameter == null) {
                return;
            }
            //update-end-author:scott date:20190729 for:更新指定字段时报错 issues/#516-

            fields = getAllFields(parameter);
        } else {
            fields = getAllFields(parameter);
        }

        for (Field field : fields) {
            try {
                if ("updateBy".equals(field.getName())) {
                    //获取登录用户信息
                    if (loginUser != null) {
                        // 登录账号
                        field.setAccessible(true);
                        field.set(parameter, loginUser.getId()+"");
                        field.setAccessible(false);
                    }
                }
                if ("updateTime".equals(field.getName())) {
                    field.setAccessible(true);
                    field.set(parameter, new Date());
                    field.setAccessible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Field> getAllFields(Object parameter) {
        Class<?> clazz = parameter.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }
}

package cn.iocoder.yudao.module.pms.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class JsonDemo {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("test.txt");
        InputStream inputStream =classPathResource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        char[] chars = new char[1024];
        int len = 0;
        StringBuilder str = new StringBuilder();
        while ((len=inputStreamReader.read(chars))!=-1){
            str.append(new String(chars,0,len));
        }
        String s = str.toString();
        String replace = s.replace("\"{","{")
                        .replace("}\"","}")
                        .replace("\\", "");

        System.out.println(replace);

        JSONArray objects = JSON.parseArray(replace);
        Object o = objects.get(0);

        for (Object object : objects) {
            System.out.println(object);
        }

//        Object title = map.get("title");
//        System.out.println(title.toString());
    }
}

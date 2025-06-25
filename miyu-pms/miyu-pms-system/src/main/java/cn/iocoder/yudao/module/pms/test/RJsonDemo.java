package cn.iocoder.yudao.module.pms.test;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RJsonDemo {
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
        String replace = s.replace("\"","\\\"")
                .replace("[\\\"","[\"")
                .replace("\\\"]", "\"]");
        System.out.println(replace);




//        Object title = map.get("title");
//        System.out.println(title.toString());
    }
}

package com.miyu.module.pdm.netty;

import java.util.UUID;

public class UUIDUtil {
        private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };

        /**
         * 生成8位uuid
         * @return
         * @author wangfan
         */
        public static String randomUUID8() {
            StringBuffer shortBuffer = new StringBuffer();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            for (int i = 0; i < 8; i++) {
                String str = uuid.substring(i * 4, i * 4 + 4);
                int x = Integer.parseInt(str, 16);
                shortBuffer.append(chars[x % 0x3E]);
            }
            return shortBuffer.toString();
        }

        /**
         * 生成32位uuid
         * @return
         * @author wangfan
         */
        public static String randomUUID32(){
            String uuid = UUID.randomUUID().toString().replace("-", "");
            return uuid;
        }

        /**
         * 生成随机小写英文字母
         * @param len 要生成的位数
         * @return
         */
        public static String randomStr8(int len){
            String result = "";
            for(int i=0;i<len;i++){
                int c='a'+(int)(Math.random()*26);   //'a'会自动转换为int以执行加法
                result += (char)c;
            }
            return result;
        }
}

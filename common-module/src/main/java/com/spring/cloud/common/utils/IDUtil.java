package com.spring.cloud.common.utils;

import java.util.UUID;

/**
 * 生成UUID类
 *
 * @author ZhongHui
 * @time 2017/7/3.
 */
public class IDUtil {

    public IDUtil(){

    }

    /**
     * 获取一个完整的uuid
     *
     * @return String
     */
    public static String getFullUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取一个较短的uuid
     *
     * @return String
     */
    public static String getShortUUID() {
        return getFullUUID().replaceAll("-","").toLowerCase();
    }

    /**
     * 获取短的uuid的数组
     *
     * @param num 要产生几个uuid
     * @return String[]
     */
    public static String[] getShortUUIDs(int num) {
        if(num <= 0) {
            return null;
        }else{
            String[] shortUUIDs = new String[num];
            // 生成uuid
            for(int i = 0 ; i < num ; i++){
                shortUUIDs[i] = getShortUUID();
            }
            return shortUUIDs;
        }
    }

    /**
     * 获取完整的uuid的数组
     *
     * @param num 要产生几个uuid
     * @return Stirng[]
     */
    public static String[] getFullUUIDs(int num){
        if(num < 1) {
            return null;
        } else {
            String[] fullUUIDs = new String[num];
            // 生成uuid
            for(int i = 0 ; i < num ; i++) {
                fullUUIDs[i] = getFullUUID();
            }
            return fullUUIDs;
        }
    }

}

package com.fulaan.utils.pojo;

import com.fulaan.utils.Iparser;
import com.fulaan.utils.Log4jUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2016/8/16.
 */
public class HubeiLoginUtil {
    public static List<String> split_list = new ArrayList<String>();

    public static void loadHubeiConfig(String filename) {
        loadHubeiConfig(filename, "utf-8");
    }


    public static void loadHubeiConfig(String filename, String encode) {
        ClassLoader cl = Iparser.class.getClassLoader();
        BufferedReader reader = null;
        try {
            InputStream in = cl.getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(in, encode));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                split_list.add(tempString);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log4jUtil.printStackTrace(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log4jUtil.printStackTrace(e);
            }
        }
    }

    public static void init() {
        loadHubeiConfig("hubeiLogin.txt");
    }
    /**
     * Description: 测试程序 1、在处理小数据量该关键词处理程序与传统遍历字符串效率等同
     * 2、处理大数据量数据时，传统比较方式时间开销取决于关键字库长度与文本长度的乘积、而用本关键词法时间开销基本上只取决于文本长度
     */
    public static void main(String[] args) {
        init();
        for(String item:split_list){
            System.out.println(item);
        }
    }

}

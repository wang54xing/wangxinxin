package com.fulaan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/3.
 */
public class KeyWordFilterUtil {

    private static HashMap keysMap = new HashMap();
    private static int matchType = 1; // 1:最小长度匹配 2：最大长度匹配
    
    private static List<String> split_list = new ArrayList<String>();

    public static void loadSplitConfig(String filename) {
        loadSplitConfig(filename, "utf-8");
    }

    public static void loadBadConfig(String filename) {
        loadBadConfig(filename, "utf-8");
    }

    public static void loadSplitConfig(String filename, String encode) {
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

    public static void loadBadConfig(String filename, String encode) {
        ClassLoader cl = Iparser.class.getClassLoader();
        BufferedReader reader = null;
        try {
            InputStream in = cl.getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(in, encode));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                addKeywords(tempString);
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
        loadBadConfig("bad.txt");
        loadSplitConfig("split.txt");
    }

    /**
     * 构造关键字树状DFA图
     */
    public static void addKeywords(/* List< */String/* > */key) {
        // for(int i = 0; i < keywords.size(); i++){
        // String key = keywords.get(i);
        HashMap nowhash = keysMap;
        for (int j = 0; j < key.length(); j++) {
            char word = key.charAt(j);
            Object wordMap = nowhash.get(word);
            if (wordMap != null) {
                nowhash = (HashMap) wordMap;
            } else {
                HashMap newWordHash = new HashMap();
                newWordHash.put("isEnd", "0");
                nowhash.put(word, newWordHash);
                nowhash = newWordHash;
            }
            if (j == key.length() - 1) {
                nowhash.put("isEnd", "1");
            }
        }
        // }
    }

    /**
     * 重置关键词
     */
    public static void clearKeywords() {
        keysMap = new HashMap();
    }

    /**
     * 检查一个字符串从begin位置起开始是否有keyword符合， 如果有符合的keyword值，返回值为匹配keyword的长度，否则返回零
     * flag 1:最小长度匹配 2：最大长度匹配
     */

    public static int checkKeyWords(String txt, int begin, int flag) {
        HashMap nowhash = keysMap;
        int maxMatchRes = 0;
        int res = 0;
        for (int i = begin; i < txt.length(); i++) {
            char word = txt.charAt(i);
            Object wordMap = nowhash.get(word);
            if (wordMap != null) {
                res++;
                nowhash = (HashMap) wordMap;
                if (((String) nowhash.get("isEnd")).equals("1")) {
                    if (flag == 1) {
                        return res;
                    } else {
                        maxMatchRes = res;
                    }
                }
            } else {
                return maxMatchRes;
            }
        }
        return maxMatchRes;
    }

    /**
     * 返回txt中关键字的列表
     */
    public static HashMap getTxtKeyWords(String txt) {
        HashMap res = new HashMap();
        for (int i = 0; i < txt.length();) {
            int len = checkKeyWords(txt, i, matchType);
            if (len > 0) {
                Object obj = res.get(txt.substring(i, i + len));
                if (obj == null) {
                    res.put(txt.substring(i, i + len), new Integer(1));
                } else {
                    Integer count = new Integer(((Integer) obj).intValue() + 1);
                    res.put(txt.substring(i, i + len), count);
                }
                i += len;
            } else {
                i++;
            }
        }
        return res;
    }

    /**
     * 仅判断txt中是否有关键字
     */
    public static boolean isContentKeyWords(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            int len = checkKeyWords(txt, i, 1);
            if (len > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * flag: 1:将txt中的关键字替换成指定字符串 2：将txt中的关键字每个字都替换成指定的字符串
     */
    public static String getReplaceStrTxtKeyWords(String txt,
                                                  String replacestr, int flag) {
        int count = 0;
        for (String c : split_list) {
            txt = txt.replace(c, "");
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < txt.length();) {
            int len = checkKeyWords(txt, i, matchType);
            if (len > 0) {
                if (flag == 2)
                    for (int j = 0; j < len; j++) {
                        res.append(replacestr);
                    }
                if (flag == 1)
                    res.append(replacestr);
                i += len;
            } else {
                res.append(txt.charAt(i));
                i++;
            }
        }
        return res.toString();
    }

    public static HashMap getKeysMap() {
        return keysMap;
    }

    public static void setKeysMap(HashMap _keysMap) {
        keysMap = _keysMap;
    }

    public static int getMatchType() {
        return matchType;
    }

    public static void setMatchType(int _matchType) {
        matchType = _matchType;
    }

    /**
     * Description: 测试程序 1、在处理小数据量该关键词处理程序与传统遍历字符串效率等同
     * 2、处理大数据量数据时，传统比较方式时间开销取决于关键字库长度与文本长度的乘积、而用本关键词法时间开销基本上只取决于文本长度
     */
    public static void main(String[] args) {
        init();
        setMatchType(1);
        System.out.println(getReplaceStrTxtKeyWords("XXX 妈妈B", "*", 2));
        setMatchType(2);
        System.out.println(getReplaceStrTxtKeyWords("XXX 草 你 妈", "*", 2));
    }

    public static void printCostTime(String name, Date begin) {
        Date now = new Date();
        System.out.println(name + (now.getTime() - begin.getTime()));
    }
}

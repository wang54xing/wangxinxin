package com.fulaan.utils;

/**
 * Created by wang_xinxin on 2015/8/3.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Iparser {

    final int number = 1000;
    final int length = 100;
    Trie trie;
    static String city[];
    public Iparser(){
        city = new String[number];
        trie = new Trie();
        System.out.println("Iparser created.");
    }

    long Hbit(long x){
        long ret = 0, flag = (x & (x - 1)) == 0 ? 1 : 0;
        --x;
        while(x > 0){
            x >>= 1;
            ++ret;
        }
        return flag + ret - 1;
    }

    String maskInterval(long s, long mask){
        String ret = "";
        for(int i = 31; i >= mask; i--){
            ret += String.valueOf((s & (1 << i)) > 0 ? 1 : 0);
        }
        return ret;
    }

    public long getMin(long ls, long rs){
        return (ls <= rs) ? ls : rs;
    }
    public void loadConfig(String filename){
        loadConfig(filename,"utf-8");
    }
    public void loadConfig(String filename,String encode){
        ClassLoader cl = Iparser.class.getClassLoader();
        BufferedReader reader=null;
        try {
            InputStream in = cl.getResourceAsStream(filename);
            reader = new BufferedReader(new InputStreamReader(in,encode));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String[] sArray = tempString.split("\t");
                long s = Long.parseLong(sArray[0].trim());
                long e = Long.parseLong(sArray[1].trim());
                int id=Integer.parseInt(sArray[3]);
                city[id] = sArray[2];
                long cur = s, ls = 0, rs = 0;
                while(cur <= e){
                    ls = Hbit(cur & (~cur + 1));
                    rs = Hbit(e - cur + 1);
                    trie.insert(maskInterval(cur, getMin(ls, rs)), id);
                    cur += 1 << getMin(ls, rs);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log4jUtil.printStackTrace(e);
        }finally{
            try {
                if(reader!=null){
                    reader.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log4jUtil.printStackTrace(e);
            }
        }
    }
    public int findGeoCode(String ip){
        if(ip==null) return 0;
        return findGeoCode(convertIP2Long(ip));
    }
    public int findGeoCode(long x){
        return trie.find(maskInterval(x, 0));
    }
    public String find(String ip){
        return find(convertIP2Long(ip));
    }
    public String find(long x){
        int code = trie.find(maskInterval(x, 0));
        if(code == 0){
            return "not found";
        }
        return city[code].toString();
    }
    public static long convertIP2Long(String ip){
        long x=0;
        if(ip!=null){
            String[] sArray=ip.split("\\.");
            x=(Long.valueOf(sArray[0])<<24)+(Long.valueOf(sArray[1])<<16)+(Long.valueOf(sArray[2])<<8)+Long.valueOf(sArray[3]);
        }
        return x;
    }
    public static String convertLong2IP(long x){
        return String.valueOf(x>>24)+"."+String.valueOf((x&0x00ff0000)>>16)+"."+String.valueOf((x&0x0000ff00)>>8)+"."+String.valueOf((x&0x000000ff));
    }
    static public void main(String[] argv){
        Iparser parser = new Iparser();
        // Read from input file.
        parser.loadConfig("bad.txt","GBK");
		/* find a concrete ip_address. using long instead of unsigned int because of Java do not
		 * have key word unsigned.
		 */
        //System.out.print(parser.find("210.122.1.10"));
        System.out.println(parser.find(1035902464));
        System.out.println(parser.findGeoCode(1035902464));
        System.out.println(parser.convertIP2Long(parser.convertLong2IP(1035902464)));
        System.out.println(parser.convertIP2Long("192.168.2.126"));
        System.out.println(parser.convertIP2Long("192.168.1.105"));
        System.out.println(parser.convertIP2Long("192.168.1.111"));
        System.out.println(parser.convertIP2Long("192.168.1.200"));
        System.out.println(parser.convertIP2Long("192.168.1.254"));
		/*
		 * deal 10 million request for test.
		 */
//		long startTime = System.currentTimeMillis();
//		for(long i = 1; i <= 10000000; i++){
//			parser.find(i);
//		}
//		long endTime = System.currentTimeMillis();
//		System.out.println("runtime:  " + (endTime - startTime) + "ms");
    }
}

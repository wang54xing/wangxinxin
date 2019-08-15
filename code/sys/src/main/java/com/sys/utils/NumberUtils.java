package com.sys.utils;

/**
 * Created by guojing on 2015/11/23.
 */
public class NumberUtils {
    static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
            "十亿", "百亿", "千亿", "万亿" };
    static String[] numArray = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };


    /**
     * @param args
     */
    public static void main(String[] args) {
        int num = 106;
        String numStr = formatInteger(num);
        print("num= " + num + ", convert result: " + numStr);
        double decimal = 16.234206;
        print("============================================================");
        String decStr = formatDecimal(decimal);
        print("decimal= " + decimal + ", decStr: " + decStr);
    }

    public static String formatInteger(int num) {
        if(num==10){
            return "十";
        }
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    public static String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = formatInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }

    public static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }

    private static void print(Object arg0) {
        System.out.println(arg0);
    }
}

package com.wordplat.ikvstockchart;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Utils {

    //<<=======  数字级别换算 ===========>>
    private static final Double MILLION = 10000.0;
    private static final Double MILLIONS = 1000000.0;
    private static final Double BILLION = 100000000.0;
    private static final String MILLION_UNIT = "万";
    private static final String BILLION_UNIT = "亿";
    public static String digitalConversion(double num){
        if (num<MILLION){
            return cutSixDouble(num,3);
        }
        if (num>=MILLION){
            Double aDouble = strDivision(num, MILLION);
            return cutSixDouble(aDouble,2)+MILLION_UNIT;
        }
        if (num>=BILLION){
            Double aDouble = strDivision(num, BILLION);
            return cutSixDouble(aDouble,4)+BILLION;
        }
        return cutSixDouble(num,2);
    }

    //去除科学计数法  不四舍五入 切割
    public static String cutSixDouble(Double dou, int x) {
        if (dou == null) return "";
        BigDecimal bigDecimal = new BigDecimal(dou.toString()).setScale(x, BigDecimal.ROUND_DOWN);
        StringBuffer buffer = new StringBuffer();
        buffer.append("##.");
        for (int i = 0; i < x; i++) {
            buffer.append("#");
        }
        DecimalFormat df = new DecimalFormat(buffer.toString());
        return df.format(bigDecimal);
    }

    //除法
    public static Double strDivision(Double d1, Double d2) {

        if (d2 == 0) {
            return 0.0;

        } else {
            return d1 / d2;
        }

    }
}

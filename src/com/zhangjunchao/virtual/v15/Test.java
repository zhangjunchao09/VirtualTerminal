package com.zhangjunchao.virtual.v15;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(20);
        BigDecimal b = new BigDecimal(33.06).setScale(2, BigDecimal.ROUND_HALF_UP);

        System.out.println(b.multiply(a).intValue());

        int y = new Date().getYear();
        System.out.println(y);

        long serial = 100;
        System.out.println(Long.MAX_VALUE);

        List<String> ss = Arrays.asList("1","2","ss");

        List<Double> ds = ss.parallelStream().map(s -> {
            Double d = null;
            try {
                d = Double.parseDouble(s);
            } catch (Exception e) {
            }
            return d;
        }).filter(d -> d != null).collect(Collectors.toList());
        ds.size();
    }
}

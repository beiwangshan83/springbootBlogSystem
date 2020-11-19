package com.beiwangshan.blog;

import java.util.Calendar;

/**
 * @className: com.beiwangshan.blog-> testDate
 * @description: 时间戳的测试类
 * @author: 曾豪
 * @createDate: 2020-11-19 15:42
 * @version: 1.0
 * @todo:
 */
public class testDate {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.set(2999,11,1);
        long timeInMillis = instance.getTimeInMillis();
        System.out.println("length ==> "+String.valueOf(timeInMillis).length());
        System.out.println("currentTimeMillis ==> "+currentTimeMillis);
    }
}

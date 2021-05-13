package com.yicj.study.bridge;

import java.util.Date;

public class DateInterval implements MyPair<Date> {

    @Override
    public Date hello(Date value) {
        System.out.println("value : " + value);
        return value ;
    }
}
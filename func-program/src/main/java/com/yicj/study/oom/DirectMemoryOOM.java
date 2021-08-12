package com.yicj.study.oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Args: -verbose:gc -Xmx20M -XX:MaxDirectMemorySize=10M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\\tmp\\
 */
public class DirectMemoryOOM {

    private static final int _1MB = 1024 *1024 ;

    public static void main(String[] args) throws IllegalAccessException {

        Field field = Unsafe.class.getDeclaredFields()[0] ;
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe)field.get(null) ;
        while (true){
            unsafe.allocateMemory(_1MB) ;
        }
    }
}

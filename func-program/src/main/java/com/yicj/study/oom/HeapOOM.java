package com.yicj.study.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -verbose:gc -Xms20m -Xmx20M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\\tmp
 */
public class HeapOOM {
    static class OOMObject{}

    public static void main(String[] args) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>() ;
        while (true){
            list.add(new OOMObject());
        }
    }
}

package com.yicj.study.proto;

import com.yicj.study.util.CompressionUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class ExRouteModelTest {

    private StringBuilder sb = new StringBuilder("") ;

    @Before
    public void before(){
        for (int i = 0 ; i < 999 ; i++){
            sb.append("1") ;
        }
    }

    @Test
    public void test1() throws IOException, DataFormatException {
        String content = "1" + sb.toString() ;
        byte [] bytes = content.getBytes() ;
        //ExRouteModel.ExRoute exRoute = ExRouteModel.ExRoute.newBuilder().setMessage(content).build();
        //byte[] bytes = exRoute.toByteArray();
        byte[] compress = CompressionUtil.compress(bytes, CompressionUtil.Level.BEST_COMPRESSION);
        StopWatch watch = new StopWatch() ;
        System.out.println("===> " + compress.length);
        watch.start();
        for (int i = 0 ; i < 1000000; i ++){
            byte[] decompress = CompressionUtil.decompress(compress);
        }
        watch.stop();
        System.out.println("===============" + watch.prettyPrint());
        //System.out.println(new String(decompress));
    }
}

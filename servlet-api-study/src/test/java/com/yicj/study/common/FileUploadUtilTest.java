package com.yicj.study.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;


@Slf4j
public class FileUploadUtilTest {
    //("filename=\".*\"\\r\\n.*\\r\\n\\r\\n(.*+)") ;
    private String content = "----------------------------914298649102139483756864\r\n" +
            "Content-Disposition: form-data; name=\"filename\"; filename=\"helloworld.txt\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n"+
            "select * from nfs_gbl.faredimpexp_push_config t ;\r\n" +
            "\r\n"+
            "----------------------------914298649102139483756864--" ;
    @Test
    public void filename() throws UnsupportedEncodingException {
        String filename = FileUploadUtil.filename(content);
        log.info("file name : {}", filename);
    }

    @Test
    public void fileRange(){
        String contentType = "text/plain" ;
        FileUploadUtil.Range range = FileUploadUtil.fileRange(content, contentType);
        log.info("range : {}" ,range);
    }
}

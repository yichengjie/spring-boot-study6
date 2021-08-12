package com.yicj.study.mvc;

import com.yicj.study.importselector.EnableServer;
import com.yicj.study.importselector.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@EnableServer(type = Server.Type.FTP)
@ServletComponentScan("com.yicj.study.mvc.servlet.mvc.servlet")
@SpringBootApplication
public class WebMvcApplication  implements ApplicationRunner {

    @Autowired
    private Server server ;

    public static void main(String[] args) {
        SpringApplication.run(WebMvcApplication.class, args) ;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.start();
    }
}

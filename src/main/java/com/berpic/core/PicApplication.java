package com.berpic.core;

import com.berpic.core.config.RundListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


/**
 * @author 任小斌
 * @version V1.0
 * @Title: PicApplication.java
 * @Package com.bersolr.core
 * @Description: TODO(启动入口)
 * @date Aug 21, 2015 5:24:11 PM
 */
@SpringBootApplication
@Import({RundListener.class})
public class PicApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PicApplication.class, args);
    }
}

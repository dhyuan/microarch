package cn.telbox.microarch.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Created by dahui on 20/11/2016.
 */
public class ConfigOnConsulApplication {
    private static final Logger logger = LoggerFactory.getLogger(ConfigOnConsulApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(ConfigOnConsulApplication.class, args);
    }
}


@RefreshScope
@RestController
class ConfigDataController {

    @Value("${message:NoMessageFound}")
    private String message;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String echoMessage() {
        return "Hello, " + message + " I comes from configuration repository based on zookeeper. " + Instant.now();
    }
}
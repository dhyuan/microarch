package cn.telbox.microarch.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by dahui on 21/11/2016.
 */

@EnableEurekaServer
@SpringBootApplication
public class DemoUrekaserverApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoUrekaserverApp.class, args);

    }
}

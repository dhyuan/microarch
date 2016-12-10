package cn.telbox.microarch.sample.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by dahui on 10/12/2016.
 *
 * The config server based on the Git repository.
 * 1) add spring-cloud-config-server in pom.xml
 * 2) add spring.cloud.config.server.git.uri in the application.properties.
 * 3) start the application
 * 4) visit http://localhost:8888/organization-service/master
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServer {


    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }
}

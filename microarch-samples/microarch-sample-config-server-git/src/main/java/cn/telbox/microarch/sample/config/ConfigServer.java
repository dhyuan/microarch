package cn.telbox.microarch.sample.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by dahui on 10/12/2016.
 *
 * The config server based on the Git repository.
 * 1) add spring-cloud-config-server in pom.xml
 * 2) create a git repository (must be root) to save the configuration data.
 *     git clone https://github.com/dhyuan/config-repo.git to your HOME directory.
 * 3) add spring.cloud.config.server.git.uri in the application.properties.
 * 4) start the application
 * 5) visit http://localhost:8888/organization-service/master
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServer {


    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }
}

package cn.telbox.microarch.sample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 *
 * Reference: http://cloud.spring.io/spring-cloud-static/spring-cloud-zookeeper/1.0.3.RELEASE/#spring-cloud-zookeeper-config
 * Spring Cloud Zookeeper Config is an alternative to the Config Server and Client.
 *
 *
 * To read the configuration data from the ZooKeeper
 * 1) Create the paths
 *              microarch-configuration/demo-ConfigOnZK-service#DEV
 *              microarch-configuration/demo-ConfigOnZK-service#TEST
 *              microarch-configuration/demo-ConfigOnZK-service#INTG
 *              microarch-configuration/demo-ConfigOnZK-service#PROD
 *              microarch-configuration/demo-ConfigOnZK-service
 *              microarch-configuration/defaultSetting#DEV
 *              microarch-configuration/defaultSetting#TEST
 *              microarch-configuration/defaultSetting
 * 2) Create the configuration item in those paths.
 * 3) Open through browser.
 * 4) Change the message's value and refresh the web page, you can find the changes is reflected on the page.
 * 5) Use the actuator to refresh:  curl -d {} http://localhost:8080/refresh
 * 6) The config data can be accessed by https://github.com/DeemOpen/zkui.
 * 
 * Created by Dahui on 2016/11/18.
 */
@SpringBootApplication
public class ConfigOnZKApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConfigOnZKApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(ConfigOnZKApplication.class, args);
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

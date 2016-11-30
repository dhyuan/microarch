package cn.telbox.microarch.sample.springbootwithjetty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dahui on 05/11/2016.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AppOnJetty {

    public static void main(String[] args) {
        SpringApplication.run(AppOnJetty.class, args);
    }

}

@org.springframework.web.bind.annotation.RestController
class MyController {

    @RequestMapping("/")
    public String helloWorld() {
        return "Hello AppOnJetty";
    }
}


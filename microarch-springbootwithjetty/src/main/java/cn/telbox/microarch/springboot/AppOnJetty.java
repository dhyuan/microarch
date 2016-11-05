package cn.telbox.microarch.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dahui on 05/11/2016.
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
public class AppOnJetty {

    public static void main(String[] args) {
        SpringApplication.run(AppOnJetty.class, args);
    }



}

@org.springframework.web.bind.annotation.RestController
class MyController {

    @RequestMapping("/")
    String helloWorld() {
        return "Hello world";
    }
}


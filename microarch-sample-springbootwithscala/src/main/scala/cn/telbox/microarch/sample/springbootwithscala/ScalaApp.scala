package cn.telbox.microarch.sample.springbootwithscala

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by dahui on 06/11/2016.
 */

@SpringBootApplication
class ScalaApp {
}

object ScalaApp extends App {
  SpringApplication.run(classOf[ScalaApp], args: _*)
}
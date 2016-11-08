package cn.telbox.microarch.sample.springbootwithscala

import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RestController}

/**
  * Created by dahui on 06/11/2016.
  */
@RestController
class HelloController {

  @RequestMapping(value = Array("/hello/{name}"), method = Array(RequestMethod.GET))
  def hello(@PathVariable name: String): String = {
    "hello " + name
  }
}

package cn.telbox.microarch.base.tools

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.{EnableScheduling, Scheduled}
;

/**
 * Created by dahui on 08/11/2016.
 */
@SpringBootApplication
@EnableScheduling
class SingletonJobDemo extends TraitOfSingletonServiceBasedOnZK[String, Int] {
  private val logger = LoggerFactory.getLogger(SingletonJobDemo.getClass)

  @Scheduled(cron = "*/5 * * * * ?")
  def singleJob() = {
    val work = (name: String) => {
      logger.info("Hello, {}\n", name)
      logger.warn("Hello, {}\n", name)
      logger.error("Hello, {}\n", name)
      0
    }
    doSingletonJob("127.0.0.1:2181", "/leader/demo", work)("Job 1")
  }
}

object SingletonJobDemo extends App {
  SpringApplication.run(classOf[SingletonJobDemo], args: _*)
}

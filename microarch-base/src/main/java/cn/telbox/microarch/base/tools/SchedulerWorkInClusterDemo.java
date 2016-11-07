package cn.telbox.microarch.base.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;


/**
 * Created by dahui on 07/11/2016.
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableScheduling
public class SchedulerWorkInClusterDemo extends AbstractSingletonServiceBasedOnZK<Long, Integer> {
    private final static Logger logger = LoggerFactory.getLogger(SchedulerWorkInClusterDemo.class);


    @Value("${nixle.pubs.zookeeper.connection:127.0.0.1:2181}")
    private String zookeeperConnectionString;

    // !!! This path may NOT the same as others !!!
    @Value("${nixle.pubs.zookeeper.path.puller.leader.demo:/nixle/safetyawareness/pubPullerScheduler/leader/demo}")
    private String pathOfPullerSchedulerLeader;

    public SchedulerWorkInClusterDemo() {}

    @PostConstruct
    private void postConstruct() {
        System.out.println("postConstruct");
        logger.info("post constructor on subClass ....1");
        logger.info("post constructor on subClass ....2");
        logger.info("post constructor on subClass ....3");
        logger.info("post constructor on subClass ....4");
    }

    Function<Long, Optional<Integer>> shedulerWorkFunction = shedulerWorkFunction -> {
        doJob1(shedulerWorkFunction);
        doJob2();
        return Optional.of(0);
    };

    @Scheduled(cron="*/5 * * * * ?")
    void schedulerJob() {
        doSingletonWork(shedulerWorkFunction, 66L).ifPresent(System.out::println);
    }

    private void doJob1(Long param1) {
        logger.info("Job1 ===> %d", param1);
    }

    private void doJob2() {
        logger.info("Job2 ===>");
    }

    @Override
    String singletonServicePath() {
        return pathOfPullerSchedulerLeader;
    }

    @Override
    public String zookeeperConnectionString() {
        return zookeeperConnectionString;
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SchedulerWorkInClusterDemo.class, args);

        System.out.println("====");
        System.in.read();
    }
}

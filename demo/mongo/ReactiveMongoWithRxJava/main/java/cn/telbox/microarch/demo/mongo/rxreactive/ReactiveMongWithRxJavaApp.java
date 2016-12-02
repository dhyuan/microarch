package cn.telbox.microarch.demo.mongo.rxreactive;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoDatabase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * Created by Dahui on 2016/12/2.
 */

@SpringBootApplication
public class ReactiveMongWithRxJavaApp {


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    private MongoDatabase mongoClient() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost");
        return mongoClient.getDatabase("yourdb");
    }

    @Bean
    private CommandLineRunner cmd(MongoDatabase db) {
        return arg -> {

            insertManyRecords();

            query();

            queryOne();

            batchUpdate();
        };
    }

    private void insertManyRecords() {

    }

    private void query() {

    }

    private void queryOne() {

    }

    private void batchUpdate() {

    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveMongWithRxJavaApp.class, args);
    }

}

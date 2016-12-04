package cn.telbox.microarch.demo.mongo.rxreactive;

import com.google.common.collect.Lists;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.operation.InsertOperation;
import com.mongodb.rx.client.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Dahui on 2016/12/2.
 */

@SpringBootApplication
public class ReactiveMongWithRxJavaApp {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveMongWithRxJavaApp.class);


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost");
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    MongoDatabase mongoDB(MongoClient mongoClient) {
        return mongoClient.getDatabase("maDB");
    }


    @Bean
    MongoCollection<Document> contactCollection(MongoDatabase db) {
        return db.getCollection("Contact");
    }

    private MongoCollection<Document> contactCollection() {
        return contactCollection(mongoDB(mongoClient()));
    }

    @Bean
    CommandLineRunner cmd(MongoDatabase db) {
        return arg -> {
            // In fact, all of the following methods are executed in parallel manor, it is no correct.
            // Should compose these APIs ...

            cleanAllContact();

            createIndex();

            addContact();
            queryOne();

            insertManyRecords();

            query();
            batchUpdate();

        };
    }

    private void cleanAllContact() {
        Subscriber<DeleteResult> subscriber = subscriberForDeleteOperation();
        contactCollection()
                .deleteMany(new Document())
                .subscribe(subscriber);
    }

    private Subscriber<DeleteResult> subscriberForDeleteOperation() {
        return new Subscriber<DeleteResult>() {
            @Override
            public void onCompleted() {
                logger.debug("---removeAllContact onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                logger.debug("---removeAllContact onError");
            }

            @Override
            public void onNext(DeleteResult deleteResult) {
                logger.debug("---removeAllContact onNext.  deletedCount={} acknowledged={}", deleteResult.getDeletedCount(), deleteResult.wasAcknowledged());
            }
        };
    }

    private void createIndex() {
        contactCollection()
                .createIndex(new Document("id", 1).append("unique", true))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        logger.debug("-->createIndex onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.debug("-->createIndex onError. {}", e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        logger.debug("-->createIndex onNext. {}", s);
                    }
                });
    }


    private void addContact() {
        logger.debug("mongoDB: " + mongoDB(mongoClient()).toString());

        Document contact = new Document("name", "contact0")
                .append("id", 0)
                .append("age", "12")
                .append("address", new Document("city", "北京").append("address", "Fuxinglu"));

        Observable<Success> observable = contactCollection().insertOne(contact);
        
        observable.subscribe(new Observer<Success>() {
            @Override
            public void onCompleted() {
                logger.debug("==> onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.debug("==> onError", throwable);
            }

            @Override
            public void onNext(Success success) {
                logger.debug("==> onNext");
            }
        });
    }

    private void queryOne() {
        logger.debug("mongoDB: " + mongoDB(mongoClient()).toString());
        contactCollection()
                .find(eq("id", 0))
                .first()
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onCompleted() {
                        logger.debug("==>queryOne onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        logger.debug("==>queryOne onError", throwable);
                    }

                    @Override
                    public void onNext(Document document) {
                        logger.debug("==>queryOne onNext:  {}" + document.toJson());
                    }
                });
    }


    private void insertManyRecords() {
        logger.debug("mongoDB: " + mongoDB(mongoClient()).toString());

        final List<WriteModel<Document>> contactDocs = new ArrayList();
        for (int i = 10; i < 1000; i ++) {
            Document contact = new Document("name", "contact" + i)
                    .append("id", i)
                    .append("age", "12")
                    .append("address", new Document("city", "北京").append("address", "Fuxinglu" + i));
            contactDocs.add(new InsertOneModel<>(contact));
        }
        contactCollection()
                .bulkWrite(contactDocs, new BulkWriteOptions().ordered(false))
                .subscribe(new Subscriber<BulkWriteResult>() {
                    @Override
                    public void onCompleted() {
                        logger.debug("--> writeBulkCompleted. ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.warn("--> writeBulkError. ", e);
                    }

                    @Override
                    public void onNext(BulkWriteResult bulkWriteResult) {
                        printBulkWriteResult(bulkWriteResult);
                    }
                });

    }

    private Action1<BulkWriteResult> printBulkWriteResultAction() {
        return bulkWriteResult -> {
            printBulkWriteResult(bulkWriteResult);
        };
    }

    private void printBulkWriteResult(BulkWriteResult bulkWriteResult) {
        logger.debug("-->bulkWriteResult.isModifiedCountAvailable: {}", bulkWriteResult.isModifiedCountAvailable());
        logger.debug("-->bulkWriteResult.wasAcknowledged: {}", bulkWriteResult.wasAcknowledged());
        logger.debug("-->bulkWriteResult.deletedCount: {}", bulkWriteResult.getDeletedCount());
        logger.debug("-->bulkWriteResult.insertedCount: {}", bulkWriteResult.getInsertedCount());
        logger.debug("-->bulkWriteResult.matchedCount: {}", bulkWriteResult.getMatchedCount());
        logger.debug("-->bulkWriteResult.modifiedCount: {}", bulkWriteResult.getModifiedCount());
        logger.debug("-->bulkWriteResult.upsertCount: {}", bulkWriteResult.getUpserts().size());
    }


    private void query() {
        logger.debug("mongoDB: " + mongoDB(mongoClient()).toString());
    }


    private void batchUpdate() {

    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveMongWithRxJavaApp.class, args);
    }

}

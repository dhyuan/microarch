package cn.telbox.microarch.mongo.demo.geo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Created by dahui on 10/1/16.
 */

@Document
public class User {


    private String name;

    private Instant usedTime;

    private LocalDateTime birthDayTime;

    public User() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Instant usedTime) {
        this.usedTime = usedTime;
    }

    public LocalDateTime getBirthDayTime() {
        return birthDayTime;
    }

    public void setBirthDayTime(LocalDateTime birthDayTime) {
        this.birthDayTime = birthDayTime;
    }
}

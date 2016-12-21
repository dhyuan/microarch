package cn.telbox.microarch.sample.organiazation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dahui on 11/12/2016.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Document
public class Organization {

    @Id
    private ObjectId _id;

    private String name;
    private String desc;

}

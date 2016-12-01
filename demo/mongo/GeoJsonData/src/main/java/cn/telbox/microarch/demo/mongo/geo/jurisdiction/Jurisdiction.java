package cn.telbox.microarch.demo.mongo.geo.jurisdiction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Dahui on 2016/12/1.
 */


@Document
@NoArgsConstructor @Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jurisdiction {

    @Id
    private ObjectId id;

    private String name;

    private GeoJsonGeometryCollection geoJsonCollection;

    @JsonProperty("areaCoordinates")
    private List<CoordinateData> coordinates;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CoordinateData {
        @JsonProperty("coordinates")
        public List<Object> coordinatesJson;
        public String type;
    }

    private Boolean isActive;
}

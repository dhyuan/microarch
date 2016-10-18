package cn.telbox.microarch.demo.mongo.geo;

import cn.telbox.microarch.demo.mongo.geo.model.DistrictInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by dahui on 10/9/16.
 */

public class TestGeo {

    public static void main(String[] args) {

        JsonReader reader = new JsonReader();
        DistrictInfo districtInfo = reader.read(GeoDataDemo.JSON, DistrictInfo.class);
        districtInfo.setQgisFeatures();



    }
}

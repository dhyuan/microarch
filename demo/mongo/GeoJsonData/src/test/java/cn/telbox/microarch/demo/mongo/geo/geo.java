package cn.telbox.microarch.demo.mongo.geo;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahui on 18/10/2016.
 */
public class geo {
    @Test
    public void testCoordinatesParse5026984() throws Exception {
        final InputStream resourceAsStream = geo.class.getResourceAsStream("/data/5026984.json");
        StringWriter writer = new StringWriter();
        IOUtils.copy(resourceAsStream, writer);
        String theString = writer.toString();
    }
}

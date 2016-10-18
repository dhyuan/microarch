package cn.telbox.microarch.demo.mongo.geo;

import cn.telbox.microarch.demo.mongo.geo.model.DistrictInfo;
import cn.telbox.microarch.demo.mongo.geo.model.UserLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * Created by dahui on 10/8/16.
 */
@SpringBootApplication
public class GeoDataDemo {


    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(), "yourdb");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;

    }

    @Autowired
    private GeoJsonModule geoJsonModule;

    @Bean
    CommandLineRunner runner(MongoTemplate mongoTemplate, UserLocation userLocation) {
        return args -> {
            mongoTemplate.save(userLocation);
            List<UserLocation> userLocations = mongoTemplate.findAll(UserLocation.class);
            userLocations.stream().forEach(System.out::println);

            JsonReader jsonReader = new JsonReader();
            DistrictInfo districtInfo = jsonReader.read(JSON, DistrictInfo.class);
            districtInfo.computeGeometriesData(null);
            districtInfo.setQgisFeatures();
            System.out.println("-===>" + districtInfo);
            mongoTemplate.save(districtInfo);
//
//            prepareDirtData(mongoTemplate, districtInfo);
//            prepareData(mongoTemplate, districtInfo);

            queryByGeoIntersect(mongoTemplate, 10, false);
            queryByGeoIntersect(mongoTemplate, 10, true);
            queryByGeoIntersect(mongoTemplate, 20, false);
            queryByGeoIntersect(mongoTemplate, 20, true);
            queryByGeoIntersect(mongoTemplate, 50, false);
            queryByGeoIntersect(mongoTemplate, 50, true);
            queryByGeoIntersect(mongoTemplate, 500, false);
            queryByGeoIntersect(mongoTemplate, 500, true);

            queryByGeoWithin(mongoTemplate, 10, false);
            queryByGeoWithin(mongoTemplate, 10, true);
            queryByGeoWithin(mongoTemplate, 20, false);
            queryByGeoWithin(mongoTemplate, 20, true);
            queryByGeoWithin(mongoTemplate, 50, false);
            queryByGeoWithin(mongoTemplate, 50, true);
            queryByGeoWithin(mongoTemplate, 500, false);
            queryByGeoWithin(mongoTemplate, 500, true);


//            mongoTemplate.findAllAndRemove(new Query(), DistrictInfo.class);
        };
    }

    private void prepareData(MongoTemplate mongoTemplate, DistrictInfo districtInfo) {
        for (int i = 0; i < 5000; i ++) {
            districtInfo.computeGeometriesData(0.000001 * i);
            mongoTemplate.save(districtInfo);
        }
    }

    private void prepareDirtData(MongoTemplate mongoTemplate, DistrictInfo districtInfo) {
        for (int i = 0; i < 5000; i ++) {
            districtInfo.computeGeometriesData(2 + 0.000001 * i);
            mongoTemplate.save(districtInfo);
        }
    }

    private void queryByGeoWithin(MongoTemplate mongoTemplate, int limitSize, Boolean isOnlyId) {
        GeoJsonPolygon polygon = new GeoJsonPolygon(
                new Point(-102.714554, 42.394023),
                new Point(-99.714554, 42.394023),
                new Point(-99.714548,40.394028),
                new Point(-102.714537,40.394029),
                new Point(-102.714554,42.394023));
        Point point = new Point(-100.714346, 41.392926);
        Criteria criteria = Criteria.where("points").within(polygon);
        Query query = Query.query(criteria);
        query.limit(limitSize);
        if (isOnlyId) query.fields().include("headline");

        long begTime = Instant.now().toEpochMilli();
        List<DistrictInfo> pubs = mongoTemplate.find(query, DistrictInfo.class);
        long endTime = Instant.now().toEpochMilli();
        System.out.println("queryByGeoWithin limitSize=" + limitSize + "  oneField=:" + isOnlyId + "  ====>" + pubs.size() + "  used millionSeconds: " + (endTime - begTime));
    }

    private void queryByGeoIntersect(MongoTemplate mongoTemplate, int limitSize, Boolean isOnlyId) {
        GeoJsonPolygon polygon = new GeoJsonPolygon(
                new Point(-100.713954, 41.393523),
                new Point(-100.714348,41.392928),
                new Point(-100.714437,41.392929),
                new Point(-100.713954,41.393523));
        Criteria criteria = Criteria.where("geometries").intersects(polygon);
        Query query = Query.query(criteria);
        query.limit(limitSize);
        if (isOnlyId) query.fields().include("headline");

        long begTime = Instant.now().toEpochMilli();
        List<DistrictInfo> pubs = mongoTemplate.find(query, DistrictInfo.class);
        long endTime = Instant.now().toEpochMilli();
        System.out.println("queryByGeoIntersect limitSize=" + limitSize + "  oneField=:" + isOnlyId + "  ====>" + pubs.size() + "  used millionSeconds: " + (endTime - begTime));
    }


    public static void main(String[] args) {
        SpringApplication.run(GeoDataDemo.class, args);
    }


    @Bean
    UserLocation userLocation() {
        UserLocation userLocation = new UserLocation();
        userLocation.setName("Location 1");

        // Point & GeoJsonPoint
        Point point = new Point(-2, 2);
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint(point);

        Point point1 = new Point(0, 0);
        Point point2 = new Point(2, 0);
        Point point3 = new Point(2, 2);
        Point point4 = new Point(0, 2);

        Point pointA1 = new Point(0, 0);
        Point pointA2 = new Point(20, 0);
        Point pointA3 = new Point(20, 20);
        Point pointA4 = new Point(0, 20);

        List<Point> squareLine = Lists.newArrayList(point1, point2, point3, point4);
        List<Point> squareLineA = Lists.newArrayList(pointA1, pointA2, pointA3, pointA4);

        // Polygon & GeoJsonPolygon
        Polygon polygon = new Polygon(squareLine);
        Polygon polygonA = new Polygon(squareLineA);

        GeoJsonPolygon geoJsonPolygon = new GeoJsonPolygon(squareLine);
        GeoJsonPolygon geoJsonPolygonA = new GeoJsonPolygon(squareLineA);

        List<GeoJsonPolygon> geoJsonPolygons = Lists.newArrayList(geoJsonPolygon, geoJsonPolygonA);

        // GeoJsonMultiPolygon
        GeoJsonMultiPolygon geoJsonMultiPolygon = new GeoJsonMultiPolygon(geoJsonPolygons);

        userLocation.setLocation(geoJsonPoint);
        userLocation.setLocPoint(point);
        userLocation.setPolygon(geoJsonPolygon);
        userLocation.setLocPolygon(polygon);
        userLocation.setMultiPolygon(geoJsonMultiPolygon);


        return userLocation;
    }


    public static final String JSON = "{\"ManagerInfoExtended\": {\"UPLOAD_URL\": \"https://s3.amazonaws.com/nixleqa1/uploads/\", \"general\": {\"addr\": \"614 W 9th St\", \"addr_accuracy\": 8, \"addr_entered\": \"614 W 9th St, North Platte, NE 69101, USA\", \"addr_geocoded\": \"614 W 9th St, North Platte, NE 69101, USA\", \"addr_provider\": \"G\", \"business_type\": null, \"can_upload_csv_contacts\": 0, \"category_id\": 1, \"city\": \"North Platte\", \"cmamtext_author\": 0, \"cmas_enabled\": 0, \"cog_id\": \"\", \"created\": \"2016-03-24 17:28:46\", \"createdby_id\": null, \"dial_group_count\": 1, \"dial_provider\": \"\", \"do_not_obfuscate_flag\": 0, \"eas_enabled\": 0, \"everbridge_agency_id\": 888409690210564, \"facebook_comments_enabled\": 0, \"facebook_default\": 0, \"id\": 13691, \"is_active\": 1, \"is_default\": 1, \"is_dial\": 0, \"is_enable_publish_api\": 0, \"is_individual\": 0, \"is_level_in_sms\": 0, \"is_national\": 0, \"is_nixle_special\": 0, \"is_private\": 0, \"is_statewide\": 0, \"is_verified\": 1, \"keyword\": \"\", \"latitude\": 41.1441897, \"logo\": \"\", \"logo_lg\": \"\", \"logo_md\": \"\", \"logo_sm\": \"\", \"longitude\": -100.768033, \"max_group_count\": null, \"max_keyword\": 1, \"max_keywords_count\": 1, \"modified\": \"2016-03-25 09:37:13\", \"name\": \"steven 360 default public\", \"phone\": \"\", \"phone2\": \"\", \"require_confirmation\": 1, \"salesforce_id\": null, \"short_code\": \"888777\", \"short_name\": \"st_360_def_pub\", \"short_url\": null, \"sms_reverse_opt_in_message\": 0, \"sp_customer_id\": 0, \"state\": \"NE\", \"subscription_plan\": \"\", \"suite\": \"\", \"timezone\": \"America/Los_Angeles\", \"tipping_keyword_group_id\": null, \"tipping_special_features\": 0, \"twitter_default\": 0, \"twitter_id\": \"\", \"twitter_oauth\": \"\", \"twitter_oauth_secret\": \"\", \"twitter_pw\": \"\", \"urlname\": \"steven-360-default-public\", \"website\": \"\", \"widget_collect_full_name\": 0, \"widget_create_default_subscription\": 0, \"widget_hide_language\": 0, \"widget_hide_powered_by\": 0, \"widget_line_1\": \"\", \"widget_line_2\": \"\", \"widget_line_3\": \"\", \"wire_group_count\": null, \"zipcode\": \"69101\"}, \"geographies\": {\"cities\": [], \"counties\": [], \"custom_areas\": [], \"jurisdiction\": [], \"zipcodes\": [{\"id\": 82711, \"name\": \"69101\", \"state\": \"NE\"}]}, \"groups\": [{\"description\": \"Citizens registered through nixle.com or mobile zip code opt-in\", \"id\": 28218, \"name\": \"Nixle Wire\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}, {\"description\": \"\", \"id\": 28221, \"name\": \"group 1\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}, {\"description\": \"\", \"id\": 28222, \"name\": \"Nixle Dial\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}, {\"description\": \"\", \"id\": 28370, \"name\": \"bb group\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}, {\"description\": \"\", \"id\": 28371, \"name\": \"betty 2\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}, {\"description\": \"\", \"id\": 28376, \"name\": \"pvt group 1\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"private\"}, {\"description\": \"\", \"id\": 28401, \"name\": \"betty public 413\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}, {\"description\": \"\", \"id\": 28402, \"name\": \"betty pri group 413\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"private\"}, {\"description\": \"\", \"id\": 28467, \"name\": \"ag-200\", \"parent_id\": 0, \"student_group\": 0, \"type\": \"public\"}], \"permissions\": {\"cap\": 1, \"custom_area\": 0, \"facebook\": 0, \"is_org_with_engage\": 0, \"is_smb\": 0, \"non_emergency_voice_dial\": 1, \"twitter\": 0, \"voice\": 1}, \"products\": [{\"name\": \"interconnect\"}, {\"name\": \"tipping\"}, {\"name\": \"everbridge_dial\"}, {\"name\": \"engage\"}, {\"name\": \"reply\"}, {\"name\": \"nws\"}, {\"name\": \"non_emergency_voice\"}], \"tipping\": {\"tipping_enabled\": 1, \"tipping_keyword\": null, \"tipping_special_features\": 0}}, \"Cap\": {\"category\": \"Security\", \"certainty\": \"Observed\", \"event_description\": \"Missing Person\", \"instructions\": \"hhjg\", \"severity\": \"Extreme\", \"urgency\": \"Immediate\"}, \"ContactInformation\": {\"division\": \"\", \"email\": \"\", \"name\": \"\", \"phone\": \"\"}, \"Detailed_Statistics\": {\"agency_id\": 13691, \"created\": \"2016-09-29 19:54:07\", \"email_data_finalized\": 0, \"email_end\": \"2016-09-29 19:54:57\", \"email_forwards\": 0, \"email_start\": \"2016-09-29 19:54:57\", \"email_total\": null, \"modified\": \"2016-09-29 19:56:11\", \"pub_id\": 5011409, \"sms_data_finalized\": 0, \"sms_end\": \"2016-09-29 19:54:12\", \"sms_forwards\": 0, \"sms_start\": \"2016-09-29 19:54:12\", \"sms_stops\": 0, \"sms_total\": 14}, \"Dial_Statistics\": null, \"DistributionAreas\": [{\"coordinates\": [[[[-100.713954, 41.393523], [-100.714348, 41.392928], [-100.714437, 41.386615], [-100.714221, 41.369398], [-100.70171, 41.368989], [-100.694809, 41.366751], [-100.695658, 41.307957], [-100.676665, 41.307966], [-100.676517, 41.300436], [-100.658408, 41.299015], [-100.63857, 41.299512], [-100.638483, 41.294731], [-100.637605, 41.29242], [-100.630499, 41.303305], [-100.62542, 41.307991], [-100.600445, 41.30718], [-100.580671, 41.307113], [-100.580599, 41.292292], [-100.553, 41.29227], [-100.552045, 41.29376], [-100.536651, 41.294975], [-100.526824, 41.294073], [-100.522882, 41.292219], [-100.485744, 41.292192], [-100.484844, 41.263896], [-100.485351, 41.248014], [-100.485589, 41.242661], [-100.504124, 41.241353], [-100.50567, 41.240965], [-100.513797, 41.235799], [-100.514797, 41.234541], [-100.523737, 41.234315], [-100.524399, 41.187832], [-100.525309, 41.184858], [-100.523946, 41.133021], [-100.525015, 41.120344], [-100.524552, 41.081203], [-100.527092, 41.079649], [-100.534068, 41.080841], [-100.542147, 41.083106], [-100.545291, 41.082329], [-100.547741, 41.082535], [-100.549192, 41.083153], [-100.553662, 41.086686], [-100.641615, 41.112995], [-100.657522, 41.120205], [-100.662455, 41.114693], [-100.663453, 41.114579], [-100.66558, 41.115733], [-100.668474, 41.114305], [-100.669572, 41.11004], [-100.655383, 41.102431], [-100.637541, 41.090496], [-100.637065, 41.089477], [-100.639516, 41.086779], [-100.644545, 41.088783], [-100.644546, 41.084736], [-100.634167, 41.084283], [-100.630389, 41.082566], [-100.635082, 41.072731], [-100.639286, 41.070514], [-100.639719, 41.069867], [-100.639755, 41.062526], [-100.637931, 41.062416], [-100.633155, 41.06381], [-100.627927, 41.063647], [-100.625047, 41.06202], [-100.624146, 41.059801], [-100.6181, 41.057037], [-100.612325, 41.053173], [-100.610873, 41.051804], [-100.605364, 41.037483], [-100.613611, 41.036473], [-100.61769, 41.037455], [-100.6218, 41.038986], [-100.621683, 41.043058], [-100.626702, 41.046467], [-100.630933, 41.046355], [-100.631086, 41.044273], [-100.627793, 41.042304], [-100.624458, 41.037726], [-100.624483, 41.032327], [-100.626109, 41.033565], [-100.626591, 41.035075], [-100.630001, 41.039286], [-100.639274, 41.044597], [-100.641908, 41.035539], [-100.64191, 41.033275], [-100.640643, 41.030918], [-100.640616, 41.026068], [-100.641735, 41.024582], [-100.659405, 41.02445], [-100.659442, 41.013975], [-100.645897, 41.014103], [-100.640405, 41.011407], [-100.639979, 40.992573], [-100.640553, 40.991681], [-100.641852, 40.991247], [-100.645836, 40.9931], [-100.6481, 40.99333], [-100.65127, 40.992736], [-100.655014, 40.992805], [-100.657428, 40.995643], [-100.658666, 40.995734], [-100.658578, 40.988711], [-100.65894, 40.988345], [-100.677024, 40.989011], [-100.677146, 40.974439], [-100.696281, 40.974554], [-100.696607, 40.931251], [-100.668012, 40.931068], [-100.667047, 40.931388], [-100.666144, 40.930265], [-100.665971, 40.925245], [-100.677072, 40.925426], [-100.676753, 40.917152], [-100.697023, 40.91695], [-100.698082, 40.915742], [-100.699047, 40.916817], [-100.715604, 40.916745], [-100.715862, 40.887739], [-100.697325, 40.887765], [-100.696905, 40.874172], [-100.69582, 40.873188], [-100.753952, 40.873037], [-100.753758, 40.916528], [-100.83005, 40.916358], [-100.829797, 40.858682], [-100.848597, 40.857853], [-100.84892, 40.84342], [-100.867475, 40.843458], [-100.905487, 40.844566], [-100.924463, 40.843906], [-100.944011, 40.844317], [-100.944101, 40.830113], [-100.991801, 40.830488], [-100.991856, 40.859171], [-101.001984, 40.859353], [-101.001038, 40.873465], [-101.00126, 40.989105], [-101.001234, 41.047427], [-100.944844, 41.047189], [-100.945189, 41.119537], [-100.926013, 41.119309], [-100.915909, 41.122853], [-100.911118, 41.126742], [-100.925915, 41.130156], [-100.926104, 41.148189], [-100.926669, 41.148492], [-100.931178, 41.146434], [-100.936836, 41.145427], [-100.943977, 41.145542], [-100.953843, 41.148972], [-100.959138, 41.14849], [-100.965311, 41.149678], [-100.973027, 41.14947], [-100.981318, 41.148621], [-100.982559, 41.147729], [-100.988004, 41.146697], [-100.992816, 41.147289], [-101.001293, 41.1469], [-101.00234, 41.147216], [-101.002375, 41.154751], [-101.000966, 41.155425], [-100.969066, 41.155281], [-100.964467, 41.156562], [-100.962409, 41.156427], [-100.960623, 41.155649], [-100.945099, 41.155629], [-100.94511, 41.156473], [-100.887244, 41.153484], [-100.887414, 41.158706], [-100.914986, 41.158715], [-100.91665, 41.159195], [-100.920686, 41.162391], [-100.964318, 41.162601], [-100.96426, 41.16984], [-100.983314, 41.169869], [-100.983215, 41.17705], [-101.002326, 41.177137], [-101.002391, 41.206162], [-100.966034, 41.205602], [-100.945501, 41.205994], [-100.945291, 41.220312], [-100.964496, 41.220034], [-101.021584, 41.220991], [-101.0217, 41.306749], [-101.002607, 41.306686], [-101.002498, 41.357281], [-101.000978, 41.359704], [-100.998676, 41.358192], [-100.991601, 41.356435], [-100.986257, 41.355866], [-100.984438, 41.358337], [-100.983894, 41.362111], [-100.982498, 41.362706], [-100.980161, 41.362273], [-100.975393, 41.359598], [-100.964797, 41.355988], [-100.95912, 41.355463], [-100.956237, 41.356173], [-100.956815, 41.359169], [-100.95809, 41.36157], [-100.96377, 41.367836], [-100.968509, 41.371997], [-100.964772, 41.377834], [-100.96453, 41.378886], [-100.965776, 41.382088], [-100.968268, 41.385976], [-100.970638, 41.387919], [-100.976927, 41.390639], [-100.983338, 41.394708], [-100.776212, 41.393726], [-100.771473, 41.393694], [-100.713954, 41.393523]]]], \"type\": \"MultiPolygon\"}], \"DistributionGroups\": [28218], \"EventLocation\": {\"GeoLocation\": {\"lat\": null, \"lon\": null}, \"LocationAddress\": {\"address\": \"Indn Route 9073\", \"city\": \"Fort Defiance\", \"state\": \"AZ\", \"zip\": \"86504\"}}, \"PubMedia\": {}, \"Status\": {\"status_code\": 1, \"status_text\": \"Active\"}, \"Translations\": {}, \"agency_id\": 13691, \"apply_geographic_filtering\": 0, \"bc_accuracy\": null, \"bc_addr\": \"614 W 9th St\", \"bc_addr_type\": 3, \"bc_all_direct\": 1, \"bc_city\": \"North Platte\", \"bc_cross_st\": \"\", \"bc_entered\": \"\", \"bc_geocoded\": \"\", \"bc_latitude\": 41.1441897, \"bc_longitude\": -100.768033, \"bc_provider\": \"\", \"bc_radius\": 0.0, \"bc_state\": \"NE\", \"bc_zipcode\": \"69101\", \"caller_id_phone\": \"\", \"cmamtext\": \"\", \"created\": \"2016-09-29 19:54:07\", \"createdby\": 30593, \"dial_provider\": \"\", \"distribution_status\": \"REDY\", \"event_code\": \"\", \"expires\": \"2016-09-30 19:54:06\", \"facebook_user_id\": null, \"filter_by_region_op\": 1, \"filter_by_wireword_op\": 0, \"headline\": \"ghh\", \"headline_sms\": \"ghh\", \"html\": \"vhhj\", \"incident_accuracy\": null, \"incident_entered\": \"\", \"incident_geocoded\": \"\", \"incident_provider\": \"\", \"include_incident\": 1, \"is_deactivated\": 0, \"is_html\": 1, \"is_test\": 0, \"last_modified\": \"2016-09-29 19:54:07\", \"leave_voicemail\": 0, \"max_retries\": 0, \"minutes_between_retry\": 10, \"mo_reply_handler\": 0, \"nixle_review\": 0, \"org_pub_id\": null, \"parent_pub_id\": null, \"priority\": \"Alert\", \"privacy\": 0, \"pub_id\": 5011409, \"publish_on\": null, \"published_time\": \"2016-09-29 19:54:07\", \"record_voice_phone\": \"\", \"reference_source\": \"\", \"reference_url\": \"\", \"require_confirmation\": 0, \"response_type\": \"\", \"send_dial\": 0, \"send_email\": 1, \"send_facebook\": 0, \"send_sms\": 1, \"send_twitter\": 0, \"sent_to_cmas\": 0, \"sent_to_eas\": 0, \"shorturl\": \"qa1-citizen.nixle.com/87L8Z\", \"soundfile_path\": \"\", \"text\": \"vhhj\", \"tts_text\": \"\", \"version\": 1}";
}

@RestController
class UserLocationController {
    @RequestMapping("userLocations")
    public List<UserLocation> userLocations() {
        return mongoTemplate.findAll(UserLocation.class);
    }

    @Autowired
    MongoTemplate mongoTemplate;
}

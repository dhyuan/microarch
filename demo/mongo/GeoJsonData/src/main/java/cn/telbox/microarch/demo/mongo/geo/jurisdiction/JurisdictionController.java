package cn.telbox.microarch.demo.mongo.geo.jurisdiction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Dahui on 2016/12/1.
 */

@Controller
@RequestMapping("/jurisdiction")
public class JurisdictionController {

    @RequestMapping(value = "/mgr", method = RequestMethod.GET)
    public String mgr() {
        return "jurisdiction";
    }
}

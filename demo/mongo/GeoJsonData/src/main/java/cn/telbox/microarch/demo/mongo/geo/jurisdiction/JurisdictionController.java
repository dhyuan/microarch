package cn.telbox.microarch.demo.mongo.geo.jurisdiction;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Dahui on 2016/12/1.
 */

@Controller
@RequestMapping("/jurisdiction")
public class JurisdictionController {

    @RequestMapping(value = "/mgr")
    public String mgr(Model model) {
        return "jurisdiction";
    }

    @RequestMapping(value = "/mgr0")
    public String mgr0(Model model) {
        return "jurisdiction.html";
    }


    @RequestMapping(value = "/hello")
    public String sayHello(Model model) {
        model.addAttribute("name", "abcddd");
        return "hello";
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}

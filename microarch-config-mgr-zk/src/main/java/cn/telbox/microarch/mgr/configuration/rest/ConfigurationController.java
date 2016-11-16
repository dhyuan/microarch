package cn.telbox.microarch.mgr.configuration.rest;

import cn.telbox.microarch.mgr.configuration.model.ConfigItem;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dahui on 16/11/2016.
 */
@RestController
public class ConfigurationController {

    @RequestMapping(value = "/mgr/cfg/", method = RequestMethod.POST)
    void setConfig(ConfigItem cfgItem) {

    }


}

package cn.telbox.microarch.mgr.configuration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by dahui on 16/11/2016.
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ConfigItem {
    private String key;
    private String value;
}

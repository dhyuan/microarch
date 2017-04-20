package cn.telbox.microarch.base.tools.validation;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;

/**
 * Created by dahui on 20/04/2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Max(20)
    private String city;


    private String street;
}

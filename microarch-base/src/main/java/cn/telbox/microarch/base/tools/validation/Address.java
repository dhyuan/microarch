package cn.telbox.microarch.base.tools.validation;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by dahui on 20/04/2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Min(2) @Max(20)
    private String city;

    @NotNull
    private String street;
}

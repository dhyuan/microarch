package cn.telbox.microarch.base.tools.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.List;

/**
 * Created by Dahui on 4/21/2017.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = OptionTConstrainValidator.class)
public @interface TValueOptions {

    String message() default "Value does not belong the options.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends List<?>>[] options();
}

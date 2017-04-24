package cn.telbox.microarch.base.tools.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Dahui on 4/21/2017.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringValueOptionsConstraintValidator.class)
public @interface StringValueOptions {

    String message() default "Value does not belong the options.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String options() default "";
    String seperator() default ",";
}

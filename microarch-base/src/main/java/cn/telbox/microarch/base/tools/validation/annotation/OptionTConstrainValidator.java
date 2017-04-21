package cn.telbox.microarch.base.tools.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Dahui on 4/21/2017.
 */
public class OptionTConstrainValidator<T> implements ConstraintValidator<TValueOptions, T> {

    @Override
    public void initialize(TValueOptions constraintAnnotation) {

    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        return false;
    }
}

package cn.telbox.microarch.base.tools.validation.annotation;

import com.google.common.collect.Sets;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * Created by Dahui on 4/21/2017.
 */
public class StringValueOptionsConstraintValidator implements ConstraintValidator<StringValueOptions, String> {

    private Set<String> options;

    @Override
    public void initialize(StringValueOptions constraintAnnotation) {
        final String optionStr = constraintAnnotation.options() == null ? "" : constraintAnnotation.options();
        final String separator = constraintAnnotation.seperator() == null ? "," : constraintAnnotation.seperator();

        options = Sets.newHashSet(optionStr.split(separator));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        return options.contains(value);
    }
}

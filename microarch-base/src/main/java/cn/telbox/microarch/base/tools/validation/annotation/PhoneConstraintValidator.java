package cn.telbox.microarch.base.tools.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Dahui on 4/21/2017.
 */
public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone phone) { }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext cxt) {
        if(phoneField == null) {
            return false;
        }

        String regex4Email = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        String regex4IdCard = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        String regex4Mobile = "(\\+\\d+)?1[3458]\\d{9}$";
        String regex4Integer = "\\-?[1-9]\\d+";
        String regex4Float = "\\-?[1-9]\\d+(\\.\\d+)?";
        String regex4Zipcode = "[1-9]\\d{5}";
        String regex4Chinese = "^[\u4E00-\u9FA5]+$";
        String regex4Url = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        String regex4IP = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";

        String regex4Phone = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return phoneField.matches(regex4Phone);
    }

}

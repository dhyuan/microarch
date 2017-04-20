package cn.telbox.microarch.base.tools.validation;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

/**
 * Created by dahui on 20/04/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootConfiguration
@SpringBootTest
public class DemoValidationByAPI {

    @Test
    public void demoValidation() {
        System.out.println("==-----------------------------------------=");
        Address addr0 = new Address();
        Address addr1 = new Address();

        User user = new User();
        user.setName("a");
        user.setAddresses(Lists.newArrayList(addr0, addr1));
        user.setAge(-9);
        user.setLoginId("u1");

        BindingResult result = new BindException(user, "user");

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.validate(user, result);

        final List<ObjectError> allErrors = result.getAllErrors();
        System.out.println(allErrors);

    }


}

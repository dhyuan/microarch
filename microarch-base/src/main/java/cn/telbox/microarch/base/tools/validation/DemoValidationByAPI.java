package cn.telbox.microarch.base.tools.validation;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

/**
 * Created by dahui on 20/04/2017.
 */

@SpringBootApplication
public class DemoValidationByAPI {
    private static final Logger logger = LoggerFactory.getLogger(DemoValidationByAPI.class);

    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Autowired
    Validator validator;

    @Bean
    CommandLineRunner cmd() {
        return args -> {
            System.out.println("==-----------------------------------------=");
            Address addr0 = new Address();
            Address addr1 = new Address();

            User user = new User();
            user.setName("a");
            user.setAddresses(Lists.newArrayList(addr0, addr1));
            user.setAge(-9);
            user.setLoginId("u1");
            user.setNickName("dolphin");

            BindingResult result = new BindException(user, "user");

            validator.validate(user, result);

            final List<ObjectError> allErrors = result.getAllErrors();
            allErrors.stream().forEach(e -> {
                if (e instanceof FieldError) {
                    final FieldError fe = (FieldError) e;
                    logger.warn(fe.getCode() + "." + fe.getObjectName() + "." + fe.getField() + "  ---> " + fe.getRejectedValue());
                }else {
                    logger.warn(e.toString());
                }
            });
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoValidationByAPI.class, args);
    }

}

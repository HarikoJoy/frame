package com.frame.hariko.web.base.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Mobile.Validator.class)
public @interface Mobile {
    String MOBILE_PHONE_REGEX = "^1[34587]\\d{9}$";

    String message() default "{Mobile.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Mobile , String >
    {
        @Override
        public void initialize(Mobile mobile)
        {
            //do nothing
        }


        @Override
        public boolean isValid(String mobile , final ConstraintValidatorContext constraintValidatorContext)
        {
            return java.util.regex.Pattern.matches(MOBILE_PHONE_REGEX,mobile);
        }

    }
}

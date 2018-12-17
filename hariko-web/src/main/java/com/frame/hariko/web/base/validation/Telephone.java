package com.frame.hariko.web.base.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseName.Validator.class)
public @interface Telephone {
    String COMPANY_FIXED_LINE_PHONE_REGEX = "^(?:010|02\\d|0[3-9]\\d{2})\\d{7,8}(?:\\-\\d{1,4}$)?";

    String message() default "{Telephone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Telephone , String >
    {
        @Override
        public void initialize(Telephone telephone)
        {
            //do nothing
        }


        @Override
        public boolean isValid(String telephone , final ConstraintValidatorContext constraintValidatorContext)
        {
           return Pattern.matches(COMPANY_FIXED_LINE_PHONE_REGEX,telephone);
        }
    }
}

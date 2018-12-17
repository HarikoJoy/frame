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
@Constraint(validatedBy = QQ.Validator.class)
public @interface QQ {
    String QQ_REGEX = "^[1-9][0-9]{4,}$";
    String message() default "{QQ.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<QQ , String >
    {
        @Override
        public void initialize(QQ qq)
        {
            //do nothing
        }

        @Override
        public boolean isValid(String qq , final ConstraintValidatorContext constraintValidatorContext)
        {
            return Pattern.matches(QQ_REGEX,qq);
        }
    }
}

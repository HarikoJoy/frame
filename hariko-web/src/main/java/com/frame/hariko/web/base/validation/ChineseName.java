package com.frame.hariko.web.base.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseName.Validator.class)
public @interface ChineseName {

    String CHINESE_NAME_ONLY_REGEX = "^[\u4e00-\u9fa5]([\u4e00-\u9fa5]{0,24}\u00b7{0,1}[\u4e00-\u9fa5]{1,24})+$";

    String message() default "{ChineseName.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ChineseName , String >
    {
        @Override
        public void initialize(ChineseName chineseName)
        {
            //do nothing
        }

        @Override
        public boolean isValid(String chineseName , final ConstraintValidatorContext constraintValidatorContext)
        {
            return java.util.regex.Pattern.matches(CHINESE_NAME_ONLY_REGEX,chineseName);
        }
    }
}

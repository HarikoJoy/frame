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

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChineseName.Validator.class)
public @interface ChineseOnly {
     String CHINESE_ONLY_REGEX = "^[\u4e00-\u9fa5]+$";

     String message() default "{ChineseOnly.message}";

     Class<?>[] groups() default {};

     Class<? extends Payload>[] payload() default {};

     class Validator implements ConstraintValidator<ChineseOnly , String >
     {
          @Override
          public void initialize(ChineseOnly chineseOnly)
          {
               //do nothing
          }


          @Override
          public boolean isValid(String chineseOnly , final ConstraintValidatorContext constraintValidatorContext)
          {
               return Pattern.matches(CHINESE_ONLY_REGEX,chineseOnly);
          }
     }
}

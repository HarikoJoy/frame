package com.frame.hariko.web.base.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.frame.hariko.util.CnidUtil;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Cnid.Validator.class)
public @interface Cnid
{
	String message() default "{Cnid.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<Cnid , String >
	{
		@Override
		public void initialize(Cnid cnidAnnotation)
		{
			//do nothing
		}


		@Override
		public boolean isValid(String cnid , final ConstraintValidatorContext constraintValidatorContext)
		{

			return CnidUtil.isValidCnid(cnid);
		}
	}
}

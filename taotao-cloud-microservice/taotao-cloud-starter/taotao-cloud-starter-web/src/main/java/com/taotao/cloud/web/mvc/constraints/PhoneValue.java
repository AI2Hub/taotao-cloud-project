package com.taotao.cloud.web.mvc.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.taotao.cloud.web.mvc.validator.PhoneValueValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 校验手机号码格式
 *
 * @author aaronuu
 */
@Documented
@Constraint(validatedBy = PhoneValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneValue {

	String message() default "手机号码格式不正确";

	Class[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 是否必填
	 * <p>
	 * 如果必填，在校验的时候本字段没值就会报错
	 */
	boolean required() default true;

	@Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RUNTIME)
	@Documented
	@interface List {
		PhoneValue[] value();
	}
}

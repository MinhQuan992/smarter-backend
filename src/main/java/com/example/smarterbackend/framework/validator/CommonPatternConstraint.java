package com.example.smarterbackend.framework.validator;

import com.example.smarterbackend.framework.common.constant.RegexConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CommonPatternValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonPatternConstraint {
  String message() default "Invalid value";
  String regex() default RegexConstants.GENDER_PATTERN;
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}

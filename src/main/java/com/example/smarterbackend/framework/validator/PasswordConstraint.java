package com.example.smarterbackend.framework.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
  String message() default "Invalid value";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}

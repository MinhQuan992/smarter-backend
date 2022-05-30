package com.example.smarterbackend.framework.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonPatternValidator
    implements ConstraintValidator<CommonPatternConstraint, String> {
  private String regex;

  @Override
  public void initialize(CommonPatternConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    regex = constraintAnnotation.regex();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (StringUtils.isEmpty(value)) {
      return true;
    }
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}

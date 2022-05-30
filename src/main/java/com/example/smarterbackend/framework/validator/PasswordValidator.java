package com.example.smarterbackend.framework.validator;

import com.example.smarterbackend.framework.common.constant.RegexConstants;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
  @Override
  public void initialize(PasswordConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (StringUtils.isEmpty(value)) {
      return true;
    }
    if (value.length() < 8 || value.length() > 20) {
      return false;
    }
    Pattern pattern = Pattern.compile(RegexConstants.PASSWORD_PATTERN);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}

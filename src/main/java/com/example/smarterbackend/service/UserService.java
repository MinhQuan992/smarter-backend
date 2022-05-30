package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.InvalidRequestException;
import com.example.smarterbackend.exception.NotFoundException;
import com.example.smarterbackend.exception.OtpException;
import com.example.smarterbackend.exception.ResourceConflictException;
import com.example.smarterbackend.framework.common.data.Gender;
import com.example.smarterbackend.framework.common.data.Role;
import com.example.smarterbackend.framework.dto.user.ChangeProfilePayload;
import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.AddUserPayload;
import com.example.smarterbackend.framework.dto.DynamicResponse;
import com.example.smarterbackend.framework.dto.user.VerifyInfoPayload;
import com.example.smarterbackend.mapper.UserMapper;
import com.example.smarterbackend.model.Authority;
import com.example.smarterbackend.model.Mail;
import com.example.smarterbackend.model.User;
import com.example.smarterbackend.repository.AuthorityRepository;
import com.example.smarterbackend.repository.UserRepository;
import com.example.smarterbackend.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final AuthorityRepository authorityRepository;
  private final OtpService otpService;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;

  public DynamicResponse verifyInfoAndGenerateOTP(VerifyInfoPayload payload) {
    String name = payload.getName();
    String email = payload.getEmail();
    log.info("Started verifying info of user {}, email {}", name, email);

    validateEmail(email);
    int otp = otpService.generateOTP(email);

    String mailSubject = "Smarter | Verification Code to Sign Up";
    String mailContent =
        "<!DOCTYPE html><html><head><style>p, h2 {font-family: sans-serif;}</style></head>\n"
            + "<body><p>Hi <span style=\"font-weight: bold;\">"
            + name
            + "</span>,</p>\n"
            + "<p>This is your verification code to create your new account:</p>\n"
            + "<h2 style=\"font-weight: bold; color: blue;\">"
            + otp
            + "</h2>\n"
            + "<p>This code will be expired in 2 minutes. Don't share this code with anyone.</p>\n"
            + "<p>Thanks for joining Smarter! We hope you will enjoy great moments with us!</p>\n"
            + "<p style=\"font-weight: bold;\">The Smarter Team</p></body></html>";
    sendMail(email, mailSubject, mailContent);
    log.info("Generated OTP: {}", otp);

    DynamicResponse response = new DynamicResponse();
    Map<String, Boolean> properties = new HashMap<>();
    properties.put("isInfoValid", true);
    response.setProperties(properties);

    return response;
  }

  private void validateEmail(String email) {
    Optional<User> userOptional = userRepository.findUsersByEmail(email);
    if (userOptional.isPresent()) {
      log.error("The email {} is already taken", email);
      throw new ResourceConflictException("Email này đã được sử dụng");
    }
  }

  private void sendMail(String receiverMail, String subject, String content) {
    Mail mail = new Mail();

    mail.setMailFrom("uteclubs@gmail.com");
    mail.setMailTo(receiverMail);
    mail.setMailSubject(subject);
    mail.setMailContent(content);

    try {
      log.info("Sending email to {}", receiverMail);
      mailService.sendEmail(mail);
      log.info("Sent email to {} successfully", receiverMail);
    } catch (Exception ex) {
      log.error("An exception has occurred while sending email to user", ex);
    }
  }

  public UserResponse addUser(AddUserPayload payload) {
    log.info("Started verifying info of user {} again", payload.getEmail());
    int inputOtp = Integer.parseInt(payload.getOtp());
    int serverOtp = otpService.getOtp(payload.getEmail());
    if (serverOtp > 0) {
      if (inputOtp != serverOtp) {
        log.error("The OTP {} is wrong", inputOtp);
        throw new OtpException("Mã OTP sai. Mời bạn thử lại!");
      }

      String name = payload.getName();
      String email = payload.getEmail();
      String password = payload.getPassword();

      validateEmail(email);
      User user =
          User.builder().name(name).email(email).password(passwordEncoder.encode(password)).build();
      Authority authority =
          authorityRepository
              .findFirstByName(Role.USER)
              .orElseThrow(() -> new NotFoundException("Authority not found"));
      user.setAuthority(authority);
      otpService.clearOTP(payload.getEmail());
      log.info("All info is valid. Adding user {} to database", email);
      return UserMapper.INSTANCE.userToUserDTO(userRepository.save(user));
    } else {
      log.error("The OTP is expired");
      throw new OtpException("Mã OTP đã hết hạn. Mời bạn lấy một mã khác!");
    }
  }

  public UserResponse getCurrentUserDTO() {
    return UserMapper.INSTANCE.userToUserDTO(getCurrentUser());
  }

  public User getCurrentUser() {
    String email = UserUtils.getCurrentUserEmail();
    return userRepository
        .findUsersByEmail(email)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public UserResponse changeProfile(ChangeProfilePayload payload) {
    User currentUser = getCurrentUser();

    validateAndSavePassword(currentUser, payload.getNewPassword(), payload.getConfirmedPassword());

    currentUser.setName(payload.getName());

    if (StringUtils.isNotEmpty(payload.getImageUrl())) {
      currentUser.setImageUrl(payload.getImageUrl());
    }

    if (StringUtils.isNotEmpty(payload.getGender())) {
      currentUser.setGender(Gender.fromGenderString(payload.getGender()));
    }

    if (StringUtils.isNotEmpty(payload.getBirthdate())) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate birthDate = LocalDate.parse(payload.getBirthdate(), formatter);
      currentUser.setDateOfBirth(birthDate);
    }

    return UserMapper.INSTANCE.userToUserDTO(userRepository.save(currentUser));
  }

  private void validateAndSavePassword(User user, String newPassword, String confirmedPassword) {
    if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(confirmedPassword)) {
      if (!newPassword.equals(confirmedPassword)) {
        throw new InvalidRequestException("Mật khẩu không khớp");
      }
      user.setPassword(passwordEncoder.encode(newPassword));
    } else if (StringUtils.isNotEmpty(newPassword) || StringUtils.isNotEmpty(confirmedPassword)) {
      throw new InvalidRequestException("Bạn phải nhập đủ mật khẩu mới và xác nhận mật khẩu mới");
    }
  }
}

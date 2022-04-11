package com.example.smarterbackend.service;

import com.example.smarterbackend.exception.NotFoundException;
import com.example.smarterbackend.exception.OtpException;
import com.example.smarterbackend.exception.ResourceConflictException;
import com.example.smarterbackend.framework.common.data.Role;
import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.framework.dto.user.AddUserPayload;
import com.example.smarterbackend.framework.dto.user.VerificationResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public VerificationResponse verifyInfoAndGenerateOTP(VerifyInfoPayload payload) {
    String name = payload.getName();
    String email = payload.getEmail();
    log.info("Started verifying info of user {}, email {}", name, email);

    validateEmail(email);
    int otp = otpService.generateOTP(email);

    log.info("Sending email to {}", email);
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
    log.info("Sent email to {} successfully", email);
    log.info("Generated OTP: {}", otp);

    VerificationResponse response = new VerificationResponse();
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

    mailService.sendEmail(mail);
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

  private User getCurrentUser() {
    String email = UserUtils.getCurrentUserEmail();
    return userRepository
        .findUsersByEmail(email)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }
}

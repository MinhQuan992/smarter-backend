package com.example.smarterbackend.service;

import com.example.smarterbackend.model.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender mailSender;

  public void sendEmail(Mail mail) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      mimeMessageHelper.setSubject(mail.getMailSubject());
      mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "UTE Clubs"));
      mimeMessageHelper.setTo(mail.getMailTo());
      mimeMessageHelper.setText(mail.getMailContent(), true);

      mailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MessagingException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}

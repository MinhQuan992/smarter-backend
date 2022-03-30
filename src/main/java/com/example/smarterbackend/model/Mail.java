package com.example.smarterbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
  private String mailFrom;
  private String mailTo;
  private String mailSubject;
  private String mailContent;
}

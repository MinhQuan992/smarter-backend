package com.example.smarterbackend.framework.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetFavoritePayload {
  private boolean isFavorite;
}

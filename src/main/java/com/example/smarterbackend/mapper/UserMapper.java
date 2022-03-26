package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.user.UserResponse;
import com.example.smarterbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserResponse userToUserDTO(User user);
  List<UserResponse> listUserToListUserDTO(List<User> users);
}

package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.RegisterUserDtoRequest;
import com.codewithmosh.store.dto.UpdateUserRequest;
import com.codewithmosh.store.dto.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserDtoRequest registerUserDtoRequest);
    void update(UpdateUserRequest request,@MappingTarget User user);
}

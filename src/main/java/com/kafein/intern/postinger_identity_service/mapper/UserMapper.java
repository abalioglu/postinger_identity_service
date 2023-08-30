package com.kafein.intern.postinger_identity_service.mapper;

import com.kafein.intern.postinger_identity_service.dto.UserDTO;
import com.kafein.intern.postinger_identity_service.model.User;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Named("toDTO")
    UserDTO toDto(User user);

    @Named("toEntity")
    User toEntity(UserDTO roleDTO);

    @IterableMapping(qualifiedByName = "toDTO")
    List<UserDTO> toDTOList(List<User> entityList);

    @IterableMapping(qualifiedByName = "toEntity")
    List<User> toEntityList(List<UserDTO> dtoList);
}

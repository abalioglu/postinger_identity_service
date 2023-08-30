package com.kafein.intern.postinger_identity_service.mapper;

import com.kafein.intern.postinger_identity_service.dto.UserInfoDTO;
import com.kafein.intern.postinger_identity_service.model.User_Info;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    @Named("toDTO")
    UserInfoDTO toDto(User_Info userInfo);

    @Named("toEntity")
    User_Info toEntity(UserInfoDTO roleDTO);

    @IterableMapping(qualifiedByName = "toDTO")
    List<UserInfoDTO> toDTOList(List<User_Info> entityList);

    @IterableMapping(qualifiedByName = "toEntity")
    List<User_Info> toEntityList(List<UserInfoDTO> dtoList);
}

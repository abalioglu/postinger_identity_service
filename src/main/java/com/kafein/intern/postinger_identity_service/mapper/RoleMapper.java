package com.kafein.intern.postinger_identity_service.mapper;

import com.kafein.intern.postinger_identity_service.dto.RoleDTO;
import com.kafein.intern.postinger_identity_service.model.Role;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Named("toDTO")
    RoleDTO toDto(Role role);

    @Named("toEntity")
    Role toEntity(RoleDTO roleDTO);

    @IterableMapping(qualifiedByName = "toDTO")
    List<RoleDTO> toDTOList(List<Role> entityList);

    @IterableMapping(qualifiedByName = "toEntity")
    List<Role> toEntityList(List<RoleDTO> dtoList);
}

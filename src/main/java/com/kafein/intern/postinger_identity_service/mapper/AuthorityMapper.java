package com.kafein.intern.postinger_identity_service.mapper;
import com.kafein.intern.postinger_identity_service.dto.AuthorityDTO;
import com.kafein.intern.postinger_identity_service.model.Authority;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;


import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    @Named("toDTO")
    AuthorityDTO toDto(Authority authority);
    @Named("toEntity")
    Authority toEntity(AuthorityDTO authorityDTO);
    @IterableMapping(qualifiedByName = "toDTO")
    List<AuthorityDTO> toDTOList(List<Authority> entityList);

    @IterableMapping(qualifiedByName = "toEntity")
    List<Authority> toEntityList(List<AuthorityDTO> dtoList);


}
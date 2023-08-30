package com.kafein.intern.postinger_identity_service.service;

import com.kafein.intern.postinger_identity_service.dto.AuthorityDTO;
import com.kafein.intern.postinger_identity_service.exceptions.*;
import com.kafein.intern.postinger_identity_service.mapper.AuthorityMapper;
import com.kafein.intern.postinger_identity_service.model.Authority;
import com.kafein.intern.postinger_identity_service.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public Authority findById(Long id){
        return authorityRepository.findById(id).orElseThrow(()-> new AuthorityDoesntExistException("Authority Not Found With ID = " + id));
    }
    public Authority findByName(String name){
        Authority authority = authorityRepository.findByName(name);
        if(authority == null)
            throw new AuthorityDoesntExistException("Authority Not Found With Name = " + name);
        return authority;
    }
    public Authority saveOrUpdate(Authority authority){
        Authority existingAuthority = null;
        Authority existingNameAuthority;
        Authority existingDescriptionAuthority;
        if (authority.getId() != null)
            existingAuthority = authorityRepository.findById(authority.getId()).orElse(null);

        String name = authority.getName();
        if(name == null)
            throw new MissingInputException("No Name Given In The Body");
        else
            existingNameAuthority = authorityRepository.findByName(name);
        String description = authority.getDescription();
        if(description == null)
            throw new  MissingInputException("No Description Given In The Body");
        else
            existingDescriptionAuthority = authorityRepository.findByDescription(description);

        if(authority.getId() == null) {
            if ((existingNameAuthority == null) && (existingDescriptionAuthority == null)) {
                return authorityRepository.save(authority);
            }
            else if (existingNameAuthority == null)
                throw new AuthorityWithSameDescriptionExistsException("This Description Already Belongs To Another Authority") ;
            else if (existingDescriptionAuthority == null)
                throw new AuthorityWithSameNameExistsException("This Name Is Already Taken");
            else {
                throw new AuthorityWithSameNameAndDescriptionExistsException("This Name And Description Are Already Taken");
            }
        }else if (existingAuthority != null && existingAuthority.equals(authority)){
            throw new AuthorityAlreadyExistsException("This Authority Already Exists");
        }else if(existingAuthority != null){
            existingAuthority.setName(authority.getName());
            existingAuthority.setDescription(authority.getDescription());
            return authorityRepository.save(existingAuthority);
        }else{
            throw new AuthorityDoesntExistException("Can't Update Authority As Authority Not Found With ID = " + authority.getId());
        }
    }
    public void delete(Long id) {
        try {
            authorityRepository.deleteById(id);
        } catch (AuthorityDoesntExistException ex) {
            throw new AuthorityDoesntExistException("Authority Not Found With ID = " + id);
        }
    }
    //------------------------------------------------------------------------------------
    //dto versions
    public AuthorityDTO getAuthorityDTOById(Long id) {
        Authority authority = findById(id);
        return authorityMapper.toDto(authority);
    }
    public AuthorityDTO createOrUpdateAuthority(AuthorityDTO authorityDTO) {
        Authority authority = authorityMapper.toEntity(authorityDTO);
        Authority authority1 = saveOrUpdate(authority);
        return authorityMapper.toDto(authority1);
    }
    public AuthorityDTO getAuthorityDTOByName(String name) {
        Authority authority = findByName(name);
        return authorityMapper.toDto(authority);
    }
    public List<AuthorityDTO> getAuthorityDTOList(List<Authority> authorityList) {
        return authorityMapper.toDTOList(authorityList);
    }
    public List<Authority> getAuthorityList(List<AuthorityDTO> authorityDTOList) {
        return authorityMapper.toEntityList(authorityDTOList);
    }



}

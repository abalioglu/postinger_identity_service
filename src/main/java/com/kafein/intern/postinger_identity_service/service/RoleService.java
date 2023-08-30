package com.kafein.intern.postinger_identity_service.service;

import com.kafein.intern.postinger_identity_service.dto.AuthorityDTO;
import com.kafein.intern.postinger_identity_service.dto.RoleDTO;
import com.kafein.intern.postinger_identity_service.exceptions.*;
import com.kafein.intern.postinger_identity_service.mapper.AuthorityMapper;
import com.kafein.intern.postinger_identity_service.mapper.RoleMapper;
import com.kafein.intern.postinger_identity_service.model.Authority;
import com.kafein.intern.postinger_identity_service.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kafein.intern.postinger_identity_service.model.Role;
import com.kafein.intern.postinger_identity_service.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleMapper roleMapper;
    private final AuthorityMapper authorityMapper;

    public Role findById(Long id){
        return roleRepository.findById(id).orElseThrow(()-> new RoleDoesntExistException("Role Not Found With ID = " + id));
    }
    public Role findByName(String name){
        Role role = roleRepository.findByName(name);
        if(role == null)
            throw new RoleDoesntExistException("Role Not Found With Name = " + name);
        return role;
    }
    public Role saveOrUpdate(Role role){
        Role existingRole = null;
        Role existingNameRole;
        Role existingDescriptionRole;
        if (role.getId() != null)
            existingRole = roleRepository.findById(role.getId()).orElse(null);

        String name = role.getName();
        if(name == null)
            throw new MissingInputException("No Name Given In The Body");
        else
            existingNameRole = roleRepository.findByName(name);
        String description = role.getDescription();
        if(description == null)
            throw new  MissingInputException("No Description Given In The Body");
        else
            existingDescriptionRole = roleRepository.findByDescription(description);

        List<Authority> authorities = role.getAuthorities();
        if(authorities.isEmpty())
            throw new  MissingInputException("No Authorities Given In The Body");

        List<Authority> allowedAuthorities = new ArrayList<>(authorityRepository.findAll());
        boolean isAllowed = allowedAuthorities.containsAll(authorities);
        if(!isAllowed)
            throw new AuthorityDoesntExistException("Authority Doesn't Exist");

        if(role.getId() == null) {
            if ((existingNameRole == null) && (existingDescriptionRole == null)) {
                return roleRepository.save(role);
            }
            else if (existingNameRole == null)
                throw new RoleWithSameDescriptionExistsException("This Description Already Belongs To Another Role") ;
            else if (existingDescriptionRole == null)
                throw new RoleWithSameNameExistsException("This Name Is Already Taken");
            else {
                throw new RoleWithSameNameAndDescriptionExistsException("This Name And Description Are Already Taken");
            }
        }else if (existingRole != null && existingRole.equals(role)){
            throw new RoleAlreadyExistsException("This Role Already Exists");
        }else if(existingRole != null){
            existingRole.setName(role.getName());
            existingRole.setDescription(role.getDescription());
            existingRole.setAuthorities(role.getAuthorities());
            return roleRepository.save(existingRole);
        }else{
            throw new RoleDoesntExistException("Can't Update Role As Role Not Found With ID = " + role.getId());
        }
    }
    public void delete(Long id) {
        try {
            roleRepository.deleteById(id);
        } catch (RoleDoesntExistException ex) {
            throw new RoleDoesntExistException("Role Not Found With ID = " + id);
        }
    }
    public List<Authority> getAuthorities(Long id){
        Role role = findById(id);
        return role.getAuthorities();
    }
    //--------------------------------------------------------------------------
    //dto versions
    public RoleDTO getRoleDTOById(Long id) {
        Role role = findById(id);
        return roleMapper.toDto(role);
    }
    public RoleDTO createOrUpdateRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role role1 = saveOrUpdate(role);
        return roleMapper.toDto(role1);
    }
    public RoleDTO getRoleDTOByName(String name) {
        Role role = findByName(name);
        return roleMapper.toDto(role);
    }
    public List<RoleDTO> getRoleDTOList(List<Role> roleList) {
        return roleMapper.toDTOList(roleList);
    }
    public List<Role> getRoleList(List<RoleDTO> roleDTOList) {
        return roleMapper.toEntityList(roleDTOList);
    }
    public List<AuthorityDTO> getAuthoityDTOs(Long id){
        List<Authority> authorities = getAuthorities(id);
        return authorityMapper.toDTOList(authorities);
    }

}

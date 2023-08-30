package com.kafein.intern.postinger_identity_service.controller;

import com.kafein.intern.postinger_identity_service.dto.AuthorityDTO;
import com.kafein.intern.postinger_identity_service.dto.RoleDTO;
import com.kafein.intern.postinger_identity_service.model.Role;
import com.kafein.intern.postinger_identity_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;
    @PostMapping("/save-update")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.createOrUpdateRole(roleDTO));
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<String> deleteRole(@RequestParam("id") Long id) {
        roleService.delete(id);
        return ResponseEntity.ok("Role deleted successfully");
    }
    @GetMapping("/find-name")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<RoleDTO> findByName(@RequestParam("name") String name){
        RoleDTO roleDTO = roleService.getRoleDTOByName(name);
        return new ResponseEntity<>(roleDTO, HttpStatus.OK);
    }
    @GetMapping("/find-id")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<RoleDTO> findByName(@RequestParam("id") Long id){
        RoleDTO roleDTO = roleService.getRoleDTOById(id);
        return new ResponseEntity<>(roleDTO, HttpStatus.OK);
    }
    @GetMapping("/authorities")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<List<AuthorityDTO>> getAuthoityDTOs(@RequestParam("id") Long id){
        List<AuthorityDTO> authorityDTOs = roleService.getAuthoityDTOs(id);
        return new ResponseEntity<>(authorityDTOs, HttpStatus.OK);
    }
}

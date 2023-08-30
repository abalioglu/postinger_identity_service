package com.kafein.intern.postinger_identity_service.controller;

import com.kafein.intern.postinger_identity_service.dto.AuthorityDTO;
import com.kafein.intern.postinger_identity_service.model.Authority;
import com.kafein.intern.postinger_identity_service.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authority")
public class AuthorityController {
    private final AuthorityService authorityService;

    @PostMapping("/save-update")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<AuthorityDTO> saveAuthority(@RequestBody AuthorityDTO authorityDTO) {
        return ResponseEntity.ok(authorityService.createOrUpdateAuthority(authorityDTO));
    }
    @GetMapping("/find-id")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<AuthorityDTO> findById(@RequestParam("id") Long id){
        AuthorityDTO authorityDTO = authorityService.getAuthorityDTOById(id);
        return new ResponseEntity<>(authorityDTO, HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<String> deleteAuthority(@RequestParam("id") Long id) {
        authorityService.delete(id);
        return ResponseEntity.ok("Authority deleted successfully");
    }
    @GetMapping("/find-name")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<AuthorityDTO> findByName(@RequestParam("name") String name){
        AuthorityDTO authorityDTO = authorityService.getAuthorityDTOByName(name);
        return new ResponseEntity<>(authorityDTO, HttpStatus.OK);
    }


}

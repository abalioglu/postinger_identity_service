package com.kafein.intern.postinger_identity_service.controller;

import com.kafein.intern.postinger_identity_service.dto.UserDTO;
import com.kafein.intern.postinger_identity_service.dto.UserInfoDTO;
import com.kafein.intern.postinger_identity_service.jwt.JwtTokenProvider;
import com.kafein.intern.postinger_identity_service.model.Post;
import com.kafein.intern.postinger_identity_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenProvider.generateToken(userDetails);

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        // Set the cookie's path to '/' to make it accessible across the entire application
        cookie.setPath("/");
        cookie.setHttpOnly(true); // Ensures the cookie is not accessible through JavaScript (improves security)
        // Add the cookie to the response
        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO){
        if(userDTO.getId() == null)
            return ResponseEntity.ok(userService.createOrUpdateUser(userDTO));
        else throw new RuntimeException("ID must be null");
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO){
        if(userDTO.getId() != null)
            return ResponseEntity.ok(userService.createOrUpdateUser(userDTO));
        else throw new RuntimeException("ID must not be null");
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<String> deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    @DeleteMapping("/delete-account")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<String> deleteAccount(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(id);
        jwtTokenProvider.invalidateToken(request, response);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @GetMapping("/find-id")
    public ResponseEntity<UserDTO> findById(@RequestParam(name = "id") Long id){
        UserDTO userDTO = userService.getUserDTOById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<List<UserDTO>> findAll(){
        return ResponseEntity.ok(userService.findAllUserDTO());
    }

    @GetMapping("/find-username")
    public ResponseEntity<UserDTO> findByUsername(@RequestParam(name = "username") String username){
        UserDTO userDTO = userService.getUserDTOByUsername(username);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @GetMapping("/find-roleId")
    @PreAuthorize("hasAnyAuthority('ADMIN_AUTHORITY')")
    public ResponseEntity<List<UserDTO>> findAllByRoleId(@RequestParam(name = "roleId") Long roleId){
        List<UserDTO> userDTOs = userService.getAllUserDTOByRoleId(roleId);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/find-fullname")
    public ResponseEntity<List<UserDTO>> findByFullname(@RequestParam(name = "fullname")String fullname){
        List<UserDTO> userDTOs = userService.getUserDTOByFullname(fullname);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfoDTOById(@RequestParam(name = "id") Long id){
        UserInfoDTO userInfoDTO = userService.getUserInfoDTOById(id);
        return new ResponseEntity<>(userInfoDTO, HttpStatus.OK);
    }
    @GetMapping("/posts") //sadece kendi postlarını görebiliyor
    public ResponseEntity<List<Post>> getAllPosts(HttpServletRequest request){
        String jwt = userService.getToken(request);
        String username = jwtTokenProvider.extractUsername(jwt);
        Long userId = userService.getUserByUsername(username).getId();
        return new ResponseEntity<>(userService.findAllPostById(userId), HttpStatus.OK);
    }

    @GetMapping("/get-username")
    public ResponseEntity<String> getUsername(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok(userService.getUsernameById(id));
    }
    @GetMapping("/get-id")
    public ResponseEntity<Long> getId(HttpServletRequest request){
        String jwt = userService.getToken(request);
        String username = jwtTokenProvider.extractUsername(jwt);
        return  ResponseEntity.ok(userService.getUserByUsername(username).getId());
    }

    @GetMapping("/following-list")
    public ResponseEntity<List<UserDTO>> getFollowedUsers(HttpServletRequest request){
        String jwt = userService.getToken(request);
        Long userId = jwtTokenProvider.extractIdClaim(jwt);
        return new ResponseEntity<>(userService.getFollowedUsersDTO(userId), HttpStatus.OK);
    }
    @GetMapping("/follower-list")
    public ResponseEntity<List<UserDTO>> getFollowers(HttpServletRequest request){
        String jwt = userService.getToken(request);
        Long userId = jwtTokenProvider.extractIdClaim(jwt);
        return new ResponseEntity<>(userService.getFollowersDTO(userId), HttpStatus.OK);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed(HttpServletRequest request){
        String jwt = userService.getToken(request);
        Long userId = jwtTokenProvider.extractIdClaim(jwt);
        List<Long> followedIds = userService.getFollowedUserIds(userId);
        return new ResponseEntity<>(userService.findAllPostsByUserIds(followedIds),HttpStatus.OK);
    }
    @PostMapping("/increment-follower-count")
    public ResponseEntity<?> incrementFollowerCount(@RequestParam(name = "followedId") Long followedId){
        userService.incrementFollowerCount(followedId);
        return new ResponseEntity<>("Follower Count +1",HttpStatus.OK);
    }
    @PostMapping("/decrement-follower-count")
    public ResponseEntity<?> decrementFollowerCount(@RequestParam(name = "followedId") Long followedId){
        userService.decrementFollowerCount(followedId);
        return new ResponseEntity<>("Follower Count -1",HttpStatus.OK);
    }
    @PostMapping("/increment-followed-count")
    public ResponseEntity<?> incrementFollowedCount(@RequestParam(name = "followerId") Long followerId){
        userService.incrementFollowedCount(followerId);
        return new ResponseEntity<>("Followed Count +1",HttpStatus.OK);
    }
    @PostMapping("/decrement-followed-count")
    public ResponseEntity<?> decrementFollowedCount(@RequestParam(name = "followerId") Long followerId){
        userService.decrementFollowedCount(followerId);
        return new ResponseEntity<>("Followed Count -1",HttpStatus.OK);
    }
}

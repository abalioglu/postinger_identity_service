package com.kafein.intern.postinger_identity_service.service;
import com.kafein.intern.postinger_identity_service.dto.UserDTO;
import com.kafein.intern.postinger_identity_service.dto.UserInfoDTO;
import com.kafein.intern.postinger_identity_service.exceptions.*;
import com.kafein.intern.postinger_identity_service.mapper.UserInfoMapper;
import com.kafein.intern.postinger_identity_service.mapper.UserMapper;
import com.kafein.intern.postinger_identity_service.model.*;
import com.kafein.intern.postinger_identity_service.repository.RoleRepository;
import com.kafein.intern.postinger_identity_service.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.kafein.intern.postinger_identity_service.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;
    private final PostServiceClient postServiceClient;
    private final FollowServiceClient followServiceClient;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getToken(HttpServletRequest request){
        String jwt = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT_TOKEN")) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }return jwt;
    }
    public Long getFollowerCount(Long followedId){
        return followServiceClient.getFollowerCount(followedId);
    }
    public Long getFollowedCount(Long followerId){
        return followServiceClient.getFollowedCount(followerId);
    }

    public void incrementFollowedCount(Long followerId){
        User_Info info = findInfoById(followerId);
        long count = info.getFollowingCount();
        if(count+1 == getFollowedCount(followerId))
                info.setFollowingCount(count+1);
        else throw new RuntimeException("Followed Count Hasn't Changed");
    }
    public void decrementFollowedCount(Long followerId){
        User_Info info = findInfoById(followerId);
        long count = info.getFollowingCount();
        if(count-1 == getFollowedCount(followerId))
            info.setFollowingCount(count-1);
        else throw new RuntimeException("Followed Count Hasn't Changed");
    }
    public void incrementFollowerCount(Long followedId){
        User_Info info = findInfoById(followedId);
        long count = info.getFollowerCount();
        if(count+1 == getFollowerCount(followedId))
            info.setFollowerCount(count+1);
        else throw new RuntimeException("Follower Count Hasn't Changed");
    }
    public void decrementFollowerCount(Long followedId){
        User_Info info = findInfoById(followedId);
        long count = info.getFollowerCount();
        if(count-1 == getFollowerCount(followedId))
            info.setFollowerCount(count-1);
        else throw new RuntimeException("Follower Count Hasn't Changed");
    }
    public List<Long> getFollowedUserIds(Long followerId) {
        return followServiceClient.getFollowedIds(followerId);

    }
    public List<Long> getFollowerIds(Long followedId) {
        return followServiceClient.getFollowerIds(followedId);
    }
    public List<User> getFollowedUsers(Long followerId) {
        List<Long> followedUserIds = followServiceClient.getFollowedIds(followerId);
        List<User> followedUsers = new ArrayList<>();
        for (Long followedUserId : followedUserIds) {
            User user = findById(followedUserId);
            followedUsers.add(user);
        }return followedUsers;
    }
    public List<User> getFollowers(Long followedId) {
        List<Long> followerIds = followServiceClient.getFollowerIds(followedId);
        List<User> followers = new ArrayList<>();
        for (Long followerId : followerIds) {
            User user = findById(followerId);
            followers.add(user);
        }return followers;
    }


    public List<Post> findAllPostById(Long userId) {;
        return postServiceClient.getPosts(userId);
    }
    public List<Post> findAllPostsByUserIds(List<Long> userIds) {
        List<Post> allPosts = new ArrayList<>();
        for (Long userId : userIds) {
            List<Post> userPosts = postServiceClient.getPosts(userId);
            allPosts.addAll(userPosts);
        }
        return allPosts;
    }

    public String getUsernameById(Long id){
        User user = findById(id);
        return user.getUsername();
    }
    public User saveOrUpdate(User user){
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);

        User existingUser = null;
        User existingUsernameUser;
        User existingEmailUser;
        if (user.getId() != null)
            existingUser = userRepository.findById(user.getId()).orElse(null);

        String username = user.getUsername();
        if(username == null)
            throw new MissingInputException("No Username Given In The Body");
        else
            existingUsernameUser = userRepository.findByUsername(username);

        String password = user.getPassword();
        if(password == null)
            throw new  MissingInputException("No Password Given In The Body");
        String email = user.getEmail();
        if(email == null)
            throw new  MissingInputException("No Email Given In The Body");
        else
            existingEmailUser = userRepository.findByEmail(email);

        Role role = user.getRole();
        if(role == null)
            throw new  MissingInputException("No Role Given In The Body");
        Role existingRole = roleRepository.findById(role.getId()).orElseThrow(()-> new RoleDoesntExistException("This Role Doesn't Exist"));

        if(user.getId() == null) {
            if ((existingUsernameUser == null) && (existingEmailUser == null)) {
                user.getInfo().setFollowerCount(0);
                user.getInfo().setFollowingCount(0);
                return userRepository.save(user);
            } else if (existingEmailUser == null)
                throw new UserWithSameUsernameAlreadyExistsException("This Username Is Already Taken");
            else if (existingUsernameUser == null)
                throw new UserWithSameEmailAlreadyExistsException("This Email Is Already Used To Open An Account");
            else {
                throw new UserWithSameusernameAndEmailAlreadyExistsException("This Username And Email Are Already Taken");
            }
        }else if (existingUser != null && existingUser.equals(user)){
            throw new UserAlreadyExistsException("This User Already Exists");
        }else if(existingUser != null){
            existingUser.setUsername(user.getUsername());
            existingUser.setRole(existingRole);
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setInfo(user.getInfo());
            return userRepository.save(existingUser);
        }else{
            throw new UserDoesntExistException("Can't Update User As User Not Found With ID = " + user.getId());
        }
    }

    public List<User> findAll(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty())
            throw new NoUserRegisteredException("There aren't any users registered");
        return users;
    }
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UserDoesntExistException("User Not Found With ID = " + id));
    }
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new UserDoesntExistException("User Not Found With Username = " + username);
        return user;
    }
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (UserDoesntExistException ex) {
            throw new UserDoesntExistException("User Not Found With ID = " + id);
        }
    }
    public List<User> getAllByRoleId(Long roleId) {
        List<User> users = userRepository.findAllByRole_Id(roleId);
        if(users.isEmpty())
            throw new UserDoesntExistException("No Users Found With This Role");
        return users;
    }

    public List<User> findByFullname(String fullname){
        List<User> users = userRepository.findAllByInfo_Fullname(fullname);
        if(users.isEmpty())
            throw new UserDoesntExistException("No Users Found With This Fullname = " + fullname);
        return users;
    }
    public User_Info findInfoById(Long id){
        return userInfoRepository.findById(id).orElseThrow(()-> new UserDoesntExistException("User Info Not Found With ID = " + id));
    }
    //-----------------------------------------------------------------------------
    //dto versions
    public UserDTO getUserDTOById(Long id) {
        User user = findById(id);
        return userMapper.toDto(user);
    }
    public UserInfoDTO getUserInfoDTOById(Long id) {
        User_Info userInfo = findInfoById(id);
        return userInfoMapper.toDto(userInfo);
    }
    public UserDTO createOrUpdateUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User user1 = saveOrUpdate(user);
        return userMapper.toDto(user1);
    }
    public UserDTO getUserDTOByUsername(String username) {
        User user = getUserByUsername(username);
        return userMapper.toDto(user);
    }
    public List<UserDTO> getUserDTOList(List<User> userList) {
        return userMapper.toDTOList(userList);
    }
    public List<User> getUserList(List<UserDTO> userDTOList) {
        return userMapper.toEntityList(userDTOList);
    }
    public List<UserDTO> findAllUserDTO(){
        List<User> allUsers = findAll();
        return getUserDTOList(allUsers);
    }
    public List<UserDTO> getAllUserDTOByRoleId(Long roleId){
        List<User> users = getAllByRoleId(roleId);
        return getUserDTOList(users);
    }
    public List<UserDTO> getUserDTOByFullname(String fullname){
        List<User> users = findByFullname(fullname);
        return getUserDTOList(users);
    }
    public List<UserDTO> getFollowedUsersDTO(Long followerId) {
        return getUserDTOList(getFollowedUsers(followerId));
    }
    public List<UserDTO> getFollowersDTO(Long followedId) {
        return getUserDTOList(getFollowers(followedId));
    }
    //---------------------------------------------------------------------
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

}


package org.solopulse.service;

import org.solopulse.exception.IdNotFoundException;
import org.solopulse.exception.ResourceNotFoundException;
import org.solopulse.dao.UserDao;
import org.solopulse.dto.UserCreateDto;
import org.solopulse.dto.UserResponseDTO;

import org.solopulse.entity.User;
import org.solopulse.enums.Roles;
import org.solopulse.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserDao userDao;

    public UserService(UserMapper userMapper, UserDao userDao) {
        this.userMapper = userMapper;
        this.userDao = userDao;
    }

    public ResponseEntity<UserResponseDTO> createUser(UserCreateDto userCreateDto){

        Roles role = userCreateDto.getRole();

        if (role == null) {
            String validRoles = Arrays.toString(Roles.values());
            throw new IllegalArgumentException("Role is required , valid roles are :" + validRoles);
        }

        boolean isValidRole = Arrays.stream(Roles.values())
                .anyMatch(validRole -> validRole.equals(role));

        if(!isValidRole){
            String validRoles = Arrays.stream(Roles.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(","));
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + validRoles);
        }

        User user = userMapper.toEntity(userCreateDto);
        User savedUser = userDao.createUser(user);
        UserResponseDTO userResponseDTO = userMapper.toResponseDto(savedUser);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }


    public ResponseEntity<UserResponseDTO> getUserById(Integer id){
        User user = userDao.findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        UserResponseDTO userResponseDTO = userMapper.toResponseDto(user);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }


    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<User> users = userDao.findAll();
        List<UserResponseDTO> userResponseDTOS = users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponseDTOS, HttpStatus.OK);
    }

    public boolean deleteUserById(Integer id){
        if(userDao.findUserById(id).isEmpty()){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userDao.deleteById(id);
        return true;
    }

    public List<UserResponseDTO> getUsersByRole(Roles role){
        if(role == null){
            String validRoles = Arrays.toString(Roles.values());
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + validRoles);
        }
        List<User> users = userDao.findByUserRole(role);
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getUsersByRoleOrdered(Roles role){
        if(role == null){
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        List<User> users = userDao.findByUserRoleOrderByCreatedAtDesc(role);
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public long countUsersByRole(Roles role){
        if(role == null){
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        return userDao.countByUserRole(role);
    }

    public List<UserResponseDTO> searchUsersByName(String name){
        List<User> users = userDao.searchByName(name);
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> updateUserImage(Integer userId, String imgUrl){
        Optional<User> user = userDao.findUserById(userId);
        if(user.isEmpty()){
            throw new IdNotFoundException("user not found with id " + userId);
        }
        if(imgUrl==null || imgUrl.trim().isEmpty()){
            ResponseEntity.badRequest().body("image url can not be empty");
        }
//
//        User existingUser =  user.get();
//        existingUser.setImageUrl(imgUrl);
//        userDao.createUser(existingUser);

        int res = userDao.updateUserImage(userId, imgUrl);
        if(res==1){
          return  ResponseEntity.ok("user image updated with user id " + userId);
        }
        else return new ResponseEntity<>(String.valueOf("can not update image for the user " + userId), HttpStatus.NOT_MODIFIED);
    }

    public ResponseEntity<String> updateUserRole(Integer id, Roles role){
        Optional<User> user = userDao.findUserById(id);
        if(user.isEmpty()){
            throw new IdNotFoundException("user not found with given id " + id);
        }

        if(role == null){
            String validRoles = Arrays.stream(Roles.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Role cannot be null. Valid roles are: " + validRoles);
        }
        userDao.updateUserRole(id, role);
        return  ResponseEntity.ok("user role updated");
    }

    public ResponseEntity<String> updateUserBio(Integer id, String updatedBio){
        User user = userDao.findUserById(id)
                            .orElseThrow(()-> new IdNotFoundException("user not found with given id:" + id));

        userDao.updateUserBio(id,updatedBio);
        return  ResponseEntity.ok("user bio updated");
    }

    public ResponseEntity<String> updateUserPassword(Integer id, String newPassword){
        User user = userDao.findUserById(id)
                .orElseThrow(()-> new IdNotFoundException("user not found with given id:" + id));

        userDao.updateUserPassword(id,newPassword);
        return ResponseEntity.ok("user password updated successfully with user id:" + id);
    }


}

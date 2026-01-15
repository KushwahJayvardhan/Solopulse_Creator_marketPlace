package org.solopulse.service;

import org.solopulse.Exception.ResourceNotFoundException;
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

}

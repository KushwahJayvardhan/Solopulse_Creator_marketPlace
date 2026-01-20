package org.solopulse.controller;

import jakarta.validation.Valid;
import org.solopulse.dto.RoleUpdateDTO;
import org.solopulse.dto.UserCreateDto;
import org.solopulse.dto.UserResponseDTO;
import org.solopulse.enums.Roles;
import org.solopulse.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserCreateDto userCreateDto){
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public boolean deleteUserById(@PathVariable Integer id){
        return userService.deleteUserById(id);
    }

    @GetMapping("/getByRole/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role){
        Roles validRole;
        try{
            validRole = Roles.valueOf(role.toUpperCase());
        } catch(IllegalArgumentException e){
            String validRoles = String.join(", ",
                java.util.Arrays.stream(Roles.values())
                .map(Enum::name)
                .toArray(String[]::new));
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + validRoles);
        }
        return new ResponseEntity<>(userService.getUsersByRole(validRole), HttpStatus.OK);
    }

    @GetMapping("/usersByRoleOrdered/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRoleOrdered(@PathVariable String role){
        Roles validRole;
        try{
            validRole = Roles.valueOf(role.toUpperCase());
        } catch(IllegalArgumentException e){
            String validRoles = String.join(", ",
                java.util.Arrays.stream(Roles.values())
                .map(Enum::name)
                .toArray(String[]::new));
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + validRoles);
        }
        return new ResponseEntity<>(userService.getUsersByRoleOrdered(validRole), HttpStatus.OK);
    }

    @GetMapping("/countByRole/{role}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable String role) {
        Roles validRole;
        try {
            validRole = Roles.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            String validRoles = String.join(", ",
                    java.util.Arrays.stream(Roles.values())
                            .map(Enum::name)
                            .toArray(String[]::new));
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + validRoles);
        }
        long count = userService.countUsersByRole(validRole);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByName(@RequestParam String name) {
        return new ResponseEntity<>(userService.searchUsersByName(name), HttpStatus.OK);
    }

    @PutMapping("/updateImage/{id}")
    public ResponseEntity<String> updateImageUrl(@PathVariable Integer id, @RequestBody Map<String,String> request){
        String imgUrl = request.get("imageUrl");
        return new ResponseEntity<>(String.valueOf(userService.updateUserImage(id,imgUrl)),HttpStatus.OK);
   }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<String> updateUserRole(@PathVariable Integer id, @RequestBody RoleUpdateDTO request){
        Roles role = request.getRole();
        return userService.updateUserRole(id,role);
    }

    @PutMapping("/updateBio/{id}")
    public ResponseEntity<String> updateUserBio(@PathVariable Integer id, @RequestBody Map<String , String> userBio){
        String changedBio =userBio.get("bio");
        return userService.updateUserBio(id, changedBio);
    }

    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<String> updateUserPassword(@PathVariable Integer id, @RequestBody Map<String,String> pass){
        String updatedPass = pass.get("password");
        return userService.updateUserPassword(id,updatedPass);
    }
}

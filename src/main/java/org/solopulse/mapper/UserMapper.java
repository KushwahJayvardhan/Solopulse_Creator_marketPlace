package org.solopulse.mapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.solopulse.dto.UserCreateDto;
import org.solopulse.dto.UserResponseDTO;
import org.solopulse.dto.UserUpdateDTO;
import org.solopulse.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void setupMapper(){
        modelMapper.createTypeMap(UserCreateDto.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setPassword);
                    mapper.skip(User::setCreatorProfile);
                    mapper.skip(User::setBrandProfile);
                    mapper.skip(User::setMarketerProfile);
                    mapper.skip(User::setProfile);
                    mapper.skip(User::setProposal);
                });

        modelMapper.createTypeMap(User.class, UserResponseDTO.class)
                .addMappings(mapper -> {

                    //mapper.skip(UserResponseDTO::setPassword);
                    mapper.map(src -> src.getCreatorProfile() != null ? src.getCreatorProfile().getId() : null,
                            UserResponseDTO::setCreatorProfileId);
                    mapper.map(src -> src.getBrandProfile() != null ? src.getBrandProfile().getId() : null,
                            UserResponseDTO::setBrandProfileId);
                    mapper.map(src -> src.getMarketerProfile() != null ? src.getMarketerProfile().getId() : null,
                            UserResponseDTO::setMarketerProfileId);
                    mapper.map(src -> src.getProfile() != null ? src.getProfile().getId() : null,
                            UserResponseDTO::setProfileId);
                    mapper.map(src -> src.getProposal() != null ? src.getProposal().getId() : null,
                            UserResponseDTO::setProposalId);
                });

        modelMapper.createTypeMap(UserUpdateDTO.class, User.class)
                .addMappings(mapper -> {
                    // Skip password - handle manually
                    mapper.skip(User::setPassword);
                    // Skip relationships
                    mapper.skip(User::setCreatorProfile);
                    mapper.skip(User::setBrandProfile);
                    mapper.skip(User::setMarketerProfile);
                    mapper.skip(User::setProfile);
                    mapper.skip(User::setProposal);
                    // Skip auto-generated fields
                    mapper.skip(User::setId);
                    mapper.skip(User::setCreatedAt);
                    mapper.skip(User::setUpdatedAt);
                });
    }

     public User toEntity(UserCreateDto userCreateDto){
        if(userCreateDto == null){
            return null;
        }
        User user = modelMapper.map(userCreateDto, User.class);

        if(userCreateDto.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        }

         if(userCreateDto.getRole() != null){
             user.setUserRole(userCreateDto.getRole());
         }

        return user;
     }

     public UserResponseDTO toResponseDto(User user){
        if(user == null){
            return null;
        }
        return modelMapper.map(user, UserResponseDTO.class);
     }

     public void updateEntityFromDto(UserUpdateDTO userUpdateDto, User user){
        if(userUpdateDto == null || user == null){
            return;
        }

        var creatorProfile = user.getCreatorProfile();
        var brandProfile = user.getBrandProfile();
        var marketerProfile = user.getMarketerProfile();
        var profile = user.getProfile();
        var proposal = user.getProposal();
        var existingPassword = user.getPassword();
        var existingId = user.getId();
        var existingCreatedAt = user.getCreatedAt();

        modelMapper.map(userUpdateDto, user);

        user.setId(existingId);
        user.setCreatedAt(existingCreatedAt);

        if(userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        } else{
            user.setPassword(existingPassword);
        }

        user.setCreatorProfile(creatorProfile);
        user.setBrandProfile(brandProfile);
        user.setMarketerProfile(marketerProfile);
        user.setProfile(profile);
        user.setProposal(proposal);
     }
}


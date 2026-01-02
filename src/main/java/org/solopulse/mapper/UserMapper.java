package org.solopulse.mapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.solopulse.dao.UserDTO;
import org.solopulse.entity.User;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupMapper() {
        // Custom mapping for User to UserDTO (extracting IDs from related entities)
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getCreatorProfile() != null ? src.getCreatorProfile().getId() : null,
                            UserDTO::setCreatorProfileId);
                    mapper.map(src -> src.getBrandProfile() != null ? src.getBrandProfile().getId() : null,
                            UserDTO::setBrandProfileId);
                    mapper.map(src -> src.getMarketerProfile() != null ? src.getMarketerProfile().getId() : null,
                            UserDTO::setMarketerProfileId);
                    mapper.map(src -> src.getProfile() != null ? src.getProfile().getId() : null,
                            UserDTO::setProfileId);
                    mapper.map(src -> src.getProposal() != null ? src.getProposal().getId() : null,
                            UserDTO::setProposalId);
                });

        // Custom mapping for UserDTO to User (skipping related entity IDs)
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setCreatorProfile);
                    mapper.skip(User::setBrandProfile);
                    mapper.skip(User::setMarketerProfile);
                    mapper.skip(User::setProfile);
                    mapper.skip(User::setProposal);
                });
    }

    /**
     * Convert User entity to UserDTO
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Convert UserDTO to User entity
     * Note: Related entities are skipped and need to be set separately if needed
     */
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return modelMapper.map(userDTO, User.class);
    }

    /**
     * Update existing User entity with UserDTO data
     * Does not update related entities
     */
    public void updateEntityFromDTO(UserDTO userDTO, User user) {
        if (userDTO == null || user == null) {
            return;
        }

        // Store existing relationships
        var creatorProfile = user.getCreatorProfile();
        var brandProfile = user.getBrandProfile();
        var marketerProfile = user.getMarketerProfile();
        var profile = user.getProfile();
        var proposal = user.getProposal();

        // Map the DTO to entity
        modelMapper.map(userDTO, user);

        // Restore relationships (prevent them from being set to null)
        user.setCreatorProfile(creatorProfile);
        user.setBrandProfile(brandProfile);
        user.setMarketerProfile(marketerProfile);
        user.setProfile(profile);
        user.setProposal(proposal);
    }
}

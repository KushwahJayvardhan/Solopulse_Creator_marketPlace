package org.solopulse.mapper;

import org.mapstruct.*;
import org.solopulse.dto.request.CreateCreatorProfileRequest;
import org.solopulse.dto.request.UpdateCreatorProfileRequest;
import org.solopulse.dto.response.CreatorProfileResponse;
import org.solopulse.entity.Creator_profile;
import org.solopulse.entity.User;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreatorProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "portfolioItems", ignore = true)
    @Mapping(target = "collaborationProjects", ignore = true)
    @Mapping(target = "associatedMarketers", ignore = true)
    Creator_profile toEntity(CreateCreatorProfileRequest request, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "portfolioItems", ignore = true)
    @Mapping(target = "collaborationProjects", ignore = true)
    @Mapping(target = "associatedMarketers", ignore = true)
    void updateEntity(UpdateCreatorProfileRequest request, @MappingTarget Creator_profile entity);

    @Mapping(target = "portfolioItemsCount", expression = "java(getPortfolioItemsCount(entity))")
    @Mapping(target = "collaborationProjectsCount", expression = "java(getCollaborationProjectsCount(entity))")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CreatorProfileResponse toResponse(Creator_profile entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    CreatorProfileResponse.UserBasicInfo toUserBasicInfo(User user);

    default Integer getPortfolioItemsCount(Creator_profile entity) {
        return entity.getPortfolioItems() != null ? entity.getPortfolioItems().size() : 0;
    }

    default Integer getCollaborationProjectsCount(Creator_profile entity) {
        return entity.getCollaborationProjects() != null ? entity.getCollaborationProjects().size() : 0;
    }
}
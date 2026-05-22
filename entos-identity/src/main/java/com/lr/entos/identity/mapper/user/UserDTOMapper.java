package com.lr.entos.identity.mapper.user;

import com.lr.entos.identity.entity.User;
import com.lr.entos.infra.config.CentralMapperConfig;
import com.lr.entos.infra.mapper.GenericMapper;
import com.lr.entos.shared.dto.request.auth.SignupRequest;
import com.lr.entos.shared.dto.request.user.UserDTOs;
import com.lr.entos.shared.dto.response.user.UserResponse;
import org.mapstruct.*;


@Mapper(config = CentralMapperConfig.class,
//        uses = { RoleMapper.class },
        builder = @Builder(disableBuilder = true)
)
public interface UserDTOMapper extends GenericMapper<User, UserResponse, SignupRequest, UserDTOs.Update>{

//    @Override
//    @Mapping(source = "id", target = "userId") // Maps entity 'id' from BaseEntity into 'userId' of the Record
//    UserResponse toResponse(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDto(UserDTOs.Update updateDto, @MappingTarget User entity);
}

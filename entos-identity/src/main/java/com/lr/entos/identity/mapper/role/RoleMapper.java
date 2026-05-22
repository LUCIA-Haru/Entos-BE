package com.lr.entos.identity.mapper.role;

import com.lr.entos.identity.entity.Role;
import com.lr.entos.infra.config.CentralMapperConfig;
import com.lr.entos.infra.mapper.GenericMapper;
import com.lr.entos.shared.dto.request.role.RoleDTOs;
import com.lr.entos.shared.dto.response.role.RoleResponse;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface RoleMapper extends GenericMapper<Role,RoleResponse, RoleDTOs.Create, RoleDTOs.Update> {


    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(RoleDTOs.Update updateDto, @MappingTarget Role entity);

}

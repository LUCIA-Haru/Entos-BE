package com.lr.entos.infra.mapper;


import org.mapstruct.MappingTarget;

public interface UpdateMapper<ENTITY, UPDATE_DTO> {
    void updateEntityFromDto(UPDATE_DTO updateDto, @MappingTarget ENTITY entity);


}

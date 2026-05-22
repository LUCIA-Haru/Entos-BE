package com.lr.entos.infra.mapper;


import org.mapstruct.MappingTarget;

import java.util.List;

public interface GenericMapper<ENTITY, RESPONSE_DTO, CREATE_DTO,UPDATE_DTO> {
    RESPONSE_DTO toResponse(ENTITY entity);
    ENTITY toEntity(CREATE_DTO createDto);
    List<RESPONSE_DTO> toDtoList(List<ENTITY> entities);
    void updateEntityFromDto(UPDATE_DTO updateDto, @MappingTarget ENTITY entity);
}



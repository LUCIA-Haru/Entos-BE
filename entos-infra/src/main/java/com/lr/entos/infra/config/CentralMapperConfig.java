package com.lr.entos.infra.config;

import org.mapstruct.Condition;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring", // Spring handles dependency injection for ALL mappers
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Keeps your build logs clean
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // Global "Partial Update" rule
)
public interface CentralMapperConfig {
    // Any default method with @Condition becomes available to every mapper
    // that uses this config. It’s like a shared utility tool.
    @Condition
    default boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}

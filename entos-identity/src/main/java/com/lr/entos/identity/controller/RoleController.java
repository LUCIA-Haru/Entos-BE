package com.lr.entos.identity.controller;

import com.lr.entos.shared.dto.request.role.RoleDTOs;
import com.lr.entos.shared.dto.response.role.RoleResponse;
import com.lr.entos.identity.service.role.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/role")
@RequiredArgsConstructor
@Tag(name = "Role",description = "API Documentation for managing Role")
public class RoleController {
    private final IRoleService roleService;

    @Operation(summary = "Create Role for user")
    @PostMapping
    public RoleResponse createRole(@Valid @RequestBody RoleDTOs.Create dto){
        return roleService.createRole(dto);
    }

    @Operation(summary = "Updating Role Data")
    @PutMapping("/{guid}")
    public RoleResponse updateRole(
            @PathVariable @NotBlank UUID guid,
            @Valid @RequestBody RoleDTOs.Update dto
            ){
        return roleService.updateRole(guid,dto);
    }

    @Operation(summary = "Updating the only role status( True or False)")
    @PutMapping("/{guid}/status")
    @PreAuthorize("hasAuthority('ADMIN")
    public String updateRoleStatus(@PathVariable @NotBlank UUID guid,
                            @Valid @NotEmpty @RequestParam boolean delete){
        return roleService.updateRoleStatus(guid,delete);
    }

    @GetMapping
    @Operation(summary = "Fetching Role Lists")
    public Page<RoleResponse> getRoleLists(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return roleService.getRoleLists(pageable);
    }

}

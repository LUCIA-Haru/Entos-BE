package com.lr.entos.identity.service.role;

import com.lr.entos.shared.dto.request.role.RoleDTOs;
import com.lr.entos.shared.dto.response.role.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    RoleResponse createRole(RoleDTOs.Create dto);
    RoleResponse updateRole(UUID guid, RoleDTOs.Update dto);
    String updateRoleStatus(UUID guid,Boolean delete);
    Page<RoleResponse> getRoleLists(Pageable pageable);

}

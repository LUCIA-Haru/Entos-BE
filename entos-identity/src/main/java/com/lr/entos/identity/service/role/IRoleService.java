package com.lr.entos.identity.service.role;

import com.lr.entos.identity.dto.records.role.RoleRequest;
import com.lr.entos.identity.entity.Role;

public interface IRoleService {
    Role createRole(RoleRequest dto);
    Role updateRole(RoleRequest dto);
}

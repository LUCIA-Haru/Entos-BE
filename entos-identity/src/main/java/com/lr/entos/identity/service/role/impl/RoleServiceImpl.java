package com.lr.entos.identity.service.role.impl;

import com.lr.entos.identity.mapper.role.RoleMapper;
import com.lr.entos.infra.utils.EntosLogUtils;
import com.lr.entos.shared.dto.request.role.RoleDTOs;
import com.lr.entos.shared.dto.response.role.RoleResponse;
import com.lr.entos.identity.entity.Role;
import com.lr.entos.identity.repository.RoleRepository;
import com.lr.entos.identity.service.role.IRoleService;
import com.lr.entos.shared.exception.ExistedRoleException;
import com.lr.entos.shared.exception.RoleNotFoundException;
import com.lr.entos.shared.utils.constants.Commons;
import com.lr.entos.shared.utils.message.ErrorsMessageUtils;
import com.lr.entos.shared.utils.message.SuccessMessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleDTOs.Create dto) {
        if (roleRepository.findByName(dto.name()).isPresent()) {
            throw new ExistedRoleException(
                    ErrorsMessageUtils.ALREADY_EXIST.formatted("Name", "Role"));
        }
        Role role = roleMapper.toEntity(dto);
        role.setStatus(true);
       Role savedRole = roleRepository.save(role);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.ROLE,"Created" + savedRole.getName()));
        return roleMapper.toResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(UUID guid, RoleDTOs.Update dto) {
        Role existingRole = roleRepository.findByGuid(guid)
                .orElseThrow(() ->
                        new RoleNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("Role")));
        roleMapper.updateEntityFromDto(dto, existingRole);
         roleRepository.save(existingRole);

        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.ROLE,"UPDATED"));
      return roleMapper.toResponse(existingRole);
    }

    @Override
    @Transactional
    public String updateRoleStatus(UUID guid, Boolean delete) {
        Role existingRole = roleRepository.findByGuid(guid)
                .orElseThrow(() ->
                        new RoleNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("Role")));
        existingRole.setStatus(delete);

        return EntosLogUtils.logStatusUpdate(Commons.ROLE,guid,delete);
    }

    @Override
    public Page<RoleResponse> getRoleLists(Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);
        return roles.map(roleMapper::toResponse);
    }


}

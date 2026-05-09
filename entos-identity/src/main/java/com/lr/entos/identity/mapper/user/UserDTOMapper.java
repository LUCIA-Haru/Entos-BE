package com.lr.entos.identity.mapper.user;

import com.lr.entos.identity.dto.response.role.RoleDTO;
import com.lr.entos.identity.dto.response.user.user.UserDTO;
import com.lr.entos.identity.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDTOMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        Set<RoleDTO> roleDTOs = user.getRoles()
                .stream()
                .map(role -> new RoleDTO(
                        role.getName(),
                        role.isStatus(),
                        role.getType(),
                        role.getDescription()
                ))
                .collect(Collectors.toSet());
        userDTO.setRoles(roleDTOs);
        userDTO.setActive(user.isActive());
        return userDTO;
    }
}

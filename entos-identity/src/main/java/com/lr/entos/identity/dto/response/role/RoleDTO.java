package com.lr.entos.identity.dto.response.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {
    private String name;
    private boolean active;
    private String type;
    private String description;
}

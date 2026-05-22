package com.lr.entos.identity.entity;

import com.lr.entos.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Builder.Default
    private Boolean status = true;
    @Column(nullable = false)
    private String type;
    private String description;

}

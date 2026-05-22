package com.lr.entos.identity.repository;

import com.lr.entos.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);

    Optional<Role> findByGuid(UUID guid);

    @Transactional // Required for data modification queries
    @Modifying    // Tells Spring Data this query alters data and isn't a SELECT statement
    @Query("UPDATE Role r SET r.status = :status WHERE r.id = :id") // Assumes your entity fields are named 'status' and 'id'
    int updateRoleStatus(@Param("id") Long id, @Param("status") boolean status);

}

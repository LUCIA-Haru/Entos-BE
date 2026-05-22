package com.lr.entos.identity.repository;

import com.lr.entos.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByGuid(UUID guid);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String name);

//    use the @EntityGraph annotation. This tells Spring Data JPA to join the roles table immediately
    @EntityGraph(attributePaths = {"role"})
    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);

}

package com.lr.entos.identity.service.user;

import com.lr.entos.identity.dto.records.SignupRequest;
import com.lr.entos.identity.dto.request.user.UserRequestDTO;
import com.lr.entos.identity.dto.response.user.user.UserDTO;
import com.lr.entos.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {
    User findUserByGuid(UUID guid);

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    boolean userExistsByEmail(String email);

    Page<UserDTO> findAllUsers(Pageable pageable);

    UserDTO createUser(SignupRequest dto);

}

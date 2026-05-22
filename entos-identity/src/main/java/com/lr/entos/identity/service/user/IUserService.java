package com.lr.entos.identity.service.user;

import com.lr.entos.shared.dto.request.auth.SignupRequest;
import com.lr.entos.shared.dto.request.user.UserDTOs;
import com.lr.entos.shared.dto.response.role.RoleResponse;
import com.lr.entos.identity.entity.User;
import com.lr.entos.shared.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User findUserByGuid(UUID guid);

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    boolean userExistsByEmail(String email);

    Page<UserResponse> findAllUsers(Pageable pageable);

    UserResponse createUser(SignupRequest dto);

//    List<RoleResponse> fetchRoleListsByUser(UUID guid);


    UserResponse updateUser(UUID guid, UserDTOs.Update dto);

    String updateUserStatus(UUID guid, Boolean status);

    String updatePassword(UUID guid, UserDTOs.UpdatePassword newPass);

    String resetPassword(UUID guid,UserDTOs.ResetPassword resetPassword);

    UserResponse fetchUserDetails(UUID guid);

    UserResponse assignRoleToUser(UUID guid,String roleName);
}

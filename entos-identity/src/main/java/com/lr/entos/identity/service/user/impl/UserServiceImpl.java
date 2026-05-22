package com.lr.entos.identity.service.user.impl;

import com.lr.entos.identity.entity.Role;
import com.lr.entos.identity.repository.RoleRepository;
import com.lr.entos.infra.utils.EntosLogUtils;
import com.lr.entos.shared.dto.request.auth.SignupRequest;
import com.lr.entos.shared.dto.request.user.UserDTOs;
import com.lr.entos.identity.entity.User;
import com.lr.entos.identity.mapper.user.UserDTOMapper;
import com.lr.entos.identity.repository.UserRepository;
import com.lr.entos.identity.service.user.IUserService;
import com.lr.entos.shared.dto.response.user.UserResponse;
import com.lr.entos.shared.exception.RoleNotFoundException;
import com.lr.entos.shared.exception.UserAlreadyExistsException;
import com.lr.entos.shared.exception.UserNotFoundException;
import com.lr.entos.shared.exception.WrongPasswordException;
import com.lr.entos.shared.utils.TimeLogUtils;
import com.lr.entos.shared.utils.constants.Commons;
import com.lr.entos.shared.utils.message.ErrorsMessageUtils;
import com.lr.entos.shared.utils.message.SuccessMessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    private static final String className = "UserServiceImpl";

    @Override
    public User findUserByGuid(UUID guid) {
        return userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User"))
        );
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("email"))
        );
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("Username"))
        );
    }

    @Override
    public boolean userExistsByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User"));
        }
        return true;

    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponse> findAllUsers(Pageable pageable) {
        TimeLogUtils.start();
        log.info("{} => findAllUsers()", className);
        Page<User> users = userRepository.findAll(pageable);
        TimeLogUtils.stop();
        Page<UserResponse> userDTOs = users.map(userDTOMapper::toResponse);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.USER + "s","Fetched"));
        return userDTOs;
    }

    @Override
    public UserResponse createUser(SignupRequest req) {
        String userRole = "User";

        if (userRepository.existsByEmail(req.email()) || userRepository.existsByUsername(req.username()))
            throw new UserAlreadyExistsException(ErrorsMessageUtils.ALREADY_EXIST.formatted("User" , req.email() +"||" + req.username()));

        Role role = roleRepository.findByName(userRole).orElseThrow(() ->
                new RoleNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("Role")));

        User user = userDTOMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(role);
        user.setActive(true);
        User savedUser = userRepository.save(user);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.USER,"Created"));
        return userDTOMapper.toResponse(savedUser);
    }

//    @Override
//    public List<RoleResponse> fetchRoleListsByUser(UUID guid) {
//        User user = userRepository.findByGuid(guid).orElseThrow(
//                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));
//
//        return user.getRoles().stream()
//                .map(roleMapper::toResponse)
//                .toList();
//    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID guid, UserDTOs.Update dto) {
        User user = userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));

        userDTOMapper.updateEntityFromDto(dto,user);

//        if (dto.roleName() != null) {
//            Role newRole = roleRepository.findByName(dto.roleName())
//                    .orElseThrow(() -> new RoleNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted(Commons.ROLE)));
//            user.setRole(newRole);
//        }
        userRepository.save(user);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.USER,"Updated"));
        return userDTOMapper.toResponse(user);
    }

    @Override
    public String updateUserStatus(UUID guid, Boolean status) {
        User user = userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));

        user.setActive(status);
       return EntosLogUtils.logStatusUpdate(Commons.USER,guid,status);

    }

    @Override
    @Transactional
    public String updatePassword(UUID guid, UserDTOs.UpdatePassword newPass) {
        User user = userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));
        if(!passwordEncoder.matches(newPass.oldPassword(), newPass.newPassword()))
            throw new WrongPasswordException(ErrorsMessageUtils.OLD_NEW_PASSWORD);
        user.setPassword(passwordEncoder.encode(newPass.newPassword()));
        return EntosLogUtils.logUpdate(Commons.USER,guid,"Password");
    }

//    TODO -> Need to update logic with otp email integration and reset password logic
    @Override
    public String resetPassword(UUID guid, UserDTOs.ResetPassword resetPassword) {
        User user = userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));
        user.setPassword(passwordEncoder.encode(resetPassword.newPassword()));
        return EntosLogUtils.logUpdate(Commons.USER,guid,"Password");
    }

    @Override
    public UserResponse fetchUserDetails(UUID guid) {
        User user = userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.USER,"Fetched"));
        return userDTOMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse assignRoleToUser(UUID guid, String roleName) {
        User user = userRepository.findByGuid(guid).orElseThrow(
                () -> new UserNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted("User")));

        Role role = roleRepository.findByName(roleName).orElseThrow(
                ()-> new RoleNotFoundException(ErrorsMessageUtils.NOT_FOUND.formatted(Commons.ROLE))
        );

        user.setRole(role);
        user = userRepository.save(user);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION_OBJ.formatted("✅"+ Commons.USER +"'s","Roles","Updated"));
        return userDTOMapper.toResponse(user);
    }


}

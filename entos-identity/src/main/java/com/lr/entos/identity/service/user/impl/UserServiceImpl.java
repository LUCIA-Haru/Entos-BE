package com.lr.entos.identity.service.user.impl;

import com.lr.entos.identity.dto.records.SignupRequest;
import com.lr.entos.identity.dto.request.user.UserRequestDTO;
import com.lr.entos.identity.dto.response.user.user.UserDTO;
import com.lr.entos.identity.entity.User;
import com.lr.entos.identity.mapper.user.UserDTOMapper;
import com.lr.entos.identity.repository.UserRepository;
import com.lr.entos.identity.service.user.IUserService;
import com.lr.entos.shared.exception.UserAlreadyExistsException;
import com.lr.entos.shared.exception.UserNotFoundException;
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
    private final PasswordEncoder passwordEncoder;

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
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        TimeLogUtils.start();
        log.info("{} => findAllUsers()", className);
        Page<User> users = userRepository.findAll(pageable);
        TimeLogUtils.stop();
        Page<UserDTO>  userDTO = users.map(UserDTOMapper::toDTO);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.USER + "s","Fetched"));
        return userDTO;
    }

    @Override
    public UserDTO createUser(SignupRequest req) {
        if (userRepository.existsByEmail(req.email()) || userRepository.existsByUsername(req.username()))
            throw new UserAlreadyExistsException(ErrorsMessageUtils.ALREADY_EXIST.formatted("User" , req.email() +"||" + req.username()));

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        User savedUser = userRepository.save(user);
        log.info(SuccessMessageUtils.SUCCESS_OPERATION.formatted("✅"+ Commons.USER,"Created"));
        return UserDTOMapper.toDTO(savedUser);
    }


}

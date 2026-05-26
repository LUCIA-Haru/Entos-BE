package com.lr.entos.identity.service.auth.impl;

import com.lr.entos.shared.dto.response.auth.JwtResponse;
import com.lr.entos.shared.dto.request.auth.LoginRequest;
import com.lr.entos.shared.dto.request.auth.SignupRequest;
import com.lr.entos.shared.dto.response.user.UserResponse;
import com.lr.entos.identity.securityConfig.CustomUserDetails;
import com.lr.entos.infra.utils.JwtUtils;
import com.lr.entos.identity.service.auth.IAuthService;
import com.lr.entos.identity.service.user.IUserService;
import com.lr.entos.shared.utils.constants.Commons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authManager;
    private final IUserService userService;
    private final JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest req){
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

        String role = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(Commons.USER);

        String avatarURL = (details.avatarUrl() != null && !details.avatarUrl().isBlank())
                ? details.avatarUrl()
                : "/assets/images/default-avatar.png";

        // Prepare your claims
        Map<String, Object> claims = Map.of(
                "role", role,
                "avatar", avatarURL,
                "guid",details.guid().toString()
        );

        String jwt = jwtUtils.generateToken(details.email(), claims);
        return new JwtResponse(jwt, details.guid(), details.getUsername(), details.email(), role);
    }

    public UserResponse signup(SignupRequest req){

        return userService.createUser(req);
    }
}

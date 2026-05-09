package com.lr.entos.identity.service.auth.impl;

import com.lr.entos.identity.dto.records.JwtResponse;
import com.lr.entos.identity.dto.records.LoginRequest;
import com.lr.entos.identity.dto.records.SignupRequest;
import com.lr.entos.identity.dto.response.user.user.UserDTO;
import com.lr.entos.identity.securityConfig.CustomUserDetails;
import com.lr.entos.identity.securityConfig.JwtUtils;
import com.lr.entos.identity.service.auth.IAuthService;
import com.lr.entos.identity.service.user.IUserService;
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

        List<String> roles = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Prepare your claims
        Map<String, Object> claims = Map.of(
                "role", details.getAuthorities().iterator().next().getAuthority(),
                "avatar", "/assets/images/default-avatar.png" // or from DB
        );

        String jwt = jwtUtils.generateToken(details.email(), claims);
        return new JwtResponse(jwt, details.id(), details.getUsername(), details.email(), roles);
    }

    public UserDTO signup(SignupRequest req){

        return userService.createUser(req);
    }
}

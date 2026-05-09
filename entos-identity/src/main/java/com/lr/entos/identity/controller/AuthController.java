package com.lr.entos.identity.controller;

import com.lr.entos.identity.dto.records.JwtResponse;
import com.lr.entos.identity.dto.records.LoginRequest;
import com.lr.entos.identity.dto.records.SignupRequest;
import com.lr.entos.identity.dto.response.user.user.UserDTO;
import com.lr.entos.identity.service.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService IAuthService;


    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest req) {
        return IAuthService.login(req);
    }

    @PostMapping("/signup")
    public UserDTO signup(@Valid @RequestBody SignupRequest req) {
       return IAuthService.signup(req);
    }
}

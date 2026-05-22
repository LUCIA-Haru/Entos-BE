package com.lr.entos.identity.service.auth;


import com.lr.entos.shared.dto.response.auth.JwtResponse;
import com.lr.entos.shared.dto.request.auth.LoginRequest;
import com.lr.entos.shared.dto.request.auth.SignupRequest;
import com.lr.entos.shared.dto.response.user.UserResponse;

public interface IAuthService {
    JwtResponse login(LoginRequest request);

    UserResponse signup(SignupRequest request);

}

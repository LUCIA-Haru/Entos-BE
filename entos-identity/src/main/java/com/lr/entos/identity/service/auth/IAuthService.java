package com.lr.entos.identity.service.auth;


import com.lr.entos.identity.dto.records.JwtResponse;
import com.lr.entos.identity.dto.records.LoginRequest;
import com.lr.entos.identity.dto.records.SignupRequest;
import com.lr.entos.identity.dto.response.user.user.UserDTO;

public interface IAuthService {
    JwtResponse login(LoginRequest request);

    UserDTO signup(SignupRequest request);

}

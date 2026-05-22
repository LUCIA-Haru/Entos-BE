package com.lr.entos.identity.controller;

import com.lr.entos.identity.service.user.IUserService;
import com.lr.entos.shared.dto.request.user.UserDTOs;
import com.lr.entos.shared.dto.response.user.UserResponse;
import com.lr.entos.shared.utils.constants.Commons;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Tag(name = Commons.USER,description = "API Documentation for managing User")
public class UserController {
    private final IUserService userService;

    @Operation(summary = "Updating User Data")
    @PutMapping("/{guid}")
    public UserResponse updateUser(
            @PathVariable @NotBlank UUID guid,
            @Valid @RequestBody UserDTOs.Update dto
            ){
        return userService.updateUser(guid,dto);
    }

    @Operation(summary = "Updating Only User Status ")
    @PutMapping("/{guid}/status")
    public String updateUserStatus(
            @PathVariable @NotBlank UUID guid,
            @Valid @NotEmpty @RequestParam boolean status
    ){
        return userService.updateUserStatus(guid,status);
    }


    @Operation(summary = "Updating user password ")
    @PutMapping("/{guid}/password")
    public String updatePassword(
            @PathVariable @NotBlank UUID guid,
            @Valid @RequestBody UserDTOs.UpdatePassword dto
    ){
        return userService.updatePassword(guid,dto);
    }

    @GetMapping
    @Operation(summary = "Fetching User Lists")
    public Page<UserResponse> getUserLists(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return userService.findAllUsers(pageable);
    }

    @GetMapping("/{guid}")
    @Operation(summary = "Fetch user details")
    public UserResponse fetchUseDetails(@PathVariable @NotBlank UUID guid){
        return userService.fetchUserDetails(guid);
    }


}

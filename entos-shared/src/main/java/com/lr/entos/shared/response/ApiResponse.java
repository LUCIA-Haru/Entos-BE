package com.lr.entos.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final String status;
    private final int statusCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final T data;
}

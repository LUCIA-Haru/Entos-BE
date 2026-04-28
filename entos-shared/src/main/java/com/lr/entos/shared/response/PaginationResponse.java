package com.lr.entos.shared.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PaginationResponse<T> extends ApiResponse<List<T>> {
    private final MetadataResponse metadataResponse;

    public PaginationResponse(String status, int statusCode, String message, LocalDateTime timestamp,
                              List<T> data, MetadataResponse metadata) {
        super(status, statusCode, message, timestamp, data);
        this.metadataResponse = metadata;
    }
}

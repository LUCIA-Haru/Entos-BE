package com.lr.entos.shared.response;

public record MetadataResponse(int page, int size, long totalItems,
                               int totalPages) {
}

package com.lr.entos.api.config;

import com.lr.entos.shared.annotation.ResponseMessage;
import com.lr.entos.shared.response.ApiResponse;
import com.lr.entos.shared.response.MetadataResponse;
import com.lr.entos.shared.response.PaginationResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        String path = request.getURI().getPath();
        // Exclude Swagger/Docs from being wrapped
        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui")) {
            return body;
        }

        if (body instanceof ApiResponse) {
            return body;
        }

        String message = getMessage(returnType, request);

        // Handle Spring Data Pagination
        if (body instanceof org.springframework.data.domain.Page<?> page) {
            MetadataResponse metadata = new MetadataResponse(
                    page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
            );
            return new PaginationResponse<>(
                    "success", HttpStatus.OK.value(), message, LocalDateTime.now(), page.getContent(), metadata
            );
        }

        return new ApiResponse<>("success", HttpStatus.OK.value(), message, LocalDateTime.now(), body);
    }

    private String getMessage(MethodParameter returnType, ServerHttpRequest request) {
        ResponseMessage ann = returnType.getMethodAnnotation(ResponseMessage.class);
        if (ann != null && !ann.value().isEmpty()) return ann.value();

        return generateDynamicMessage(request);
    }

    private String generateDynamicMessage(ServerHttpRequest request){
        String operation = switch (request.getMethod().toString()){
            case "GET" -> "Fetched";
            case "POST" -> "Created";
            case "PUT" -> "Updated";
            case "DELETE" -> "Deleted";
            default -> "Processed";
        };

        //Determine entity from first path segment
        String path = request.getURI().getPath();
        String entity = "data";
        String[] segments = path.split("/");

        if (segments.length > 1 && StringUtils.hasText(segments[1])){
            entity = segments[1];
        }

        entity = toReadablePlural(entity);

        return String.format("%s %s successfully", operation, entity);
    }

    private String toReadablePlural(String entity) {
        // Insert space before capital letters
        String readable = entity.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        // Simple pluralization (you can improve with a library)
        if (!readable.endsWith("s")) {
            readable += "s";
        }
        return readable;
    }
}

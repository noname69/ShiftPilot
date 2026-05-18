package lt.techin.shiftpilot.response;

import jakarta.servlet.http.HttpServletRequest;
import lt.techin.shiftpilot.exception.ApiError;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice(annotations = Controller.class)
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {

        if (body instanceof ApiResponse<?> ||
                body instanceof ApiError) {
            return body;
        }

        HttpServletRequest servletRequest =
                ((ServletRequestAttributes)
                        Objects.requireNonNull(
                                RequestContextHolder.getRequestAttributes()
                        ))
                        .getRequest();

        int status = ((ServletServerHttpResponse) response)
                .getServletResponse()
                .getStatus();

        return new ApiResponse<>(
                status,
                true,
                "OK",
                body,
                servletRequest.getRequestURI(),
                LocalDateTime.now()
        );
    }
}

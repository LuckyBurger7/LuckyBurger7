package org.example.luckyburger.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiErrorResponse;
import org.example.luckyburger.common.security.code.AccessErrorCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // API 에러 응답 객체 생성 (당신의 ApiErrorResponse와 유사하게 구성)
        ApiErrorResponse errorResponse = ApiErrorResponse.from(
                AccessErrorCode.NO_ACCESS,
                request
        );

        // JSON 형태로 응답 작성
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

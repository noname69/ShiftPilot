package lt.techin.shiftpilot.security.exception;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws java.io.IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String path = request.getRequestURI();

        String message;

        if (path.contains("/login")) {
            message = "Invalid username or password";
        }
        else {
            message = "Authentication required or token expired";
        }

        response.getWriter().write("""
        {
            "status": 401,
            "error": "UNAUTHORIZED",
            "message": "%s"
        }
        """.formatted(message));
    }
}

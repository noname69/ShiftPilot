package lt.techin.shiftpilot.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.security.service.CustomUserDetailsService;
import lt.techin.shiftpilot.security.jwt.JwtService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {

        String authHeader = request.getHeader("Authorization");

        // 2. No token → continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (token.isBlank() || token.split("\\.").length != 3) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String username = jwtService.extractUsername(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (ExpiredJwtException ex) {
            writeError(response, "Token expired");
            return;
        }

        catch (SignatureException | MalformedJwtException ex) {
            writeError(response, "Invalid token");
            return;
        }

        catch (Exception ex) {
            writeError(response, "Authentication failed");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, String message) throws java.io.IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write("""
    {
        "status": 401,
        "error": "UNAUTHORIZED",
        "message": "%s"
    }
    """.formatted(message));
    }
}

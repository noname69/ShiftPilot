package lt.techin.shiftpilot.security.filter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.security.service.CustomUserDetailsService;
import lt.techin.shiftpilot.security.jwt.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
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
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {

//        String path = request.getServletPath();
//
//        // 1. Skip auth endpoints FIRST
//        if (path.startsWith("/api/auth")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

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

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new BadCredentialsException("Invalid JWT token");
            // ❌ DO NOT throw exception
            //SecurityContextHolder.clearContext();

            // optional: mark request as unauthorized
            //request.setAttribute("jwt_error", true);
        }
//        catch (io.jsonwebtoken.JwtException ex) {
//
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid JWT");
//            return;
//        }

        filterChain.doFilter(request, response);
    }
}

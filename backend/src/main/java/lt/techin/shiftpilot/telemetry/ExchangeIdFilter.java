package lt.techin.shiftpilot.telemetry;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class ExchangeIdFilter extends OncePerRequestFilter {

    public static final String EXCHANGE_ID_KEY = "exchangeId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String exchangeId = UUID.randomUUID().toString();
        MDC.put(EXCHANGE_ID_KEY, exchangeId);
        response.setHeader("X-Exchange-Id", exchangeId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(EXCHANGE_ID_KEY);
        }
    }
}
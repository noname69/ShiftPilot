package lt.techin.shiftpilot.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.DispatcherType;
import lt.techin.shiftpilot.telemetry.ExchangeIdFilter;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.servlet.LogbookFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class LogbookConfig {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Set<String> HEADERS_TO_KEEP = Set.of(
            "Content-Type",
            "Pragma",
            "X-Exchange-Id",
            "X-Frame-Options"
    );

    @Bean
    public CorrelationId exchangeCorrelationId() {
        return request -> {
            String id = MDC.get(ExchangeIdFilter.EXCHANGE_ID_KEY);
            return id != null ? id : UUID.randomUUID().toString();
        };
    }

    @Bean
    public HttpLogFormatter compactLogFormatter() {
        return new HttpLogFormatter() {

            @Override
            public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {
                ObjectNode node = mapper.createObjectNode();
                node.put("type", "request");
                node.put("correlation", precorrelation.getId());
                node.put("method", request.getMethod());
                node.put("uri", request.getRequestUri());
                node.put("path", request.getPath());
                node.putPOJO("headers", request.getHeaders().entrySet().stream()
                        .filter(e -> HEADERS_TO_KEEP.contains(e.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                String body = request.getBodyAsString();
                if (!body.isBlank()) {
                    try {
                        node.set("body", mapper.readTree(body));
                    } catch (Exception e) {
                        node.put("body", body);
                    }
                }
                return "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            }

            @Override
            public String format(Correlation correlation, HttpResponse response) throws IOException {
                ObjectNode node = mapper.createObjectNode();
                node.put("type", "response");
                node.put("correlation", correlation.getId());
                node.put("status", response.getStatus());
                node.put("duration_ms", correlation.getDuration().toMillis());
                node.putPOJO("headers", response.getHeaders().entrySet().stream()
                        .filter(e -> HEADERS_TO_KEEP.contains(e.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                String body = response.getBodyAsString();
                if (!body.isBlank()) {
                    try {
                        node.set("body", mapper.readTree(body));
                    } catch (Exception e) {
                        node.put("body", body);
                    }
                }
                return "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            }
        };
    }

    @Bean
    public FilterRegistrationBean<ExchangeIdFilter> exchangeIdFilterRegistration() {
        FilterRegistrationBean<ExchangeIdFilter> reg =
                new FilterRegistrationBean<>(new ExchangeIdFilter());
        reg.setOrder(-102);
        reg.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        return reg;
    }

    @Bean
    public FilterRegistrationBean<LogbookFilter> logbookFilterRegistration(Logbook logbook) {
        FilterRegistrationBean<LogbookFilter> reg =
                new FilterRegistrationBean<>(new LogbookFilter(logbook));
        reg.setOrder(-101);
        reg.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        return reg;
    }
}
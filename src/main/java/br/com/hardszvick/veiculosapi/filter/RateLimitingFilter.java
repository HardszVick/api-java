package br.com.hardszvick.veiculosapi.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Order(1)
public class RateLimitingFilter implements Filter {

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ip = resolveClientIp(request);
        String pathKey = resolvePathKey(request.getRequestURI());
        String cacheKey = ip + ":" + pathKey;

        Bucket bucket = cache.get(cacheKey, k -> createBucket(pathKey));

        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
            return;
        }
        response.setStatus(429);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"success\":false,\"message\":\"Muitas requisições. Tente novamente mais tarde.\",\"data\":null}"
        );
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String resolvePathKey(String uri) {
        if (uri.equals("/login") || uri.equals("/refresh")) return "auth";
        if (uri.equals("/register")) return "register";
        return "default";
    }

    private Bucket createBucket(String pathKey) {
        Bandwidth limit = switch (pathKey) {
            case "auth" -> Bandwidth.builder()
                    .capacity(5)
                    .refillGreedy(5, Duration.ofMinutes(1))
                    .build();
            case "register" -> Bandwidth.builder()
                    .capacity(1)
                    .refillGreedy(1, Duration.ofHours(1))
                    .build();
            default -> Bandwidth.builder()
                    .capacity(120)
                    .refillGreedy(120, Duration.ofMinutes(1))
                    .build();
        };

        return Bucket.builder().addLimit(limit).build();
    }
}

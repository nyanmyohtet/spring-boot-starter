package com.nyanmyohtet.springbootstarter.security;

import com.nyanmyohtet.springbootstarter.service.TokenBucketService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String RETRY_AFTER_SECONDS = "X-Rate-Limit-Retry-After-Seconds";

    private final TokenBucketService tokenBucketService;

    private final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    public RateLimitFilter(TokenBucketService tokenBucketService) {
        this.tokenBucketService = tokenBucketService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        logger.info("Client IP Address: {}", clientIp);

        Bucket bucket = tokenBucketService.resolveTokenBucket(clientIp);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            handleRateLimitNotExceeded(request, response, filterChain, probe);
        } else {
            handleRateLimitExceeded(response, probe);
        }
    }

    private void handleRateLimitNotExceeded(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, ConsumptionProbe probe) throws IOException, ServletException {
        response.setHeader(RATE_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
        filterChain.doFilter(request, response);
    }

    private void handleRateLimitExceeded(HttpServletResponse response, ConsumptionProbe probe) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader(RETRY_AFTER_SECONDS, String.valueOf(NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));
        response.getWriter().write("Rate limit exceeded. Please try again later.");
        response.getWriter().flush();
    }
}

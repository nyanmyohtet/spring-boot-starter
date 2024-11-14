package com.nyanmyohtet.springbootstarter.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FixedWindowRateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final int RATE_LIMIT_RESET_IN_MINUTE = 1;
    private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(FixedWindowRateLimitingFilter.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public FixedWindowRateLimitingFilter() {
        scheduler.scheduleAtFixedRate(this::resetRateLimits, RATE_LIMIT_RESET_IN_MINUTE, RATE_LIMIT_RESET_IN_MINUTE,
                TimeUnit.MINUTES);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String clientIp = request.getRemoteAddr();

        requestCountsPerIpAddress.putIfAbsent(clientIp, new AtomicInteger(0));
        int requests = requestCountsPerIpAddress.get(clientIp).incrementAndGet();

        if (requests > MAX_REQUESTS_PER_MINUTE) {
            response.getWriter().write("Rate limit exceeded. Try again later.");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        scheduler.shutdown();
    }

    private void resetRateLimits() {
        logger.info("Resetting rate limits for all IPs.");
        requestCountsPerIpAddress.clear();
    }
}

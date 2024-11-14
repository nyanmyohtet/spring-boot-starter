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
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SlidingWindowRateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final int WINDOW_SIZE_IN_SECOND = 60;
    private final Logger logger = LoggerFactory.getLogger(SlidingWindowRateLimitingFilter.class);
    private final Map<String, Queue<Long>> requestTimestampsPerIpAddress = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String clientIp = request.getRemoteAddr();

        Queue<Long> timestamps = requestTimestampsPerIpAddress.computeIfAbsent(clientIp, k -> new LinkedList<>());

        long currentTimeMillis = System.currentTimeMillis();
        while (!timestamps.isEmpty() && currentTimeMillis - timestamps.peek() > (WINDOW_SIZE_IN_SECOND * 1000)) {
            timestamps.poll();  // Remove the timestamp if it's outside the window
        }

        timestamps.offer(currentTimeMillis);

        if (timestamps.size() > MAX_REQUESTS_PER_MINUTE) {
            response.getWriter().write("Rate limit exceeded. Try again later.");
            response.getWriter().flush();
            return;
        }

        logger.info("Requests in the last {} second(s): {}", MAX_REQUESTS_PER_MINUTE, timestamps.size());

        filterChain.doFilter(request, response);
    }
}

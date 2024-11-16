package com.nyanmyohtet.springbootstarter.service.impl;

import com.nyanmyohtet.springbootstarter.service.TokenBucketService;
import com.nyanmyohtet.springbootstarter.util.TimeUtil;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBucketServiceImpl implements TokenBucketService {

    private final Integer bucketCapacity;
    private final Integer bucketTime;
    private final String bucketTimeunit;
    private final long cleanupTimeMillisecond;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(TokenBucketServiceImpl.class);

    public TokenBucketServiceImpl(@Value("${tokenBucket.bandwidth.capacity}") Integer bucketCapacity,
                                  @Value("${tokenBucket.bandwidth.time}") Integer bucketTime,
                                  @Value("${tokenBucket.bandwidth.unit}") String bucketTimeunit,
                                  @Value("${tokenBucket.cleanupCache.time}") Integer cleanupTime,
                                  @Value("${tokenBucket.cleanupCache.unit}") String cleanupTimeunit) {

        CommonValidationService.validatePositive(bucketCapacity, "Bucket capacity must be positive. Given ");
        CommonValidationService.validatePositive(bucketTime, "Bucket time must be positive. Given ");
        CommonValidationService.validatePositive(cleanupTime, "Cleanup time must be positive. Given ");
        CommonValidationService.validTimeUnit(bucketTimeunit);
        CommonValidationService.validTimeUnit(cleanupTimeunit);

        this.bucketCapacity = bucketCapacity;
        this.bucketTime = bucketTime;
        this.bucketTimeunit = bucketTimeunit;
        this.cleanupTimeMillisecond = TimeUtil.getMillisecond(cleanupTime, cleanupTimeunit);
    }

    public Bucket resolveTokenBucket(String ipAddress) {
        CommonValidationService.validateIpAddress(ipAddress);
        CacheEntry entry = cache.computeIfAbsent(ipAddress, this::createCacheEntry);
        entry.updateLastAccess();
        return entry.bucket;
    }

    private CacheEntry createCacheEntry(String ipAddress) {
        Duration period = TimeUtil.getDuration(bucketTime, bucketTimeunit);
        Bucket bucket = Bucket.builder()
                .addLimit(limit ->
                        limit.capacity(bucketCapacity)
                                .refillGreedy(bucketCapacity, period))
                .build();
        return new CacheEntry(bucket);
    }

    /**
     * Scheduled cleanup to remove stale cache entries.
     */
    @SuppressWarnings("unused")
    @Scheduled(fixedRateString = "#{@tokenBucketServiceImpl.getCleanupRate()}")
    public void cleanupCache() {
        logger.info("Scheduler: start cleanup Rate Limit Cache.");
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry ->
                (now - entry.getValue().lastAccess) > cleanupTimeMillisecond);
    }

    @SuppressWarnings("unused")
    public String getCleanupRate() {
        return String.valueOf(cleanupTimeMillisecond);
    }

    /**
     * Wrapper class for cache entries to track last access time.
     */
    private static class CacheEntry {
        final Bucket bucket;
        volatile long lastAccess;

        CacheEntry(Bucket bucket) {
            this.bucket = bucket;
            this.lastAccess = System.currentTimeMillis();
        }

        void updateLastAccess() {
            this.lastAccess = System.currentTimeMillis();
        }
    }
}

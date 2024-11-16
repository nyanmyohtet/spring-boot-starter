package com.nyanmyohtet.springbootstarter.service.impl;

import com.nyanmyohtet.springbootstarter.service.TokenBucketService;
import com.nyanmyohtet.springbootstarter.util.TimeUtil;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBucketServiceImpl implements TokenBucketService {

    @Value("${tokenBucket.bandwidth.capacity}")
    private final Integer bucketCapacity;

    @Value("${tokenBucket.bandwidth.time}")
    private final Integer bucketTime;

    @Value("${tokenBucket.bandwidth.unit}")
    private final String bucketUnit;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public TokenBucketServiceImpl(@Value("${tokenBucket.bandwidth.capacity}") Integer bucketCapacity,
                                  @Value("${tokenBucket.bandwidth.time}") Integer bucketTime,
                                  @Value("${tokenBucket.bandwidth.unit}") String bucketUnit) {

        validatePositive(bucketCapacity, "Bucket capacity must be positive. Given ");
        validatePositive(bucketTime, "Bucket time must be positive. Given ");
        validTimeUnit(bucketUnit);

        this.bucketCapacity = bucketCapacity;
        this.bucketTime = bucketTime;
        this.bucketUnit = bucketUnit;
    }

    public Bucket resolveTokenBucket(String ipAddress) {
        return cache.computeIfAbsent(ipAddress, this::newTokenBucket);
    }

    private Bucket newTokenBucket(String ipAddress) {
        Duration period = TimeUtil.getDuration(bucketTime, bucketUnit);
        return Bucket.builder()
                .addLimit(limit ->
                        limit.capacity(bucketCapacity)
                                .refillGreedy(bucketCapacity, period))
                .build();
    }

    private void validatePositive(Integer value, String message) {
        if (value <= 0) throw new IllegalArgumentException(message + value);
    }

    // TODO: separate into validation service
    private void validTimeUnit(String value) {

        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Timeunit value is empty.");

        TimeUnit[] timeUnitValues = TimeUnit.values();

        try {
            TimeUnit.valueOf(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Timeunit is not valid. Given " + value + ". Supported values are "
                    + Arrays.toString(timeUnitValues));
        }
    }
}

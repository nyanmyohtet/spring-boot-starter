package com.nyanmyohtet.springbootstarter.service;

import io.github.bucket4j.Bucket;

public interface TokenBucketService {
    Bucket resolveTokenBucket(String ipAddress);
}

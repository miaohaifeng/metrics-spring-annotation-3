package com.madhouse.metrics.util;

import com.codahale.metrics.health.HealthCheck;

/**
* Created by
* $ miaohaifeng
* on 2015/12/17.
*/
public class DatabaseHealthCheck extends HealthCheck {
    private int count;

    public DatabaseHealthCheck(int count) {
        this.count = count;
    }

    @Override
    protected Result check() throws Exception {
        if (count < 1000) {
            return Result.healthy();
        }
        return Result.unhealthy("Can't ping database");
    }
}
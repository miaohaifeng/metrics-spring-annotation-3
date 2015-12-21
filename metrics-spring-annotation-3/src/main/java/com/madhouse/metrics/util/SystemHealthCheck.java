package com.madhouse.metrics.util;

/**
* Created by
* $ miaohaifeng
* on 2015/12/17.
*/

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;

public class SystemHealthCheck implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SystemHealthCheck.class);

    private static final HealthCheckRegistry healthChecks = new HealthCheckRegistry();

    public static boolean registerToSysHealthCheck(final String name, final HealthCheck healthCheck) {
        if (healthChecks.getNames().contains(name)) {
            return false;
        }
        healthChecks.register(name, healthCheck);
        return true;
    }

    public void run() {
        final Map<String, Result> results = healthChecks.runHealthChecks();
        StringBuilder message = null;
        for (Entry<String, Result> entry : results.entrySet()) {
            message = new StringBuilder(entry.getKey());
            if (entry.getValue().isHealthy()) {
                message.append(" is healthy: ").append(entry.getValue().getMessage());
                LOG.info(message.toString());
            } else {
                message.append(" is UNHEALTHY: ").append(entry.getValue().getMessage());
                LOG.error(message.toString(), entry.getValue().getError());
            }
        }
    }
}
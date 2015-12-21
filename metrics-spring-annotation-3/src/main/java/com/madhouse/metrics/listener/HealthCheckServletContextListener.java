package com.madhouse.metrics.listener;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;

/**
* Created by
* $ miaohaifeng
* on 2015/12/15.
*/
public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener{

    public static final HealthCheckRegistry HEALTH_CHECK_REGISTRY = new HealthCheckRegistry();

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return HEALTH_CHECK_REGISTRY;
    }
}

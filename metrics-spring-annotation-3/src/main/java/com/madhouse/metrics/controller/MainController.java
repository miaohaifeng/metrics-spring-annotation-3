package com.madhouse.metrics.controller;

import com.codahale.metrics.*;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.madhouse.metrics.util.DatabaseHealthCheck;
import com.madhouse.metrics.util.HealthCheckDemo;
import com.madhouse.metrics.util.MetricsFactory;
import com.ryantenney.metrics.annotation.CachedGauge;
import com.ryantenney.metrics.annotation.Counted;
import com.ryantenney.metrics.annotation.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by
 * $ miaohaifeng
 * on 2015/12/14.
 */
@Controller("mainController")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private Counter runCounter;
    private Counter successCounter;
    private Counter failCounter;

    @Autowired
    private HealthCheckRegistry healthCheckRegistry;
    @Autowired
    private MetricRegistry metricRegistry;
    @Autowired
    private HealthCheckDemo healthCheckDemo;
    @Autowired
    private HealthCheckDemo.HealthCheckClass healthCheckClass;

    @Metric
    public Histogram histogram;
    @Metric
    public Histogram uniformHistogram = new Histogram(new UniformReservoir(300));

    @RequestMapping(value = "/testHealthCheck", method = RequestMethod.GET)
    public void testHealthCheck() {

        healthCheckRegistry.register("health1",new DatabaseHealthCheck(4));
        final Map<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        for (Map.Entry<String, HealthCheck.Result> entry : results.entrySet()) {
            if (entry.getValue().isHealthy()) {
                System.out.println(entry.getKey() + " is healthy");
            } else {
                System.err.println(entry.getKey() + " is UNHEALTHY: " + entry.getValue().getMessage());
                final Throwable e = entry.getValue().getError();
                if (e != null) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @Timed(name = "index")
    @ExceptionMetered(name = "getOrganizationByName_exceptions")
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        runCounter.inc();
        System.out.println("runCounter.getCount():" + runCounter.getCount());
        LOGGER.info("Processing Request");
        return "index";
    }

    @RequestMapping(value = "/index1", method = RequestMethod.GET)
    @Timed(name = "index1")
    public String index1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        runCounter.inc();
        System.out.println("runCounter.getCount():" + runCounter.getCount());
        LOGGER.info("Processing Request");
        return "index1";
    }

    @RequestMapping(value = "/createBuilding", method = RequestMethod.GET)
    @Metered(name = "MainController_createBuilding")
    public String createBuilding() {
        runCounter.inc();
        System.out.println("runCounter.getCount():" + runCounter.getCount());
        LOGGER.info("createBuilding");
        return "createBuilding";
    }

    @RequestMapping(value = "/createHistogram", method = RequestMethod.GET)
    public void createHistogram() {

    }

    @ExceptionMetered(name = "BulkJobScheduledService_submitWork_exceptions")
    @RequestMapping(value = "/excptionTest", method = RequestMethod.GET)
    public void excptionTest() {
        try {
            int count = 1 / 0;
        } catch (Exception e) {
            failCounter.inc();
            System.out.println("runCounter.getCount():" + failCounter.getCount());
            throw new NullPointerException();
        }
    }

    @RequestMapping(value = "/countedTest", method = RequestMethod.GET)
    @Counted(name = "Counted")
    public String countedTest() {
        successCounter.inc();
        System.out.println("successCounter.getCount():" + successCounter.getCount());
        runCounter.dec();
        System.out.println("runCounter.getCount():" + runCounter.getCount());
        return "counted";
    }

    @Gauge(name = "gaugeTest")
    @ExceptionMetered(name = "gauge_exceptions")
    public long gaugeTest() {
        System.out.println("runCounter.getCount():" + runCounter.getCount());
        return runCounter.getCount();
    }

    @CachedGauge(name = "cachedGaugeTest", timeout = 30, timeoutUnit = TimeUnit.SECONDS)
    @ExceptionMetered(name = "CachedGauge_exceptions")
    public long cachedGaugeTest() {
        System.out.println("runCounter.getCount():" + runCounter.getCount());
        return runCounter.getCount();
    }

    /**
     * Set the metrics factory
     */
    public void setMetricsFactory(MetricsFactory metricsFactory) {
        runCounter = metricsFactory.getCounter(this.getClass(), "running_workers");
        successCounter = metricsFactory.getCounter(this.getClass(), "successful_jobs");
        failCounter = metricsFactory.getCounter(this.getClass(), "failed_jobs");
    }
    @RequestMapping(value = "/createCsv", method = RequestMethod.GET)
    public void createCsv(){
//        final CsvReporter reporter = CsvReporter.forRegistry(metricRegistry)
//                .formatFor(Locale.US)
//                .convertRatesTo(TimeUnit.SECONDS)
//                .convertDurationsTo(TimeUnit.MILLISECONDS)
//                .build(new File("E:\\code\\metrics-spring-annotation-3\\projects\\data\\"));
//        reporter.start(10, TimeUnit.SECONDS);
        System.out.println("createCsv");
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);
    }
}

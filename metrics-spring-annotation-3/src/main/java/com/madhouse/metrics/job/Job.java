package com.madhouse.metrics.job;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.madhouse.metrics.util.MetricsFactory;

/**
 * Created by
 * $ miaohaifeng
 * on 2015/12/16.
 */
public class Job {
    private Timer jobTimer;
    private Counter runCounter;
    private Counter successCounter;
    private Counter failCounter;
//    @Autowired
//    private MetricsFactory metricsFactory;

    public Job() {

    }
    /**
     * Set the metrics factory
     */
    public void setMetricsFactory(MetricsFactory metricsFactory) {
        jobTimer = metricsFactory.getTimer(this.getClass(), "job_execution_timer");
        runCounter = metricsFactory.getCounter(this.getClass(), "running_workers");
        successCounter = metricsFactory.getCounter(this.getClass(), "successful_jobs");
        failCounter = metricsFactory.getCounter(this.getClass(), "failed_jobs");
    }
}

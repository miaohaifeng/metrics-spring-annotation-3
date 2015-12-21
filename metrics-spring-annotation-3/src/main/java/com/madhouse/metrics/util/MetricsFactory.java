/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madhouse.metrics.util;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class to manage metrics.
 */
@Component("metricsFactory")
public class MetricsFactory {
    public  MetricRegistry registry ;
    private GraphiteReporter graphiteReporter;
    private JmxReporter jmxReporter;
    private ConcurrentHashMap<String, Metric> hashMap;
    private static final Logger LOG = LoggerFactory.getLogger(MetricsFactory.class);

    public MetricsFactory() {

    }
    @PostConstruct
    void init() {
        registry = new MetricRegistry();
        hashMap = new ConcurrentHashMap<String, Metric>();
        jmxReporter = JmxReporter.forRegistry( registry ).build();
        jmxReporter.start();
    }
    public MetricRegistry getRegistry() {
        return registry;
    }

    public Timer getTimer(Class<?> klass, String name) {
        return getMetric(Timer.class, klass, name);
    }

    public Histogram getHistogram(Class<?> klass, String name) {
        return getMetric(Histogram.class, klass, name);
    }

    public Counter getCounter(Class<?> klass, String name) {
        return getMetric(Counter.class, klass, name);
    }

    public Meter getMeter(Class<?> klass, String name) {
        return getMetric(Meter.class, klass, name);
    }

    private <T> T getMetric(Class<T> metricClass, Class<?> klass, String name) {
        String key = metricClass.getName() + klass.getName() + name;
        Metric metric = hashMap.get(key);
        if (metric == null) {
            if (metricClass == Histogram.class) {
                metric = this.getRegistry().histogram(MetricRegistry.name(klass, name));
            }
            if (metricClass == Timer.class) {
                metric = this.getRegistry().timer(MetricRegistry.name(klass, name));
            }
            if (metricClass == Meter.class) {
                metric = this.getRegistry().meter(MetricRegistry.name(klass, name));
            }
            if (metricClass == Counter.class) {
                metric = this.getRegistry().counter(MetricRegistry.name(klass, name));
            }
            hashMap.put(key, metric);
        }
        return (T) metric;
    }
}

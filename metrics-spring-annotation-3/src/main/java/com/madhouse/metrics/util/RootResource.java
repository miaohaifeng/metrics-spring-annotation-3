//package com.madhouse.metrics.util;
//
//import com.codahale.metrics.Counter;
//import com.codahale.metrics.Histogram;
//import org.codehaus.jackson.node.JsonNodeFactory;
//import org.codehaus.jackson.node.ObjectNode;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * Created by
// * $ miaohaifeng
// * on 2015/12/16.
// */
//@Component
//@Scope("singleton")
//public class RootResource implements MetricProcessor<RootResource.MetricContext> {
//    static final class MetricContext {
//        final boolean showFullSamples;
//        final ObjectNode objectNode;
//
//        MetricContext(ObjectNode objectNode, boolean showFullSamples) {
//            this.objectNode = objectNode;
//            this.showFullSamples = showFullSamples;
//        }
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger(RootResource.class);
//    long started = System.currentTimeMillis();
//
//    public RootResource() {
//    }
//
//    @Override
//    public void processHistogram(MetricName name, Histogram histogram, MetricContext context) throws Exception {
//        final ObjectNode node = context.objectNode;
//        node.put("type", "histogram");
//        node.put("count", histogram.count());
//        writeSummarizable(histogram, node);
//        writeSampling(histogram, node);
//    }
//
//
//    @Override
//    public void processCounter(MetricName name, Counter counter, MetricContext context) throws Exception {
//        final ObjectNode node = context.objectNode;
//        node.put("type", "counter");
//        node.put("count", counter.count());
//    }
//
//
//    @Override
//    public void processGauge(MetricName name, Gauge<?> gauge, MetricContext context) throws Exception {
//        final ObjectNode node = context.objectNode;
//        node.put("type", "gauge");
//        node.put("vale", "[disabled]");
//    }
//
//
//    @Override
//    public void processMeter(MetricName name, Metered meter, MetricContext context) throws Exception {
//        final ObjectNode node = context.objectNode;
//        node.put("type", "meter");
//        node.put("event_type", meter.eventType());
//        writeMeteredFields(meter, node);
//    }
//
//
//    @Override
//    public void processTimer(MetricName name, Timer timer, MetricContext context) throws Exception {
//        final ObjectNode node = context.objectNode;
//
//        node.put("type", "timer");
//        // json.writeFieldName("duration");
//        node.put("unit", timer.durationUnit().toString().toLowerCase());
//        ObjectNode durationNode = JsonNodeFactory.instance.objectNode();
//        writeSummarizable(timer, durationNode);
//        writeSampling(timer, durationNode);
//        node.put("duration", durationNode);
//        writeMeteredFields(timer, node);
//    }
//
//
//    private static void writeSummarizable(Summarizable metric, ObjectNode mNode) throws IOException {
//        mNode.put("min", metric.min());
//        mNode.put("max", metric.max());
//        mNode.put("mean", metric.mean());
//        mNode.put("std_dev", metric.stdDev());
//    }
//
//
//    private static void writeSampling(Sampling metric, ObjectNode mNode) throws IOException {
//
//        final Snapshot snapshot = metric.getSnapshot();
//        mNode.put("median", snapshot.getMedian());
//        mNode.put("p75", snapshot.get75thPercentile());
//        mNode.put("p95", snapshot.get95thPercentile());
//        mNode.put("p98", snapshot.get98thPercentile());
//        mNode.put("p99", snapshot.get99thPercentile());
//        mNode.put("p999", snapshot.get999thPercentile());
//    }
//
//
//    private static void writeMeteredFields(Metered metered, ObjectNode node) throws IOException {
//        ObjectNode mNode = JsonNodeFactory.instance.objectNode();
//        mNode.put("unit", metered.rateUnit().toString().toLowerCase());
//        mNode.put("count", metered.count());
//        mNode.put("mean", metered.meanRate());
//        mNode.put("m1", metered.oneMinuteRate());
//        mNode.put("m5", metered.fiveMinuteRate());
//        mNode.put("m15", metered.fifteenMinuteRate());
//        node.put("rate", mNode);
//    }
//}

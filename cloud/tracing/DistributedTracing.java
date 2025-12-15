package com.zanta.lfp.cloud.tracing;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DistributedTracing {

    public static class Span {
        private String traceId;
        private String spanId;
        private String serviceName;
        private String operationName;
        private long startTime;
        private long endTime;

        public Span(String traceId, String serviceName, String operationName) {
            this.traceId = traceId;
            this.spanId = UUID.randomUUID().toString();
            this.serviceName = serviceName;
            this.operationName = operationName;
            this.startTime = System.currentTimeMillis();
        }

        public void end() {
            this.endTime = System.currentTimeMillis();
        }

        public String getTraceId() { return traceId; }
        public String getSpanId() { return spanId; }
        public String getServiceName() { return serviceName; }
        public String getOperationName() { return operationName; }
        public long getDuration() { return endTime - startTime; }
    }

    public Span startSpan(String traceId, String serviceName, String operationName) {
        String actualTraceId = traceId != null ? traceId : UUID.randomUUID().toString();
        Span span = new Span(actualTraceId, serviceName, operationName);
        System.out.println("Distributed Tracing: Started span " + span.getSpanId() + 
                         " for trace " + actualTraceId);
        return span;
    }

    public void endSpan(Span span) {
        span.end();
        System.out.println("Distributed Tracing: Ended span " + span.getSpanId() + 
                         " (duration: " + span.getDuration() + "ms)");
    }

    public Span createChildSpan(Span parentSpan, String operationName) {
        Span childSpan = new Span(parentSpan.getTraceId(), 
                                 parentSpan.getServiceName(), 
                                 operationName);
        System.out.println("Distributed Tracing: Created child span " + childSpan.getSpanId());
        return childSpan;
    }

    public void addTag(Span span, String key, String value) {
        System.out.println("Distributed Tracing: Added tag " + key + "=" + value + 
                         " to span " + span.getSpanId());
    }
}

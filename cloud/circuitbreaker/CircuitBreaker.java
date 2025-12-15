package com.zanta.lfp.cloud.circuitbreaker;

import org.springframework.stereotype.Component;

@Component
public class CircuitBreaker {

    public enum CircuitState {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    private CircuitState state = CircuitState.CLOSED;
    private int failureCount = 0;
    private final int failureThreshold = 5;
    private final long timeout = 60000;

    public Object execute(Runnable serviceCall, Object fallback) {
        if (state == CircuitState.OPEN) {
            System.out.println("Circuit Breaker: Circuit is OPEN, using fallback");
            return fallback;
        }

        try {
            if (state == CircuitState.HALF_OPEN) {
                System.out.println("Circuit Breaker: Testing service recovery (HALF_OPEN)");
            }

            serviceCall.run();
            onSuccess();
            return null;
        } catch (Exception e) {
            onFailure();
            System.out.println("Circuit Breaker: Service call failed: " + e.getMessage());
            return fallback;
        }
    }

    private void onSuccess() {
        failureCount = 0;
        if (state == CircuitState.HALF_OPEN) {
            state = CircuitState.CLOSED;
            System.out.println("Circuit Breaker: Service recovered, circuit CLOSED");
        }
    }

    private void onFailure() {
        failureCount++;
        if (failureCount >= failureThreshold && state == CircuitState.CLOSED) {
            state = CircuitState.OPEN;
            System.out.println("Circuit Breaker: Failure threshold reached, circuit OPEN");
        }
    }

    public CircuitState getState() {
        return state;
    }

    public void reset() {
        state = CircuitState.CLOSED;
        failureCount = 0;
        System.out.println("Circuit Breaker: Manually reset to CLOSED");
    }
}

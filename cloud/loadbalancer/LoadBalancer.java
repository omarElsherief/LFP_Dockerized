package com.zanta.lfp.cloud.loadbalancer;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LoadBalancer {

    public enum LoadBalancingStrategy {
        ROUND_ROBIN,
        RANDOM,
        LEAST_CONNECTIONS,
        WEIGHTED_ROUND_ROBIN
    }

    private LoadBalancingStrategy strategy = LoadBalancingStrategy.ROUND_ROBIN;
    private int currentIndex = 0;

    public String selectInstance(List<String> serviceInstances) {
        if (serviceInstances == null || serviceInstances.isEmpty()) {
            System.out.println("Load Balancer: No instances available");
            return null;
        }

        String selected = switch (strategy) {
            case ROUND_ROBIN -> {
                String instance = serviceInstances.get(currentIndex % serviceInstances.size());
                currentIndex++;
                yield instance;
            }
            case RANDOM -> {
                int randomIndex = (int) (Math.random() * serviceInstances.size());
                yield serviceInstances.get(randomIndex);
            }
            case LEAST_CONNECTIONS -> {
                yield serviceInstances.get(0);
            }
            case WEIGHTED_ROUND_ROBIN -> {
                yield serviceInstances.get(0);
            }
        };

        System.out.println("Load Balancer: Selected instance " + selected + " using " + strategy);
        return selected;
    }

    public void setStrategy(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
        System.out.println("Load Balancer: Strategy set to " + strategy);
    }

    public void markUnhealthy(String instanceUrl) {
        System.out.println("Load Balancer: Marking " + instanceUrl + " as unhealthy");
    }
}

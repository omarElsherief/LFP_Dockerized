package com.zanta.lfp.cloud;

import com.zanta.lfp.cloud.circuitbreaker.CircuitBreaker;
import com.zanta.lfp.cloud.config.ConfigServer;
import com.zanta.lfp.cloud.discovery.ServiceDiscovery;
import com.zanta.lfp.cloud.gateway.ApiGateway;
import com.zanta.lfp.cloud.loadbalancer.LoadBalancer;
import com.zanta.lfp.cloud.messaging.MessageQueue;
import com.zanta.lfp.cloud.ratelimit.RateLimiter;
import com.zanta.lfp.cloud.registry.ServiceRegistry;
import com.zanta.lfp.cloud.tracing.DistributedTracing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudComponentsConfig {

    @Bean
    public ApiGateway apiGateway() {
        return new ApiGateway();
    }

    @Bean
    public ServiceDiscovery serviceDiscovery() {
        return new ServiceDiscovery();
    }

    @Bean
    public LoadBalancer loadBalancer() {
        return new LoadBalancer();
    }

    @Bean
    public CircuitBreaker circuitBreaker() {
        return new CircuitBreaker();
    }

    @Bean
    public ConfigServer configServer() {
        return new ConfigServer();
    }

    @Bean
    public MessageQueue messageQueue() {
        return new MessageQueue();
    }

    @Bean
    public DistributedTracing distributedTracing() {
        return new DistributedTracing();
    }

    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter();
    }

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistry();
    }
}

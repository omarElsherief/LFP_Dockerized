package com.zanta.lfp.cloud.registry;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServiceRegistry {

    public static class ServiceInstance {
        private String instanceId;
        private String serviceName;
        private String host;
        private int port;
        private String status;
        private long lastHeartbeat;

        public ServiceInstance(String instanceId, String serviceName, String host, int port) {
            this.instanceId = instanceId;
            this.serviceName = serviceName;
            this.host = host;
            this.port = port;
            this.status = "UP";
            this.lastHeartbeat = System.currentTimeMillis();
        }

        public String getInstanceId() { return instanceId; }
        public String getServiceName() { return serviceName; }
        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public void updateHeartbeat() { this.lastHeartbeat = System.currentTimeMillis(); }
        public String getUrl() { return "http://" + host + ":" + port; }
    }

    private Map<String, List<ServiceInstance>> registry = new ConcurrentHashMap<>();

    public void register(ServiceInstance instance) {
        registry.computeIfAbsent(instance.getServiceName(), k -> new java.util.ArrayList<>())
                .add(instance);
        System.out.println("Service Registry: Registered " + instance.getServiceName() + 
                         " instance " + instance.getInstanceId() + " at " + instance.getUrl());
    }

    public void deregister(String serviceName, String instanceId) {
        List<ServiceInstance> instances = registry.get(serviceName);
        if (instances != null) {
            instances.removeIf(inst -> inst.getInstanceId().equals(instanceId));
            System.out.println("Service Registry: Deregistered " + serviceName + 
                             " instance " + instanceId);
        }
    }

    public List<ServiceInstance> getInstances(String serviceName) {
        return registry.getOrDefault(serviceName, List.of());
    }

    public void receiveHeartbeat(String serviceName, String instanceId) {
        List<ServiceInstance> instances = registry.get(serviceName);
        if (instances != null) {
            instances.stream()
                    .filter(inst -> inst.getInstanceId().equals(instanceId))
                    .forEach(ServiceInstance::updateHeartbeat);
            System.out.println("Service Registry: Received heartbeat from " + 
                             serviceName + " instance " + instanceId);
        }
    }

    public void markDown(String serviceName, String instanceId) {
        List<ServiceInstance> instances = registry.get(serviceName);
        if (instances != null) {
            instances.stream()
                    .filter(inst -> inst.getInstanceId().equals(instanceId))
                    .forEach(inst -> inst.setStatus("DOWN"));
            System.out.println("Service Registry: Marked " + serviceName + 
                             " instance " + instanceId + " as DOWN");
        }
    }
}

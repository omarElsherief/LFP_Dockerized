package com.zanta.lfp.cloud.messaging;

import org.springframework.stereotype.Component;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageQueue {

    private Map<String, Queue<Message>> queues = new ConcurrentHashMap<>();

    public static class Message {
        private String topic;
        private Object payload;
        private Map<String, String> headers;

        public Message(String topic, Object payload) {
            this.topic = topic;
            this.payload = payload;
            this.headers = new ConcurrentHashMap<>();
        }

        public String getTopic() { return topic; }
        public Object getPayload() { return payload; }
        public Map<String, String> getHeaders() { return headers; }
    }

    public void publish(String topic, Object message) {
        queues.putIfAbsent(topic, new LinkedList<>());
        Message msg = new Message(topic, message);
        queues.get(topic).offer(msg);
        System.out.println("Message Queue: Published message to topic " + topic);
    }

    public void subscribe(String topic, MessageHandler handler) {
        System.out.println("Message Queue: Subscribed to topic " + topic);
    }

    public Message consume(String topic) {
        Queue<Message> queue = queues.get(topic);
        if (queue != null && !queue.isEmpty()) {
            Message msg = queue.poll();
            System.out.println("Message Queue: Consumed message from topic " + topic);
            return msg;
        }
        return null;
    }

    @FunctionalInterface
    public interface MessageHandler {
        void handle(Message message);
    }
}

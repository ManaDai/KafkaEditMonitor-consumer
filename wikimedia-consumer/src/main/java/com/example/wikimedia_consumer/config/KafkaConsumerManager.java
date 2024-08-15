package com.example.wikimedia_consumer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.example.wikimedia_consumer.consumer.WikimediaConsumer;
import com.example.wikimedia_consumer.domain.AppUser;

@Component
public class KafkaConsumerManager {

    private final ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

    @Autowired
    private WikimediaConsumer wikimediaConsumer;

    private ConcurrentMessageListenerContainer<String, String> container;

    public KafkaConsumerManager(ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory) {
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
    }

    public synchronized ConcurrentMessageListenerContainer<String, String> createConsumer(String topic,
            AppUser appUser) {
        wikimediaConsumer.setAppUser(appUser);
        ConcurrentMessageListenerContainer<String, String> container = kafkaListenerContainerFactory
                .createContainer(topic);
        container.setupMessageListener(wikimediaConsumer);
        return container;
    }

    public synchronized void startContainer(String topic, AppUser appUser) {
        if (container != null) {
            container.stop();
        }
        container = createConsumer(topic, appUser);
        container.start();
    }

    public synchronized void stopContainer() {
        if (container != null) {
            container.stop();
            container = null;
        }
    }
}

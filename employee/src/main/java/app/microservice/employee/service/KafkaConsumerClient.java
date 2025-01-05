package app.microservice.employee.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Service
public class KafkaConsumerClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerClient.class);

    private Properties getProducerProperties(String groupId) {
        var properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        if (groupId != null) {
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        }

        return properties;
    }

    public void consume(String topic) {
        try (var consumer = new KafkaConsumer<String, String>(this.getProducerProperties(null))) {
            consumer.assign(List.of(
                    new TopicPartition(topic, 0),
                    new TopicPartition(topic, 1),
                    new TopicPartition(topic, 2)
            ));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
            StreamSupport.stream(records.spliterator(), false)
                    .forEach(record -> LOGGER.info("Record: {}, value: {}", record, record.value()));
        }
    }

    public void consume(String topic, String groupId) {
        try (var consumer = new KafkaConsumer<String, String>(this.getProducerProperties(groupId))) {
            consumer.subscribe(Pattern.compile(topic), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                    LOGGER.info("Partitions revoked: {}", collection);
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                    LOGGER.info("Partitions assigned: {}", collection);
                }
            });

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(30));
            StreamSupport.stream(records.spliterator(), false)
                    .forEach(record ->
                            LOGGER.info("Record: {}, value: {}", record, record.value()
                            ));
        }
    }

}

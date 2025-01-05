package app.microservice.client.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducerClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerClient.class);


    private Properties getProducerProperties(String transactionId) {
        var properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*
          Отключение от idempotent producer
        */
//        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, Boolean.FALSE.toString());

        /*
           Ожидаем подтверждение только от лидирующей реплики портиции
        */
//        properties.put(ProducerConfig.ACKS_CONFIG, "1");


        if (transactionId != null) {
            /*
               Используем транзакции в качестве id транзакции
            */
            properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionId);
        }

        return properties;
    }


    public void send(String topic, String message, String key) {
        try (var producer = new KafkaProducer<String, String>(this.getProducerProperties(null))) {
            producer.send(new ProducerRecord<>(topic, key, message)).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String topic, ArrayList<String> messages, String key, String transactionId) {
        try (var producer = new KafkaProducer<String, String>(this.getProducerProperties(transactionId))) {
            producer.initTransactions();
            producer.beginTransaction();

            for (String message : messages) {
                producer.send(new ProducerRecord<>(topic, key, message));
            }
            producer.commitTransaction();
        }

    }

}

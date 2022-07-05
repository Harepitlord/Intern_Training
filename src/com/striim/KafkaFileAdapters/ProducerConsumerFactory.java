package com.striim.KafkaFileAdapters;

import com.striim.BasicFileAdapters.database.DataRecord;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import scala.collection.immutable.Stream;

import java.util.Properties;

public class ProducerConsumerFactory {

    private static final Properties producerProperties = new Properties();
    private static final Properties consumerProperties = new Properties();

    public static class KafkaObjectSerializer implements Serializer<DataRecord> {

        @Override
        public byte[] serialize(String s, DataRecord dataRecord) {
            return SerializationUtils.serialize(dataRecord);
        }
    }

    public static class KafkaObjectDeserializer implements Deserializer<DataRecord> {
        @Override
        public DataRecord deserialize(String s, byte[] bytes) {
            return SerializationUtils.deserialize(bytes);
        }
    }

    static {
        String bootstrapServer = "localhost:9092";

        producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,KafkaObjectSerializer.class.getName());

        consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,KafkaObjectDeserializer.class.getName());
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
    }
    public static KafkaProducer<String, DataRecord> getProducer() {
        return new KafkaProducer<>(producerProperties);
    }

    public static KafkaConsumer<String,DataRecord> getConsumer() {
        return new KafkaConsumer<>(consumerProperties);
    }

}

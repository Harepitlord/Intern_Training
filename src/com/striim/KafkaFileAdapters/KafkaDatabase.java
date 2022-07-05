package com.striim.KafkaFileAdapters;

import com.striim.BasicFileAdapters.database.DataRecord;
import com.striim.BasicFileAdapters.database.Database;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KafkaDatabase implements Database {

    private final KafkaProducer<String,DataRecord> KafkaProducer;
    private final KafkaConsumer<String,DataRecord> KafkaConsumer;

    public KafkaDatabase() {
        this.KafkaProducer = ProducerConsumerFactory.getProducer();
        this.KafkaConsumer = ProducerConsumerFactory.getConsumer();
    }

    @Override
    public ArrayList<DataRecord> getDataObjArray(String topicId) {
        KafkaConsumer.subscribe(Collections.singletonList(topicId));

            ConsumerRecords<String, DataRecord> records=KafkaConsumer.poll(Duration.ofMillis(600));
            ArrayList<DataRecord> dataRecords = new ArrayList<>();
            records.forEach(e->dataRecords.add(e.value()));
            return dataRecords;
    }

    public String getType() {
        return "Kafka";
    }

    @Override
    public void addDataObjects(ArrayList<DataRecord> data) {
        for(int i = 0;i< data.size();i++) {
            DataRecord e = data.get(i);
            ProducerRecord<String, DataRecord> record = new ProducerRecord<>(e.getTopicId(), e);
            this.KafkaProducer.send(record, (recordMetadata, e1) -> {
                Logger logger = LoggerFactory.getLogger(KafkaDatabase.class);
                if (e1 == null) {
                    logger.info("Successfully received the details as: \n" +
                            "Topic:" + recordMetadata.topic() + "\n" +
                            "Partition:" + recordMetadata.partition() + "\n" +
                            "Offset" + recordMetadata.offset() + "\n" +
                            "Timestamp" + recordMetadata.timestamp());
                } else {
                    logger.error("Can't produce,getting error", e1);

                }
            });
            if((i+1)%30==0)
                KafkaProducer.flush();
        }
        KafkaProducer.flush();
        KafkaProducer.close();
    }
}

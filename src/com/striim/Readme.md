
Database: In-Memory or Kafka (based on user choice)

This version of project supports multiple csv files with same schema.   
The data read is stored in In-Memory Database or Kafka during runtime. 
The data is later queried based on the user's queries. 
The queried data is stored in a json file as output.

Inputs : Multiple CSV files with same schema 
Outputs: Multiple JSON file with its queried data


Kafka Commands

Terminal at Kafka folder

To Start Zookeeper server
./bin/zookeeper-server-start.sh config/zookeeper.properties

To start Kafka Server
./bin/kafka-server-start.sh config/server.properties

To do task related to kafka topics
./bin/kafka-topics.sh --bootstrap-server localhost:9092

To Create a topic in kafka
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic Database


# tradeAnalytics
Trade Analytics using kafka, spark-streaming, cassandra in Java8

Before running below steps ensure that your Cassandra cluster is up and running - 

1. Start zookeeper
F:\kafka\kafka_2.10-0.8.2.1>.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

2. Start Kafka Server
F:\kafka\kafka_2.10-0.8.2.1>.\bin\windows\kafka-server-start.bat .\config\server.properties

3. Create Kafka topic
F:\kafka\kafka_2.10-0.8.2.1>.\bin\windows\kafka-create-topic.bat --zookeeper localhost:2181 --replica 1 --partition 1 --topic tradesTopic

Above command has been changed in recent versions.You may need to use below command to create the topic 
F:\kafka\kafka_2.10-0.8.2.1>.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partition 1 --topic tradesTopic


4. Run Trade data producer - TradeDataKafkaProducer.java
This is a java application, which writes data on Kafka topic, named tradesTopic,created at previous step.

5. Run KafkaSparkStreamingApp.java to consume Trade data messages from Kafka using Spark Streaming and will also dump the data in Cassandra.This application also calculates the top N trades-book combinations, at present, based upon risk/sensitivity amount and sensitivity/risk type.

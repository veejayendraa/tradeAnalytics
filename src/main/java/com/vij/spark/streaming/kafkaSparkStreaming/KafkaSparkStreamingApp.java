package com.vij.spark.streaming.kafkaSparkStreaming;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.google.common.base.Optional;
import com.vij.spark.streaming.kafkaSparkStreaming.cassandra.TradesDAO;
import com.vij.spark.streaming.kafkaSparkStreaming.pojo.TradeData;
import com.vij.spark.streaming.kafkaSparkStreaming.pojo.TradeDataKey;
import com.vij.spark.streaming.kafkaSparkStreaming.util.TradesConverter;


/**
 * Hello world!
 *
 */
public class KafkaSparkStreamingApp implements Serializable
{
	transient CassandraConnector connector;
	 transient JavaStreamingContext jssc;
	 JavaPairReceiverInputDStream<String, String> messages;
	 transient SparkConf sparkConf;
	 TradesDAO tradesDAO;
	 private static Function2<Double, Double, Double> SUM_REDUCER = (a, b) -> a + b;
	 private static  Function2<List<Double>, Optional<Double>, Optional<Double>>
     COMPUTE_RUNNING_SUM = (nums, current) -> {
       double sum = current.or(0d);
       for (double i : nums) {
         sum += i;
       }
       return Optional.of(sum);
     };
     private static class ValueComparator<K, V>
     implements Comparator<Tuple2<K, V>>, Serializable {
    private Comparator<V> comparator;

    public ValueComparator(Comparator<V> comparator) {
      this.comparator = comparator;
    }

    @Override
    public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
      return comparator.compare(o2._2(), o1._2());
    }
  }
	public KafkaSparkStreamingApp()
	{
		init();
	}
	
	private void run()
	{
		
		 JavaDStream<String> lines = messages.map(message->message._2());
		 System.out.println("Saving data");
		 jssc.checkpoint("F://work//log-analyzer-streaming");
		 saveRawData(lines);
		 System.out.println("Caclulating top 10");
		 JavaPairDStream<TradeDataKey, Double> aggregatedStreamedTradeData = lines.mapToPair(line->new Tuple2<TradeDataKey,Double>(TradesConverter.getKey(line), Double.parseDouble(line.split(",")[4])))
		 .reduceByKey(SUM_REDUCER)
		 .updateStateByKey(COMPUTE_RUNNING_SUM);
	

		 aggregatedStreamedTradeData.foreach(rdd -> {
			 List<Tuple2<TradeDataKey, Double>> topTradeBookCombination =
				      rdd.takeOrdered(10, new ValueComparator<>(Comparator.<Double>naturalOrder()));
				  System.out.println("Top TradeBook combination: " + topTradeBookCombination);
				  System.out.println("####  START  ####");
				  for (Tuple2<TradeDataKey, Double> data :  topTradeBookCombination)
				  {
					  TradeDataKey dataKey = data._1;
					 
					  System.out.println("Trade data "+dataKey + " #Amount# "+data._2);
				  }
				  System.out.println("####  END  ####");
				  return null;
				});
    	 jssc.start();
    	 jssc.awaitTermination();
		
	}
	
	private void saveRawData(JavaDStream<String> lines)
	{
		
		JavaDStream<TradeData> tradeData = lines.map(line->TradesConverter.convert(line));
    	
		 tradeData.print();
    	 tradesDAO.save(jssc,tradeData);
	}
    public static void main( String[] args )
    { 
    	 
    	new KafkaSparkStreamingApp().run();
    }
    
    private void init()
    {
    	 sparkConf = new SparkConf()
			.setAppName("kafka-spark-streaming")
			.setMaster("local[2]")
			.set("spark.cassandra.connection.host", "127.0.0.1")
			.set("spark.cassandra.auth.username", "cassandra")
			.set("spark.cassandra.auth.password", "cassandra");
		
		 
		  jssc = new JavaStreamingContext(sparkConf, new Duration(2*60*1000));
		 // jsc = new JavaSparkContext(sparkConf);
		  Map<String,Integer> topicMap = new HashMap<String,Integer>();
		  topicMap.put("tradesTopic", 1);
	 	  messages = KafkaUtils.createStream(jssc, "localhost:2181", "trades", topicMap);
	 	
	 	  tradesDAO = new TradesDAO();
	 	  
    	createTables();
    }
    
    private void createTables()
    {
    	 connector = CassandraConnector.apply(sparkConf);
    	 try (Session session = connector.openSession()) {
	          session.execute("DROP KEYSPACE IF EXISTS tradesensstorage");
	          session.execute("CREATE KEYSPACE tradesensstorage WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
	          session.execute("CREATE TABLE tradesensstorage.tradelevelstorage (businessdate TIMESTAMP,bookid int,utid text,risktype text,amt double,PRIMARY KEY(bookid,businessdate,utid,risktype))");
	          session.execute("CREATE TABLE tradesensstorage.booklevelstorage  (businessdate TIMESTAMP,bookid int,risktype text,amt double,PRIMARY KEY(bookid,businessdate,risktype))");
		       
	          System.out.println("****************KEYSPACE AND TABLES CREATED***************");
	      }
    }
}
